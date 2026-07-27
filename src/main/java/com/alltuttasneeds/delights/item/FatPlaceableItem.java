package com.alltuttasneeds.delights.item;

import com.alltuttasneeds.delights.DelightsTextUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.item.PlaceableItem;

public class FatPlaceableItem extends PlaceableItem {
    public FatPlaceableItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    public InteractionResult place(BlockPlaceContext context) {
        BlockState potentialState = this.getBlock().getStateForPlacement(context);
        if (potentialState == null) {
            Level level = context.getLevel();
            if (level.isClientSide()) {
                Player player = context.getPlayer();
                if (player != null) {
                    player.displayClientMessage(DelightsTextUtils.block("feast.space_required"), true);
                }
            }

            return InteractionResult.FAIL;
        } else {
            return super.place(context);
        }
    }
}
