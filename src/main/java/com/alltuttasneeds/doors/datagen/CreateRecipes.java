package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.core.condition.DoorSetEnabledCondition;
import com.alltuttasneeds.core.condition.ModuleEnabledCondition;
import com.alltuttasneeds.doors.compat.CompatRegistry;
import com.alltuttasneeds.doors.compat.DoorVariant;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.SecretDoorFamily;
import com.alltuttasneeds.doors.compat.WoodFamily;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.EnumSet;
import java.util.List;

import static com.alltuttasneeds.doors.datagen.RecipeIngredients.getDoorItem;
import static com.alltuttasneeds.doors.datagen.RecipeIngredients.getItemLike;

public final class CreateRecipes {

    private CreateRecipes() {}

    public static void register(RecipeOutput output) {
        RecipeOutput enabledOutput = output.withConditions(
                ModuleEnabledCondition.DOORS,
                new ModLoadedCondition(Mods.CREATE.id()));

        CompatRegistry.loaded().forEach(compat -> {
            RecipeOutput compatOutput = conditional(enabledOutput, compat.mod());
            compat.woodFamilies().forEach(family -> {
                cutWoodToDoors(compatOutput, family, compat);
                recycleDoors(compatOutput, family, compat);
            });

            RecipeOutput secretOutput = secretDoorOutput(enabledOutput, compat)
                    .withConditions(DoorSetEnabledCondition.SECRET);
            compat.secretDoorFamilies().forEach(secret ->
                    registerSecretDoorRecipes(secretOutput, secret, compat));
        });
    }

    private static void cutWoodToDoors(RecipeOutput output, WoodFamily family, ModCompat compat) {
        Ingredient input = woodInput(family);
        if (input.isEmpty()) return;

        for (DoorVariant variant : family.registeredVariants()) {
            ItemLike door = getDoorItem(family, compat, variant);
            if (door.asItem() == Items.AIR) continue;

            createRecipe(output.withConditions(DoorSetEnabledCondition.forSet(variant.set())),
                    input, door, 2, compat.namespace(),
                    BuiltInRegistries.ITEM.getKey(door.asItem()).getPath() + "_from_" + woodInputName(family));
        }
    }

    private static void recycleDoors(RecipeOutput output, WoodFamily family, ModCompat compat) {
        ItemLike material = getItemLike(family.cuttingOutputId());
        if (material.asItem() == Items.AIR) return;

        createRecyclingRecipe(output, Ingredient.of(getItemLike(family.originalLocation().toString())),
                material, compat.namespace(), family.registryName() + "_door");

        EnumSet<DoorVariant> registered = family.registeredVariants();
        createVariantRecyclingRecipe(output.withConditions(DoorSetEnabledCondition.CONSISTENT),
                family, compat, material, registered,
                List.of(DoorVariant.NORMAL, DoorVariant.INDISCRETE, DoorVariant.DISCRETE),
                family.registryName() + "_consistent_doors");
        createVariantRecyclingRecipe(output.withConditions(DoorSetEnabledCondition.TRANSIT),
                family, compat, material, registered, List.of(DoorVariant.TRANSIT),
                family.registryName() + "_transit_door");
        createVariantRecyclingRecipe(output.withConditions(DoorSetEnabledCondition.PET),
                family, compat, material, registered, List.of(DoorVariant.PET),
                family.registryName() + "_pet_door");
    }

    private static void createVariantRecyclingRecipe(RecipeOutput output, WoodFamily family, ModCompat compat,
                                                      ItemLike material, EnumSet<DoorVariant> registered,
                                                      List<DoorVariant> variants, String name) {
        ItemLike[] doors = variants.stream()
                .filter(registered::contains)
                .map(variant -> getDoorItem(family, compat, variant))
                .filter(door -> door.asItem() != Items.AIR)
                .toArray(ItemLike[]::new);
        if (doors.length == 0) return;

        createRecyclingRecipe(output, Ingredient.of(doors), material, compat.namespace(), name);
    }

    private static void registerSecretDoorRecipes(RecipeOutput output, SecretDoorFamily secret,
                                                  ModCompat compat) {
        ItemLike bookshelf = () -> secret.bookshelf().get().asItem();
        ItemLike secretDoor = getItemLike(compat.namespace() + ":" + secret.woodName() + "_bookshelf_door");
        if (bookshelf.asItem() == Items.AIR || secretDoor.asItem() == Items.AIR) return;

        createRecipe(output, Ingredient.of(bookshelf), secretDoor, 1, compat.namespace(),
                secret.woodName() + "_bookshelf_door_from_" + secret.woodName() + "_bookshelf");
        createRecyclingRecipe(output, Ingredient.of(secretDoor), bookshelf, compat.namespace(),
                secret.woodName() + "_bookshelf_door");
    }

    private static void createRecyclingRecipe(RecipeOutput output, Ingredient input, ItemLike result,
                                              String namespace, String inputName) {
        if (input.isEmpty()) return;
        createRecipe(output, input, result, 1, namespace, "recycling/" + inputName);
    }

    private static void createRecipe(RecipeOutput output, Ingredient input, ItemLike result, int count,
                                     String namespace, String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, "create/" + name);
        new StandardProcessingRecipe.Builder<CuttingRecipe>(CuttingRecipe::new, id)
                .require(input)
                .output(result, count)
                .duration(50)
                .build(output);
    }

    private static Ingredient woodInput(WoodFamily family) {
        ResourceLocation tagLocation = family.logTagLocation();
        if (tagLocation != null) {
            return Ingredient.of(TagKey.create(Registries.ITEM, tagLocation));
        }

        ItemLike log = getItemLike(family.logId().toString());
        return log.asItem() == Items.AIR ? Ingredient.EMPTY : Ingredient.of(log);
    }

    private static String woodInputName(WoodFamily family) {
        ResourceLocation tagLocation = family.logTagLocation();
        return tagLocation != null ? tagLocation.getPath() : family.logId().getPath();
    }

    private static RecipeOutput conditional(RecipeOutput output, Mods mod) {
        return mod == Mods.ALLTUTTASNEEDS || mod == Mods.CREATE
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
