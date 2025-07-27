package com.alltuttasneeds.blocks;

import com.alltuttasneeds.registry.TDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SlidingDoorBlock extends DoorBlock {
    public static final BooleanProperty IN_WALL = BlockStateProperties.IN_WALL;

    protected static final VoxelShape SHAPE_NS  = Block.box(0, 0, 7, 16, 16, 9);
    protected static final VoxelShape SHAPE_WE = Block.box(7, 0, 0, 9, 16, 16);

    protected static final VoxelShape SHAPE_NORTH_OPEN  = Block.box(0, 0, 0, 2, 16, 16);
    protected static final VoxelShape SHAPE_SOUTH_OPEN = Block.box(14, 0, 0, 16, 16, 16);
    protected static final VoxelShape SHAPE_WEST_OPEN  = Block.box(0, 0, 0, 16, 16, 2);
    protected static final VoxelShape SHAPE_EAST_OPEN = Block.box(0, 0, 14, 16, 16, 16);

    protected static final VoxelShape SHAPE_NORTH_TRUE_OPEN      = Block.box(14, 0, 5, 30, 16, 7);
    protected static final VoxelShape SHAPE_NORTH_TRUE_OPEN_ALT  = Block.box(14, 0, 9, 30, 16, 11);
    protected static final VoxelShape SHAPE_SOUTH_TRUE_OPEN      = Block.box(-14, 0, 9, 2, 16, 11);
    protected static final VoxelShape SHAPE_SOUTH_TRUE_OPEN_ALT  = Block.box(-14, 0, 5, 2, 16, 7);
    protected static final VoxelShape SHAPE_WEST_TRUE_OPEN       = Block.box(5, 0, -14, 7, 16, 2);
    protected static final VoxelShape SHAPE_WEST_TRUE_OPEN_ALT   = Block.box(9, 0, -14, 11, 16, 2);
    protected static final VoxelShape SHAPE_EAST_TRUE_OPEN       = Block.box(9, 0, 14, 11, 16, 30);

    protected static final VoxelShape SHAPE_EAST_TRUE_OPEN_ALT   = Block.box(5, 0, 14, 7, 16, 30);
    public SlidingDoorBlock(BlockSetType type, Properties properties) {
        super(type, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(IN_WALL, false));
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        boolean open = state.getValue(OPEN);
        boolean inWall = state.getValue(IN_WALL);
        DoorHingeSide hinge = state.getValue(HINGE);

        if (inWall && open) {
            switch (facing) {
                case NORTH:
                    return hinge == DoorHingeSide.RIGHT ? SHAPE_SOUTH_OPEN : SHAPE_NORTH_OPEN;
                case SOUTH:
                    return hinge == DoorHingeSide.RIGHT ? SHAPE_NORTH_OPEN : SHAPE_SOUTH_OPEN;
                case WEST:
                    return hinge == DoorHingeSide.RIGHT ? SHAPE_WEST_OPEN : SHAPE_EAST_OPEN;
                case EAST:
                    return hinge == DoorHingeSide.RIGHT ? SHAPE_EAST_OPEN : SHAPE_WEST_OPEN;
            }
        }

        if (!inWall && open) {
            switch (facing) {
                case NORTH:
                    return hinge == DoorHingeSide.RIGHT ? SHAPE_NORTH_TRUE_OPEN_ALT : SHAPE_SOUTH_TRUE_OPEN;
                case SOUTH:
                    return hinge == DoorHingeSide.RIGHT ? SHAPE_SOUTH_TRUE_OPEN_ALT : SHAPE_NORTH_TRUE_OPEN;
                case WEST:
                    return hinge == DoorHingeSide.RIGHT ? SHAPE_WEST_TRUE_OPEN_ALT : SHAPE_EAST_TRUE_OPEN;
                case EAST:
                    return hinge == DoorHingeSide.RIGHT ? SHAPE_EAST_TRUE_OPEN_ALT : SHAPE_WEST_TRUE_OPEN;
            }
        }

        return facing.getAxis() == Direction.Axis.X ? SHAPE_WE : SHAPE_NS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(IN_WALL);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState base = super.getStateForPlacement(ctx);
        return base == null ? null
                : updateInWall(base, ctx.getLevel(), ctx.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        BlockState updated = super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        if (updated.is(this) && updated.getProperties().contains(IN_WALL)) {
            BlockState newState = updateInWall(updated, world, pos);

            BlockPos otherHalfPos = newState.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos.above();
            BlockState otherState = world.getBlockState(otherHalfPos);
            if (otherState.is(this)) {
                world.setBlock(otherHalfPos, otherState.setValue(IN_WALL, newState.getValue(IN_WALL)), 2);
            }

            return newState;
        }

        return updated;
    }

    private static BlockState updateInWall(BlockState st, LevelReader world, BlockPos pos) {
        Direction face       = st.getValue(FACING);
        DoorHingeSide hinge   = st.getValue(HINGE);
        Direction side        = hinge == DoorHingeSide.LEFT ? face.getCounterClockWise() : face.getClockWise();
        Direction vert        = st.getValue(HALF) == DoubleBlockHalf.UPPER ? Direction.DOWN : Direction.UP;

        for (BlockPos p : new BlockPos[]{ pos.relative(side), pos.relative(side).relative(vert) }) {
            BlockState bs = world.getBlockState(p);
            if (bs.isSolid() && !(bs.getBlock() instanceof SlidingDoorBlock) && !(bs.getBlock() instanceof IronBarsBlock) && !bs.is(TDTags.POSSIBLE_SLIDING)) {
                return st.setValue(IN_WALL, true);
            }
        }
        return st.setValue(IN_WALL, false);
    }
}