package com.alltuttasneeds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
public class TransitDoorBlock extends DoorBlock {

    protected static final VoxelShape LOWER_SOUTH_AABB = Block.box(0.0F, 3.0F, 0.0F, 16.0F, 16.0F, 3.0F);
    protected static final VoxelShape LOWER_NORTH_AABB = Block.box(0.0F, 3.0F, 13.0F, 16.0F, 16.0F, 16.0F);
    protected static final VoxelShape LOWER_WEST_AABB  = Block.box(13.0F, 3.0F, 0.0F, 16.0F, 16.0F, 16.0F);
    protected static final VoxelShape LOWER_EAST_AABB  = Block.box(0.0F, 3.0F, 0.0F, 3.0F, 16.0F, 16.0F);
    protected static final VoxelShape UPPER_SOUTH_AABB = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 14.0F, 3.0F);
    protected static final VoxelShape UPPER_NORTH_AABB = Block.box(0.0F, 0.0F, 13.0F, 16.0F, 14.0F, 16.0F);
    protected static final VoxelShape UPPER_WEST_AABB  = Block.box(13.0F, 0.0F, 0.0F, 16.0F, 14.0F, 16.0F);
    protected static final VoxelShape UPPER_EAST_AABB  = Block.box(0.0F, 0.0F, 0.0F, 3.0F, 14.0F, 16.0F);

    protected static final VoxelShape LOWER_SOUTH_ENTITY_AABB = Block.box(0.0F, 3.0F, 1.0F, 16.0F, 16.0F, 2.0F);
    protected static final VoxelShape LOWER_NORTH_ENTITY_AABB = Block.box(0.0F, 3.0F, 14.0F, 16.0F, 16.0F, 15.0F);
    protected static final VoxelShape LOWER_WEST_ENTITY_AABB  = Block.box(14.0F, 3.0F, 0.0F, 15.0F, 16.0F, 16.0F);
    protected static final VoxelShape LOWER_EAST_ENTITY_AABB  = Block.box(1.0F, 3.0F, 0.0F, 2.0F, 16.0F, 16.0F);
    protected static final VoxelShape UPPER_SOUTH_ENTITY_AABB = Block.box(0.0F, 0.0F, 1.0F, 16.0F, 14.0F, 2.0F);
    protected static final VoxelShape UPPER_NORTH_ENTITY_AABB = Block.box(0.0F, 0.0F, 14.0F, 16.0F, 14.0F, 15.0F);
    protected static final VoxelShape UPPER_WEST_ENTITY_AABB  = Block.box(14.0F, 0.0F, 0.0F, 15.0F, 14.0F, 16.0F);
    protected static final VoxelShape UPPER_EAST_ENTITY_AABB  = Block.box(1.0F, 0.0F, 0.0F, 2.0F, 14.0F, 16.0F);

    public TransitDoorBlock(BlockSetType type, Properties properties) {
        super(type, properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean closed    = !state.getValue(OPEN);
        boolean rightHinge = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        boolean isUpper   = state.getValue(HALF) == DoubleBlockHalf.UPPER;

        VoxelShape south = isUpper ? UPPER_SOUTH_AABB : LOWER_SOUTH_AABB;
        VoxelShape north = isUpper ? UPPER_NORTH_AABB : LOWER_NORTH_AABB;
        VoxelShape west  = isUpper ? UPPER_WEST_AABB  : LOWER_WEST_AABB;
        VoxelShape east  = isUpper ? UPPER_EAST_AABB  : LOWER_EAST_AABB;

        return switch (direction) {
            case SOUTH -> closed ? south : (rightHinge ? east  : west);
            case WEST  -> closed ? west  : (rightHinge ? south : north);
            case NORTH -> closed ? north : (rightHinge ? west  : east);
            default    -> closed ? east  : (rightHinge ? north : south);
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean closed     = !state.getValue(OPEN);
        boolean rightHinge = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        boolean isUpper    = state.getValue(HALF) == DoubleBlockHalf.UPPER;

        VoxelShape south = isUpper ? UPPER_SOUTH_ENTITY_AABB : LOWER_SOUTH_ENTITY_AABB;
        VoxelShape north = isUpper ? UPPER_NORTH_ENTITY_AABB : LOWER_NORTH_ENTITY_AABB;
        VoxelShape west  = isUpper ? UPPER_WEST_ENTITY_AABB  : LOWER_WEST_ENTITY_AABB;
        VoxelShape east  = isUpper ? UPPER_EAST_ENTITY_AABB  : LOWER_EAST_ENTITY_AABB;

        return switch (direction) {
            case SOUTH -> closed ? south : (rightHinge ? east  : west);
            case WEST  -> closed ? west  : (rightHinge ? south : north);
            case NORTH -> closed ? north : (rightHinge ? west  : east);
            default    -> closed ? east  : (rightHinge ? north : south);
        };
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1);
        }
    }

    private void handleEntityLogic(BlockState state, ServerLevel level, BlockPos pos) {
        boolean open = state.getValue(OPEN);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, getBoundingBox(pos));

        for (Entity entity : entities) {
            if (entity.isCrouching() || entity instanceof ItemEntity) {
                continue;
            }

            if (entity instanceof Animal) {
                boolean isMounted = !entity.getPassengers().isEmpty();
                boolean isTamed   = (entity instanceof TamableAnimal tamable && tamable.isTame());
                if (!isMounted && !isTamed) {
                    continue;
                }
            }

            Direction facing   = state.getValue(FACING);
            DoorHingeSide hinge = state.getValue(HINGE);

            Direction hingeSide = (hinge == DoorHingeSide.RIGHT)
                    ? facing.getClockWise()
                    : facing.getCounterClockWise();

            Vec3 entityPos = entity.position();
            double dx = entityPos.x - (pos.getX() + 0.5);
            double dz = entityPos.z - (pos.getZ() + 0.5);

            boolean isOnHingeSide = switch (hingeSide.getAxis()) {
                case X -> hingeSide.getStepX() > 0 ? dx > 0 : dx < 0;
                case Z -> hingeSide.getStepZ() > 0 ? dz > 0 : dz < 0;
                default -> false;
            };

            if (isOnHingeSide == open) {
                this.setOpen(null, level, state, pos, !open);
            }

            break;
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isReceivingRedstonePower(state, level, pos)) {
            return;
        }

        if (!level.getEntitiesOfClass(Entity.class, getBoundingBox(pos)).isEmpty()) {
            handleEntityLogic(state, level, pos);
            level.scheduleTick(pos, this, 20);
            return;
        }
        this.setOpen(null, level, state, pos, false);
    }

    private static boolean isReceivingRedstonePower(BlockState state, ServerLevel level, BlockPos pos) {
        BlockPos lowerPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
        return level.hasNeighborSignal(lowerPos) || level.hasNeighborSignal(lowerPos.above());
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
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return type != PathComputationType.WATER;
    }

    private Direction getHingeDirection(BlockState state) {
        return state.getValue(HINGE) == DoorHingeSide.LEFT
                ? state.getValue(FACING).getCounterClockWise()
                : state.getValue(FACING).getClockWise();
    }

    private boolean hasHingeSupport(LevelReader level, BlockPos pos, Direction dir) {
        pos = pos.relative(dir);
        return level.getBlockState(pos).isFaceSturdy(level, pos, dir.getOpposite());
    }

    private boolean hasFloorSupport(LevelReader level, BlockPos pos) {
        pos = pos.below();
        return level.getBlockState(pos).isFaceSturdy(level, pos, Direction.UP);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos base = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
        Direction hinge = getHingeDirection(state);

        return hasFloorSupport(level, base)
                || hasHingeSupport(level, base, hinge)
                && hasHingeSupport(level, base.above(), hinge);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return null;

        BlockPos pos = context.getClickedPos();
        Direction hinge = getHingeDirection(state);

        return hasFloorSupport(context.getLevel(), pos)
                || hasHingeSupport(context.getLevel(), pos, hinge)
                && hasHingeSupport(context.getLevel(), pos.above(), hinge)
                ? state
                : null;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.getValue(HALF);

        if (direction.getAxis() == Direction.Axis.Y
                && (half == DoubleBlockHalf.LOWER) == (direction == Direction.UP)) {
            return neighborState.getBlock() instanceof DoorBlock
                    && neighborState.getValue(HALF) != half
                    ? neighborState.setValue(HALF, half)
                    : Blocks.AIR.defaultBlockState();
        }

        return state.canSurvive(level, pos)
                ? super.updateShape(state, direction, neighborState, level, pos, neighborPos)
                : Blocks.AIR.defaultBlockState();
    }
}