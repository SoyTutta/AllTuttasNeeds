package com.alltuttasneeds.beds.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

final class DefaultBedIngredients {
    private DefaultBedIngredients() {}

    static Ingredient cover(String suffix) {
        JsonObject json = read("bed_covers", suffix);
        Set<Item> items = new LinkedHashSet<>();
        if (json.has("item")) addCoverItem(suffix, GsonHelper.getAsString(json, "item"), items);
        if (json.has("items")) {
            for (JsonElement element : GsonHelper.getAsJsonArray(json, "items")) {
                addCoverItem(suffix, element.getAsString(), items);
            }
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("bed_covers/" + suffix + ".json must contain at least one valid item");
        }
        return Ingredient.of(items.toArray(Item[]::new));
    }

    private static void addCoverItem(String suffix, String value, Set<Item> items) {
        ResourceLocation itemId = ResourceLocation.parse(value);
        Item item = BuiltInRegistries.ITEM.getOptional(itemId)
                .orElseThrow(() -> new IllegalStateException("bed_covers/" + suffix + ".json references unknown item " + itemId));
        items.add(item);
    }

    private static JsonObject read(String folder, String suffix) {
        String path = "data/tuttasbeds/" + folder + "/" + suffix + ".json";
        try (InputStream stream = DefaultBedIngredients.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalStateException("Missing default bed ingredient JSON at " + path);
            }
            return GsonHelper.parse(new InputStreamReader(stream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + path, e);
        }
    }
}
