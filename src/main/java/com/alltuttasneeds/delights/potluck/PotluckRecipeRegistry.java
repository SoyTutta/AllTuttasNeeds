package com.alltuttasneeds.delights.potluck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PotluckRecipeRegistry extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();
    public static final PotluckRecipeRegistry INSTANCE = new PotluckRecipeRegistry();

    private static volatile Map<ResourceLocation, PotluckRecipe> recipes = Map.of();
    private static volatile List<PotluckRecipe> orderedRecipes = List.of();
    private static volatile HolderLookup.Provider registries;

    private PotluckRecipeRegistry() {
        super(GSON, "potluck_recipes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager,
                         ProfilerFiller profiler) {
        Map<ResourceLocation, PotluckRecipe> loaded = new LinkedHashMap<>();
        resources.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(ResourceLocation::toString)))
                .forEach(entry -> {
                    try {
                        if (registries == null) throw new IllegalStateException("Registry access is not available");
                        PotluckRecipe recipe = PotluckRecipe.fromJson(entry.getKey(),
                                entry.getValue().getAsJsonObject(), registries);
                        loaded.put(entry.getKey(), recipe);
                    } catch (RuntimeException exception) {
                        LOGGER.error("Could not load Potluck recipe {}", entry.getKey(), exception);
                    }
                });
        recipes = Map.copyOf(loaded);
        orderedRecipes = List.copyOf(loaded.values());
        LOGGER.info("Loaded {} Potluck recipes", recipes.size());
    }

    public static void setRegistryAccess(HolderLookup.Provider provider) {
        registries = provider;
    }

    @Nullable
    public static PotluckRecipe get(ResourceLocation id) {
        return recipes.get(id);
    }

    public static List<PotluckRecipe> forCapacity(int capacity) {
        List<PotluckRecipe> result = new ArrayList<>();
        for (PotluckRecipe recipe : orderedRecipes) if (recipe.fits(capacity)) result.add(recipe);
        return result;
    }

    @Nullable
    public static PotluckRecipe forReturnedDish(ItemStack stack, int capacity) {
        for (PotluckRecipe recipe : orderedRecipes) {
            if (recipe.fits(capacity) && recipe.returnableDishes().test(stack)) return recipe;
        }
        return null;
    }
}
