package com.alltuttasneeds.delights.item;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CreeperSaladItem extends ExplosiveConsumableItem {

    public CreeperSaladItem(Properties properties) {
        this(properties, 0.5F, 4.0F);
    }

    public CreeperSaladItem(Properties properties, float explosionChance) {
        this(properties, explosionChance, 4.0F);
    }

    public CreeperSaladItem(Properties properties, float explosionChance, float explosionDamage) {
        super(properties, explosionChance, explosionDamage);
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
}
