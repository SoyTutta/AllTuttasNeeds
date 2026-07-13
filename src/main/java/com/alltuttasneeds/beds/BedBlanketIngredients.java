package com.alltuttasneeds.beds;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class BedBlanketIngredients extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(BedBlanketIngredients.class);

    private static Map<String, Map<DyeColor, Item>> ingredients = Map.of();

    public BedBlanketIngredients() {
        super(new Gson(), "bed_blankets");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<String, Map<DyeColor, Item>> built = new HashMap<>();
        resources.forEach((id, json) -> {
            try {
                JsonObject colors = GsonHelper.getAsJsonObject(json.getAsJsonObject(), "colors");
                Map<DyeColor, Item> byColor = new EnumMap<>(DyeColor.class);
                for (Map.Entry<String, JsonElement> entry : colors.entrySet()) {
                    DyeColor color = DyeColor.byName(entry.getKey(), null);
                    if (color == null) {
                        LOGGER.warn("Bed blanket ingredient {} has unknown colour {}", id, entry.getKey());
                        continue;
                    }
                    ResourceLocation itemId = ResourceLocation.parse(entry.getValue().getAsString());
                    Item item = BuiltInRegistries.ITEM.getOptional(itemId).orElse(null);
                    if (item == null) {
                        LOGGER.warn("Bed blanket ingredient {} references unknown item {}", id, itemId);
                        continue;
                    }
                    byColor.put(color, item);
                }
                built.put(id.getPath(), Map.copyOf(byColor));
            } catch (Exception e) {
                LOGGER.error("Failed to parse bed blanket ingredient {}", id, e);
            }
        });
        ingredients = Map.copyOf(built);
    }

    @Nullable
    public static Item itemFor(String suffix, DyeColor color) {
        Map<DyeColor, Item> byColor = ingredients.get(suffix);
        return byColor == null ? null : byColor.get(color);
    }

    @Nullable
    public static DyeColor colorFor(String suffix, ItemStack stack) {
        Map<DyeColor, Item> byColor = ingredients.get(suffix);
        if (byColor == null) return null;
        for (Map.Entry<DyeColor, Item> entry : byColor.entrySet()) {
            if (stack.is(entry.getValue())) return entry.getKey();
        }
        return null;
    }
}
