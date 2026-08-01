package com.alltuttasneeds.delights.datagen;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.delights.CompatTags;
import com.alltuttasneeds.delights.DelightsCommonTags;
import com.alltuttasneeds.delights.DelightsModule;
import com.alltuttasneeds.delights.DelightsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class DelightsItemTags extends ItemTagsProvider {
    public DelightsItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider,
                            CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider,
                            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, DelightsModule.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        registerCommonTags();
        registerNeoForgeTags();
        registerMinecraftTags();
        registerModTags();
        registerCompatTags();
    }

    private void registerCommonTags() {
        tag(DelightsCommonTags.FOODS_COOKED_POTATO)
                .add(Items.BAKED_POTATO)
                .addOptional(id("baked_potato_slices"));
        tag(CommonTags.Items.FOODS_CABBAGE).addOptional(id("mint_lettuce"));
        tag(CommonTags.Items.CROPS_CABBAGE).addOptional(id("mint_lettuce"));
        tag(CommonTags.Items.FOODS_SAFE_RAW_FISH).addOptional(id("raw_frog_leg"));
        tag(CommonTags.Items.FOODS_RAW_CHICKEN).addOptional(id("raw_frog_leg"));
        tag(CommonTags.Items.FOODS_COOKED_CHICKEN).addOptional(id("cooked_frog_leg"));
        tag(CommonTags.Items.FOODS_RAW_MUTTON).addOptional(id("ancient_ribs"));
        tag(CommonTags.Items.FOODS_COOKED_MUTTON).addOptional(id("cooked_ancient_ribs"));
        tag(CommonTags.Items.FOODS_RAW_BEEF).addOptional(id("ancient_ribs"));
        tag(CommonTags.Items.FOODS_COOKED_BEEF).addOptional(id("cooked_ancient_ribs"));

        tag(DelightsCommonTags.RAW_FROG_MEAT)
                .addOptional(id("raw_frog_leg"))
                .addOptional(Mods.NOMANSLAND.location("frog_leg"));
        tag(DelightsCommonTags.COOKED_FROG_MEAT)
                .addOptional(id("cooked_frog_leg"))
                .addOptional(Mods.NOMANSLAND.location("cooked_frog_leg"));
        tag(DelightsCommonTags.FROG_MEAT)
                .addTag(DelightsCommonTags.RAW_FROG_MEAT)
                .addTag(DelightsCommonTags.COOKED_FROG_MEAT);

        tag(DelightsCommonTags.FOODS_BOILED_EGG).addOptional(id("ancient_boiled_egg"));
        tag(CommonTags.Items.FOODS_COOKED_EGG).addTag(DelightsCommonTags.FOODS_BOILED_EGG);
        tag(DelightsCommonTags.ROTTEN_VEGETABLES).add(Items.POISONOUS_POTATO, ModItems.ROTTEN_TOMATO.get());
        tag(DelightsCommonTags.ROTTEN_MEALS)
                .add(Items.ROTTEN_FLESH)
                .addOptional(id("rotten_bacon"))
                .addOptional(id("rotten_minced_beef"))
                .addOptional(id("rotten_chicken_cuts"))
                .addOptional(id("rotten_mutton_chops"))
                .addOptional(id("rotten_sausage"));

    }

    private void registerNeoForgeTags() {
        tag(Tags.Items.FOODS)
                .addTag(ModTags.Items.MEALS)
                .addTag(ModTags.Items.SNACKS)
                .addTag(ModTags.Items.SWEETS)
                .addTag(ModTags.Items.DRINKS)
                .addTag(ModTags.Items.FEASTS)
                .addOptional(id("mint_lettuce"))
                .addOptional(id("rotten_bacon"))
                .addOptional(id("rotten_minced_beef"))
                .addOptional(id("rotten_mutton_chops"))
                .addOptional(id("rotten_chicken_cuts"))
                .addOptional(id("rotten_sausage"))
                .addOptional(id("rotten_meat_on_a_bone"))
                .addOptional(id("rotten_ham"))
                .addOptional(id("pod_slice"))
                .addOptional(id("ancient_boiled_egg"))
                .addOptional(id("ancient_ribs"))
                .addOptional(id("cooked_ancient_ribs"))
                .addOptional(id("raw_breaded_meat"))
                .addOptional(id("cooked_breaded_meat"))
                .addOptional(id("potato_slices"))
                .addOptional(id("baked_potato_slices"))
                .addOptional(id("fries_potatoes"))
                .addOptional(id("raw_frog_leg"))
                .addOptional(id("cooked_frog_leg"));
        tag(Tags.Items.FOODS_EDIBLE_WHEN_PLACED).addTag(ModTags.Items.FEASTS);
        tag(Tags.Items.FOODS_COOKED_FISH).addOptional(id("cooked_frog_leg"));
        tag(Tags.Items.FOODS_RAW_FISH).addOptional(id("raw_frog_leg"));
        tag(Tags.Items.FOODS_VEGETABLE)
                .addOptional(id("potato_slices"))
                .addOptional(id("pod_slice"));
        tag(Tags.Items.CROPS_POTATO).addOptional(id("potato_slices"));
        tag(Tags.Items.SLIMEBALLS).addOptional(id("slimecube"));
        tag(Tags.Items.BONES).addOptional(id("broken_bones"));
        tag(Tags.Items.FOODS_RAW_MEAT).addTag(DelightsTags.RAW_FROG_LEGS);
        tag(Tags.Items.FOODS_COOKED_MEAT).addTag(DelightsTags.COOKED_FROG_LEGS);
    }

    private void registerModTags() {
        addOptional(ModTags.Items.MEALS,
                "sticky_green_noodles", "creeper_salad", "disgusting_stew", "ribs_with_eggs",
                "ancient_ribs_with_seeds", "ancient_breakfast", "pasta_with_pod", "ancient_salad",
                "plate_of_ancient_soft-boiled_egg_and_bread", "potato_omelet",
                "napolitana_with_fries_potatoes", "breaded_meat_with_mashed_potato",
                "breaded_meat_with_pasta", "mashed_potato_with_meatballs", "plate_of_potato_and_meat_pie",
                "cooked_pasta", "froggle_rice_chowder", "honey_wings", "plate_of_muzzle_with_vegetables",
                "plate_of_ham_with_vegetables", "plate_of_suckling_pig_with_vegetables",
                "beet_and_egg_salad", "pasta_with_beet");
        addOptional(ModTags.Items.SNACKS,
                "slime_dumplings", "pod_with_honey_on_a_stick", "ancient_egg_sandwich",
                "half_a_ancient_egg_sandwich", "breaded_meat_sandwich", "half_a_breaded_meat_sandwich",
                "froggle_sandwich", "half_a_froggle_sandwich", "frog_legs_on_a_stick",
                "half_a_bacon_sandwich", "half_a_egg_sandwich", "half_a_chicken_sandwich", "half_a_hamburger");
        tag(ModTags.Items.SNACKS)
                .addOptional(ResourceLocation.fromNamespaceAndPath("mynethersdelight", "half_a_nether_burger"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("mynethersdelight", "half_a_hotdog"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("mynethersdelight", "half_a_hotdog_with_fries_potatoes"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("mynethersdelight", "half_a_hotdog_with_mixed_salad"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("mynethersdelight", "half_a_hotdog_with_nether_salad"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("mynethersdelight", "half_a_chilidog"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("brewinandchewin", "half_a_ham_and_cheese_sandwich"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("minersdelight", "half_a_vegan_hamburger"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("minersdelight", "half_a_caveburger"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("minersdelight", "half_a_insect_sandwich"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("minersdelight", "half_a_squid_sandwich"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("spawn", "half_a_tuna_sandwich"));
        addOptional(ModTags.Items.SWEETS,
                "squishmallow", "smoked_squishmallow", "mintmallow_bite", "slime_jelly_slice",
                "sweet_trigger", "glistering_melon_popsicle", "cocoa_popsicle", "mint_popsicle",
                "sweet_berries_popsicle", "glow_berries_popsicle", "apple_popsicle",
                "golden_apple_popsicle", "torch_popsicle", "pumpkin_head_slice");
        addOptional(ModTags.Items.FEASTS,
                "slime_jelly", "mint_roulette", "ancient_soft-boiled_egg_and_breads", "potato_and_meat_pie",
                "suckling_pig_with_vegetables", "ice_cream_in_a_pumpkin_head");
        tag(ModTags.Items.DRINKS).addOptional(id("beet_juice"));
        tag(DelightsTags.SLIME_JELLY_BLOCKS).addOptional(id("slime_jelly"));
        tag(DelightsTags.RAW_FROG_LEGS)
                .addOptional(id("raw_frog_leg"))
                .addOptional(Mods.NOMANSLAND.location("frog_leg"));
        tag(DelightsTags.COOKED_FROG_LEGS)
                .addOptional(id("cooked_frog_leg"))
                .addOptional(Mods.NOMANSLAND.location("cooked_frog_leg"));
        tag(DelightsTags.FROG_LEGS)
                .addTag(DelightsTags.COOKED_FROG_LEGS)
                .addTag(DelightsTags.RAW_FROG_LEGS);
    }

    private void registerMinecraftTags() {
        tag(ItemTags.SWORDS).addOptional(id("rotten_meat_on_a_bone"));
        tag(ItemTags.SWORD_ENCHANTABLE).addOptional(id("rotten_meat_on_a_bone"));
        tag(ItemTags.CAT_FOOD)
                .add(ModItems.COD_SLICE.get(), ModItems.SALMON_SLICE.get(), ModItems.COD_ROLL.get(), ModItems.SALMON_ROLL.get())
                .addTag(DelightsCommonTags.RAW_FROG_MEAT);
        tag(ItemTags.WOLF_FOOD)
                .addTag(DelightsCommonTags.ROTTEN_MEALS)
                .addTag(DelightsCommonTags.FROG_MEAT)
                .addOptional(id("broken_bones"))
                .addOptional(id("rotten_meat_on_a_bone"))
                .addOptional(id("rotten_ham"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("mynethersdelight", "half_a_hotdog"));
    }

    private void registerCompatTags() {
        tag(CompatTags.BOILED_EGG_CANDIDATE).add(Items.TURTLE_EGG);
        tag(CompatTags.FOOD_JERKY_MEAT).addTag(DelightsCommonTags.ROTTEN_MEALS);
        tag(CompatTags.FOOD_PIZZA_TOPPING)
                .addTag(DelightsTags.COOKED_FROG_LEGS)
                .addOptional(id("pod_slice"))
                .addOptional(id("mint_lettuce"));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(DelightsModule.MODID, path);
    }

    private void addOptional(TagKey<Item> tagKey, String... paths) {
        var appender = tag(tagKey);
        for (String path : paths) {
            appender.addOptional(id(path));
        }
    }
}
