package com.alltuttasneeds.doors.block;

import com.alltuttasneeds.doors.config.TDConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class PetDoorBlock extends TrapDoorBlock {

    protected static final VoxelShape SOUTH_CLOSED_AABB =  Block.box(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 3.0F);
    protected static final VoxelShape NORTH_CLOSED_AABB = Block.box(0.0F, 0.0F, 13.0F, 16.0F, 16.0F, 16.0F);
    protected static final VoxelShape EAST_CLOSED_AABB =  Block.box(0.0F, 0.0F, 0.0F, 3.0F, 16.0F, 16.0F);
    protected static final VoxelShape WEST_CLOSED_AABB = Block.box(13.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F);

    protected static final VoxelShape SOUTH_CLOSED_ENTITY_AABB = Block.box(0.0F, 0.0F, 1.0F, 16.0F, 16.0F, 2.0F);
    protected static final VoxelShape NORTH_CLOSED_ENTITY_AABB = Block.box(0.0F, 0.0F, 14.0F, 16.0F, 16.0F, 15.0F);
    protected static final VoxelShape WEST_CLOSED_ENTITY_AABB = Block.box(14.0F, 0.0F, 0.0F, 15.0F, 16.0F, 16.0F);
    protected static final VoxelShape EAST_CLOSED_ENTITY_AABB = Block.box(1.0F, 0.0F, 0.0F, 2.0F, 16.0F, 16.0F);

    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);
    protected static final VoxelShape TOP_AABB = Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape BOTTOM_AABB_ENTITY = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    protected static final VoxelShape TOP_AABB_ENTITY = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);

    public PetDoorBlock(BlockSetType type, Properties properties) {
        super(type, properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(OPEN)) {
            return state.getValue(HALF) == Half.TOP ? TOP_AABB : BOTTOM_AABB;
        } else {
            switch (state.getValue(FACING)) {
                case NORTH:
                default:
                    return NORTH_CLOSED_AABB;
                case SOUTH:
                    return SOUTH_CLOSED_AABB;
                case WEST:
                    return WEST_CLOSED_AABB;
                case EAST:
                    return EAST_CLOSED_AABB;
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(OPEN)) {
            return state.getValue(HALF) == Half.TOP ? TOP_AABB_ENTITY : BOTTOM_AABB_ENTITY;
        } else {
            switch (state.getValue(FACING)) {
                case NORTH:
                default:
                    return NORTH_CLOSED_ENTITY_AABB;
                case SOUTH:
                    return SOUTH_CLOSED_ENTITY_AABB;
                case WEST:
                    return WEST_CLOSED_ENTITY_AABB;
                case EAST:
                    return EAST_CLOSED_ENTITY_AABB;
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!TDConfig.petAutomaticClosingEnabled.get()) return;
        if (!level.getEntitiesOfClass(Entity.class, getBoundingBox(pos)).isEmpty()) {
            level.scheduleTick(pos, state.getBlock(), TDConfig.automaticClosingDelay());
            return;
        }

        if (TDConfig.shouldAutomaticallyClose(state.getValue(POWERED))) {
            this.setOpen(null, level, state, pos, false);
        } else if (state.getValue(OPEN)) {
            level.scheduleTick(pos, state.getBlock(), TDConfig.automaticClosingDelay());
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        InteractionResult result = super.useWithoutItem(state, level, pos, player, hitResult);
        BlockState updated = level.getBlockState(pos);
        if (!level.isClientSide && TDConfig.petAutomaticClosingEnabled.get()
                && updated.is(this) && updated.getValue(OPEN)) {
            level.scheduleTick(pos, this, TDConfig.automaticClosingDelay());
        }
        return result;
    }

    public void setOpen(@Nullable Entity entity, Level level, BlockState state, BlockPos pos, boolean open) {
        if (state.is(this) && state.getValue(OPEN) != open) {
            BlockState newState = state.setValue(OPEN, open);
            level.setBlock(pos, newState, 10);

            if (newState.getValue(WATERLOGGED)) {
                level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }

            this.playSound((Player) entity, level, pos, open);
            level.gameEvent(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.isClientSide) return;

        boolean powered = level.hasNeighborSignal(pos);
        if (powered == state.getValue(POWERED)) return;

        boolean open = state.getValue(OPEN);
        if (powered && !open) {
            playSound(null, level, pos, true);
            level.gameEvent(null, GameEvent.BLOCK_OPEN, pos);
            open = true;
        }

        BlockState updated = state.setValue(POWERED, powered).setValue(OPEN, open);
        level.setBlock(pos, updated, 2);
        if (updated.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (open && TDConfig.petAutomaticClosingEnabled.get()) {
            level.scheduleTick(pos, this, TDConfig.automaticClosingDelay());
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        Direction face = context.getClickedFace();
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());

        Direction facing = face.getAxis().isHorizontal()
                ? face
                : context.getHorizontalDirection();

        boolean invert = player != null && player.isShiftKeyDown();

        Half half = (player != null && player.getLookAngle().y > 0)
                ? Half.TOP
                : Half.BOTTOM;

        if (invert) {
            half = half == Half.TOP ? Half.BOTTOM : Half.TOP;
        }

        BlockState state = defaultBlockState()
                .setValue(FACING, facing)
                .setValue(HALF, half)
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);

        if (context.getLevel().hasNeighborSignal(context.getClickedPos())) {
            state = state.setValue(OPEN, true)
                    .setValue(POWERED, true);
        }

        return state;
    }

    private static AABB getBoundingBox(BlockPos pos) {
        double offset = 0.05;
        return new AABB(
                pos.getX() - offset,
                pos.getY() - offset,
                pos.getZ() - offset,
                pos.getX() + 1 + offset,
                pos.getY() + 1 + offset,
                pos.getZ() + 1 + offset
        );
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        switch (pathComputationType) {
            case LAND, AIR:
                return true;
            case WATER:
                return state.getValue(WATERLOGGED);
            default:
                return false;
        }
    }
}
