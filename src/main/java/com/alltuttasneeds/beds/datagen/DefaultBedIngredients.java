package com.alltuttasneeds.beds.datagen;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

final class DefaultBedIngredients {
    private DefaultBedIngredients() {}

    static Item cover(String suffix) {
        JsonObject json = read("bed_covers", suffix);
        ResourceLocation itemId = ResourceLocation.parse(GsonHelper.getAsString(json, "item"));
        return BuiltInRegistries.ITEM.getOptional(itemId)
                .orElseThrow(() -> new IllegalStateException("bed_covers/" + suffix + ".json references unknown item " + itemId));
    }

    @Nullable
    static Item blanket(String suffix, DyeColor color) {
        JsonObject json = read("bed_blankets", suffix);
        JsonObject colors = GsonHelper.getAsJsonObject(json, "colors");
        if (!colors.has(color.getSerializedName())) return null;

        ResourceLocation itemId = ResourceLocation.parse(GsonHelper.getAsString(colors, color.getSerializedName()));
        return BuiltInRegistries.ITEM.getOptional(itemId)
                .orElseThrow(() -> new IllegalStateException("bed_blankets/" + suffix + ".json references unknown item " + itemId));
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
