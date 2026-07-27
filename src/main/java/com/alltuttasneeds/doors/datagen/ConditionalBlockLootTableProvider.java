package com.alltuttasneeds.doors.datagen;

import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ConditionalBlockLootTableProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public ConditionalBlockLootTableProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries
    ) {
        this.pathProvider = output.createRegistryElementsPathProvider(Registries.LOOT_TABLE);
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return registries.thenCompose(provider -> run(output, provider));
    }

    private CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider provider) {
        WritableRegistry<LootTable> tables = new MappedRegistry<>(Registries.LOOT_TABLE, Lifecycle.experimental());
        Map<RandomSupport.Seed128bit, ResourceLocation> sequences = new Object2ObjectOpenHashMap<>();
        BlockLootTables subProvider = new BlockLootTables(provider);

        subProvider.generate((key, builder) -> {
            ResourceLocation sequence = key.location();
            ResourceLocation previous = sequences.put(RandomSequence.seedForKey(sequence), sequence);
            if (previous != null) {
                Util.logAndPauseIfInIde(
                        "Loot table random sequence seed collision on " + previous + " and " + key.location());
            }

            LootTable table = builder
                    .setRandomSequence(sequence)
                    .setParamSet(LootContextParamSets.BLOCK)
                    .build();
            tables.register(key, table, RegistrationInfo.BUILT_IN);
        });
        tables.freeze();
        validate(tables);

        Codec<Optional<WithConditions<LootTable>>> codec =
                ConditionalOps.createConditionalCodecWithConditions(LootTable.DIRECT_CODEC);
        return CompletableFuture.allOf(tables.entrySet().stream().map(entry -> {
            ResourceKey<LootTable> key = entry.getKey();
            WithConditions<LootTable> conditionalTable =
                    new WithConditions<>(subProvider.conditions(key), entry.getValue());
            Path path = pathProvider.json(key.location());
            return DataProvider.saveStable(
                    output, provider, codec, Optional.of(conditionalTable), path);
        }).toArray(CompletableFuture[]::new));
    }

    private static void validate(WritableRegistry<LootTable> tables) {
        ProblemReporter.Collector problems = new ProblemReporter.Collector();
        HolderGetter.Provider lookup = new RegistryAccess.ImmutableRegistryAccess(List.of(tables))
                .freeze()
                .asGetterLookup();
        ValidationContext context = new ValidationContext(
                problems, LootContextParamSets.ALL_PARAMS, lookup);
        tables.holders().forEach(holder -> holder.value().validate(
                context.setParams(holder.value().getParamSet())
                        .enterElement("{" + holder.key().location() + "}", holder.key())));

        Multimap<String, String> messages = problems.get();
        if (!messages.isEmpty()) {
            messages.forEach((key, message) ->
                    LOGGER.warn("Found validation problem in {}: {}", key, message));
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        }
    }

    @Override
    public String getName() {
        return "Tutta's Doors conditional block loot tables";
    }
}
