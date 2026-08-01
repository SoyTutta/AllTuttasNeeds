package com.alltuttasneeds.delights.datagen;

import com.alltuttasneeds.delights.DelightsItems;
import com.alltuttasneeds.delights.compat.MinersDelightCompat;
import com.alltuttasneeds.delights.compat.SpawnCompat;
import com.ninni.spawn.registry.SpawnItems;
import com.sammy.minersdelight.setup.MDItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import static vectorwing.farmersdelight.data.recipe.CuttingRecipes.KNIVES;
import static com.alltuttasneeds.delights.config.DelightGroup.*;
import static com.alltuttasneeds.delights.datagen.DelightsRecipeConditions.groups;

public class CuttingRecipes {

    public static void register(RecipeOutput output) {
        cuttingAnimalItems(output);
        cuttingVegetables(output);
        cuttingFoods(output);
    }

    private static void cuttingAnimalItems(RecipeOutput output) {
        if (GUARDIAN.configurable()) {
            CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.GUARDIAN.get()), KNIVES, DelightsItems.GUARDIAN_HEAD.get())
                    .addResultWithChance(DelightsItems.RAW_GUARDIAN_SLICE.get(), 0.25f, 2)
                    .addResultWithChance(DelightsItems.RAW_GUARDIAN_SLICE.get(), 0.25f, 2)
                    .addResult(DelightsItems.RAW_GUARDIAN_TAIL.get())
                    .save(groups(output, GUARDIAN));
            CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.GUARDIAN_HEAD.get()), KNIVES, Items.PRISMARINE_SHARD, 3)
                    .addResult(Items.PRISMARINE_CRYSTALS, 2)
                    .addResultWithChance(Items.PRISMARINE_SHARD, 0.25f, 1)
                    .addResultWithChance(Items.PRISMARINE_CRYSTALS, 0.25f, 1)
                    .save(groups(output, GUARDIAN));
            CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.RAW_GUARDIAN_TAIL.get()), KNIVES, DelightsItems.RAW_GUARDIAN_SLICE.get(), 3)
                    .addResultWithChance(DelightsItems.RAW_GUARDIAN_SLICE.get(), 0.25f, 2)
                    .save(groups(output, GUARDIAN));
            CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.SMOKED_GUARDIAN_TAIL.get()), KNIVES, DelightsItems.COOKED_GUARDIAN_SLICE.get(), 3)
                    .addResultWithChance(DelightsItems.COOKED_GUARDIAN_SLICE.get(), 0.25f, 2)
                    .save(groups(output, GUARDIAN));
            CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.ELDER_GUARDIAN.get()), KNIVES, DelightsItems.ELDER_GUARDIAN_HEAD.get())
                    .addResultWithChance(DelightsItems.RAW_ELDER_GUARDIAN_SLICE.get(), 0.5f, 4)
                    .addResultWithChance(DelightsItems.RAW_ELDER_GUARDIAN_SLICE.get(), 0.5f, 4)
                    .addResult(DelightsItems.RAW_ELDER_GUARDIAN_TAIL.get())
                    .save(groups(output, GUARDIAN));
            CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.ELDER_GUARDIAN_HEAD.get()), KNIVES, Items.PRISMARINE_SHARD, 4)
                    .addResult(Items.PRISMARINE_CRYSTALS, 3)
                    .addResultWithChance(Items.PRISMARINE_SHARD, 0.5f, 2)
                    .addResultWithChance(Items.PRISMARINE_CRYSTALS, 0.5f, 2)
                    .save(groups(output, GUARDIAN));
            CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.RAW_ELDER_GUARDIAN_TAIL.get()), KNIVES, DelightsItems.RAW_ELDER_GUARDIAN_SLICE.get(), 5)
                    .addResultWithChance(DelightsItems.RAW_ELDER_GUARDIAN_SLICE.get(), 0.25f, 2)
                    .save(groups(output, GUARDIAN));
            CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.SMOKED_ELDER_GUARDIAN_TAIL.get()), KNIVES, DelightsItems.COOKED_ELDER_GUARDIAN_SLICE.get(), 5)
                    .addResultWithChance(DelightsItems.COOKED_ELDER_GUARDIAN_SLICE.get(), 0.25f, 2)
                    .save(groups(output, GUARDIAN));
        }
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.ANCIENT_RIBS.get()), KNIVES, ModItems.MUTTON_CHOPS.get(), 4)
                .save(groups(output, ANCIENT));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.COOKED_ANCIENT_RIBS.get()), KNIVES, ModItems.COOKED_MUTTON_CHOPS.get(), 4)
                .save(groups(output, ANCIENT));
    }

    private static void cuttingVegetables(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.POTATO), KNIVES, DelightsItems.POTATO_SLICES.get(), 2)
                .save(groups(output, POTATO));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BAKED_POTATO), KNIVES, DelightsItems.BAKED_POTATO_SLICES.get(), 2)
                .save(groups(output, POTATO));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PITCHER_POD), KNIVES, DelightsItems.POD_SLICE.get(), 2)
                .save(groups(output, ANCIENT));
    }

    private static void cuttingFoods(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BONE), KNIVES, DelightsItems.BROKEN_BONES.get(),3)
                .save(groups(output, UNDEAD));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.ROTTEN_HAM.get()), KNIVES, DelightsItems.ROTTEN_BACON.get(),4).addResult(Items.BONE_MEAL)
                .save(groups(output, UNDEAD));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ROTTEN_FLESH), KNIVES, DelightsItems.ROTTEN_BACON.get())
                .save(groups(output, UNDEAD));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.ROTTEN_BACON.get()), KNIVES, DelightsItems.ROTTEN_MINCED_BEEF.get())
                .save(groups(output, UNDEAD));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.FROGGLE_SANDWICH.get()), KNIVES, DelightsItems.HALF_A_FROGGLE_SANDWICH.get(), 2)
                .save(groups(output, FROG));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.BREADED_MEAT_SANDWICH.get()), KNIVES, DelightsItems.HALF_A_BREADED_MEAT_SANDWICH.get(), 2)
                .save(groups(output, BREADED));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.BACON_SANDWICH.get()), KNIVES, DelightsItems.HALF_A_BACON_SANDWICH.get(), 2)
                .save(groups(output, SANDWICH_PORTIONS));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.EGG_SANDWICH.get()), KNIVES, DelightsItems.HALF_A_EGG_SANDWICH.get(), 2)
                .save(groups(output, SANDWICH_PORTIONS));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.ANCIENT_EGG_SANDWICH.get()), KNIVES, DelightsItems.HALF_A_ANCIENT_EGG_SANDWICH.get(), 2)
                .save(groups(output, ANCIENT));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CHICKEN_SANDWICH.get()), KNIVES, DelightsItems.HALF_A_CHICKEN_SANDWICH.get(), 2)
                .save(groups(output, SANDWICH_PORTIONS));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.HAMBURGER.get()), KNIVES, DelightsItems.HALF_A_HAMBURGER.get(), 2)
                .save(groups(output, SANDWICH_PORTIONS));
        RecipeOutput minersDelightOutput = groups(output, SANDWICH_PORTIONS)
                .withConditions(new ModLoadedCondition("minersdelight"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MDItems.VEGAN_HAMBURGER.get()), KNIVES,
                        MinersDelightCompat.HALF_A_VEGAN_HAMBURGER.get(), 2)
                .save(minersDelightOutput);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MDItems.CAVE_HAMBURGER.get()), KNIVES,
                        MinersDelightCompat.HALF_A_CAVEBURGER.get(), 2)
                .save(minersDelightOutput);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MDItems.INSECT_SANDWICH.get()), KNIVES,
                        MinersDelightCompat.HALF_A_INSECT_SANDWICH.get(), 2)
                .save(minersDelightOutput);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MDItems.SQUID_SANDWICH.get()), KNIVES,
                        MinersDelightCompat.HALF_A_SQUID_SANDWICH.get(), 2)
                .save(minersDelightOutput);
        RecipeOutput spawnOutput = groups(output, SANDWICH_PORTIONS)
                .withConditions(new ModLoadedCondition("spawn"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(SpawnItems.TUNA_SANDWICH.get()), KNIVES,
                        SpawnCompat.HALF_A_TUNA_SANDWICH.get(), 2)
                .save(spawnOutput);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.ICE_CREAM_IN_A_PUMPKIN_HEAD.get()), KNIVES, DelightsItems.PUMPKIN_HEAD_SLICE.get(), 4)
                .save(groups(output, FROZEN_TREATS));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(DelightsItems.SLIME_JELLY.get()), KNIVES, DelightsItems.SLIME_JELLY_SLICE.get(), 4)
                .save(groups(output, SLIME));
    }
}
