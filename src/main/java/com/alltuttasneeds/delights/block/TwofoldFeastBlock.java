package com.alltuttasneeds.delights.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.function.Supplier;

public abstract class TwofoldFeastBlock extends FeastBlock {
    public final Supplier<Item> altServingOneItem;
    public final int altServingOneCount;

    public TwofoldFeastBlock(Properties properties, Supplier<Item> altServingOneItem, int altServingOneCount, Supplier<Item> servingItem, boolean hasLeftovers) {
        super(properties, servingItem, hasLeftovers);
        this.altServingOneItem = altServingOneItem;
        this.altServingOneCount = altServingOneCount;
    }

    public ItemStack getaltServingOneItem(BlockState state) {
        return new ItemStack(this.altServingOneItem.get());
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
        ItemStack servingTwo = this.getServingItem(state);

        if (servings > 0) {
            ItemStack selectedServing = determineServing(servingOne, servingTwo, pos, state, player);
            if (selectedServing != null && processServing(level, pos, state, player, hand, selectedServing, servings)) {
                level.setBlock(pos, state.setValue(this.getServingsProperty(), servings - 1), 3);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    boolean processServing(LevelAccessor level, BlockPos pos, BlockState state, Player player, InteractionHand hand, ItemStack selectedServing, int servings) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (!selectedServing.hasCraftingRemainingItem() || ItemStack.isSameItem(heldStack, selectedServing.getCraftingRemainingItem())) {
            level.setBlock(pos, state.setValue(this.getServingsProperty(), servings - 1), 3);
            if (!player.getAbilities().instabuild && selectedServing.hasCraftingRemainingItem()) {
                heldStack.shrink(1);
            }

            if (!player.getInventory().add(selectedServing)) {
                player.drop(selectedServing, false);
            }

            if (level.getBlockState(pos).getValue(this.getServingsProperty()) == 0 && !this.hasLeftovers) {
                level.removeBlock(pos, false);
            }

            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        } else {
            displayUseContainerMessage(state, player, selectedServing);
            return false;
        }
    }

    public void displayUseContainerMessage(BlockState state, Player player, ItemStack selectedServing) {
        player.displayClientMessage(TextUtils.block("feast.use_container", selectedServing.getCraftingRemainingItem().getHoverName()), true);
    }

    protected ItemStack determineServing(ItemStack servingOne, ItemStack servingTwo, BlockPos pos, BlockState state, Player player) {
        int maxServings = getMaxServings();
        int servings = state.getValue(getServingsProperty());
        if (servings > (maxServings - altServingOneCount)) {
            return servingOne;
        }
        return servingTwo;
    }
}
