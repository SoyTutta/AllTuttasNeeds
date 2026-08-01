package com.alltuttasneeds.delights.datagen;

import com.alltuttasneeds.delights.DelightsItems;

import com.alltuttasneeds.delights.DelightsCommonTags;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;

import static com.alltuttasneeds.delights.config.DelightGroup.*;
import static com.alltuttasneeds.delights.datagen.DelightsRecipeConditions.groups;

public class CookingRecipes {

    public CookingRecipes() {
    }

    public static void register(RecipeOutput output) {
        cookMinecraftSoups(output);
        cookMeals(output);
    }

    private static void cookMinecraftSoups(RecipeOutput output) {
        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.DISGUSTING_STEW.get(), 1, 400, 1.0F)
                .addIngredient(DelightsCommonTags.ROTTEN_MEALS).addIngredient(DelightsCommonTags.ROTTEN_MEALS).addIngredient(DelightsCommonTags.ROTTEN_VEGETABLES)
                .addIngredient(CompoundIngredient.of(Ingredient.of(DelightsCommonTags.ROTTEN_MEALS), Ingredient.of(DelightsCommonTags.ROTTEN_VEGETABLES), Ingredient.of(ModItems.STRAW.get())))
                .unlockedByAnyIngredient(Items.ROTTEN_FLESH, DelightsItems.ROTTEN_SAUSAGE.get(), DelightsItems.ROTTEN_MINCED_BEEF.get(), DelightsItems.ROTTEN_BACON.get(), DelightsItems.ROTTEN_MUTTON_CHOPS.get(), DelightsItems.ROTTEN_CHICKEN_CUTS.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, UNDEAD), "tuttasdelights:cooking/disgusting_stew");
        CookingPotRecipeBuilder.cookingPotRecipe(net.minecraft.world.item.Items.RABBIT_STEW, 1, 200, 1.0F, net.minecraft.world.item.Items.BOWL)
                .addIngredient(DelightsCommonTags.FOODS_COOKED_POTATO).addIngredient(Items.RABBIT).addIngredient(Items.CARROT)
                .addIngredient(Ingredient.of(Items.BROWN_MUSHROOM,Items.RED_MUSHROOM))
                .unlockedByAnyIngredient(net.minecraft.world.item.Items.RABBIT, net.minecraft.world.item.Items.BROWN_MUSHROOM, net.minecraft.world.item.Items.RED_MUSHROOM, net.minecraft.world.item.Items.CARROT, DelightsItems.BAKED_POTATO_SLICES.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output, "tuttasdelights:cooking/rabbit_stew_from_cooked_potato");
    }

    private static void cookMeals(RecipeOutput output) {
        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.COOKED_PASTA.get(), 1, 200, 1.0F)
                .addIngredient(CommonTags.Items.FOODS_PASTA)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(groups(output, EXTRA_MEALS), "tuttasdelights:cooking/cooked_pasta");

        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.FRIES_POTATOES.get(), 1, 200, 1.0F,Items.PAPER)
                .addIngredient(Tags.Items.CROPS_POTATO).addIngredient(Tags.Items.CROPS_POTATO)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(groups(output, POTATO), "tuttasdelights:cooking/fries_potato");
        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.POTATO_OMELET.get(), 1, 400, 1.0F)
                .addIngredient(Tags.Items.CROPS_POTATO).addIngredient(Tags.Items.CROPS_POTATO).addIngredient(CommonTags.Items.FOODS_ONION)
                .addIngredient(Tags.Items.EGGS)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, POTATO), "tuttasdelights:cooking/potato_omelet");
        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.MASHED_POTATO_WITH_MEATBALLS.get(), 1, 200, 1.0F)
                .addIngredient(ModItems.MINCED_BEEF.get()).addIngredient(ModItems.TOMATO_SAUCE.get())
                .addIngredient(Items.BAKED_POTATO)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, POTATO), "tuttasdelights:cooking/mashed_potato_with_meatballs");

        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.PASTA_WITH_BEET.get(), 1, 200, 1.0F)
                .addIngredient(CommonTags.Items.FOODS_PASTA).addIngredient(Items.BEETROOT,2)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, EXTRA_MEALS), "tuttasdelights:cooking/pasta_with_beet");
        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.BEET_JUICE.get(), 1, 200, 1.0F)
                .addIngredient(Items.BEETROOT,3).addIngredient(Items.SUGAR)
                .setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
                .save(groups(output, EXTRA_MEALS), "tuttasdelights:cooking/beet_juice");

        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.SLIME_DUMPLINGS.get(), 1, 200, 1.0F)
                .addIngredient(DelightsItems.SLIMECUBE.get(),2).addIngredient(CommonTags.Items.FOODS_ONION)
                .addIngredient(vegetablesPatch())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, SLIME), "tuttasdelights:cooking/slime_dumplings");
        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.STICKY_GREEN_NOODLES.get(), 1, 400, 1.0F)
                .addIngredient(CommonTags.Items.FOODS_PASTA).addIngredient(CommonTags.Items.FOODS_COOKED_EGG)
                .addIngredient(DelightsItems.SLIMECUBE.get(),2)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, SLIME), "tuttasdelights:cooking/sticky_green_noodles");

        if (GUARDIAN.configurable()) {
            CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.POTLUCK_SOUP.get(), 1, 400, 2.0F, DelightsItems.GUARDIAN_HEAD.get())
                    .addIngredient(DelightsCommonTags.GUARDIAN_TAILS).addIngredient(Ingredient.of(DelightsCommonTags.ANY_GUARDIAN_MEATS), 3)
                    .addIngredient(CommonTags.Items.FOODS_ONION).addIngredient(CommonTags.Items.FOODS_TOMATO)
                    .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                    .save(groups(output, GUARDIAN), "tuttasdelights:cooking/potluck_soup");
            CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.ELDER_POTLUCK_SOUP.get(), 1, 600, 4.0F, DelightsItems.ELDER_GUARDIAN_HEAD.get())
                    .addIngredient(DelightsCommonTags.ELDER_GUARDIAN_TAILS).addIngredient(Ingredient.of(DelightsCommonTags.ANY_GUARDIAN_TAILS), 2)
                    .addIngredient(DelightsCommonTags.ANY_GUARDIAN_MEATS).addIngredient(ModItems.ONION_CRATE.get()).addIngredient(ModItems.TOMATO_CRATE.get())
                    .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                    .save(groups(output, GUARDIAN), "tuttasdelights:cooking/elder_potluck_soup");
        }

        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.ANCIENT_BOILED_EGG.get(), 8, 400, 2.0F)
                .addIngredient(Items.SNIFFER_EGG)
                .unlockedByAnyIngredient(Items.SNIFFER_EGG)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, ANCIENT), "tuttasdelights:cooking/ancient_boiled_egg");
        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.RIBS_WITH_EGGS.get(), 1, 200, 2.0F)
                .addIngredient(DelightsItems.ANCIENT_RIBS.get()).addIngredient(CommonTags.Items.FOODS_COOKED_EGG).addIngredient(CommonTags.Items.FOODS_ONION)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, ANCIENT), "tuttasdelights:cooking/ribs_with_eggs");
        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.ANCIENT_RIBS_WITH_SEEDS.get(), 1, 200, 2.0F)
                .addIngredient(DelightsItems.ANCIENT_RIBS.get())
                .addIngredient(Ingredient.of(Items.PITCHER_POD,DelightsItems.POD_SLICE.get()))
                .addIngredient(Ingredient.of(Items.PITCHER_POD,DelightsItems.POD_SLICE.get(),Items.TORCHFLOWER_SEEDS,Items.TORCHFLOWER))
                .addIngredient(Items.TORCHFLOWER)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, ANCIENT), "tuttasdelights:cooking/ancient_ribs_with_seeds");
        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.PASTA_WITH_POD.get(), 1, 200, 2.0F)
                .addIngredient(CommonTags.Items.FOODS_PASTA).addIngredient(Ingredient.of(Items.PITCHER_POD,DelightsItems.POD_SLICE.get()),2)
                .addIngredient(ModItems.TOMATO_SAUCE.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, ANCIENT), "tuttasdelights:cooking/pasta_with_pod");
        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.ANCIENT_SOFT_BOILED_EGG_AND_BREADS.get(), 1, 300, 4.0F,Items.BOWL)
                .addIngredient(Items.SNIFFER_EGG).addIngredient(Ingredient.of(Items.TORCHFLOWER_SEEDS,Items.TORCHFLOWER))
                .addIngredient(CommonTags.Items.FOODS_ONION).addIngredient(Tags.Items.FOODS_BREAD).addIngredient(Tags.Items.FOODS_BREAD)
                .unlockedByAnyIngredient(Items.SNIFFER_EGG)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, ANCIENT), "tuttasdelights:cooking/ancient_soft-boiled_egg_and_breads");

        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.SMOKED_HAM.get(), 1, 400, 2.0F)
                .addIngredient(Items.GHAST_TEAR)
                .addIngredient(DelightsItems.ROTTEN_HAM.get())
                .addIngredient(Items.GLOWSTONE_DUST)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(groups(output, UNDEAD), "tuttasdelights:cooking/cure_rotten_ham");
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.COOKED_BACON.get(), 4, 400, 2.0F)
                .addIngredient(Items.GHAST_TEAR)
                .addIngredient(DelightsItems.ROTTEN_BACON.get(),4)
                .addIngredient(Items.GLOWSTONE_DUST)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(groups(output, UNDEAD), "tuttasdelights:cooking/cure_rotten_bacon");
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.BEEF_PATTY.get(), 4, 400, 2.0F)
                .addIngredient(Items.GHAST_TEAR)
                .addIngredient(DelightsItems.ROTTEN_MINCED_BEEF.get(),4)
                .addIngredient(Items.REDSTONE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(groups(output, UNDEAD), "tuttasdelights:cooking/cure_minced_beef");
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.COOKED_MUTTON_CHOPS.get(), 4, 400, 2.0F)
                .addIngredient(Items.GHAST_TEAR)
                .addIngredient(DelightsItems.ROTTEN_MUTTON_CHOPS.get(),4)
                .addIngredient(Items.REDSTONE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(groups(output, UNDEAD), "tuttasdelights:cooking/cure_mutton_chops");
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.COOKED_CHICKEN_CUTS.get(), 4, 400, 2.0F)
                .addIngredient(Items.GHAST_TEAR)
                .addIngredient(DelightsItems.ROTTEN_CHICKEN_CUTS.get(),4)
                .addIngredient(Items.GLOWSTONE_DUST)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(groups(output, UNDEAD), "tuttasdelights:cooking/cure_chicken_cuts");

        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.BEEF_STEW.get(), 1, 400, 2.0F)
                .addIngredient(Items.GHAST_TEAR)
                .addIngredient(DelightsItems.DISGUSTING_STEW.get())
                .addIngredient(Items.REDSTONE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(groups(output, UNDEAD), "tuttasdelights:cooking/cure_disgusting_stew");
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.CHICKEN_SOUP.get(), 1, 400, 2.0F)
                .addIngredient(Items.GHAST_TEAR)
                .addIngredient(DelightsItems.DISGUSTING_STEW.get())
                .addIngredient(Items.GLOWSTONE_DUST)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(groups(output, UNDEAD), "tuttasdelights:cooking/cure_disgusting_stew_alt");

        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.SUCKLING_PIG_WITH_VEGETABLES.get(), 1, 400, 2.0F, Items.BOWL)
                .addIngredient(DelightsItems.RAW_SUCKLING_PIG.get())
                .addIngredient(Items.POTATO,2)
                .addIngredient(CommonTags.Items.FOODS_TOMATO).addIngredient(ModItems.CABBAGE.get()).addIngredient(net.neoforged.neoforge.common.Tags.Items.CROPS_CARROT)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(groups(output, SUCKLING_PIG), "tuttasdelights:cooking/suckling_pig_with_vegetables");

        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.HONEY_WINGS.get(), 1,  200, 1.0F)
                .addIngredient(CommonTags.Items.FOODS_RAW_CHICKEN)
                .addIngredient(Items.HONEY_BOTTLE)
                .addIngredient(CommonTags.Items.FOODS_ONION)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(groups(output, EXTRA_MEALS), "tuttasdelights:cooking/honey_wings");
        CookingPotRecipeBuilder.cookingPotRecipe(DelightsItems.FROGGLE_RICE_CHOWDER.get(), 1,  200, 1.0F)
                .addIngredient(DelightsCommonTags.FROG_MEAT)
                .addIngredient(Tags.Items.DRINKS_MILK)
                .addIngredient(CommonTags.Items.CROPS_RICE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(groups(output, FROG), "tuttasdelights:cooking/froggle_rice_chowdew");
    }

    private static Ingredient vegetablesPatch() {
        return DifferenceIngredient.of(Ingredient.of(Tags.Items.FOODS_VEGETABLE), Ingredient.of(new ItemLike[]{net.minecraft.world.item.Items.MELON_SLICE}));
    }
}
