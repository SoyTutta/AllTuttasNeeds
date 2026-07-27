package com.alltuttasneeds.delights.datagen;
import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.delights.DelightsModule;
import com.alltuttasneeds.delights.DelightsItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;

import java.util.Arrays;

import static com.alltuttasneeds.delights.config.DelightGroup.*;
import static com.alltuttasneeds.delights.datagen.DelightsRecipeConditions.groups;


public class SmeltingRecipes {

    public static void register(RecipeOutput output) {
        foodSmeltingRecipes("potato_slices", Ingredient.of(DelightsItems.POTATO_SLICES.get()), DelightsItems.BAKED_POTATO_SLICES.get(), 0.35F, groups(output, POTATO));
        foodSmeltingRecipes("breaded_meat", Ingredient.of(DelightsItems.RAW_BREADED_MEAT.get()), DelightsItems.COOKED_BREADED_MEAT.get(), 0.35F, groups(output, BREADED));
        foodSmeltingRecipes("frog_leg", Ingredient.of(DelightsItems.RAW_FROG_LEG.get()), DelightsItems.COOKED_FROG_LEG.get(), 0.35F,
                groups(output, FROG).withConditions(new NotCondition(new ModLoadedCondition(Mods.NOMANSLAND.id()))));
        foodSmeltingRecipes("ancient_ribs", Ingredient.of(DelightsItems.ANCIENT_RIBS.get()), DelightsItems.COOKED_ANCIENT_RIBS.get(), 0.35F, groups(output, ANCIENT));
        if (GUARDIAN.configurable()) {
            foodSmeltingRecipes("guardian_slice", Ingredient.of(DelightsItems.RAW_GUARDIAN_SLICE.get()), DelightsItems.COOKED_GUARDIAN_SLICE.get(), 0.35F, groups(output, GUARDIAN));
            foodSmeltingRecipes("elder_guardian_slice", Ingredient.of(DelightsItems.RAW_ELDER_GUARDIAN_SLICE.get()), DelightsItems.COOKED_ELDER_GUARDIAN_SLICE.get(), 0.35F, groups(output, GUARDIAN));
        }

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(DelightsItems.SQUISHMALLOW.get()), RecipeCategory.FOOD,
                        DelightsItems.SMOKED_SQUISHMALLOW.get(), 0.35F, 100)
                .unlockedBy("has_squishmallow", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.SMOKED_SQUISHMALLOW.get()))
                .save(groups(output, SLIME), ResourceLocation.fromNamespaceAndPath(DelightsModule.MODID, "squishmallow") + "_from_smoking");
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(DelightsItems.SQUISHMALLOW.get()), RecipeCategory.FOOD,
                        DelightsItems.SMOKED_SQUISHMALLOW.get(), 0.35F, 100)
                .unlockedBy("has_squishmallow", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.SMOKED_SQUISHMALLOW.get()))
                .save(groups(output, SLIME), ResourceLocation.fromNamespaceAndPath(DelightsModule.MODID, "squishmallow") + "_from_campfire_cooking");

        if (GUARDIAN.configurable()) {
            SimpleCookingRecipeBuilder.smoking(Ingredient.of(DelightsItems.RAW_GUARDIAN_TAIL.get()), RecipeCategory.FOOD,
                            DelightsItems.SMOKED_GUARDIAN_TAIL.get(), 0.35F, 100)
                    .unlockedBy("has_guardian_tail", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.RAW_GUARDIAN_TAIL.get()))
                    .save(groups(output, GUARDIAN), ResourceLocation.fromNamespaceAndPath(DelightsModule.MODID, "guardian_tail") + "_from_smoking");
            SimpleCookingRecipeBuilder.smoking(Ingredient.of(DelightsItems.RAW_ELDER_GUARDIAN_TAIL.get()), RecipeCategory.FOOD,
                            DelightsItems.SMOKED_ELDER_GUARDIAN_TAIL.get(), 0.35F, 100)
                    .unlockedBy("has_elder_guardian_tail", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.RAW_ELDER_GUARDIAN_TAIL.get()))
                    .save(groups(output, GUARDIAN), ResourceLocation.fromNamespaceAndPath(DelightsModule.MODID, "elder_guardian_tail") + "_from_smoking");
        }
    }

    private static void foodSmeltingRecipes(String name, Ingredient ingredient, ItemLike result, float experience, RecipeOutput output) {
        String namePrefix = ResourceLocation.fromNamespaceAndPath("tuttasdelights", name).toString();
        ItemLike[] items = Arrays.stream(ingredient.getItems())
                .map(ItemStack::getItem)
                .toArray(ItemLike[]::new);

        RecipeBuilder smeltingRecipe = SimpleCookingRecipeBuilder.smelting(ingredient, RecipeCategory.FOOD, result, experience, 200)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(items));
        smeltingRecipe.save(output, namePrefix + "_cooking");

        RecipeBuilder campfireRecipe = SimpleCookingRecipeBuilder.campfireCooking(ingredient, RecipeCategory.FOOD, result, experience, 600)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(items));
        campfireRecipe.save(output, namePrefix + "_from_campfire_cooking");

        RecipeBuilder smokingRecipe = SimpleCookingRecipeBuilder.smoking(ingredient, RecipeCategory.FOOD, result, experience, 100)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(items));
        smokingRecipe.save(output, namePrefix + "_from_smoking");
    }

}
