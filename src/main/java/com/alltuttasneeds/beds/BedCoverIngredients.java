package com.alltuttasneeds.beds;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class BedCoverIngredients extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(BedCoverIngredients.class);

    private static Map<String, Item> ingredients = Map.of();

    public BedCoverIngredients() {
        super(new Gson(), "bed_covers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<String, Item> built = new HashMap<>();
        resources.forEach((id, json) -> {
            try {
                ResourceLocation itemId = ResourceLocation.parse(GsonHelper.getAsString(json.getAsJsonObject(), "item"));
                Item item = BuiltInRegistries.ITEM.getOptional(itemId).orElse(null);
                if (item == null) {
                    LOGGER.warn("Bed cover ingredient {} references unknown item {}", id, itemId);
                    return;
                }
                built.put(id.getPath(), item);
            } catch (Exception e) {
                LOGGER.error("Failed to parse bed cover ingredient {}", id, e);
            }
        });
        ingredients = Map.copyOf(built);
    }

    @Nullable
    public static Item ingredientFor(String suffix) {
        return ingredients.get(suffix);
    }
}
