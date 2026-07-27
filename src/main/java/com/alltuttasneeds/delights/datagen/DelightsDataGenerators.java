package com.alltuttasneeds.delights.datagen;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.delights.config.DelightsConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;


@SuppressWarnings("unused")
@EventBusSubscriber(modid = AllTuttasNeeds.MODID)
public class DelightsDataGenerators {

    public DelightsDataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        String profile = System.getProperty("alltuttasneeds.datagen.profile", "all");
        boolean enabled = DelightsConfig.isModuleEnabled()
                && (profile.equals("all") || profile.equals("delights"));
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        DelightsBlockTags blockTags = new DelightsBlockTags(output, lookupProvider, helper);
        generator.addProvider(enabled && event.includeServer(), named("Tuttas Delights Block Tags", blockTags));
        generator.addProvider(enabled && event.includeServer(), named("Tuttas Delights Item Tags",
                new DelightsItemTags(output, lookupProvider, blockTags.contentsGetter(), helper)));
        generator.addProvider(enabled && event.includeServer(), named("Tuttas Delights Recipes",
                new DelightsRecipes(output, lookupProvider)));
        generator.addProvider(enabled && event.includeServer(), named("Tuttas Delights Data Maps",
                new DelightsDataMaps(output, lookupProvider)));
        generator.addProvider(enabled && event.includeServer(), named("Tuttas Delights Snow Golem Feast Face Rules",
                new FaceRuleProvider(output)));
        generator.addProvider(enabled && event.includeClient(), named("Tuttas Delights Languages",
                new DelightsLang(output)));
        generator.addProvider(enabled && event.includeClient(), named("Tuttas Delights Spanish Languages",
                new DelightsLangES(output)));

        generator.addProvider(enabled && event.includeClient(), named("Tuttas Delights Snow Golem Feast Appearances",
                new FaceVariantProvider(output)));
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
}
