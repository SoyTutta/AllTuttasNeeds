package com.alltuttasneeds.core.config;

import com.alltuttasneeds.beds.config.TBConfig;
import com.alltuttasneeds.doors.config.TDConfig;
import com.alltuttasneeds.delights.config.DelightsConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ATNConfig {
    private ATNConfig() {}

    public static final ModConfigSpec CLIENT_SPEC;
    public static final ModConfigSpec COMMON_SPEC;
    @Deprecated(forRemoval = false)
    public static final ModConfigSpec SERVER_SPEC;
    public static final ModConfigSpec SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("doors");
        TDConfig.init(builder);
        builder.pop();

        builder.push("beds");
        TBConfig.init(builder);
        builder.pop();

        builder.push("delights");
        DelightsConfig.init(builder);
        builder.pop();

        SPEC = builder.build();

        ModConfigSpec.Builder clientBuilder = new ModConfigSpec.Builder();
        clientBuilder.push("doors");
        TDConfig.initClient(clientBuilder);
        clientBuilder.pop();

        clientBuilder.push("beds");
        TBConfig.initClient(clientBuilder);
        clientBuilder.pop();
        CLIENT_SPEC = clientBuilder.build();

        ModConfigSpec.Builder commonBuilder = new ModConfigSpec.Builder();
        commonBuilder.push("doors");
        TDConfig.initServer(commonBuilder);
        commonBuilder.pop();

        commonBuilder.push("beds");
        TBConfig.initServer(commonBuilder);
        commonBuilder.pop();

        commonBuilder.push("delights");
        DelightsConfig.initServer(commonBuilder);
        commonBuilder.pop();
        COMMON_SPEC = commonBuilder.build();
        SERVER_SPEC = COMMON_SPEC;
    }

    public static void register(ModContainer container) {
        ATNConfigMigration.migrateLegacyConfig();
        container.registerConfig(ModConfig.Type.STARTUP, SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        container.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
    }
}
