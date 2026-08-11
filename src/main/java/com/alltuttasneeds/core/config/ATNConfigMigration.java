package com.alltuttasneeds.core.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class ATNConfigMigration {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String LEGACY_FILE = "alltuttasneeds-startup.toml";
    private static final String CLIENT_FILE = "alltuttasneeds-client.toml";
    private static final String COMMON_FILE = "alltuttasneeds-common.toml";
    private static final String OBSOLETE_SERVER_FILE = "alltuttasneeds-server.toml";

    private static final List<String> CLIENT_PATHS = List.of(
            "doors.behavior.tooltipsEnabled",
            "beds.general.tooltipsEnabled"
    );

    private static final List<String> COMMON_PATHS = createCommonPaths();

    private ATNConfigMigration() {}

    static void migrateLegacyConfig() {
        Path configDirectory = FMLPaths.CONFIGDIR.get();
        Path legacyPath = configDirectory.resolve(LEGACY_FILE);
        Path obsoleteServerPath = configDirectory.resolve(OBSOLETE_SERVER_FILE);
        if (!Files.isRegularFile(legacyPath) && !Files.isRegularFile(obsoleteServerPath)) return;

        try {
            int migrated = 0;
            if (Files.isRegularFile(obsoleteServerPath)) {
                migrated += migrateFrom(obsoleteServerPath, configDirectory.resolve(COMMON_FILE), COMMON_PATHS);
            }
            if (Files.isRegularFile(legacyPath)) {
                migrated += migrateFrom(legacyPath, configDirectory.resolve(CLIENT_FILE), CLIENT_PATHS);
                migrated += migrateFrom(legacyPath, configDirectory.resolve(COMMON_FILE), COMMON_PATHS);
            }
            if (migrated > 0) {
                LOGGER.info("Migrated {} legacy AllTuttasNeeds config values to their client and common config files", migrated);
            }
        } catch (RuntimeException exception) {
            LOGGER.warn("Could not migrate legacy AllTuttasNeeds config values; the source config files were left unchanged", exception);
        }
    }

    private static int migrateFrom(Path sourcePath, Path destinationPath, List<String> paths) {
        try (CommentedFileConfig source = open(sourcePath)) {
            source.load();
            return migrate(source, destinationPath, paths);
        }
    }

    private static int migrate(CommentedFileConfig legacy, Path destinationPath, List<String> paths) {
        try (CommentedFileConfig destination = open(destinationPath)) {
            destination.load();
            int migrated = 0;
            for (String path : paths) {
                if (!legacy.contains(path) || destination.contains(path)) continue;

                Object value = legacy.get(path);
                if (value instanceof List<?> list) value = new ArrayList<>(list);
                destination.set(path, value);
                migrated++;
            }
            if (migrated > 0) destination.save();
            return migrated;
        }
    }

    private static CommentedFileConfig open(Path path) {
        return CommentedFileConfig.builder(path).sync().build();
    }

    private static List<String> createCommonPaths() {
        List<String> paths = new ArrayList<>(List.of(
                "doors.behavior.transitAutomaticOpeningEnabled",
                "doors.behavior.transitAutomaticClosingEnabled",
                "doors.behavior.petAutomaticOpeningEnabled",
                "doors.behavior.petAutomaticClosingEnabled",
                "doors.behavior.automaticClosingDelayTicks",
                "doors.behavior.invertAutomaticClosingRedstone",
                "beds.gameplay.interactions.directApplyDisabled",
                "beds.gameplay.tiers.tieredSleepDurationEnabled",
                "beds.gameplay.tiers.vanillaBedsUseTieredSleepDuration",
                "beds.gameplay.tiers.vanillaBedsUseTierSpawnRules",
                "beds.gameplay.tiers.vanillaBedsUseTierWakeEffects",
                "delights.recipes.useCheeseWedges"
        ));

        for (String tier : List.of("basic", "low", "normal", "deluxe")) {
            String tierPath = "beds.gameplay.tiers." + tier;
            paths.add(tierPath + ".setsSpawn");
            paths.add(tierPath + ".sleepDurationMultiplier");
            paths.add(tierPath + ".ignoresNearbyMonsters");
            paths.add(tierPath + ".blockOverrides");
            paths.add(tierPath + ".blockExclusions");
            paths.add(tierPath + ".wakeEffect.effectEnabled");
            paths.add(tierPath + ".wakeEffect.effectId");
            paths.add(tierPath + ".wakeEffect.effectDurationSeconds");
        }
        return List.copyOf(paths);
    }
}
