package com.alltuttasneeds.delights.item;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class MintmallowBitItem extends ConsumableItem {

    public MintmallowBitItem(Properties properties) {
        super(properties);
    }

    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if (consumer.hasEffect(MobEffects.CONFUSION) || consumer.hasEffect(MobEffects.POISON)) {
            consumer.removeEffect(MobEffects.CONFUSION);
            consumer.removeEffect(MobEffects.POISON);
        }
    }
}
