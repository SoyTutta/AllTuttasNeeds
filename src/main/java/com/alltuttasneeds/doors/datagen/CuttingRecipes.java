package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.core.condition.DoorSetEnabledCondition;
import com.alltuttasneeds.core.condition.ModuleEnabledCondition;
import com.alltuttasneeds.doors.compat.CompatRegistry;
import com.alltuttasneeds.doors.compat.DoorVariant;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.SecretDoorFamily;
import com.alltuttasneeds.doors.compat.WoodFamily;
import com.alltuttasneeds.core.Mods;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.EnumSet;
import java.util.List;

import static com.alltuttasneeds.doors.datagen.RecipeIngredients.getDoorItem;
import static com.alltuttasneeds.doors.datagen.RecipeIngredients.getItemLike;

public class CuttingRecipes {

    public static void register(RecipeOutput output) {
        RecipeOutput enabledOutput = output.withConditions(ModuleEnabledCondition.DOORS);
        CompatRegistry.loaded().forEach(compat -> {
            RecipeOutput compatOutput = conditional(enabledOutput, compat.mod());
            compat.woodFamilies().forEach(family -> cutDoorsToPlank(compatOutput, family, compat));
            compat.secretDoorFamilies().forEach(secret -> cutSecretDoorToBookshelf(
                    compatOutput.withConditions(DoorSetEnabledCondition.SECRET), secret, compat));
        });
    }

    private static void cutDoorsToPlank(RecipeOutput output, WoodFamily family, ModCompat compat) {
        ItemLike plank = getItemLike(family.cuttingOutputId());
        if (plank.asItem() == Items.AIR) return;

        EnumSet<DoorVariant> registered = family.registeredVariants();

        createDoorCuttingRecipe(output, getItemLike(family.originalLocation().toString()), plank,
                compat, family.registryName() + "_door");
        createDoorCuttingRecipes(output.withConditions(DoorSetEnabledCondition.CONSISTENT), family, compat,
                plank, registered, List.of(DoorVariant.NORMAL, DoorVariant.INDISCRETE, DoorVariant.DISCRETE),
                family.registryName() + "_consistent_doors");
        createDoorCuttingRecipes(output.withConditions(DoorSetEnabledCondition.TRANSIT), family, compat,
                plank, registered, List.of(DoorVariant.TRANSIT), family.registryName() + "_transit_door");
        createDoorCuttingRecipes(output.withConditions(DoorSetEnabledCondition.PET), family, compat,
                plank, registered, List.of(DoorVariant.PET), family.registryName() + "_pet_door");
    }

    private static void createDoorCuttingRecipes(RecipeOutput output, WoodFamily family, ModCompat compat,
                                                 ItemLike plank, EnumSet<DoorVariant> registered,
                                                 List<DoorVariant> variants, String recipeName) {
        ItemLike[] doors = variants.stream()
                .filter(registered::contains)
                .map(variant -> getDoorItem(family, compat, variant))
                .filter(door -> door.asItem() != Items.AIR)
                .toArray(ItemLike[]::new);
        if (doors.length == 0) return;

        CuttingBoardRecipeBuilder.cuttingRecipe(
                Ingredient.of(doors), Ingredient.of(ItemTags.AXES), plank, 1
        ).build(output, ResourceLocation.fromNamespaceAndPath(compat.namespace(), "cutting/" + recipeName));
    }

    private static void createDoorCuttingRecipe(RecipeOutput output, ItemLike door, ItemLike plank,
                                                ModCompat compat, String recipeName) {
        if (door.asItem() == Items.AIR) return;
        CuttingBoardRecipeBuilder.cuttingRecipe(
                Ingredient.of(door), Ingredient.of(ItemTags.AXES), plank, 1
        ).build(output, ResourceLocation.fromNamespaceAndPath(compat.namespace(), "cutting/" + recipeName));
    }

    private static void cutSecretDoorToBookshelf(RecipeOutput output, SecretDoorFamily secret, ModCompat compat) {
        ItemLike bookshelf = () -> secret.bookshelf().get().asItem();
        ItemLike secretDoor = getItemLike(compat.namespace() + ":" + secret.woodName() + "_bookshelf_door");
        if (bookshelf.asItem() == Items.AIR || secretDoor.asItem() == Items.AIR) return;

        CuttingBoardRecipeBuilder.cuttingRecipe(
                Ingredient.of(secretDoor),
                Ingredient.of(ItemTags.AXES),
                bookshelf, 1
        ).build(output, ResourceLocation.fromNamespaceAndPath(
                compat.namespace(), "cutting/" + secret.woodName() + "_bookshelf_door"));
    }

    private static RecipeOutput conditional(RecipeOutput output, Mods mod) {
        return mod == Mods.ALLTUTTASNEEDS
                ? output
                : output.withConditions(new ModLoadedCondition(mod.id()));
    }
}
