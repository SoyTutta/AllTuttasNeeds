package com.alltuttasneeds.core.config;

import com.alltuttasneeds.beds.config.TBConfig;
import com.alltuttasneeds.doors.config.TDConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ATNConfig {
    private ATNConfig() {}

    public static final ModConfigSpec SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("doors");
        TDConfig.init(builder);
        builder.pop();

        builder.push("beds");
        TBConfig.init(builder);
        builder.pop();

        SPEC = builder.build();
    }

    public static void register(ModContainer container) {
        container.registerConfig(ModConfig.Type.STARTUP, SPEC);
    }
}
