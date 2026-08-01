package com.alltuttasneeds.delights.item;

import com.alltuttasneeds.delights.DelightsTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class SlimeConsumableItem extends ConsumableItem {
    private static final int EFFECT_TRANSFER_DURATION = 200;
    private static final double EFFECT_STEAL_PERCENTAGE_PER_LEVEL = 0.10;
    private static final double MAX_EFFECT_STEAL_PERCENTAGE = 0.50;
    private static final int HUNGER_COST_PER_LEVEL = 4;
    private static final ThreadLocal<Boolean> DEFER_SLIME_EFFECTS = new ThreadLocal<>();
    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;

    public SlimeConsumableItem(Properties properties) {
        super(properties);
        this.hasFoodEffectTooltip = false;
        this.hasCustomTooltip = false;
    }

    public SlimeConsumableItem(Properties properties, boolean hasFoodEffectTooltip) {
        super(properties);
        this.hasFoodEffectTooltip = hasFoodEffectTooltip;
        this.hasCustomTooltip = false;
    }

    public SlimeConsumableItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties);
        this.hasFoodEffectTooltip = hasFoodEffectTooltip;
        this.hasCustomTooltip = hasCustomTooltip;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        if (level.isClientSide) return super.finishUsingItem(stack, level, consumer);

        ItemStack result;
        DEFER_SLIME_EFFECTS.set(true);
        try {
            result = super.finishUsingItem(stack, level, consumer);
        } finally {
            DEFER_SLIME_EFFECTS.remove();
        }

        affectConsumer(stack, level, consumer);
        return result;
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if (Boolean.TRUE.equals(DEFER_SLIME_EFFECTS.get())) return;

        if (!level.isClientSide) {
            MobEffectInstance currentEffect = consumer.getEffect(MobEffects.OOZING);
            int newAmplifier = 0;
            int newDuration = 600;
            RandomSource random = level.getRandom();

            if (currentEffect != null) {
                int currentDuration = currentEffect.getDuration();
                int currentAmplifier = currentEffect.getAmplifier();

                if (currentDuration >= 1200 * (currentAmplifier + 1) && currentAmplifier < 4) {
                    newAmplifier = currentAmplifier + 1;
                    newDuration = 1200;
                } else {
                    newAmplifier = currentAmplifier;
                    newDuration = Math.min(currentDuration + 200, 6000);
                }

                double slimeChance = 0.15 * (newAmplifier + 1);

                if (random.nextDouble() < slimeChance) {
                    int effectLevel = newAmplifier + 1;
                    boolean maximumLevel = effectLevel == 5;
                    int slimeCount = maximumLevel ? 1 : effectLevel;
                    int slimeSize = maximumLevel ? 2 : 1;

                    List<Slime> spawnedSlimes = new ArrayList<>();
                    int successfulLevels = 0;

                    for (int i = 0; i < slimeCount; i++) {
                        Slime slime = EntityType.SLIME.create(level);
                        if (slime != null) {
                            slime.setSize(slimeSize, true);
                            slime.setPos(consumer.getX() + (random.nextDouble() - 0.5) * 2,
                                    consumer.getY() + consumer.getEyeHeight(),
                                    consumer.getZ() + (random.nextDouble() - 0.5) * 2);

                            if (level.addFreshEntity(slime)) {
                                spawnedSlimes.add(slime);
                                successfulLevels += maximumLevel ? effectLevel : 1;
                            }
                        }
                    }

                    if (!spawnedSlimes.isEmpty()) {
                        if (consumer instanceof Player player) {
                            player.getFoodData().setFoodLevel(Math.max(0,
                                    player.getFoodData().getFoodLevel() - successfulLevels * HUNGER_COST_PER_LEVEL));
                            transferEffects(player, spawnedSlimes, successfulLevels);
                        }
                        consumer.hurt(level.damageSources().mobAttack(spawnedSlimes.get(spawnedSlimes.size() - 1)), successfulLevels);
                    }
                }
            }

            MobEffectInstance newEffect = new MobEffectInstance(MobEffects.OOZING, newDuration, newAmplifier);
            consumer.addEffect(newEffect);
        }
    }

    private static void transferEffects(Player player, List<Slime> slimes, int effectLevels) {
        int slimeCount = slimes.size();
        if (slimeCount == 0) return;

        double totalShare = Math.min(effectLevels * EFFECT_STEAL_PERCENTAGE_PER_LEVEL, MAX_EFFECT_STEAL_PERCENTAGE);

        for (MobEffectInstance effect : List.copyOf(player.getActiveEffects())) {
            if (effect.is(MobEffects.OOZING)) continue;

            if (effect.isInfiniteDuration()) {
                for (Slime slime : slimes) {
                    slime.addEffect(new MobEffectInstance(
                            effect.getEffect(),
                            EFFECT_TRANSFER_DURATION,
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()));
                }
                continue;
            }

            int totalTransferDuration = (int) (effect.getDuration() * totalShare);
            if (totalTransferDuration <= 0) continue;

            int perSlimeDuration = totalTransferDuration / slimeCount;
            int durationRemainder = totalTransferDuration % slimeCount;

            int totalTransferred = 0;
            for (int i = 0; i < slimeCount; i++) {
                int transferDuration = perSlimeDuration + (i < durationRemainder ? 1 : 0);
                if (transferDuration <= 0) continue;

                MobEffectInstance transferredEffect = new MobEffectInstance(
                        effect.getEffect(),
                        transferDuration,
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.isVisible(),
                        effect.showIcon());
                if (slimes.get(i).addEffect(transferredEffect)) {
                    totalTransferred += transferDuration;
                }
            }
            if (totalTransferred <= 0) continue;

            int remainingDuration = effect.getDuration() - totalTransferred;
            MobEffectInstance remainingEffect = remainingDuration > 0
                    ? copyWithDuration(effect, remainingDuration)
                    : null;
            player.removeEffect(effect.getEffect());
            if (remainingEffect != null) {
                player.addEffect(remainingEffect);
            }
        }
    }

    private static MobEffectInstance copyWithDuration(MobEffectInstance effect, int duration) {
        if (effect.save() instanceof CompoundTag effectTag) {
            effectTag.putInt("duration", duration);
            MobEffectInstance copy = MobEffectInstance.load(effectTag);
            if (copy != null) return copy;
        }

        return new MobEffectInstance(
                effect.getEffect(),
                duration,
                effect.getAmplifier(),
                effect.isAmbient(),
                effect.isVisible(),
                effect.showIcon());
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
