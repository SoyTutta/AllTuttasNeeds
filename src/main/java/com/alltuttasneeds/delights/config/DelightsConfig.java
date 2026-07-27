package com.alltuttasneeds.delights.config;

import com.alltuttasneeds.core.Mods;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public final class DelightsConfig {
    private DelightsConfig() {}

    public static ModConfigSpec.BooleanValue moduleEnabled;
    public static ModConfigSpec.BooleanValue useCheeseWedges;

    private static final Map<DelightGroup, ModConfigSpec.BooleanValue> GROUP_ENABLED =
            new EnumMap<>(DelightGroup.class);

    public static void init(ModConfigSpec.Builder builder) {
        builder.push("general");
        moduleEnabled = builder
                .comment("Master switch for Tutta's Delights.",
                        "The module only loads when Farmer's Delight is installed.")
                .define("moduleEnabled", true);
        builder.pop();

        builder.push("content");
        for (DelightGroup group : DelightGroup.values()) {
            if (!group.configurable()) continue;
            GROUP_ENABLED.put(group, builder
                    .comment("Enables the " + group.name().toLowerCase(Locale.ROOT).replace('_', ' ') + " content group.")
                    .define(group.configKey() + "Enabled", true));
        }
        builder.pop();

        builder.push("recipes");
        useCheeseWedges = builder
                .comment("Uses cheese wedges instead of milk in recipes where milk represents cheese.",
                        "The milk recipe is used when Brewin' and Chewin' is not installed.")
                .define("useCheeseWedges", false);
        builder.pop();
    }

    public static boolean isModuleEnabled() {
        return moduleEnabled != null && moduleEnabled.get() && Mods.FARMERS_DELIGHT.isLoaded();
    }

    public static boolean isGroupEnabled(DelightGroup group) {
        if (!group.configurable()) return false;
        ModConfigSpec.BooleanValue value = GROUP_ENABLED.get(group);
        return isModuleEnabled() && value != null && value.get();
    }

    public static boolean areGroupsEnabled(DelightGroup... groups) {
        for (DelightGroup group : groups) {
            if (!isGroupEnabled(group)) return false;
        }
        return true;
    }

    public static boolean anyGroupEnabled() {
        if (!isModuleEnabled()) return false;
        for (ModConfigSpec.BooleanValue value : GROUP_ENABLED.values()) {
            if (value.get()) return true;
        }
        return false;
    }

    public static boolean useCheeseWedges() {
        return useCheeseWedges != null && useCheeseWedges.get();
    }
}
