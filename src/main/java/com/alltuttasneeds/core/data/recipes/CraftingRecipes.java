package com.alltuttasneeds.core.data.recipes;

import com.alltuttasneeds.registry.TDTags;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.fml.ModList;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class CraftingRecipes {

    private record WoodData(
            String name, String modId, String plankId, String baseDoorId,
            boolean hasNormal, boolean hasIndiscrete, boolean hasTransit, boolean hasPet, boolean hasDiscrete,
            String cuttingOutputId
    ) {}

    private record BookshelfData(
            String woodName, String modId, String bookshelfId, String secretDoorId
    ) {}

    private static final List<WoodData> WOOD_DATA_LIST = List.of(
            // Vanilla
            new WoodData("oak", "tuttasdoors", "minecraft:oak_planks", "minecraft:oak_door", false, true, true, true, true, "minecraft:oak_planks"),
            new WoodData("spruce", "tuttasdoors", "minecraft:spruce_planks", "minecraft:spruce_door", true, true, true, true, false, "minecraft:spruce_planks"),
            new WoodData("birch", "tuttasdoors", "minecraft:birch_planks", "minecraft:birch_door", true, true, true, true, false, "minecraft:birch_planks"),
            new WoodData("jungle", "tuttasdoors", "minecraft:jungle_planks", "minecraft:jungle_door", false, true, true, true, true, "minecraft:jungle_planks"),
            new WoodData("acacia", "tuttasdoors", "minecraft:acacia_planks", "minecraft:acacia_door", true, false, true, true, true, "minecraft:acacia_planks"),
            new WoodData("dark_oak", "tuttasdoors", "minecraft:dark_oak_planks", "minecraft:dark_oak_door", true, true, true, true, false, "minecraft:dark_oak_planks"),
            new WoodData("mangrove", "tuttasdoors", "minecraft:mangrove_planks", "minecraft:mangrove_door", true, true, true, true, false, "minecraft:mangrove_planks"),
            new WoodData("cherry", "tuttasdoors", "minecraft:cherry_planks", "minecraft:cherry_door", false, true, true, true, true, "minecraft:cherry_planks"),
            new WoodData("bamboo", "tuttasdoors", "minecraft:bamboo_planks", "minecraft:bamboo_door", false, true, true, true, true, "minecraft:bamboo_planks"),
            new WoodData("crimson", "tuttasdoors", "minecraft:crimson_planks", "minecraft:crimson_door", true, true, true, true, false, "minecraft:crimson_planks"),
            new WoodData("warped", "tuttasdoors", "minecraft:warped_planks", "minecraft:warped_door", true, true, true, true, false, "minecraft:warped_planks"),

            // My Nether's Delight
            new WoodData("powdery", "mynethersdelight", "mynethersdelight:powdery_planks", "mynethersdelight:powdery_door", false, true, true, true, true, "mynethersdelight:powdery_planks"),

            // New World
            new WoodData("fir", "newworld", "newworld:fir_planks", "newworld:fir_door", true, true, true, true, false, "newworld:fir_planks"),

            // Arts and Crafts
            new WoodData("cork", "arts_and_crafts", "arts_and_crafts:cork_planks", "arts_and_crafts:cork_door", true, true, true, true, false, "arts_and_crafts:cork_planks"),

            // No Man's Land
            new WoodData("pine", "nomansland", "nomansland:pine_planks", "nomansland:pine_door", false, true, true, true, true, "nomansland:pine_planks"),
            new WoodData("walnut", "nomansland", "nomansland:walnut_planks", "nomansland:walnut_door", false, true, true, true, true, "nomansland:walnut_planks"),
            new WoodData("willow", "nomansland", "nomansland:willow_planks", "nomansland:willow_door", false, true, true, true, true, "nomansland:willow_planks"),
            new WoodData("maple", "nomansland", "nomansland:maple_planks", "nomansland:maple_door", true, true, true, true, false, "nomansland:maple_planks"),

            // Enderscape
            new WoodData("veiled", "enderscape", "enderscape:veiled_planks", "enderscape:veiled_door", false, true, true, true, true, "enderscape:veiled_planks"),
            new WoodData("celestial", "enderscape", "enderscape:celestial_planks", "enderscape:celestial_door", true, true, true, true, false, "enderscape:celestial_planks"),
            new WoodData("murublight", "enderscape", "enderscape:murublight_planks", "enderscape:murublight_door", true, true, true, true, false, "enderscape:murublight_planks"),

            // Malum (Usa tags, pero ahora especifica una salida de corte concreta)
            new WoodData("runewood", "malum", "#malum:runewood_planks", "malum:runewood_door", false, true, true, true, true, "malum:runewood_planks"),
            new WoodData("runewood_boards", "malum", "#malum:runewood_boards", "malum:runewood_boards_door", true, true, true, true, false, "malum:runewood_boards"),
            new WoodData("soulwood", "malum", "#malum:soulwood_planks", "malum:soulwood_door", false, true, true, true, true, "malum:soulwood_planks"),
            new WoodData("soulwood_boards", "malum", "#malum:soulwood_boards", "malum:soulwood_boards_door", true, true, true, true, false, "malum:soulwood_boards"),

            // Biomes O' Plenty
            new WoodData("fir", "biomesoplenty", "biomesoplenty:fir_planks", "biomesoplenty:fir_door", true, true, true, true, false, "biomesoplenty:fir_planks"),
            new WoodData("pine", "biomesoplenty", "biomesoplenty:pine_planks", "biomesoplenty:pine_door", true, true, true, true, false, "biomesoplenty:pine_planks"),
            new WoodData("maple", "biomesoplenty", "biomesoplenty:maple_planks", "biomesoplenty:maple_door", true, true, true, true, false, "biomesoplenty:maple_planks"),
            new WoodData("redwood", "biomesoplenty", "biomesoplenty:redwood_planks", "biomesoplenty:redwood_door", true, true, true, true, false, "biomesoplenty:redwood_planks"),
            new WoodData("mahogany", "biomesoplenty", "biomesoplenty:mahogany_planks", "biomesoplenty:mahogany_door", true, false, true, true, true, "biomesoplenty:mahogany_planks"),
            new WoodData("jacaranda", "biomesoplenty", "biomesoplenty:jacaranda_planks", "biomesoplenty:jacaranda_door", true, false, true, true, true, "biomesoplenty:jacaranda_planks"),
            new WoodData("palm", "biomesoplenty", "biomesoplenty:palm_planks", "biomesoplenty:palm_door", true, false, true, true, true, "biomesoplenty:palm_planks"),
            new WoodData("willow", "biomesoplenty", "biomesoplenty:willow_planks", "biomesoplenty:willow_door", true, false, true, true, true, "biomesoplenty:willow_planks"),
            new WoodData("dead", "biomesoplenty", "biomesoplenty:dead_planks", "biomesoplenty:dead_door", true, false, true, true, true, "biomesoplenty:dead_planks"),
            new WoodData("magic", "biomesoplenty", "biomesoplenty:magic_planks", "biomesoplenty:magic_door", true, false, true, true, true, "biomesoplenty:magic_planks"),
            new WoodData("umbran", "biomesoplenty", "biomesoplenty:umbran_planks", "biomesoplenty:umbran_door", false, true, true, true, true, "biomesoplenty:umbran_planks"),
            new WoodData("hellbark", "biomesoplenty", "biomesoplenty:hellbark_planks", "biomesoplenty:hellbark_door", false, true, true, true, true, "biomesoplenty:hellbark_planks"),
            new WoodData("empyreal", "biomesoplenty", "biomesoplenty:empyreal_planks", "biomesoplenty:empyreal_door", true, true, true, true, false, "biomesoplenty:empyreal_planks"),

            // Nature's Spirit
            new WoodData("aspen", "natures_spirit", "natures_spirit:aspen_planks", "natures_spirit:aspen_door", true, false, true, true, true, "natures_spirit:aspen_planks"),
            new WoodData("cedar", "natures_spirit", "natures_spirit:cedar_planks", "natures_spirit:cedar_door", false, true, true, true, true, "natures_spirit:cedar_planks"),
            new WoodData("coconut", "natures_spirit", "natures_spirit:coconut_planks", "natures_spirit:coconut_door", true, true, false, true, true, "natures_spirit:coconut_planks"),
            new WoodData("cypress", "natures_spirit", "natures_spirit:cypress_planks", "natures_spirit:cypress_door", true, false, true, true, true, "natures_spirit:cypress_planks"),
            new WoodData("fir", "natures_spirit", "natures_spirit:fir_planks", "natures_spirit:fir_door", true, false, true, true, true, "natures_spirit:fir_planks"),
            new WoodData("ghaf", "natures_spirit", "natures_spirit:ghaf_planks", "natures_spirit:ghaf_door", true, true, false, true, true, "natures_spirit:ghaf_planks"),
            new WoodData("joshua", "natures_spirit", "natures_spirit:joshua_planks", "natures_spirit:joshua_door", true, false, true, true, true, "natures_spirit:joshua_planks"),
            new WoodData("larch", "natures_spirit", "natures_spirit:larch_planks", "natures_spirit:larch_door", false, true, true, true, true, "natures_spirit:larch_planks"),
            new WoodData("mahogany", "natures_spirit", "natures_spirit:mahogany_planks", "natures_spirit:mahogany_door", true, true, false, true, true, "natures_spirit:mahogany_planks"),
            new WoodData("maple", "natures_spirit", "natures_spirit:maple_planks", "natures_spirit:maple_door", true, false, true, true, true, "natures_spirit:maple_planks"),
            new WoodData("olive", "natures_spirit", "natures_spirit:olive_planks", "natures_spirit:olive_door", true, false, true, true, true, "natures_spirit:olive_planks"),
            new WoodData("palo_verde", "natures_spirit", "natures_spirit:palo_verde_planks", "natures_spirit:palo_verde_door", false, true, true, true, true, "natures_spirit:palo_verde_planks"),
            new WoodData("redwood", "natures_spirit", "natures_spirit:redwood_planks", "natures_spirit:redwood_door", true, false, true, true, true, "natures_spirit:redwood_planks"),
            new WoodData("saxaul", "natures_spirit", "natures_spirit:saxaul_planks", "natures_spirit:saxaul_door", false, true, true, true, true, "natures_spirit:saxaul_planks"),
            new WoodData("sugi", "natures_spirit", "natures_spirit:sugi_planks", "natures_spirit:sugi_door", false, true, true, true, true, "natures_spirit:sugi_planks"),
            new WoodData("willow", "natures_spirit", "natures_spirit:willow_planks", "natures_spirit:willow_door", true, false, true, true, true, "natures_spirit:willow_planks"),
            new WoodData("wisteria", "natures_spirit", "natures_spirit:wisteria_planks", "natures_spirit:wisteria_door", true, false, true, true, true, "natures_spirit:wisteria_planks")
    );

    private static final List<BookshelfData> BOOKSHELF_DATA_LIST = List.of(
            new BookshelfData("oak", "tuttasdoors", "minecraft:bookshelf", "tuttasdoors:oak_bookshelf_door"),
            new BookshelfData("spruce", "nomansland", "nomansland:spruce_bookshelf", "nomansland:spruce_bookshelf_door"),
            new BookshelfData("birch", "nomansland", "nomansland:birch_bookshelf", "nomansland:birch_bookshelf_door"),
            new BookshelfData("jungle", "nomansland", "nomansland:jungle_bookshelf", "nomansland:jungle_bookshelf_door"),
            new BookshelfData("dark_oak", "nomansland", "nomansland:dark_oak_bookshelf", "nomansland:dark_oak_bookshelf_door"),
            new BookshelfData("mangrove", "nomansland", "nomansland:mangrove_bookshelf", "nomansland:mangrove_bookshelf_door"),
            new BookshelfData("cherry", "nomansland", "nomansland:cherry_bookshelf", "nomansland:cherry_bookshelf_door"),
            new BookshelfData("bamboo", "nomansland", "nomansland:bamboo_bookshelf", "nomansland:bamboo_bookshelf_door"),
            new BookshelfData("crimson", "nomansland", "nomansland:crimson_bookshelf", "nomansland:crimson_bookshelf_door"),
            new BookshelfData("warped", "nomansland", "nomansland:warped_bookshelf", "nomansland:warped_bookshelf_door"),
            new BookshelfData("fir", "newworld", "nomansland:fir_bookshelf", "newworld:fir_bookshelf_door"),
            new BookshelfData("pine", "nomansland", "nomansland:pine_bookshelf", "nomansland:pine_bookshelf_door"),
            new BookshelfData("willow", "nomansland", "nomansland:willow_bookshelf", "nomansland:willow_bookshelf_door"),
            new BookshelfData("walnut", "nomansland", "nomansland:walnut_bookshelf", "nomansland:walnut_bookshelf_door"),
            new BookshelfData("maple", "nomansland", "nomansland:maple_bookshelf", "nomansland:maple_bookshelf_door")
    );


    public static void register(RecipeOutput output) {
        for (WoodData wood : WOOD_DATA_LIST) {
            if (isModCorrect(wood)) {
                if (wood.plankId.startsWith("#")) {
                    registerRecipesForTag(output, wood);
                } else {
                    registerRecipesForItem(output, wood);
                }
                registerDoorCuttingRecipes(output, wood);
            }
        }

        registerSecretDoorRecipes(output);

        createSlidingDoorRecipe(output, "iron_bars", "minecraft:iron_bars", "tuttasdoors:iron_bars_sliding_door");

        createSlidingDoorRecipe(output, "golden_bars", "blockbox:golden_bars", "blockbox:golden_bars_sliding_door");
        createSlidingDoorRecipe(output, "copper_bars", "blockbox:copper_bars", "blockbox:copper_bars_sliding_door");

        createSlidingDoorRecipe(output, "brass_bars"   , "create:brass_bars"   , "create:brass_bars_sliding_door");
        createSlidingDoorRecipe(output, "andesite_bars", "create:andesite_bars", "create:andesite_bars_sliding_door");
        createSlidingDoorRecipe(output, "copper_bars"  , "create:copper_bars"  , "create:copper_bars_sliding_door");

        if (ModList.get().isLoaded("blockbox")) {
            registerBlockBoxRecipes(output);
        }
    }

    private static void registerSecretDoorRecipes(RecipeOutput output) {
        Ingredient anyDiscreteDoor = Ingredient.of(TDTags.WOODEN_DISCRETE_DOORS_ITEMS);

        for (BookshelfData data : BOOKSHELF_DATA_LIST) {
            ItemLike bookshelf = getItemLike(data.bookshelfId());
            ItemLike secretDoor = getItemLike(data.secretDoorId());

            if (bookshelf.asItem() == Items.AIR || secretDoor.asItem() == Items.AIR) {
                continue;
            }
            createSecretDoorShapedRecipe(output, data, bookshelf, secretDoor);
            createSecretDoorConversionRecipe(output, data, bookshelf, anyDiscreteDoor, secretDoor);
            createSecretDoorCuttingRecipe(output, data, secretDoor, bookshelf);
        }
    }

    private static void createSecretDoorShapedRecipe(RecipeOutput output, BookshelfData data, ItemLike bookshelf, ItemLike secretDoor) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, secretDoor, 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', bookshelf)
                .group("secret_doors")
                .unlockedBy("has_" + data.woodName() + "_bookshelf", has(bookshelf))
                .save(output, ResourceLocation.fromNamespaceAndPath(data.modId(), "crafting/" + data.woodName() + "_bookshelf_door"));
    }

    private static void createSecretDoorConversionRecipe(RecipeOutput output, BookshelfData data, ItemLike bookshelf, Ingredient discreteDoorIngredient, ItemLike secretDoor) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, secretDoor, 1)
                .requires(bookshelf)
                .requires(discreteDoorIngredient)
                .group("secret_door_conversion")
                .unlockedBy("has_any_discrete_door", has(TDTags.WOODEN_DISCRETE_DOORS_ITEMS))
                .save(output, ResourceLocation.fromNamespaceAndPath(data.modId(), "crafting/" + data.woodName() + "_bookshelf_door_from_conversion"));
    }

    private static void createSecretDoorCuttingRecipe(RecipeOutput output, BookshelfData data, ItemLike secretDoor, ItemLike bookshelf) {
        CuttingBoardRecipeBuilder.cuttingRecipe(
                        Ingredient.of(secretDoor),
                        Ingredient.of(ItemTags.AXES),
                        bookshelf, 1
                )
                .build(output, ResourceLocation.fromNamespaceAndPath(data.modId(), data.woodName() + "_bookshelf_door"));
    }

    private static boolean isModCorrect(WoodData wood) {
        if (!wood.modId.equals("tuttasdoors") && !wood.modId.equals("minecraft")) {
            return ModList.get().isLoaded(wood.modId);
        }
        return true;
    }

    private static void registerRecipesForItem(RecipeOutput output, WoodData wood) {
        ItemLike plank = getItemLike(wood.plankId());
        registerMainCraftingRecipes(output, wood, plank);
        registerConversionRecipes(output, wood);
        // --- MODIFICADO: La llamada al método de corte se ha movido al bucle principal ---
    }

    private static void registerMainCraftingRecipes(RecipeOutput output, WoodData wood, ItemLike plank) {
        createShapedDoorRecipe(output, "discrete", wood, Ingredient.of(plank), 3);
        createShapedDoorRecipe(output, "normal", wood, Ingredient.of(plank), 3);
        createShapedDoorRecipe(output, "indiscrete", wood, Ingredient.of(plank), 3);

        if (wood.hasTransit()) {
            createShapedDoorRecipe(output, "transit", wood, Ingredient.of(plank), 3);
        }
        if (wood.hasPet()) {
            createShapedDoorRecipe(output, "pet", wood, Ingredient.of(plank), 2);
        }
    }

    private static void registerConversionRecipes(RecipeOutput output, WoodData wood) {
        ItemLike normalDoor = getDoorItem(wood, "normal");
        if (normalDoor.asItem() == Items.AIR) normalDoor = getItemLike(wood.baseDoorId());

        ItemLike discreteDoor = getDoorItem(wood, "discrete");
        if (discreteDoor.asItem() == Items.AIR) discreteDoor = getItemLike(wood.baseDoorId());

        ItemLike indiscreteDoor = getDoorItem(wood, "indiscrete");
        if (indiscreteDoor.asItem() == Items.AIR) indiscreteDoor = getItemLike(wood.baseDoorId());

        ItemLike plank = getItemLike(wood.plankId());

        createConversionRecipe(output, normalDoor, List.of(discreteDoor, () -> Items.STICK), "normal_from_discrete", wood);
        createConversionRecipe(output, discreteDoor, List.of(normalDoor, plank), "discrete_from_normal", wood);
        createConversionRecipe(output, normalDoor, List.of(indiscreteDoor, plank), "normal_from_indiscrete", wood);
        createConversionRecipe(output, indiscreteDoor, List.of(normalDoor, () -> Items.STICK), "indiscrete_from_normal", wood);
        createConversionRecipe(output, discreteDoor, List.of(indiscreteDoor, plank, plank), "discrete_from_indiscrete", wood);
        createConversionRecipe(output, indiscreteDoor, List.of(discreteDoor, () -> Items.STICK, () -> Items.STICK), "indiscrete_from_discrete", wood);

        if (wood.hasTransit()) createConversionRecipe(output, getDoorItem(wood, "transit"), 1, List.of(normalDoor), "transit_from_normal", wood);
        if (wood.hasPet()) createConversionRecipe(output, getDoorItem(wood, "pet"), 2, List.of(discreteDoor), "pet_from_discrete", wood);
    }

    private static void registerDoorCuttingRecipes(RecipeOutput output, WoodData wood) {
        ItemLike plankOutput = getItemLike(wood.cuttingOutputId());
        if (plankOutput.asItem() == Items.AIR) {
            return;
        }

        List<String> variants = List.of("normal", "indiscrete", "transit", "pet", "discrete");
        for (String variant : variants) {
            ItemLike doorItem = getDoorItem(wood, variant);
            if (doorItem.asItem() == Items.AIR) {
                if ("discrete".equals(variant) && !wood.hasDiscrete) doorItem = getItemLike(wood.baseDoorId);
                if ("normal".equals(variant) && !wood.hasNormal) doorItem = getItemLike(wood.baseDoorId);
                if ("indiscrete".equals(variant) && !wood.hasIndiscrete) doorItem = getItemLike(wood.baseDoorId);
            }

            if (doorItem.asItem() != Items.AIR) {
                ResourceLocation doorId = BuiltInRegistries.ITEM.getKey(doorItem.asItem());

                CuttingBoardRecipeBuilder.cuttingRecipe(
                        Ingredient.of(doorItem),
                        Ingredient.of(ItemTags.AXES),
                        plankOutput, 1
                ).build(output, doorId.getNamespace() + doorId.getPath());
            }
        }
    }

    private static void registerRecipesForTag(RecipeOutput output, WoodData wood) {
        TagKey<Item> plankTag = TagKey.create(Registries.ITEM, ResourceLocation.tryParse(wood.plankId().substring(1)));
        Ingredient plankIngredient = Ingredient.of(plankTag);

        registerMainCraftingRecipesForTag(output, wood, plankIngredient);
        registerConversionRecipesForTag(output, wood, plankIngredient);
    }

    private static void registerMainCraftingRecipesForTag(RecipeOutput output, WoodData wood, Ingredient plankIngredient) {
        createShapedDoorRecipe(output, "discrete", wood, plankIngredient, 3);
        createShapedDoorRecipe(output, "normal", wood, plankIngredient, 3);
        createShapedDoorRecipe(output, "indiscrete", wood, plankIngredient, 3);

        if (wood.hasTransit()) {
            createShapedDoorRecipe(output, "transit", wood, plankIngredient, 3);
        }
        if (wood.hasPet()) {
            createShapedDoorRecipe(output, "pet", wood, plankIngredient, 2);
        }
    }

    private static void registerConversionRecipesForTag(RecipeOutput output, WoodData wood, Ingredient plankIngredient) {
        ItemLike normalDoor = getDoorItem(wood, "normal");
        if (normalDoor.asItem() == Items.AIR) normalDoor = getItemLike(wood.baseDoorId());

        ItemLike discreteDoor = getDoorItem(wood, "discrete");
        if (discreteDoor.asItem() == Items.AIR) discreteDoor = getItemLike(wood.baseDoorId());

        ItemLike indiscreteDoor = getDoorItem(wood, "indiscrete");
        if (indiscreteDoor.asItem() == Items.AIR) indiscreteDoor = getItemLike(wood.baseDoorId());

        List<Ingredient> ingredientsNormalFromDiscrete = List.of(Ingredient.of(discreteDoor), Ingredient.of(Items.STICK));
        List<Ingredient> ingredientsDiscreteFromNormal = List.of(Ingredient.of(normalDoor), plankIngredient);
        List<Ingredient> ingredientsNormalFromIndiscrete = List.of(Ingredient.of(indiscreteDoor), plankIngredient);
        List<Ingredient> ingredientsIndiscreteFromNormal = List.of(Ingredient.of(normalDoor), Ingredient.of(Items.STICK));
        List<Ingredient> ingredientsDiscreteFromIndiscrete = List.of(Ingredient.of(indiscreteDoor), plankIngredient, plankIngredient);
        List<Ingredient> ingredientsIndiscreteFromDiscrete = List.of(Ingredient.of(discreteDoor), Ingredient.of(Items.STICK), Ingredient.of(Items.STICK));

        createConversionRecipeForTag(output, normalDoor, 1, ingredientsNormalFromDiscrete, "normal_from_discrete", wood);
        createConversionRecipeForTag(output, discreteDoor, 1, ingredientsDiscreteFromNormal, "discrete_from_normal", wood);
        createConversionRecipeForTag(output, normalDoor, 1, ingredientsNormalFromIndiscrete, "normal_from_indiscrete", wood);
        createConversionRecipeForTag(output, indiscreteDoor, 1, ingredientsIndiscreteFromNormal, "indiscrete_from_normal", wood);
        createConversionRecipeForTag(output, discreteDoor, 1, ingredientsDiscreteFromIndiscrete, "discrete_from_indiscrete", wood);
        createConversionRecipeForTag(output, indiscreteDoor, 1, ingredientsIndiscreteFromDiscrete, "indiscrete_from_discrete", wood);

        if (wood.hasTransit()) createConversionRecipeForTag(output, getDoorItem(wood, "transit"), 1, List.of(Ingredient.of(normalDoor)), "transit_from_normal", wood);
        if (wood.hasPet()) createConversionRecipeForTag(output, getDoorItem(wood, "pet"), 2, List.of(Ingredient.of(discreteDoor)), "pet_from_discrete", wood);
    }

    private static void createShapedDoorRecipe(RecipeOutput output, String type, WoodData wood, Ingredient plank, int count) {
        ItemLike result = getDoorItem(wood, type);

        if (result.asItem() == Items.AIR) {
            if ("normal".equals(type) || "discrete".equals(type) || "indiscrete".equals(type)) {
                result = getItemLike(wood.baseDoorId());
            }
        }
        if (result.asItem() == Items.AIR) return;

        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result, count);

        switch (type) {
            case "discrete" -> builder.pattern("##").pattern("##").pattern("##").define('#', plank).group("discrete_doors");
            case "normal" -> builder.pattern("s#").pattern("##").pattern("##").define('s', Items.STICK).define('#', plank).group("wooden_doors");
            case "indiscrete" -> builder.pattern("s#").pattern("s#").pattern("##").define('s', Items.STICK).define('#', plank).group("wooden_indiscrete_doors");
            case "transit" -> builder.pattern(" #").pattern("##").pattern("##").define('#', plank).group("wooden_transit_doors");
            case "pet" -> builder.pattern("ss").pattern("##").pattern("##").define('s', Items.STICK).define('#', plank).group("wooden_pet_doors");
            default -> { return; }
        }

        ResourceLocation baseDoorLocation = ResourceLocation.tryParse(wood.baseDoorId());
        if (baseDoorLocation == null) return;

        String targetNamespace = baseDoorLocation.getNamespace();

        String recipePath = "discrete".equals(type)
                ? wood.name() + "_door"
                : wood.name() + "_" + type + "_door";

        boolean omitCraftingFolder = "minecraft".equals(targetNamespace) || "malum".equals(targetNamespace) || "enderscape".equals(targetNamespace) || "biomesoplenty".equals(targetNamespace) || "natures_spirit".equals(targetNamespace);
        boolean NMLCraftingFolder = "nomansland".equals(targetNamespace);

        if (NMLCraftingFolder) {
            recipePath = "wood/" + wood.name() + "/" + recipePath;
        } else if (!omitCraftingFolder) {
            recipePath = "crafting/" + recipePath;
        }

        ResourceLocation desiredRecipeId = ResourceLocation.fromNamespaceAndPath(targetNamespace, recipePath);
        ResourceLocation defaultRecipeId = BuiltInRegistries.ITEM.getKey(result.asItem());

        builder.unlockedBy("has_" + wood.name() + "_planks", has(plank));

        if (desiredRecipeId.equals(defaultRecipeId)) {
            builder.save(output);
        } else {
            builder.save(output, desiredRecipeId);
        }
    }

    private static void createConversionRecipe(RecipeOutput output, ItemLike result, int count, List<ItemLike> ingredients, String name, WoodData wood) {
        if (result.asItem() == Items.AIR || ingredients.stream().anyMatch(i -> i.asItem() == Items.AIR)) return;
        List<Ingredient> ingredientList = ingredients.stream().map(Ingredient::of).collect(Collectors.toList());
        createConversionRecipeForTag(output, result, count, ingredientList, name, wood);
    }

    private static void createConversionRecipeForTag(RecipeOutput output, ItemLike result, int count, List<Ingredient> ingredients, String name, WoodData wood) {
        if (result.asItem() == Items.AIR || ingredients.stream().anyMatch(Ingredient::isEmpty)) return;

        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, result, count);
        ingredients.forEach(builder::requires);
        builder.unlockedBy("has_" + wood.name() + "_door", has(ingredients.get(0)))
                .group("door_conversions")
                .save(output, String.format("%s:crafting/%s_%s", wood.modId(), wood.name(), name));
    }

    private static void createSlidingDoorRecipe(RecipeOutput output, String recipeName, String barsId, String doorId) {
        ItemLike doorItem = getItemLike(doorId);
        ItemLike barsItem = getItemLike(barsId);

        if (doorItem.asItem() == Items.AIR || barsItem.asItem() == Items.AIR) {
            return;
        }

        ResourceLocation doorLocation = ResourceLocation.tryParse(doorId);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, doorItem, 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', barsItem)
                .group("sliding_doors")
                .unlockedBy("has_" + recipeName, has(barsItem))
                .save(output, doorLocation);
    }

    private static void registerBlockBoxRecipes(RecipeOutput output) {
        registerCopperWaxingRecipes(output);
    }

    private static void registerCopperWaxingRecipes(RecipeOutput output) {
        List<String> copperVariants = List.of("copper_bars_sliding_door", "exposed_copper_bars_sliding_door", "weathered_copper_bars_sliding_door", "oxidized_copper_bars_sliding_door");

        for (String variant : copperVariants) {
            ItemLike unwaxed = getItemLike("blockbox:" + variant);
            ItemLike waxed = getItemLike("blockbox:waxed_" + variant);

            if (unwaxed.asItem() == Items.AIR || waxed.asItem() == Items.AIR) continue;

            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, waxed)
                    .requires(unwaxed).requires(Items.HONEYCOMB)
                    .unlockedBy("has_" + variant, has(unwaxed))
                    .group("waxed_copper_sliding_doors")
                    .save(output, "blockbox:crafting/waxed_" + variant);
        }
    }

    private static ItemLike getDoorItem(WoodData wood, String variant) {
        return getItemLike(String.format("%s:%s_%s_door", wood.modId(), wood.name(), variant));
    }

    private static ItemLike getItemLike(String id) {
        if (id == null || id.isBlank() || id.startsWith("#")) return Items.AIR;
        ResourceLocation rl = ResourceLocation.tryParse(id);
        if (rl == null) return Items.AIR;
        return () -> BuiltInRegistries.ITEM.getOptional(rl).orElse(Items.AIR);
    }

    private static Criterion<?> has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

    private static Criterion<?> has(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }

    private static Criterion<?> has(Ingredient ingredient) {
        if (ingredient.getItems().length == 0) {
            return InventoryChangeTrigger.TriggerInstance.hasItems(Items.AIR);
        }
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ingredient.getItems()[0].getItem()).build());
    }

    private static void createConversionRecipe(RecipeOutput output, ItemLike result, List<ItemLike> ingredients, String name, WoodData wood) {
        createConversionRecipe(output, result, 1, ingredients, name, wood);
    }
}