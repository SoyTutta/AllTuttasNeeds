package com.alltuttasneeds.core.data;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.core.data.recipes.TDRecipes;
import com.alltuttasneeds.core.data.tags.TDBlockTags;
import com.alltuttasneeds.core.data.tags.TDItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import java.util.Set;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = AllTuttasNeeds.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ATNDataGenerators {

    public ATNDataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(event.includeClient(), new TDLang(output));
        generator.addProvider(event.includeClient(), new ESLang(output));

        TDBlockTags blockTags = new TDBlockTags(output, lookupProvider, helper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new TDItemTags(output, lookupProvider, blockTags.contentsGetter(), helper));

        generator.addProvider(event.includeServer(), new TDRecipes(output, lookupProvider));
        generator.addProvider(event.includeServer(), new DataMaps(output, lookupProvider));

        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK)
        ), lookupProvider));
        generator.addProvider(true, new DatapackBuiltinEntriesProvider(
                output,
                lookupProvider,
                new RegistrySetBuilder(),
                Set.of(AllTuttasNeeds.MODID)
        ));
        TDBlockStates blockStates = new TDBlockStates(output, helper);
        generator.addProvider(event.includeClient(), blockStates);
        generator.addProvider(event.includeClient(), new TDItemsModels(output, blockStates.models().existingFileHelper));

    }
}