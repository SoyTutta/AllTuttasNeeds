package com.alltuttasneeds.delights.item;

import com.alltuttasneeds.delights.DelightsTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class MintConsumableItem extends ExplosiveConsumableItem {
    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;

    public MintConsumableItem(Properties properties) {
        this(properties, 0.05F, 8.0F);
    }

    public MintConsumableItem(Properties properties, float explosionChance) {
        this(properties, explosionChance, 8.0F);
    }

    public MintConsumableItem(Properties properties, float explosionChance, float explosionDamage) {
        this(properties, explosionChance, explosionDamage, false, false);
    }

    public MintConsumableItem(Properties properties, boolean hasFoodEffectTooltip) {
        this(properties, 0.05F, 8.0F, hasFoodEffectTooltip, false);
    }

    public MintConsumableItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        this(properties, 0.05F, 8.0F, hasFoodEffectTooltip, hasCustomTooltip);
    }

    public MintConsumableItem(Properties properties, float explosionChance, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        this(properties, explosionChance, 8.0F, hasFoodEffectTooltip, hasCustomTooltip);
    }

    public MintConsumableItem(Properties properties, float explosionChance, float explosionDamage,
                              boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, explosionChance, explosionDamage);
        this.hasFoodEffectTooltip = hasFoodEffectTooltip;
        this.hasCustomTooltip = hasCustomTooltip;
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if (consumer.hasEffect(MobEffects.CONFUSION)) {
            consumer.removeEffect(MobEffects.CONFUSION);
        } else if (consumer.hasEffect(MobEffects.POISON)) {
            consumer.removeEffect(MobEffects.POISON);
        }
        tryExplode(level, consumer);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            if (this.hasCustomTooltip) {
                MutableComponent textEmpty = DelightsTextUtils.tooltip(BuiltInRegistries.ITEM.getKey(this).getPath());
                tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
            }
            if (this.hasFoodEffectTooltip) {
                TextUtils.addFoodEffectTooltip(stack, tooltip::add, 1.0F, context.tickRate());
            }
        }
    }
}
