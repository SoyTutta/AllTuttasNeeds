package com.alltuttasneeds.delights.item;

import com.alltuttasneeds.delights.DelightsTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.List;

public class RottenArmItem extends ConsumableItem {

    public RottenArmItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            MutableComponent textEmpty = DelightsTextUtils.tooltip(BuiltInRegistries.ITEM.getKey(this).getPath());
            tooltip.add(textEmpty.withStyle(ChatFormatting.DARK_GREEN));
        }
    }
}
