package com.alltuttasneeds.beds.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public final class TierGameplayConfig {
    public final ModConfigSpec.BooleanValue setsSpawn;
    public final ModConfigSpec.DoubleValue sleepDurationMultiplier;
    public final SleepEffectConfig wakeEffect;
    public final ModConfigSpec.ConfigValue<List<? extends String>> blockOverrides;

    private TierGameplayConfig(ModConfigSpec.BooleanValue setsSpawn,
                               ModConfigSpec.DoubleValue sleepDurationMultiplier,
                               SleepEffectConfig wakeEffect,
                               ModConfigSpec.ConfigValue<List<? extends String>> blockOverrides) {
        this.setsSpawn = setsSpawn;
        this.sleepDurationMultiplier = sleepDurationMultiplier;
        this.wakeEffect = wakeEffect;
        this.blockOverrides = blockOverrides;
    }

    static TierGameplayConfig define(ModConfigSpec.Builder builder, String section, boolean defaultSetsSpawn,
                                     double defaultSleepMultiplier, boolean defaultEffectEnabled,
                                     int defaultEffectDurationSeconds) {
        builder.push(section);
        ModConfigSpec.BooleanValue setsSpawn = builder
                .comment("Whether beds in this tier set the player's respawn point.")
                .define("setsSpawn", defaultSetsSpawn);
        ModConfigSpec.DoubleValue sleepDurationMultiplier = builder
                .comment("Multiplier applied to vanilla's five-second sleep duration.")
                .defineInRange("sleepDurationMultiplier", defaultSleepMultiplier, 0.1D, 10.0D);
        SleepEffectConfig wakeEffect = SleepEffectConfig.define(
                builder, "wakeEffect", defaultEffectEnabled, "minecraft:regeneration", defaultEffectDurationSeconds);
        ModConfigSpec.ConfigValue<List<? extends String>> blockOverrides = builder
                .comment("Optional block IDs assigned to this tier, overriding their automatic classification.",
                        "Use a full ID such as \"minecraft:red_bed\" or a namespace wildcard such as \"minecraft:*\".",
                        "If a block appears in multiple tiers, the highest tier wins.")
                .defineList("blockOverrides", List.of(), TierGameplayConfig::isValidBlockPattern);
        builder.pop();
        return new TierGameplayConfig(setsSpawn, sleepDurationMultiplier, wakeEffect, blockOverrides);
    }

    private static boolean isValidBlockPattern(Object value) {
        if (!(value instanceof String string)) return false;
        if (string.endsWith(":*")) {
            String namespace = string.substring(0, string.length() - 2);
            return net.minecraft.resources.ResourceLocation.tryParse(namespace + ":placeholder") != null;
        }
        return net.minecraft.resources.ResourceLocation.tryParse(string) != null;
    }
}
