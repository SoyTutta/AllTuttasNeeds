package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.BedDyeRecipe;
import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.MattressFamily;
import com.alltuttasneeds.beds.TBContent;
import com.alltuttasneeds.beds.WoolColors;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import com.alltuttasneeds.core.Mods;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.Map;
import java.util.function.Supplier;

public final class TBRecipes {
    private TBRecipes() {}

    public static void register(RecipeOutput output) {
        registerFrame(output);
        registerBedDyeing(output);

        BedCompatRegistry.loaded().forEach(compat -> compat.families().forEach(family -> {
            Mods owner = compat.mod();
            RecipeOutput familyOutput = conditional(output, owner);
            registerMattress(familyOutput, family);
            registerFrameMattressCombos(output, family, owner);
            registerDirectApplyRecipes(output, family, owner);
            registerWoolBlankets(familyOutput, family);
            registerLeatherBlankets(familyOutput, family);
            registerDeluxeUpgrades(familyOutput, family);
        }));
    }

    private static void registerFrame(RecipeOutput output) {
        if (TBContent.BED_FRAME_ITEM == null) return;

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TBContent.BED_FRAME_ITEM.get())
                .pattern("PPP")
                .pattern("###")
                .define('P', Items.IRON_NUGGET)
                .define('#', ItemTags.PLANKS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output, ResourceLocation.fromNamespaceAndPath("tuttasbeds", "crafting/bed_frame"));
    }

    private static void registerBedDyeing(RecipeOutput output) {
        SpecialRecipeBuilder.special(BedDyeRecipe::new)
                .save(output, ResourceLocation.fromNamespaceAndPath("tuttasbeds", "crafting/bed_dye"));
    }

    private static void registerFrameMattressCombos(RecipeOutput output, MattressFamily family, Mods owner) {
        if (TBContent.BED_FRAME_ITEM == null) return;
        Item frame = TBContent.BED_FRAME_ITEM.get();

        registerCombine(conditional(output, owner), frame, family.looseMattress().get(), family.bedBare().get(), "frame");

        family.looseMattressCovers().forEach((cover, mattress) -> {
            Supplier<Block> coveredBed = family.bedBasicCovers().get(cover);
            if (coveredBed != null) {
                registerCombine(conditionalForCover(output, owner, cover), frame, mattress.get(), coveredBed.get(), "frame");
            }
        });
    }

    private static void registerDirectApplyRecipes(RecipeOutput output, MattressFamily family, Mods owner) {
        family.looseMattressCovers().forEach((cover, result) -> {
            if (cover.isDirectApplyEnabled()) {
                registerCombine(conditionalForCover(output, owner, cover), family.looseMattress().get(), DefaultBedIngredients.cover(cover.suffix()), result.get(), "direct");
            }
        });
        family.bedBasicCovers().forEach((cover, result) -> {
            if (cover.isDirectApplyEnabled()) {
                registerCombine(conditionalForCover(output, owner, cover), family.bedBare().get(), DefaultBedIngredients.cover(cover.suffix()), result.get(), "direct");
            }
        });

        Supplier<Block> bareBed = family.bedBare();
        if (bareBed == null) return;

        family.bedBlankets().forEach((blanket, byColor) -> {
            if (!blanket.isDirectApplyEnabled()) return;
            for (DyeColor color : DyeColor.values()) {
                Supplier<Block> result = byColor.get(color);
                Item ingredient = result == null ? null : DefaultBedIngredients.blanket(blanket.suffix(), color);
                if (ingredient != null) {
                    registerCombine(conditional(output, owner), bareBed.get(), ingredient, result.get(), "direct");
                }
            }
        });
    }

    private static void registerMattress(RecipeOutput output, MattressFamily family) {
        Block mattress = family.looseMattress().get();
        ResourceLocation mattressId = BuiltInRegistries.BLOCK.getKey(mattress);
        if (!mattressId.getNamespace().equals("farmersdelight")) return;

        Item strawBale = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("farmersdelight", "straw_bale"));
        if (family.material().id().equals("straw")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, mattress)
                    .pattern("SSS")
                    .define('S', strawBale)
                    .unlockedBy("has_straw_bale", has(strawBale))
                    .save(output, idFor(mattress));
            return;
        }

        if (family.material().id().equals("canvas")) {
            Item canvas = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("farmersdelight", "canvas"));
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, mattress)
                    .pattern("###")
                    .pattern("SSS")
                    .pattern("###")
                    .define('#', canvas)
                    .define('S', strawBale)
                    .unlockedBy("has_canvas", has(canvas))
                    .unlockedBy("has_straw_bale", has(strawBale))
                    .save(output, idFor(mattress));
        }
    }

    private static void registerCombine(RecipeOutput output, ItemLike base, ItemLike ingredient, Block result, String kind) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, result)
                .requires(base)
                .requires(ingredient)
                .unlockedBy("has_base", has(base))
                .save(output, idFor(result, kind));
    }

    private static void registerWoolBlankets(RecipeOutput output, MattressFamily family) {
        Supplier<Block> bareBed = family.bedBare();
        if (bareBed == null) return;

        Map<DyeColor, Supplier<Block>> byColor = blanketColors(family, "wool_blanket");
        for (DyeColor color : DyeColor.values()) {
            Supplier<Block> result = byColor.get(color);
            if (result == null) continue;

            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, result.get())
                    .requires(bareBed.get())
                    .requires(WoolColors.woolItem(color), 3)
                    .unlockedBy("has_bed", has(bareBed.get()))
                    .save(output, idFor(result.get()));
        }
    }

    private static void registerLeatherBlankets(RecipeOutput output, MattressFamily family) {
        Supplier<Block> bareBed = family.bedBare();
        if (bareBed == null) return;

        Map<DyeColor, Supplier<Block>> byColor = blanketColors(family, "leather_blanket");

        Supplier<Block> brown = byColor.get(DyeColor.BROWN);
        if (brown != null) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, brown.get())
                    .requires(bareBed.get())
                    .requires(Items.LEATHER, 3)
                    .unlockedBy("has_bed", has(bareBed.get()))
                    .save(output, idFor(brown.get()));
        }

        for (DyeColor color : DyeColor.values()) {
            if (color == DyeColor.BROWN) continue;
            Supplier<Block> result = byColor.get(color);
            if (result == null) continue;

            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, result.get())
                    .requires(bareBed.get())
                    .requires(Items.LEATHER, 3)
                    .requires(DyeItem.byColor(color))
                    .unlockedBy("has_bed", has(bareBed.get()))
                    .save(output, idFor(result.get()));
        }
    }

    private static void registerDeluxeUpgrades(RecipeOutput output, MattressFamily family) {
        Map<DyeColor, Supplier<Block>> wool = blanketColors(family, "wool_blanket");
        Map<DyeColor, Supplier<Block>> deluxe = family.bedDeluxe();

        for (DyeColor color : DyeColor.values()) {
            Supplier<Block> normal = wool.get(color);
            Supplier<Block> result = deluxe.get(color);
            if (normal == null || result == null) continue;

            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get())
                    .pattern(" P ")
                    .pattern("P#P")
                    .pattern(" P ")
                    .define('P', Items.IRON_NUGGET)
                    .define('#', normal.get())
                    .unlockedBy("has_normal_bed", has(normal.get()))
                    .save(output, idFor(result.get()));
        }
    }

    private static Map<DyeColor, Supplier<Block>> blanketColors(MattressFamily family, String blanketSuffix) {
        for (Map.Entry<BlanketMaterial, Map<DyeColor, Supplier<Block>>> entry : family.bedBlankets().entrySet()) {
            if (entry.getKey().suffix().equals(blanketSuffix)) return entry.getValue();
        }
        return Map.of();
    }

    private static ResourceLocation idFor(Block result) {
        ResourceLocation resultId = BuiltInRegistries.BLOCK.getKey(result);
        return ResourceLocation.fromNamespaceAndPath(resultId.getNamespace(), "crafting/" + resultId.getPath());
    }

    private static ResourceLocation idFor(Block result, String kind) {
        ResourceLocation resultId = BuiltInRegistries.BLOCK.getKey(result);
        return ResourceLocation.fromNamespaceAndPath(resultId.getNamespace(), "crafting/" + resultId.getPath() + "_" + kind);
    }

    private static Criterion<?> has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

    private static Criterion<?> has(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }

    private static RecipeOutput conditionalForCover(RecipeOutput output, Mods owner, CoverMaterial cover) {
        return cover.suffix().equals("canvas_cover")
                ? conditional(output, owner, Mods.FARMERS_DELIGHT)
                : conditional(output, owner);
    }

    private static RecipeOutput conditional(RecipeOutput output, Mods... mods) {
        ModLoadedCondition[] conditions = java.util.Arrays.stream(mods)
                .filter(mod -> mod != Mods.ALLTUTTASNEEDS)
                .distinct()
                .map(mod -> new ModLoadedCondition(mod.id()))
                .toArray(ModLoadedCondition[]::new);
        return conditions.length == 0 ? output : output.withConditions(conditions);
    }
}
