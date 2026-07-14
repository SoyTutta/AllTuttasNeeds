package com.alltuttasneeds.beds.config;

import com.alltuttasneeds.beds.BedTier;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public final class TBConfig {
    private TBConfig() {}

    public static ModConfigSpec.BooleanValue moduleEnabled;
    public static ModConfigSpec.BooleanValue tooltipsEnabled;

    public static ModConfigSpec.BooleanValue strawMattressEnabled;
    public static ModConfigSpec.BooleanValue softMattressEnabled;

    public static ModConfigSpec.BooleanValue wheatCoverEnabled;
    public static ModConfigSpec.BooleanValue leatherCoverEnabled;

    public static ModConfigSpec.BooleanValue woolBlanketEnabled;
    public static ModConfigSpec.BooleanValue leatherBlanketEnabled;
    public static ModConfigSpec.BooleanValue deluxeTierEnabled;

    public static ModConfigSpec.BooleanValue vanillaBedsSetSpawn;
    public static ModConfigSpec.BooleanValue villagersCanUseTuttaBeds;
    public static ModConfigSpec.BooleanValue tieredSleepDurationEnabled;
    public static ModConfigSpec.BooleanValue vanillaBedsUseTieredSleepDuration;
    public static ModConfigSpec.BooleanValue vanillaBedsUseTierSpawnRules;
    public static ModConfigSpec.BooleanValue vanillaBedsUseTierWakeEffects;
    public static ModConfigSpec.BooleanValue deluxeIgnoresNearbyMonsters;
    public static TierGameplayConfig basicTierGameplay;
    public static TierGameplayConfig lowTierGameplay;
    public static TierGameplayConfig normalTierGameplay;
    public static TierGameplayConfig deluxeTierGameplay;

    public static ModConfigSpec.DoubleValue basicTierSleepMultiplier;
    public static ModConfigSpec.DoubleValue lowTierSleepMultiplier;
    public static ModConfigSpec.DoubleValue normalTierSleepMultiplier;
    public static ModConfigSpec.DoubleValue deluxeTierSleepMultiplier;
    public static SleepEffectConfig vanillaBedsEffects;

    public static SleepEffectConfig basicTierEffects;
    public static SleepEffectConfig lowTierEffects;
    public static SleepEffectConfig normalTierEffects;
    public static SleepEffectConfig deluxeTierEffects;

    public static ModConfigSpec.BooleanValue canvasMattressEnabled;
    public static ModConfigSpec.BooleanValue farmersDelightStrawMattressEnabled;
    public static ModConfigSpec.BooleanValue canvasCoverEnabled;

    public static ModConfigSpec.ConfigValue<List<? extends String>> directApplyDisabled;

    public static void init(ModConfigSpec.Builder builder) {
        builder.push("general");
        moduleEnabled = builder
                .comment("Master switch for the whole beds module. Disabling registers none of its content",
                        "and leaves vanilla and other mods' bed behavior unchanged.")
                .define("moduleEnabled", true);
        tooltipsEnabled = builder
                .comment("Shows Tutta's Beds descriptions on bed items.")
                .define("tooltipsEnabled", true);
        builder.pop();

        builder.push("content");
        builder.push("mattresses");
        strawMattressEnabled = builder
                .comment("Disabling also disables every straw-mattress bed variant.")
                .define("strawMattressEnabled", true);
        softMattressEnabled = builder
                .comment("Disabling also disables every soft-mattress bed variant.")
                .define("softMattressEnabled", true);
        builder.pop();

        builder.push("basicCovers");
        wheatCoverEnabled = builder
                .comment("Disabling also disables every Tier LOW wheat-covered variant.")
                .define("wheatCoverEnabled", true);
        leatherCoverEnabled = builder
                .comment("Disabling also disables every Tier LOW raw-leather-covered variant.")
                .define("leatherCoverEnabled", true);
        builder.pop();

        builder.push("blankets");
        woolBlanketEnabled = builder
                .comment("Disabling also disables every Tier NORMAL wool-blanket bed and, since it's",
                        "the only blanket with a Tier DELUXE upgrade path, every Tier DELUXE bed too.")
                .define("woolBlanketEnabled", true);
        leatherBlanketEnabled = builder
                .comment("Disabling also disables every Tier NORMAL leather-blanket bed.")
                .define("leatherBlanketEnabled", true);
        deluxeTierEnabled = builder
                .comment("Master switch for Tier DELUXE specifically, independent of woolBlanketEnabled.")
                .define("deluxeTierEnabled", true);
        builder.pop();
        builder.pop();

        builder.push("gameplay");
        builder.push("tiers");
        tieredSleepDurationEnabled = builder
                .comment("Whether the time required to complete sleep changes with the bed tier.",
                        "Other mods' beds are treated as Tier BASIC.")
                .define("tieredSleepDurationEnabled", true);
        vanillaBedsUseTieredSleepDuration = builder
                .comment("Whether vanilla Minecraft beds use the Tier BASIC sleep-duration multiplier.",
                        "When disabled, vanilla beds always keep vanilla's five-second sleep duration.")
                .define("vanillaBedsUseTieredSleepDuration", true);
        vanillaBedsUseTierSpawnRules = builder
                .comment("Whether vanilla Minecraft beds use their resolved tier's respawn rule.",
                        "When disabled, vanilla beds always set spawn normally.")
                .define("vanillaBedsUseTierSpawnRules", true);
        vanillaBedsUseTierWakeEffects = builder
                .comment("Whether vanilla Minecraft beds apply their resolved tier's wake effect.")
                .define("vanillaBedsUseTierWakeEffects", true);
        basicTierGameplay = TierGameplayConfig.define(builder, "basic", false, 2.0D, false, 0);
        lowTierGameplay = TierGameplayConfig.define(builder, "low", true, 1.5D, false, 0);
        normalTierGameplay = TierGameplayConfig.define(builder, "normal", true, 1.0D, true, 5);
        deluxeTierGameplay = TierGameplayConfig.define(builder, "deluxe", true, 0.5D, true, 10);
        builder.push("deluxe");
        deluxeIgnoresNearbyMonsters = builder
                .comment("Whether Tier DELUXE beds allow sleeping while nearby monsters would normally prevent it.")
                .define("ignoresNearbyMonsters", true);
        builder.pop();
        builder.pop();

        vanillaBedsSetSpawn = basicTierGameplay.setsSpawn;
        vanillaBedsEffects = basicTierGameplay.wakeEffect;
        basicTierEffects = basicTierGameplay.wakeEffect;
        lowTierEffects = lowTierGameplay.wakeEffect;
        normalTierEffects = normalTierGameplay.wakeEffect;
        deluxeTierEffects = deluxeTierGameplay.wakeEffect;
        basicTierSleepMultiplier = basicTierGameplay.sleepDurationMultiplier;
        lowTierSleepMultiplier = lowTierGameplay.sleepDurationMultiplier;
        normalTierSleepMultiplier = normalTierGameplay.sleepDurationMultiplier;
        deluxeTierSleepMultiplier = deluxeTierGameplay.sleepDurationMultiplier;

        builder.push("villagers");
        villagersCanUseTuttaBeds = builder
                .comment("Allows villagers to claim and sleep in Tutta's Beds, loose mattresses and bed frames.")
                .define("canUseTuttaBeds", true);
        builder.pop();

        builder.push("interactions");
        directApplyDisabled = builder
                .comment("Cover and blanket suffixes that cannot be applied directly to a mattress or bed.",
                        "This only disables the interaction shortcut; normal recipes still work.",
                        "The list applies to native and compatible materials with the same suffix.")
                .defineList("directApplyDisabled", List.of("wool_blanket", "leather_blanket"), o -> o instanceof String);
        builder.pop();
        builder.pop();

        builder.push("compatibility");
        builder.push("farmersDelight");
        canvasMattressEnabled = builder
                .comment("Disabling also disables every canvas-mattress bed variant.")
                .define("canvasMattressEnabled", true);
        farmersDelightStrawMattressEnabled = builder
                .comment("Disabling also disables every Farmer's Delight straw-mattress bed variant",
                        "(independent of content.mattresses.strawMattressEnabled, which is this module's own wheat mattress).")
                .define("strawMattressEnabled", true);
        canvasCoverEnabled = builder
                .comment("Disabling also disables every Tier LOW canvas-covered variant, for every material.")
                .define("canvasCoverEnabled", true);
        builder.pop();
        builder.pop();
    }

    public static SleepEffectConfig effectsFor(BedTier tier) {
        return gameplayFor(tier).wakeEffect;
    }

    public static boolean isModuleEnabled() {
        return moduleEnabled != null && moduleEnabled.get();
    }

    public static boolean setsSpawn(BedTier tier) {
        return gameplayFor(tier).setsSpawn.get();
    }

    public static double sleepDurationMultiplier(BedTier tier) {
        return gameplayFor(tier).sleepDurationMultiplier.get();
    }

    private static TierGameplayConfig gameplayFor(BedTier tier) {
        return switch (tier) {
            case BASIC -> basicTierGameplay;
            case LOW -> lowTierGameplay;
            case NORMAL -> normalTierGameplay;
            case DELUXE -> deluxeTierGameplay;
        };
    }
}
