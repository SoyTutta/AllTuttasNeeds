package com.alltuttasneeds.beds;

import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public final class BedTierSyncState {
    private static volatile Snapshot snapshot = Snapshot.local();

    private BedTierSyncState() {}

    public static void apply(Map<ResourceLocation, BedTier> tiers,
                             boolean moduleEnabled,
                             boolean tieredSleepDurationEnabled,
                             boolean vanillaBedsUseTieredSleepDuration,
                             Map<BedTier, Double> sleepDurationMultipliers) {
        snapshot = new Snapshot(
                true,
                Map.copyOf(tiers),
                moduleEnabled,
                tieredSleepDurationEnabled,
                vanillaBedsUseTieredSleepDuration,
                copyMultipliers(sleepDurationMultipliers));
    }

    public static void clear() {
        snapshot = Snapshot.local();
    }

    @Nullable
    public static BedTier resolveForClient(Block block) {
        Snapshot current = snapshot;
        if (!current.synced()) return BedTierResolver.resolve(block);
        return current.tiers().get(BuiltInRegistries.BLOCK.getKey(block));
    }

    public static boolean tieredSleepDurationEnabled() {
        Snapshot current = snapshot;
        return current.synced()
                ? current.tieredSleepDurationEnabled()
                : TBConfig.tieredSleepDurationEnabled.get();
    }

    public static boolean moduleEnabled() {
        Snapshot current = snapshot;
        return current.synced() ? current.moduleEnabled() : TBConfig.isModuleEnabled();
    }

    public static boolean vanillaBedsUseTieredSleepDuration() {
        Snapshot current = snapshot;
        return current.synced()
                ? current.vanillaBedsUseTieredSleepDuration()
                : TBConfig.vanillaBedsUseTieredSleepDuration.get();
    }

    public static double sleepDurationMultiplier(BedTier tier) {
        Snapshot current = snapshot;
        return current.synced()
                ? current.sleepDurationMultipliers().getOrDefault(tier, 1.0D)
                : TBConfig.sleepDurationMultiplier(tier);
    }

    private static Map<BedTier, Double> copyMultipliers(Map<BedTier, Double> multipliers) {
        EnumMap<BedTier, Double> copy = new EnumMap<>(BedTier.class);
        copy.putAll(multipliers);
        return Map.copyOf(copy);
    }

    private record Snapshot(boolean synced,
                            Map<ResourceLocation, BedTier> tiers,
                            boolean moduleEnabled,
                            boolean tieredSleepDurationEnabled,
                            boolean vanillaBedsUseTieredSleepDuration,
                            Map<BedTier, Double> sleepDurationMultipliers) {
        private static Snapshot local() {
            return new Snapshot(false, Map.of(), false, false, false, Map.of());
        }
    }
}
