package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.core.condition.DoorSetEnabledCondition;
import com.alltuttasneeds.core.condition.ModuleEnabledCondition;
import com.alltuttasneeds.doors.compat.CompatRegistry;
import com.alltuttasneeds.doors.compat.DoorVariant;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.SecretDoorFamily;
import com.alltuttasneeds.doors.compat.WoodFamily;
import com.teamabnormals.woodworks.core.data.server.WoodworksRecipeProvider;
import com.teamabnormals.woodworks.core.other.WoodworksConditions;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import static com.alltuttasneeds.doors.datagen.RecipeIngredients.getDoorItem;
import static com.alltuttasneeds.doors.datagen.RecipeIngredients.getItemLike;

public final class WoodworksRecipes {

    private WoodworksRecipes() {}

    public static void register(RecipeOutput output) {
        RecipeOutput enabledOutput = output.withConditions(
                ModuleEnabledCondition.DOORS,
                new ModLoadedCondition(Mods.WOODWORKS.id()),
                WoodworksConditions.SAWMILL_ENABLED);

        CompatRegistry.loaded().forEach(compat -> {
            RecipeOutput compatOutput = conditional(enabledOutput, compat.mod());
            compat.woodFamilies().forEach(family -> registerWoodRecipes(compatOutput, family, compat));

            RecipeOutput secretOutput = secretDoorOutput(enabledOutput, compat)
                    .withConditions(DoorSetEnabledCondition.SECRET);
            compat.secretDoorFamilies().forEach(secret ->
                    registerSecretDoorRecipe(secretOutput, secret, compat));
        });
    }

    private static void registerWoodRecipes(RecipeOutput output, WoodFamily family, ModCompat compat) {
        SawmillInput input = woodInput(family);
        if (input == null) return;

        for (DoorVariant variant : family.registeredVariants()) {
            ItemLike door = getDoorItem(family, compat, variant);
            if (door.asItem() == Items.AIR) continue;

            createRecipe(output.withConditions(DoorSetEnabledCondition.forSet(variant.set())),
                    input.ingredient(), input.criterion(), door, 2, compat.namespace(), input.name());
        }
    }

    private static void registerSecretDoorRecipe(RecipeOutput output, SecretDoorFamily secret, ModCompat compat) {
        ItemLike bookshelf = () -> secret.bookshelf().get().asItem();
        ItemLike secretDoor = getItemLike(compat.namespace() + ":" + secret.woodName() + "_bookshelf_door");
        if (bookshelf.asItem() == Items.AIR || secretDoor.asItem() == Items.AIR) return;

        ResourceLocation bookshelfId = BuiltInRegistries.ITEM.getKey(bookshelf.asItem());
        createRecipe(output, Ingredient.of(bookshelf), has(bookshelf), secretDoor, 1,
                compat.namespace(), bookshelfId.getPath());
    }

    private static SawmillInput woodInput(WoodFamily family) {
        ResourceLocation tagLocation = family.logTagLocation();
        if (tagLocation != null) {
            TagKey<Item> tag = TagKey.create(Registries.ITEM, tagLocation);
            return new SawmillInput(Ingredient.of(tag), has(tag), tagLocation.getPath());
        }

        ItemLike log = getItemLike(family.logId().toString());
        if (log.asItem() == Items.AIR) return null;
        return new SawmillInput(Ingredient.of(log), has(log), family.logId().getPath());
    }

    private static void createRecipe(RecipeOutput output, Ingredient ingredient, Criterion<?> criterion,
                                     ItemLike result, int count, String namespace, String inputName) {
        ResourceLocation resultId = BuiltInRegistries.ITEM.getKey(result.asItem());
        WoodworksRecipeProvider.sawing(RecipeCategory.REDSTONE, ingredient, result, count)
                .unlockedBy("has_" + inputName, criterion)
                .save(output, ResourceLocation.fromNamespaceAndPath(namespace,
                        "sawmill/" + resultId.getPath() + "_from_" + inputName + "_sawing"));
    }

    private static Criterion<?> has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

    private static Criterion<?> has(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }

    private static RecipeOutput conditional(RecipeOutput output, Mods mod) {
        return mod == Mods.ALLTUTTASNEEDS || mod == Mods.WOODWORKS
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

    private record SawmillInput(Ingredient ingredient, Criterion<?> criterion, String name) {}
}
