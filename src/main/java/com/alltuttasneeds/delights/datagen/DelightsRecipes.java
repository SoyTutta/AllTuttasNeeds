package com.alltuttasneeds.delights.datagen;

import com.alltuttasneeds.core.condition.ModuleEnabledCondition;
import com.alltuttasneeds.delights.datagen.CraftingRecipes;
import com.alltuttasneeds.delights.datagen.CookingRecipes;
import com.alltuttasneeds.delights.datagen.CuttingRecipes;
import com.alltuttasneeds.delights.datagen.SmeltingRecipes;
import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

public class DelightsRecipes extends RecipeProvider {

    public DelightsRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }
    protected void buildRecipes(RecipeOutput output) {
        RecipeOutput enabledOutput = output.withConditions(ModuleEnabledCondition.DELIGHTS);
        CraftingRecipes.registerCheeseOverrides(enabledOutput);
        SmeltingRecipes.register(enabledOutput);
        CraftingRecipes.register(enabledOutput);
        CuttingRecipes.register(enabledOutput);
        CookingRecipes.register(enabledOutput);
    }
}
