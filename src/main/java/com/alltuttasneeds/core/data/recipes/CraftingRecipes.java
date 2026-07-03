package com.alltuttasneeds.core.data.recipes;

import com.alltuttasneeds.registry.TDTags;
import com.alltuttasneeds.registry.compat.framework.CompatRegistry;
import com.alltuttasneeds.registry.compat.framework.DoorTag;
import com.alltuttasneeds.registry.compat.framework.DoorVariant;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.WeatheringDoorChain;
import com.alltuttasneeds.registry.compat.framework.WoodFamily;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.EnumSet;
import java.util.List;

import static com.alltuttasneeds.core.data.recipes.RecipeIngredients.getDoorItem;
import static com.alltuttasneeds.core.data.recipes.RecipeIngredients.getItemLike;

public class CraftingRecipes {

    public static void register(RecipeOutput output) {
        CompatRegistry.loaded().forEach(compat ->
                compat.woodFamilies().forEach(family -> registerFamilyRecipes(output, family, compat))
        );

        registerSecretDoorRecipes(output);
        registerSlidingDoorRecipes(output);
        registerWaxingRecipes(output);
    }

    private static void registerFamilyRecipes(RecipeOutput output, WoodFamily family, ModCompat compat) {
        EnumSet<DoorVariant> registered = family.registeredVariants();

        String plankId = family.resolvedPlankId();
        Ingredient plank = plankId.startsWith("#")
                ? Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.tryParse(plankId.substring(1))))
                : Ingredient.of(getItemLike(plankId));

        if (plank.isEmpty()) return;

        registerMainCraftingRecipes(output, family, compat, plank, registered);
        registerConversionRecipes(output, family, compat, plank, registered);
    }

    private static void registerMainCraftingRecipes(RecipeOutput output, WoodFamily family, ModCompat compat,
                                                    Ingredient plank, EnumSet<DoorVariant> registered) {
        createShapedDoorRecipe(output, DoorVariant.DISCRETE,   family, compat, plank, 3, registered);
        createShapedDoorRecipe(output, DoorVariant.NORMAL,     family, compat, plank, 3, registered);
        createShapedDoorRecipe(output, DoorVariant.INDISCRETE, family, compat, plank, 3, registered);
        if (registered.contains(DoorVariant.TRANSIT))
            createShapedDoorRecipe(output, DoorVariant.TRANSIT, family, compat, plank, 3, registered);
        if (registered.contains(DoorVariant.PET))
            createShapedDoorRecipe(output, DoorVariant.PET, family, compat, plank, 2, registered);
    }

    private static void registerConversionRecipes(RecipeOutput output, WoodFamily family, ModCompat compat,
                                                  Ingredient plank, EnumSet<DoorVariant> registered) {
        String base = family.originalLocation().toString();

        ItemLike normalDoor     = registered.contains(DoorVariant.NORMAL)
                ? getDoorItem(family, compat, DoorVariant.NORMAL)     : getItemLike(base);
        ItemLike discreteDoor   = registered.contains(DoorVariant.DISCRETE)
                ? getDoorItem(family, compat, DoorVariant.DISCRETE)   : getItemLike(base);
        ItemLike indiscreteDoor = registered.contains(DoorVariant.INDISCRETE)
                ? getDoorItem(family, compat, DoorVariant.INDISCRETE) : getItemLike(base);

        Ingredient stick = Ingredient.of(Items.STICK);

        createConversionRecipe(output, normalDoor,     1, List.of(Ingredient.of(discreteDoor),   stick),        "normal_from_discrete",     family, compat);
        createConversionRecipe(output, discreteDoor,   1, List.of(Ingredient.of(normalDoor),     plank),        "discrete_from_normal",     family, compat);
        createConversionRecipe(output, normalDoor,     1, List.of(Ingredient.of(indiscreteDoor), plank),        "normal_from_indiscrete",   family, compat);
        createConversionRecipe(output, indiscreteDoor, 1, List.of(Ingredient.of(normalDoor),     stick),        "indiscrete_from_normal",   family, compat);
        createConversionRecipe(output, discreteDoor,   1, List.of(Ingredient.of(indiscreteDoor), plank, plank), "discrete_from_indiscrete", family, compat);
        createConversionRecipe(output, indiscreteDoor, 1, List.of(Ingredient.of(discreteDoor),   stick, stick), "indiscrete_from_discrete", family, compat);

        if (registered.contains(DoorVariant.TRANSIT))
            createConversionRecipe(output, getDoorItem(family, compat, DoorVariant.TRANSIT), 1,
                    List.of(Ingredient.of(normalDoor)), "transit_from_normal", family, compat);
        if (registered.contains(DoorVariant.PET))
            createConversionRecipe(output, getDoorItem(family, compat, DoorVariant.PET), 2,
                    List.of(Ingredient.of(discreteDoor)), "pet_from_discrete", family, compat);
    }

    private static void createShapedDoorRecipe(RecipeOutput output, DoorVariant variant, WoodFamily family,
                                               ModCompat compat, Ingredient plank, int count,
                                               EnumSet<DoorVariant> registered) {
        ItemLike result;
        if (registered.contains(variant)) {
            result = getDoorItem(family, compat, variant);
        } else if (variant == DoorVariant.DISCRETE || variant == DoorVariant.NORMAL || variant == DoorVariant.INDISCRETE) {
            result = getItemLike(family.originalLocation().toString());
        } else {
            return;
        }
        if (result.asItem() == Items.AIR) return;

        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result, count);
        switch (variant) {
            case DISCRETE   -> builder.pattern("##").pattern("##").pattern("##").define('#', plank).group("discrete_doors");
            case NORMAL     -> builder.pattern("s#").pattern("##").pattern("##").define('s', Items.STICK).define('#', plank).group("wooden_doors");
            case INDISCRETE -> builder.pattern("s#").pattern("s#").pattern("##").define('s', Items.STICK).define('#', plank).group("wooden_indiscrete_doors");
            case TRANSIT    -> builder.pattern(" #").pattern("##").pattern("##").define('#', plank).group("wooden_transit_doors");
            case PET        -> builder.pattern("ss").pattern("##").pattern("##").define('s', Items.STICK).define('#', plank).group("wooden_pet_doors");
            default -> { return; }
        }

        builder.unlockedBy("has_" + family.registryName() + "_planks", hasPlank(family));

        ResourceLocation desiredId = shapeRecipeId(variant, family, compat);
        ResourceLocation defaultId = BuiltInRegistries.ITEM.getKey(result.asItem());
        if (desiredId.equals(defaultId)) {
            builder.save(output);
        } else {
            builder.save(output, desiredId);
        }
    }

    /** DISCRETE overrides the original mod's own door recipe slot (e.g. {@code minecraft:oak_door}). */
    private static ResourceLocation shapeRecipeId(DoorVariant variant, WoodFamily family, ModCompat compat) {
        String basePath = variant == DoorVariant.DISCRETE
                ? family.registryName() + "_door"
                : family.registryName() + "_" + variant.suffix();

        String ns = family.familyNamespace();
        String folder = compat.recipeFolder(family);
        String path = folder != null ? folder + basePath : basePath;
        return ResourceLocation.fromNamespaceAndPath(ns, path);
    }

    private static void createConversionRecipe(RecipeOutput output, ItemLike result, int count,
                                               List<Ingredient> ingredients, String name,
                                               WoodFamily family, ModCompat compat) {
        if (result.asItem() == Items.AIR || ingredients.stream().anyMatch(Ingredient::isEmpty)) return;

        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, result, count);
        ingredients.forEach(builder::requires);
        builder.unlockedBy("has_" + family.registryName() + "_door", has(ingredients.get(0)))
                .group("door_conversions")
                .save(output, ResourceLocation.tryParse(
                        compat.namespace() + ":crafting/" + family.registryName() + "_" + name));
    }

    private static void registerSecretDoorRecipes(RecipeOutput output) {
        Ingredient anyDiscreteDoor = Ingredient.of(TDTags.WOODEN_DISCRETE_DOORS_ITEMS);

        CompatRegistry.loaded().forEach(compat ->
                compat.secretDoorFamilies().forEach(secret -> {
                    ItemLike bookshelf = () -> secret.bookshelf().get().asItem();
                    ItemLike secretDoor = getItemLike(compat.namespace() + ":" + secret.woodName() + "_bookshelf_door");
                    if (bookshelf.asItem() == Items.AIR || secretDoor.asItem() == Items.AIR) return;

                    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, secretDoor, 3)
                            .pattern("##").pattern("##").pattern("##")
                            .define('#', bookshelf)
                            .group("secret_doors")
                            .unlockedBy("has_" + secret.woodName() + "_bookshelf", has(bookshelf))
                            .save(output, ResourceLocation.fromNamespaceAndPath(
                                    compat.namespace(), "crafting/" + secret.woodName() + "_bookshelf_door"));

                    ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, secretDoor, 1)
                            .requires(bookshelf)
                            .requires(anyDiscreteDoor)
                            .group("secret_door_conversion")
                            .unlockedBy("has_any_discrete_door", has(TDTags.WOODEN_DISCRETE_DOORS_ITEMS))
                            .save(output, ResourceLocation.fromNamespaceAndPath(
                                    compat.namespace(), "crafting/" + secret.woodName() + "_bookshelf_door_from_conversion"));
                })
        );

        createCrossCompatBookshelfRecipe(output, "fir", "nomansland:fir_bookshelf",
                "newworld:fir_bookshelf_door", "newworld", anyDiscreteDoor);
    }

    private static void createCrossCompatBookshelfRecipe(RecipeOutput output, String woodName,
                                                         String bookshelfId, String doorId,
                                                         String recipeNamespace, Ingredient anyDiscreteDoor) {
        ItemLike bookshelf = getItemLike(bookshelfId);
        ItemLike door = getItemLike(doorId);
        if (bookshelf.asItem() == Items.AIR || door.asItem() == Items.AIR) return;

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, door, 1)
                .requires(bookshelf)
                .requires(anyDiscreteDoor)
                .group("secret_door_conversion")
                .unlockedBy("has_any_discrete_door", has(TDTags.WOODEN_DISCRETE_DOORS_ITEMS))
                .save(output, ResourceLocation.fromNamespaceAndPath(
                        recipeNamespace, "crafting/" + woodName + "_bookshelf_door_from_" + bookshelfId.replace(':', '_')));
    }

    private static void registerSlidingDoorRecipes(RecipeOutput output) {
        CompatRegistry.loaded().forEach(compat ->
                compat.extraDoors().stream()
                        .filter(extra -> extra.tags().contains(DoorTag.SLIDING) && extra.creativeTabAnchor() != null)
                        .forEach(extra -> createSlidingDoorRecipe(output,
                                extra.creativeTabAnchor().toString(),
                                compat.registryLocation(extra.name()).toString())));
    }

    private static void createSlidingDoorRecipe(RecipeOutput output, String barsId, String doorId) {
        ItemLike door = getItemLike(doorId);
        ItemLike bars = getItemLike(barsId);
        if (door.asItem() == Items.AIR || bars.asItem() == Items.AIR) return;

        ResourceLocation barsLocation = ResourceLocation.tryParse(barsId);
        String criterionName = barsLocation != null ? barsLocation.getPath() : barsId;

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, door, 3)
                .pattern("##").pattern("##").pattern("##")
                .define('#', bars)
                .group("sliding_doors")
                .unlockedBy("has_" + criterionName, has(bars))
                .save(output, ResourceLocation.tryParse(doorId));
    }

    private static void registerWaxingRecipes(RecipeOutput output) {
        CompatRegistry.loaded().forEach(compat ->
                compat.weatheringLinks().forEach(link -> registerWaxingRecipe(output, compat.namespace(), link)));
    }

    private static void registerWaxingRecipe(RecipeOutput output, String namespace, WeatheringDoorChain.Link link) {
        ItemLike unwaxed = getItemLike(namespace + ":" + link.from());
        ItemLike waxed = getItemLike(namespace + ":" + link.waxed());
        if (unwaxed.asItem() == Items.AIR || waxed.asItem() == Items.AIR) return;

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, waxed)
                .requires(unwaxed).requires(Items.HONEYCOMB)
                .unlockedBy("has_" + link.from(), has(unwaxed))
                .group("waxed_copper_sliding_doors")
                .save(output, namespace + ":crafting/waxed_" + link.from());
    }

    private static Criterion<?> hasPlank(WoodFamily family) {
        String plankId = family.resolvedPlankId();
        if (plankId.startsWith("#"))
            return has(TagKey.create(Registries.ITEM, ResourceLocation.tryParse(plankId.substring(1))));
        return has(getItemLike(plankId));
    }

    private static Criterion<?> has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

    private static Criterion<?> has(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }

    private static Criterion<?> has(Ingredient ingredient) {
        if (ingredient.getItems().length == 0)
            return InventoryChangeTrigger.TriggerInstance.hasItems(Items.AIR);
        return InventoryChangeTrigger.TriggerInstance.hasItems(
                ItemPredicate.Builder.item().of(ingredient.getItems()[0].getItem()).build());
    }
}