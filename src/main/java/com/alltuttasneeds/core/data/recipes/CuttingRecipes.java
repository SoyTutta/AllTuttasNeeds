package com.alltuttasneeds.core.data.recipes;

import com.alltuttasneeds.registry.compat.framework.CompatRegistry;
import com.alltuttasneeds.registry.compat.framework.DoorVariant;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.SecretDoorFamily;
import com.alltuttasneeds.registry.compat.framework.WoodFamily;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.alltuttasneeds.core.data.recipes.RecipeIngredients.getDoorItem;
import static com.alltuttasneeds.core.data.recipes.RecipeIngredients.getItemLike;

public class CuttingRecipes {

    public static void register(RecipeOutput output) {
        CompatRegistry.loaded().forEach(compat -> {
            compat.woodFamilies().forEach(family -> cutDoorsToPlank(output, family, compat));
            compat.secretDoorFamilies().forEach(secret -> cutSecretDoorToBookshelf(output, secret, compat));
        });
    }

    private static void cutDoorsToPlank(RecipeOutput output, WoodFamily family, ModCompat compat) {
        ItemLike plank = getItemLike(family.cuttingOutputId());
        if (plank.asItem() == Items.AIR) return;

        String baseDoorId = family.originalLocation().toString();
        EnumSet<DoorVariant> registered = family.registeredVariants();
        List<ItemLike> doors = new ArrayList<>();
        Set<Item> seen = new HashSet<>();

        for (DoorVariant variant : List.of(DoorVariant.NORMAL, DoorVariant.INDISCRETE, DoorVariant.TRANSIT, DoorVariant.PET, DoorVariant.DISCRETE)) {
            ItemLike door;
            if (registered.contains(variant)) {
                door = getDoorItem(family, compat, variant);
            } else if (variant == DoorVariant.NORMAL || variant == DoorVariant.DISCRETE || variant == DoorVariant.INDISCRETE) {
                door = getItemLike(baseDoorId);
            } else {
                continue;
            }
            if (door.asItem() != Items.AIR && seen.add(door.asItem())) doors.add(door);
        }

        if (doors.isEmpty()) return;

        CuttingBoardRecipeBuilder.cuttingRecipe(
                Ingredient.of(doors.toArray(ItemLike[]::new)),
                Ingredient.of(ItemTags.AXES),
                plank, 1
        ).build(output, ResourceLocation.fromNamespaceAndPath(
                compat.namespace(), "cutting/" + family.registryName() + "_door"));
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
}