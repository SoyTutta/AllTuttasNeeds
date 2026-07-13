package com.alltuttasneeds.core.datagen;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.doors.datagen.BlockLootTables;
import com.alltuttasneeds.doors.datagen.DataMaps;
import com.alltuttasneeds.doors.datagen.ESLang;
import com.alltuttasneeds.beds.datagen.TBBlockLootTables;
import com.alltuttasneeds.beds.datagen.TBBlockStates;
import com.alltuttasneeds.beds.datagen.TBItemsModels;
import com.alltuttasneeds.beds.datagen.TBRecipeProvider;
import com.alltuttasneeds.beds.datagen.TBLangES;
import com.alltuttasneeds.beds.datagen.TBLangUS;
import com.alltuttasneeds.doors.datagen.ShapeMapProvider;
import com.alltuttasneeds.doors.datagen.TDBlockStates;
import com.alltuttasneeds.doors.datagen.TDItemsModels;
import com.alltuttasneeds.doors.datagen.USLang;
import com.alltuttasneeds.doors.datagen.TDRecipes;
import com.alltuttasneeds.beds.datagen.TBBlockTags;
import com.alltuttasneeds.beds.datagen.TBItemTags;
import com.alltuttasneeds.doors.datagen.TDBlockTags;
import com.alltuttasneeds.doors.datagen.TDItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID)
public class ATNDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataProfile profile = DataProfile.fromSystemProperty();
        boolean doorsClient = profile.doors && event.includeClient();
        boolean doorsServer = profile.doors && event.includeServer();
        boolean bedsClient = profile.beds && event.includeClient();
        boolean bedsServer = profile.beds && event.includeServer();
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(doorsClient, new USLang(output));
        generator.addProvider(doorsClient, new ESLang(output));
        generator.addProvider(bedsClient, new TBLangUS(output));
        generator.addProvider(bedsClient, new TBLangES(output));

        TDBlockTags blockTags = new TDBlockTags(output, lookupProvider, helper);
        generator.addProvider(doorsServer, blockTags);
        generator.addProvider(doorsServer, new TDItemTags(output, lookupProvider, blockTags.contentsGetter(), helper));
        TBBlockTags bedBlockTags = new TBBlockTags(output, lookupProvider, helper);
        generator.addProvider(bedsServer, bedBlockTags);
        generator.addProvider(bedsServer, new TBItemTags(output, lookupProvider, bedBlockTags.contentsGetter(), helper));

        generator.addProvider(doorsServer, named("Tuttas Doors Recipes", new TDRecipes(output, lookupProvider)));
        generator.addProvider(bedsServer, named("Tuttas Beds Recipes", new TBRecipeProvider(output, lookupProvider)));
        generator.addProvider(doorsServer, new DataMaps(output, lookupProvider));
        generator.addProvider(doorsServer, new ShapeMapProvider(output));

        generator.addProvider(doorsServer, named("Tuttas Doors Loot Tables", new LootTableProvider(
                output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK)
        ), lookupProvider)));
        generator.addProvider(bedsServer, named("Tuttas Beds Loot Tables", new LootTableProvider(
                output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(TBBlockLootTables::new, LootContextParamSets.BLOCK)
        ), lookupProvider)));
        TDBlockStates blockStates = new TDBlockStates(output, helper);
        generator.addProvider(doorsClient, blockStates);
        generator.addProvider(doorsClient, new TDItemsModels(output, blockStates.models().existingFileHelper));

        TBBlockStates bedBlockStates = new TBBlockStates(output, helper);
        generator.addProvider(bedsClient, bedBlockStates);
        generator.addProvider(bedsClient, new TBItemsModels(output, bedBlockStates.models().existingFileHelper));
    }

    private static DataProvider named(String name, DataProvider delegate) {
        return new DataProvider() {
            @Override
            public CompletableFuture<?> run(CachedOutput output) {
                return delegate.run(output);
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    private record DataProfile(boolean doors, boolean beds) {
        private static DataProfile fromSystemProperty() {
            return switch (System.getProperty("alltuttasneeds.datagen.profile", "all")) {
                case "doors" -> new DataProfile(true, false);
                case "beds" -> new DataProfile(false, true);
                default -> new DataProfile(true, true);
            };
        }
    }
}
