package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.core.condition.DoorSetEnabledCondition;
import com.alltuttasneeds.core.condition.ModuleEnabledCondition;
import com.alltuttasneeds.doors.TDTags;
import com.alltuttasneeds.doors.compat.CompatRegistry;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.SecretDoorFamily;
import com.alltuttasneeds.doors.compat.WoodFamily;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import static com.alltuttasneeds.doors.datagen.RecipeIngredients.getItemLike;

public class CuttingRecipes {

    public static void register(RecipeOutput output) {
        RecipeOutput enabledOutput = output.withConditions(ModuleEnabledCondition.DOORS);
        CompatRegistry.loaded().forEach(compat -> {
            RecipeOutput compatOutput = conditional(enabledOutput, compat.mod());
            compat.woodFamilies().forEach(family -> cutDoorsToPlank(compatOutput, family, compat));
            compat.secretDoorFamilies().forEach(secret -> cutSecretDoorToBookshelf(
                    secretDoorOutput(enabledOutput, compat)
                            .withConditions(DoorSetEnabledCondition.SECRET), secret, compat));
        });
    }

    private static void cutDoorsToPlank(RecipeOutput output, WoodFamily family, ModCompat compat) {
        ItemLike plank = getItemLike(family.cuttingOutputId());
        if (plank.asItem() == Items.AIR) return;

        CuttingBoardRecipeBuilder.cuttingRecipe(
                Ingredient.of(TDTags.woodFamilyDoors(ResourceLocation.parse(family.familyId()))),
                Ingredient.of(ItemTags.AXES), plank, 1
        ).build(output, ResourceLocation.fromNamespaceAndPath(
                compat.namespace(), "cutting/" + family.registryName() + "_doors"));
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

    private static RecipeOutput secretDoorOutput(RecipeOutput output, ModCompat compat) {
        output = conditional(output, compat.mod());
        for (Mods dependency : compat.secretDoorDependencies()) {
            output = conditional(output, dependency);
        }
        return output;
    }
}
