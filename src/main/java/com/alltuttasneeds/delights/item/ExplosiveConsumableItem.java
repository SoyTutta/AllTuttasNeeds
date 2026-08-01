package com.alltuttasneeds.delights.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public abstract class ExplosiveConsumableItem extends ConsumableItem {
    private final float explosionChance;
    private final float explosionDamage;

    protected ExplosiveConsumableItem(Properties properties, float explosionChance, float explosionDamage) {
        super(properties);
        this.explosionChance = explosionChance;
        this.explosionDamage = explosionDamage;
    }

    protected void tryExplode(Level level, LivingEntity consumer) {
        if (level.getRandom().nextFloat() < explosionChance) {
            Creeper creeper = new Creeper(EntityType.CREEPER, level);
            creeper.setPos(consumer.position());
            level.explode(creeper, consumer.getX(), consumer.getEyeY(), consumer.getZ(), 0.25F, Level.ExplosionInteraction.NONE);
            consumer.hurt(level.damageSources().explosion(creeper, creeper), explosionDamage);
        }
    }
}
