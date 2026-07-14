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
    public static ModConfigSpec.BooleanValue transitAutomaticClosingEnabled;
    public static ModConfigSpec.BooleanValue petAutomaticOpeningEnabled;
    public static ModConfigSpec.BooleanValue petAutomaticClosingEnabled;
    public static ModConfigSpec.IntValue automaticClosingDelayTicks;
    public static ModConfigSpec.BooleanValue invertAutomaticClosingRedstone;

    private static final Map<DoorSet, ModConfigSpec.BooleanValue> SET_ENABLED = new EnumMap<>(DoorSet.class);

    public static void init(ModConfigSpec.Builder builder) {
        builder.push("general");
        moduleEnabled = builder
                .comment("Master switch for Tutta's Doors. Disabling it removes the module's content and creative tab",
                        "and leaves vanilla and other mods' doors unchanged.")
                .define("moduleEnabled", true);
        builder.pop();

        builder.push("content");
        for (DoorSet set : DoorSet.values()) {
            String key = set.name().toLowerCase(Locale.ROOT) + "Enabled";
            ModConfigSpec.BooleanValue value = builder
                    .comment("Enables the " + set.name().toLowerCase(Locale.ROOT) + " door set for every available wood family.")
                    .define(key, true);
            SET_ENABLED.put(set, value);
        }
        builder.pop();

        builder.push("behavior");
        tooltipsEnabled = builder
                .comment("Shows Tutta's Doors style descriptions on door items.")
                .define("tooltipsEnabled", true);
        transitAutomaticOpeningEnabled = builder
                .comment("Allows Transit Doors to open automatically for eligible entities.")
                .define("transitAutomaticOpeningEnabled", true);
        transitAutomaticClosingEnabled = builder
                .comment("Allows Transit Doors to close automatically after entities move away.")
                .define("transitAutomaticClosingEnabled", true);
        petAutomaticOpeningEnabled = builder
                .comment("Allows Pet Doors to open automatically for entities.")
                .define("petAutomaticOpeningEnabled", true);
        petAutomaticClosingEnabled = builder
                .comment("Allows Pet Doors to close automatically after entities move away.")
                .define("petAutomaticClosingEnabled", true);
        automaticClosingDelayTicks = builder
                .comment("Delay before an automatically closing door checks whether it can close.",
                        "20 ticks equals one second.")
                .defineInRange("automaticClosingDelayTicks", 20, 1, 1200);
        invertAutomaticClosingRedstone = builder
                .comment("By default, automatic opening and closing work without redstone and stop while powered.",
                        "Enable this to make both behaviors work only while powered.")
                .define("invertAutomaticClosingRedstone", false);
        builder.pop();
    }

    public static boolean isModuleEnabled() {
        return moduleEnabled != null && moduleEnabled.get();
    }

    public static boolean isSetEnabled(DoorSet set) {
        return isModuleEnabled() && SET_ENABLED.get(set).get();
    }

    public static boolean isVariantEnabled(DoorVariant variant) {
        DoorSet set = variant.set();
        return set == null || isSetEnabled(set);
    }

    public static boolean anySetEnabled() {
        if (!isModuleEnabled()) return false;
        for (ModConfigSpec.BooleanValue value : SET_ENABLED.values()) {
            if (value.get()) return true;
        }
        return false;
    }

    public static boolean shouldAutomaticallyClose(boolean powered) {
        return invertAutomaticClosingRedstone.get() == powered;
    }

    public static int automaticClosingDelay() {
        return automaticClosingDelayTicks.get();
    }
}
