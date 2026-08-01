package com.alltuttasneeds.delights.item;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MintPopsicleItem extends ExplosiveConsumableItem {

    public MintPopsicleItem(Properties properties) {
        this(properties, 0.5F, 8.0F);
    }

    public MintPopsicleItem(Properties properties, float explosionChance) {
        this(properties, explosionChance, 8.0F);
    }

    public MintPopsicleItem(Properties properties, float explosionChance, float explosionDamage) {
        super(properties, explosionChance, explosionDamage);
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        consumer.clearFire();
        if (consumer.hasEffect(MobEffects.CONFUSION) || consumer.hasEffect(MobEffects.POISON)) {
            consumer.removeEffect(MobEffects.CONFUSION);
            consumer.removeEffect(MobEffects.POISON);
        }
        tryExplode(level, consumer);
    }
}
