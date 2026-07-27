package com.alltuttasneeds.delights.block;

import com.alltuttasneeds.delights.DelightsTextUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.function.Supplier;

public class AncientSoftBoiledEggBlock extends TwofoldFeastBlock{
    protected static final VoxelShape PLATE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
    protected static final VoxelShape[] SHAPES_NORTH = new VoxelShape[]{
            Block.box(0, 0, 0, 0, 0, 0),
            Block.box(1, 1, 5, 11, 4, 15),
            Block.box(1, 1, 5, 11, 6, 15),
            Block.box(1, 1, 5, 11, 8, 15),
            Block.box(1, 1, 5, 11, 10, 15)
    };
    protected static final VoxelShape[] SHAPES_SOUTH = new VoxelShape[]{
            Block.box(0, 0, 0, 0, 0, 0),
            Block.box(5, 1, 1, 15, 4, 11),
            Block.box(5, 1, 1, 15, 6, 11),
            Block.box(5, 1, 1, 15, 8, 11),
            Block.box(5, 1, 1, 15, 10, 11)
    };
    protected static final VoxelShape[] SHAPES_WEST = new VoxelShape[]{
            Block.box(0, 0, 0, 0, 0, 0),
            Block.box(5, 1, 5, 15, 4, 15),
            Block.box(5, 1, 5, 15, 6, 15),
            Block.box(5, 1, 5, 15, 8, 15),
            Block.box(5, 1, 5, 15, 10, 15)
    };
    protected static final VoxelShape[] SHAPES_EAST = new VoxelShape[]{
            Block.box(0, 0, 0, 0, 0, 0),
            Block.box(1, 1, 1, 11, 4, 11),
            Block.box(1, 1, 1, 11, 6, 11),
            Block.box(1, 1, 1, 11, 8, 11),
            Block.box(1, 1, 1, 11, 10, 11)
    };

    public AncientSoftBoiledEggBlock(Properties properties, Supplier<Item> altServingOneItem, int altServingOneCount, Supplier<Item> servingItem, boolean hasLeftovers) {
        super(properties, altServingOneItem, altServingOneCount, servingItem, hasLeftovers);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        int servings = state.getValue(SERVINGS);

        return switch (facing) {
            case NORTH -> Shapes.or(PLATE_SHAPE, SHAPES_NORTH[servings]);
            case SOUTH -> Shapes.or(PLATE_SHAPE, SHAPES_SOUTH[servings]);
            case WEST -> Shapes.or(PLATE_SHAPE, SHAPES_WEST[servings]);
            case EAST -> Shapes.or(PLATE_SHAPE, SHAPES_EAST[servings]);
            default -> Shapes.or(PLATE_SHAPE, SHAPES_NORTH[servings]);
        };
    }
    @Override
    protected ItemStack determineServing(ItemStack servingOne, ItemStack servingTwo, BlockPos pos, BlockState state, Player player) {
        if (ItemUtils.isKnife(player.getMainHandItem())) {
            return servingTwo;
        }
        return servingOne;
    }

    @Override
    public void displayUseContainerMessage(BlockState state, Player player, ItemStack selectedServing) {
        player.displayClientMessage(DelightsTextUtils.block("feast.use_alt_container", selectedServing.getCraftingRemainingItem().getHoverName(), DelightsTextUtils.block("feast.need_knives")), true);
    }
}
