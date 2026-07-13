package com.alltuttasneeds.doors.config;

import com.alltuttasneeds.doors.compat.DoorVariant;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public final class TDConfig {
    private TDConfig() {}

    public static ModConfigSpec.BooleanValue moduleEnabled;
    public static ModConfigSpec.BooleanValue tooltipsEnabled;
    public static ModConfigSpec.BooleanValue transitAutomaticOpeningEnabled;
    public static ModConfigSpec.BooleanValue petAutomaticOpeningEnabled;
    public static ModConfigSpec.BooleanValue invertAutomaticClosingRedstone;

    private static final Map<DoorSet, ModConfigSpec.BooleanValue> SET_ENABLED = new EnumMap<>(DoorSet.class);

    public static void init(ModConfigSpec.Builder builder) {
        builder.push("general");
        moduleEnabled = builder
                .comment("Master switch for the whole doors module. Disabling registers none of its content and hides its creative tab.")
                .define("moduleEnabled", true);
        builder.pop();

        builder.push("content");
        for (DoorSet set : DoorSet.values()) {
            String key = set.name().toLowerCase(Locale.ROOT) + "Enabled";
            ModConfigSpec.BooleanValue value = builder
                    .comment("Whether the " + set.name().toLowerCase(Locale.ROOT) + " door set is registered, for every wood family/compat mod.")
                    .define(key, true);
            SET_ENABLED.put(set, value);
        }
        builder.pop();

        builder.push("behavior");
        tooltipsEnabled = builder
                .comment("Whether Tuttas Doors adds its style tooltips to door items.")
                .define("tooltipsEnabled", true);
        transitAutomaticOpeningEnabled = builder
                .comment("Whether Transit Doors open automatically for eligible entities.")
                .define("transitAutomaticOpeningEnabled", true);
        petAutomaticOpeningEnabled = builder
                .comment("Whether Pet Doors open automatically for entities.")
                .define("petAutomaticOpeningEnabled", true);
        invertAutomaticClosingRedstone = builder
                .comment("Normally automatic opening and closing are allowed without redstone and blocked while powered.",
                        "When enabled, both automatic behaviors are allowed only while powered.")
                .define("invertAutomaticClosingRedstone", false);
        builder.pop();
    }

    public static boolean isModuleEnabled() {
        return moduleEnabled.get();
    }

    public static boolean isSetEnabled(DoorSet set) {
        return moduleEnabled.get() && SET_ENABLED.get(set).get();
    }

    public static boolean isVariantEnabled(DoorVariant variant) {
        DoorSet set = variant.set();
        return set == null || isSetEnabled(set);
    }

    public static boolean anySetEnabled() {
        if (!moduleEnabled.get()) return false;
        for (ModConfigSpec.BooleanValue value : SET_ENABLED.values()) {
            if (value.get()) return true;
        }
        return false;
    }

    public static boolean shouldAutomaticallyClose(boolean powered) {
        return invertAutomaticClosingRedstone.get() == powered;
    }
}
