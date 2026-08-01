package com.alltuttasneeds.delights.datagen;

import com.alltuttasneeds.delights.DelightsItems;
import com.alltuttasneeds.delights.DelightsTags;
import com.alltuttasneeds.delights.DelightsCommonTags;
import com.alltuttasneeds.delights.crafting.ConfigurableCheeseIngredient;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import vectorwing.farmersdelight.common.crafting.FoodServingRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;

import static com.alltuttasneeds.delights.config.DelightGroup.*;
import static com.alltuttasneeds.delights.datagen.DelightsRecipeConditions.groups;

public class CraftingRecipes {

    public static void register(RecipeOutput output) {
        recipesVanillaAlternatives(output);
        recipesBlocks(output);
        recipesCraftedMeals(output);
        SpecialRecipeBuilder.special(FoodServingRecipe::new).save(output, "food_serving");
    }

    private static void recipesVanillaAlternatives(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.SLIME_BALL, 1)
                .requires(DelightsItems.SLIMECUBE.get())
                .unlockedBy("has_slimecube", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.SLIMECUBE.get()))
                .save(groups(output, SLIME), "tuttasdelights:crafting/slime_ball");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.BONE_MEAL)
                .requires(DelightsItems.BROKEN_BONES.get())
                .unlockedBy("has_bone", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BONE_MEAL))
                .save(groups(output, UNDEAD), "tuttasdelights:crafting/bone_meal");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.RABBIT_STEW)
                .requires(DelightsCommonTags.FOODS_COOKED_POTATO).requires(Items.COOKED_RABBIT)
                .requires(Items.BOWL).requires(Items.CARROT)
                .requires(Ingredient.of(Items.RED_MUSHROOM,Items.BROWN_MUSHROOM))
                .group("rabbit_stew")
                .unlockedBy("has_cooked_rabbit", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COOKED_RABBIT))
                .save(output, "tuttasdelights:crafting/rabbit_stew");


    }

    private static void recipesBlocks(RecipeOutput output) {
    }

    public static void registerCheeseOverrides(RecipeOutput output) {
        Ingredient cheese = ConfigurableCheeseIngredient.ingredient();
        RecipeOutput farmersDelightOutput = output.withConditions(new ModLoadedCondition("farmersdelight"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.STUFFED_POTATO.get())
                .requires(Items.BAKED_POTATO)
                .requires(CommonTags.Items.FOODS_COOKED_BEEF)
                .requires(cheese)
                .unlockedBy("has_baked_potato", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BAKED_POTATO))
                .save(farmersDelightOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.SWEET_BERRY_CHEESECAKE.get())
                .pattern("sss")
                .pattern("sss")
                .pattern("mOm")
                .define('s', Items.SWEET_BERRIES)
                .define('m', cheese)
                .define('O', ModItems.PIE_CRUST.get())
                .unlockedBy("has_pie_crust", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PIE_CRUST.get()))
                .group("fd_sweet_berry_cheesecake")
                .save(farmersDelightOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MNDItems.STUFFED_PEPPER.get())
                .requires(MNDItems.BULLET_PEPPER.get())
                .requires(CommonTags.Items.FOODS_COOKED_PORK)
                .requires(cheese)
                .unlockedBy("has_pepper", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.BULLET_PEPPER.get()))
                .save(output.withConditions(new ModLoadedCondition("mynethersdelight")),
                        "mynethersdelight:crafting/stuffed_pepper");
    }

    private static void recipesCraftedMeals(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.BREADED_MEAT_WITH_PASTA.get())
                .requires(DelightsItems.COOKED_PASTA.get()).requires(CommonTags.Items.FOODS_COOKED_EGG)
                .requires(DelightsItems.COOKED_BREADED_MEAT.get())
                .requires(Items.BOWL)
                .unlockedBy("has_pasta", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.COOKED_PASTA.get()))
                .save(groups(output, POTATO, BREADED, EXTRA_MEALS), "tuttasdelights:crafting/breaded_meat_with_pasta");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.STEAK_AND_POTATOES.get())
                .requires(DelightsCommonTags.FOODS_COOKED_POTATO).requires(Items.COOKED_BEEF).requires(Items.BOWL)
                .requires(CommonTags.Items.CROPS_ONION).requires(ModItems.COOKED_RICE.get())
                .unlockedBy("has_baked_potato", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BAKED_POTATO))
                .save(output, "tuttasdelights:crafting/steak_and_potatoes");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.ROAST_CHICKEN_BLOCK.get())
                .requires(CommonTags.Items.CROPS_ONION).requires(net.neoforged.neoforge.common.Tags.Items.EGGS)
                .requires(Items.BREAD).requires(Items.CARROT).requires(Items.COOKED_CHICKEN).requires(DelightsCommonTags.FOODS_COOKED_POTATO).requires(Items.CARROT)
                .requires(Items.BOWL).requires(DelightsCommonTags.FOODS_COOKED_POTATO)
                .unlockedBy("has_cooked_chicken", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COOKED_CHICKEN))
                .save(output, "tuttasdelights:crafting/roast_chiken_block");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.RAW_BREADED_MEAT.get())
                .requires(Tags.Items.FOODS_RAW_MEAT)
                .requires(Tags.Items.EGGS)
                .requires(Tags.Items.CROPS_WHEAT)
                .unlockedBy("has_wheat", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT, Items.EGG))
                .save(groups(output, BREADED), "tuttasdelights:crafting/raw_breaded_meat");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.BREADED_MEAT_SANDWICH.get())
                .requires(DelightsItems.COOKED_BREADED_MEAT.get())
                .requires(Tags.Items.FOODS_BREAD)
                .requires(CommonTags.Items.CROPS_CABBAGE)
                .requires(CommonTags.Items.CROPS_TOMATO)
                .unlockedBy("has_breaded_meat", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.COOKED_BREADED_MEAT.get(), DelightsItems.RAW_BREADED_MEAT.get()))
                .save(groups(output, BREADED), "tuttasdelights:crafting/breaded_meat_sandwich");

        napolitanaRecipe(groups(output, POTATO, BREADED), ConfigurableCheeseIngredient.ingredient(),
                "napolitana_with_fries_potatoes");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.BREADED_MEAT_WITH_MASHED_POTATO.get())
                .requires(Items.BAKED_POTATO)
                .requires(DelightsItems.COOKED_BREADED_MEAT.get())
                .requires(Items.BOWL)
                .unlockedBy("has_breaded_meat", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.COOKED_BREADED_MEAT.get(), DelightsItems.RAW_BREADED_MEAT.get()))
                .save(groups(output, POTATO, BREADED), "tuttasdelights:crafting/breaded_meat_with_mashed_potato");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.BEET_AND_EGG_SALAD.get())
                .requires(Tags.Items.CROPS_BEETROOT)
                .requires(Tags.Items.CROPS_BEETROOT)
                .requires(DelightsCommonTags.FOODS_BOILED_EGG)
                .requires(Items.BOWL)
                .unlockedBy("has_beetroot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BEETROOT))
                .save(groups(output, EXTRA_MEALS), "tuttasdelights:crafting/beet_and_egg_salad");

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.GLISTERING_MELON_POPSICLE.get(), 1)
                .pattern("ggg")
                .pattern("g#g")
                .pattern("ggg")
                .define('#', ModItems.MELON_POPSICLE.get())
                .define('g', Items.GOLD_NUGGET)
                .unlockedBy("has_melon_popsicle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MELON_POPSICLE.get()))
                .save(groups(output, FROZEN_TREATS), "tuttasdelights:crafting/glistering_melon_popsicle");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.COCOA_POPSICLE.get(), 1)
                .pattern(" ##")
                .pattern("i##")
                .pattern("-i ")
                .define('#', Items.COCOA_BEANS)
                .define('i', Items.ICE)
                .define('-', Items.STICK)
                .unlockedBy("has_cocoa", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COCOA_BEANS))
                .save(groups(output, FROZEN_TREATS), "tuttasdelights:crafting/cocoa_popsicle");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.MINT_POPSICLE.get(), 1)
                .pattern(" ##")
                .pattern("i##")
                .pattern("-i ")
                .define('#', DelightsItems.MINT_LETTUCE.get())
                .define('i', Items.ICE)
                .define('-', Items.STICK)
                .unlockedBy("has_mint", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.MINT_LETTUCE.get()))
                .save(groups(output, FROZEN_TREATS, CREEPER), "tuttasdelights:crafting/mint_popsicle");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.SWEET_BERRIES_POPSICLE.get(), 1)
                .pattern(" ##")
                .pattern("i##")
                .pattern("-i ")
                .define('#', Items.SWEET_BERRIES)
                .define('i', Items.ICE)
                .define('-', Items.STICK)
                .unlockedBy("has_sweet_berries", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SWEET_BERRIES))
                .save(groups(output, FROZEN_TREATS), "tuttasdelights:crafting/sweet_berries_popsicle");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.GLOW_BERRIES_POPSICLE.get(), 1)
                .pattern(" ##")
                .pattern("i##")
                .pattern("-i ")
                .define('#', Items.GLOW_BERRIES)
                .define('i', Items.ICE)
                .define('-', Items.STICK)
                .unlockedBy("has_sweet_berries", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GLOW_BERRIES))
                .save(groups(output, FROZEN_TREATS), "tuttasdelights:crafting/glow_berries_popsicle");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.APPLE_POPSICLE.get(), 1)
                .pattern(" ##")
                .pattern("i##")
                .pattern("-i ")
                .define('#', Items.APPLE)
                .define('i', Items.ICE)
                .define('-', Items.STICK)
                .unlockedBy("has_apple", InventoryChangeTrigger.TriggerInstance.hasItems(Items.APPLE))
                .save(groups(output, FROZEN_TREATS), "tuttasdelights:crafting/apple_popsicle");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.GOLDEN_APPLE_POPSICLE.get(), 1)
                .pattern("ggg")
                .pattern("g#g")
                .pattern("ggg")
                .define('#', DelightsItems.APPLE_POPSICLE.get())
                .define('g', Items.GOLD_NUGGET)
                .unlockedBy("has_apple_popsicle", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.APPLE_POPSICLE.get()))
                .save(groups(output, FROZEN_TREATS), "tuttasdelights:crafting/golden_apple_popsicle");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.TORCH_POPSICLE.get(), 1)
                .pattern(" ##")
                .pattern("i##")
                .pattern("-i ")
                .define('#', Ingredient.of(Items.TORCHFLOWER,Items.TORCHFLOWER_SEEDS))
                .define('i', Items.ICE)
                .define('-', Items.STICK)
                .unlockedBy("has_apple", InventoryChangeTrigger.TriggerInstance.hasItems(Items.APPLE))
                .save(groups(output, FROZEN_TREATS), "tuttasdelights:crafting/torch_popsicle");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.SQUISHMALLOW.get())
                .requires(Items.STICK)
                .requires(DelightsItems.SLIMECUBE.get())
                .unlockedBy("has_slime", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SLIME_BALL, DelightsItems.SLIMECUBE.get()))
                .save(groups(output, SLIME), "tuttasdelights:crafting/squishmallow");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.MINTMALLOW_BIT.get())
                .requires(DelightsItems.SMOKED_SQUISHMALLOW.get())
                .requires(DelightsItems.MINT_LETTUCE.get())
                .unlockedBy("has_slime", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SLIME_BALL, DelightsItems.SLIMECUBE.get()))
                .save(groups(output, SLIME, CREEPER), "tuttasdelights:crafting/mintmallow_bit");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.SLIME_JELLY.get(), 1)
                .pattern("sss")
                .pattern("SwS")
                .pattern("ccc")
                .define('s', DelightsItems.SLIMECUBE.get())
                .define('w', Tags.Items.BUCKETS_WATER).define('S', Items.SUGAR)
                .define('c', Items.SUGAR_CANE)
                .unlockedBy("has_slime", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SLIME_BALL, DelightsItems.SLIMECUBE.get()))
                .save(groups(output, SLIME), "tuttasdelights:crafting/slime_jelly");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.SWEET_TRIGGER.get(),2)
                .requires(Items.HONEY_BOTTLE)
                .requires(DelightsItems.MINT_LETTUCE.get(),2)
                .requires(Items.SWEET_BERRIES)
                .unlockedBy("has_mint", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.MINT_LETTUCE.get()))
                .save(groups(output, CREEPER), "tuttasdelights:crafting/sweet_trigger");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.CREEPER_SALAD.get())
                .requires(Tags.Items.GUNPOWDERS)
                .requires(DelightsItems.MINT_LETTUCE.get(),2)
                .requires(Items.BOWL)
                .unlockedBy("has_mint", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.MINT_LETTUCE.get()))
                .save(groups(output, CREEPER), "tuttasdelights:crafting/creeper_salad");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.MINT_ROULETTE.get())
                .pattern("msm")
                .pattern("mcm")
                .define('s', DelightsItems.SWEET_TRIGGER.get())
                .define('m', DelightsItems.MINT_LETTUCE.get())
                .define('c', DelightsItems.CREEPER_SALAD.get())
                .unlockedBy("has_mint", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.MINT_LETTUCE.get()))
                .save(groups(output, CREEPER), "tuttasdelights:crafting/creeper_roulette");

        if (GUARDIAN.configurable()) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.GUARDIAN_GRILLED_ON_A_STICK.get())
                    .requires(Items.STICK)
                    .requires(DelightsItems.COOKED_GUARDIAN_SLICE.get(), 2)
                    .unlockedBy("has_guardian_slice", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.COOKED_GUARDIAN_SLICE.get()))
                    .save(groups(output, GUARDIAN), "tuttasdelights:crafting/guardian_grilled_on_a_stick");
            ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.ELDER_GUARDIAN_GRILLED_ON_A_STICK.get())
                    .requires(Items.STICK)
                    .requires(DelightsItems.COOKED_ELDER_GUARDIAN_SLICE.get(), 2)
                    .unlockedBy("has_elder_guardian_slice", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.COOKED_ELDER_GUARDIAN_SLICE.get()))
                    .save(groups(output, GUARDIAN), "tuttasdelights:crafting/elder_guardian_grilled_on_a_stick");
        }

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.ANCIENT_BREAKFAST.get())
                .requires(Ingredient.of(Items.PITCHER_POD,DelightsItems.POD_SLICE.get()),3)
                .requires(DelightsItems.ANCIENT_BOILED_EGG.get(),2)
                .requires(Items.BOWL)
                .unlockedBy("has_ancient_boiled_egg_or_pitcher_pod", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PITCHER_POD,DelightsItems.POD_SLICE.get(),DelightsItems.ANCIENT_BOILED_EGG.get()))
                .save(groups(output, ANCIENT), "tuttasdelights:crafting/ancient_breakfast");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.POD_WITH_HONEY_ON_A_STICK.get())
                .requires(Ingredient.of(Items.PITCHER_POD,DelightsItems.POD_SLICE.get()),2)
                .requires(Items.STICK)
                .requires(Items.HONEY_BOTTLE)
                .unlockedBy("has_pitcher_pod", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PITCHER_POD,DelightsItems.POD_SLICE.get()))
                .save(groups(output, ANCIENT), "tuttasdelights:crafting/pod_with_honey_on_a_stick");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.ANCIENT_SALAD.get())
                .requires(Items.TORCHFLOWER)
                .requires(Ingredient.of(Items.TORCHFLOWER,Items.TORCHFLOWER_SEEDS),2)
                .requires(Items.BOWL)
                .unlockedBy("has_torchflower_or_seeds", InventoryChangeTrigger.TriggerInstance.hasItems(Items.TORCHFLOWER,Items.TORCHFLOWER_SEEDS))
                .save(groups(output, ANCIENT), "tuttasdelights:crafting/ancient_salad");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.ANCIENT_EGG_SANDWICH.get())
                .requires(Tags.Items.FOODS_BREAD)
                .requires(DelightsItems.ANCIENT_BOILED_EGG.get(), 2)
                .unlockedBy("has_ancient_eggs", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.ANCIENT_BOILED_EGG.get()))
                .save(groups(output, ANCIENT), "tuttasdelights:crafting/ancient_egg_sandwich");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.ROTTEN_MEAT_ON_A_BONE.get())
                .requires(Items.BONE)
                .requires(DelightsCommonTags.ROTTEN_MEALS)
                .requires(DelightsCommonTags.ROTTEN_MEALS)
                .requires(DelightsCommonTags.ROTTEN_MEALS)
                .unlockedBy("has_bone", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BONE))
                .save(groups(output, UNDEAD), "tuttasdelights:crafting/rotten_meat_on_a_bone");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.FROGGLE_SANDWICH.get())
                .requires(Tags.Items.FOODS_BREAD)
                .requires(DelightsTags.COOKED_FROG_LEGS).requires(DelightsTags.COOKED_FROG_LEGS)
                .requires(CommonTags.Items.CROPS_ONION)
                .unlockedBy("has_frog_leg", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(DelightsTags.RAW_FROG_LEGS).build()))
                .save(groups(output, FROG), "tuttasdelights:crafting/froggle_sandwich");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.FROG_LEG_ON_A_STICK.get())
                .requires(Items.STICK)
                .requires(DelightsTags.COOKED_FROG_LEGS).requires(DelightsTags.COOKED_FROG_LEGS)
                .unlockedBy("has_frog_leg", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(DelightsTags.RAW_FROG_LEGS).build()))
                .save(groups(output, FROG), "tuttasdelights:crafting/frog_legs_on_a_stick");

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.ROTTEN_CHICKEN_CUTS.get())
                .pattern("r")
                .pattern("#")
                .define('r', DelightsItems.ROTTEN_MINCED_BEEF.get())
                .define('#', DelightsItems.BROKEN_BONES.get())
                .unlockedBy("has_rotten_minced_beef", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.ROTTEN_MINCED_BEEF.get()))
                .save(groups(output, UNDEAD), "tuttasdelights:crafting/rotten_chicken_cuts");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.ROTTEN_MUTTON_CHOPS.get())
                .pattern("#")
                .pattern("r")
                .define('r', DelightsItems.ROTTEN_MINCED_BEEF.get())
                .define('#', DelightsItems.BROKEN_BONES.get())
                .unlockedBy("has_rotten_minced_beef", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.ROTTEN_MINCED_BEEF.get()))
                .save(groups(output, UNDEAD), "tuttasdelights:crafting/rotten_mutton_chops");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.ROTTEN_SAUSAGE.get())
                .requires(Items.STRING)
                .requires(DelightsItems.ROTTEN_MINCED_BEEF.get())
                .unlockedBy("has_rotten_minced_beef", InventoryChangeTrigger.TriggerInstance.hasItems(DelightsItems.ROTTEN_MINCED_BEEF.get()))
                .save(groups(output, UNDEAD).withConditions(new ModLoadedCondition("mynethersdelight")),
                        "mynethersdelight:crafting/rotten_sausage");

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.RAW_SUCKLING_PIG.get(), 1)
                .pattern("HB ")
                .pattern("BbB")
                .pattern(" Bb")
                .define('H', ModItems.HAM.get())
                .define('B', Items.PORKCHOP).define('b', ModItems.BACON.get())
                .unlockedBy("has_ham", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HAM.get()))
                .save(groups(output, SUCKLING_PIG), "tuttasdelights:crafting/raw_suckling_pig");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, DelightsItems.RAW_SUCKLING_PIG.get(), 1)
                .pattern(" BH")
                .pattern("BbB")
                .pattern("bB ")
                .define('H', ModItems.HAM.get())
                .define('B', Items.PORKCHOP).define('b', ModItems.BACON.get())
                .unlockedBy("has_ham", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HAM.get()))
                .save(groups(output, SUCKLING_PIG), "tuttasdelights:crafting/raw_suckling_pig_alt");

    }

    private static void napolitanaRecipe(RecipeOutput output, Ingredient cheese, String name) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DelightsItems.NAPOLITANA_WITH_FRIES_POTATOES.get())
                .requires(cheese)
                .requires(CommonTags.Items.CROPS_TOMATO)
                .requires(DelightsItems.FRIES_POTATOES.get())
                .requires(DelightsItems.COOKED_BREADED_MEAT.get())
                .requires(Items.BOWL)
                .unlockedBy("has_breaded_meat", InventoryChangeTrigger.TriggerInstance.hasItems(
                        DelightsItems.COOKED_BREADED_MEAT.get(), DelightsItems.RAW_BREADED_MEAT.get()))
                .save(output, "tuttasdelights:crafting/" + name);
    }
}
