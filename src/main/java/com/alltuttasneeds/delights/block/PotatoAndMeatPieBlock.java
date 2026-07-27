package com.alltuttasneeds.delights.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.function.Supplier;

public class PotatoAndMeatPieBlock extends FeastBlock {
    public static final IntegerProperty EXTRASERVINGS = IntegerProperty.create("servings", 0, 6);
    protected static final VoxelShape PLATE_SHAPE_NS = Block.box(0, 0, 1, 16, 2, 15);
    protected static final VoxelShape PLATE_SHAPE_WE = Block.box(1, 0, 0, 15, 2, 16);
    protected static final VoxelShape[] SHAPES_NORTH = new VoxelShape[]{
            Block.box(0, 0, 0, 0, 0, 0),
            Block.box(11, 2, 8, 16, 7, 14),
            Block.box(11, 2, 2, 16, 7, 14),
            Block.box(5, 2, 2, 16, 7, 14),
            Block.box(5, 2, 2, 16, 7, 14),
            Block.box(0, 2, 2, 16, 7, 14),
            Block.box(0, 2, 2, 16, 7, 14)
    };
    protected static final VoxelShape[] SHAPES_SOUTH = new VoxelShape[]{
            Block.box(0, 0, 0, 0, 0, 0),
            Block.box(0, 2, 2, 5, 7, 8),
            Block.box(0, 2, 2, 5, 7, 14),
            Block.box(0, 2, 2, 11, 7, 14),
            Block.box(0, 2, 2, 11, 7, 14),
            Block.box(0, 2, 2, 16, 7, 14),
            Block.box(0, 2, 2, 16, 7, 14),
    };
    protected static final VoxelShape[] SHAPES_WEST = new VoxelShape[]{
            Block.box(0, 0, 0, 0, 0, 0),
            Block.box(8, 2, 0, 14, 7, 5),
            Block.box(2, 2, 0, 14, 7, 5),
            Block.box(2, 2, 0, 14, 7, 11),
            Block.box(2, 2, 0, 14, 7, 11),
            Block.box(2, 2, 0, 14, 7, 16),
            Block.box(2, 2, 0, 14, 7, 16)
    };
    protected static final VoxelShape[] SHAPES_EAST = new VoxelShape[]{
            Block.box(0, 0, 0, 0, 0, 0),
            Block.box(2, 2, 11, 8, 7, 16),
            Block.box(2, 2, 11, 14, 7, 16),
            Block.box(2, 2, 5, 14, 7, 16),
            Block.box(2, 2, 5, 14, 7, 16),
            Block.box(2, 2, 0, 14, 7, 16),
            Block.box(2, 2, 0, 14, 7, 16)
    };


    public PotatoAndMeatPieBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
        super(properties, servingItem, hasLeftovers);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(EXTRASERVINGS, 6));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        int servings = state.getValue(EXTRASERVINGS);

        return switch (facing) {
            case NORTH -> Shapes.or(PLATE_SHAPE_NS, SHAPES_NORTH[servings]);
            case SOUTH -> Shapes.or(PLATE_SHAPE_NS, SHAPES_SOUTH[servings]);
            case WEST -> Shapes.or(PLATE_SHAPE_WE, SHAPES_WEST[servings]);
            case EAST -> Shapes.or(PLATE_SHAPE_WE, SHAPES_EAST[servings]);
            default -> Shapes.or(PLATE_SHAPE_NS, SHAPES_NORTH[servings]);
        };
    }

    @Override
    public IntegerProperty getServingsProperty() {
        return EXTRASERVINGS;
    }

    @Override
    public int getMaxServings() {
        return 6;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, EXTRASERVINGS);
    }
}
