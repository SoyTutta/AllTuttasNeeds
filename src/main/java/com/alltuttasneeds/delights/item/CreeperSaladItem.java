package com.alltuttasneeds.delights.item;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.Random;

public class CreeperSaladItem extends ConsumableItem {

    public CreeperSaladItem(Properties properties) {
        super(properties);
    }

    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if (consumer.hasEffect(MobEffects.CONFUSION)) {
            consumer.removeEffect(MobEffects.CONFUSION);
        } else if (consumer.hasEffect(MobEffects.POISON)) {
            consumer.removeEffect(MobEffects.POISON);
        }

        Random random = new Random();
        if (random.nextDouble() < 0.5) {
            level.explode(null, consumer.getX(), consumer.getEyeY(), consumer.getZ(), 0.25F, Level.ExplosionInteraction.NONE);
            consumer.hurt(consumer.damageSources().magic(), 4.0F);
        }
    }
}
