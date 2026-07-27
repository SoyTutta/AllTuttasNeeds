package com.alltuttasneeds.delights.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class ThreefoldFeastBlock extends TwofoldFeastBlock {
    public final Supplier<Item> altServingTwoItem;
    public final int altServingTwoCount;

    public ThreefoldFeastBlock(Properties properties, Supplier<Item> altServingOneItem, int altServingOneCount, Supplier<Item> altServingTwoItem, int altServingTwoCount, Supplier<Item> servingItem, boolean hasLeftovers) {
        super(properties, altServingOneItem, altServingOneCount, servingItem, hasLeftovers);
        this.altServingTwoItem = altServingTwoItem;
        this.altServingTwoCount = altServingTwoCount;
    }

    public ItemStack getaltServingOneItem(BlockState state) {
        return new ItemStack(this.altServingOneItem.get());
    }

    public ItemStack getaltServingTwoItem(BlockState state) {
        return new ItemStack(this.altServingTwoItem.get());
    }

    @Override
    protected ItemInteractionResult takeServing(LevelAccessor level, BlockPos pos, BlockState state, Player player, InteractionHand hand) {
        int servings = state.getValue(getServingsProperty());

        if (servings == 0) {
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
            level.destroyBlock(pos, true);
            return ItemInteractionResult.SUCCESS;
        }
        ItemStack servingOne = this.getaltServingOneItem(state);
        ItemStack servingTwo = this.getaltServingTwoItem(state);
        ItemStack servingThree = this.getServingItem(state);

        if (servings > 0) {
            ItemStack selectedServing = determineServing(servingOne, servingTwo, servingThree, pos, state, player);
            if (selectedServing != null && processServing(level, pos, state, player, hand, selectedServing, servings)) {
                level.setBlock(pos, state.setValue(this.getServingsProperty(), servings - 1), 3);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    protected ItemStack determineServing(ItemStack servingOne, ItemStack servingTwo, ItemStack servingThree, BlockPos pos, BlockState state, Player player) {
        int maxServings = getMaxServings();
        int servings = state.getValue(getServingsProperty());
        if (servings > (maxServings - altServingOneCount)) {
            return servingOne;
        }
        if (servings > (maxServings - (altServingOneCount + altServingTwoCount))) {
            return servingTwo;
        }
        return servingThree;
    }

}
