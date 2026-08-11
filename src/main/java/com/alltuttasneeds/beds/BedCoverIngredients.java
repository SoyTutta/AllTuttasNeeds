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
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BedCoverIngredients extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(BedCoverIngredients.class);

    private static volatile Map<String, List<Item>> loadedIngredients = Map.of();
    private static volatile Map<String, List<Item>> activeIngredients = Map.of();

    public BedCoverIngredients() {
        super(new Gson(), "bed_covers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<String, List<Item>> built = new HashMap<>();
        Set<String> duplicateSuffixes = duplicateSuffixes(resources);
        resources.forEach((id, json) -> {
            if (duplicateSuffixes.contains(id.getPath())) return;
            try {
                List<Item> items = readItems(id, json.getAsJsonObject());
                if (!items.isEmpty()) built.put(id.getPath(), items);
            } catch (Exception e) {
                LOGGER.error("Failed to parse bed cover ingredient {}", id, e);
            }
        });
        loadedIngredients = Map.copyOf(built);
        activeIngredients = loadedIngredients;
    }

    private static Set<String> duplicateSuffixes(Map<ResourceLocation, JsonElement> resources) {
        Map<String, ResourceLocation> sources = new HashMap<>();
        Set<String> duplicates = new HashSet<>();
        resources.keySet().forEach(id -> {
            ResourceLocation previous = sources.putIfAbsent(id.getPath(), id);
            if (previous != null) {
                duplicates.add(id.getPath());
                LOGGER.error("Duplicate bed cover suffix {} in {} and {}; ignoring this suffix", id.getPath(), previous, id);
            }
        });
        return duplicates;
    }

    private static List<Item> readItems(ResourceLocation source, JsonObject root) {
        Set<Item> resolved = new LinkedHashSet<>();
        if (root.has("item")) addItem(source, GsonHelper.getAsString(root, "item"), resolved);
        if (root.has("items")) {
            for (JsonElement element : GsonHelper.getAsJsonArray(root, "items")) {
                addItem(source, element.getAsString(), resolved);
            }
        }
        if (!root.has("item") && !root.has("items")) {
            throw new IllegalArgumentException("Expected item or items");
        }
        return List.copyOf(resolved);
    }

    private static void addItem(ResourceLocation source, String value, Set<Item> resolved) {
        ResourceLocation itemId = ResourceLocation.parse(value);
        Item item = BuiltInRegistries.ITEM.getOptional(itemId).orElse(null);
        if (item == null) {
            LOGGER.warn("Bed cover ingredient {} references unknown item {}", source, itemId);
            return;
        }
        resolved.add(item);
    }

    public static List<Item> ingredientsFor(String suffix) {
        return activeIngredients.getOrDefault(suffix, List.of());
    }

    public static List<SyncedEntry> syncedEntries() {
        return loadedIngredients.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(item ->
                        new SyncedEntry(entry.getKey(), BuiltInRegistries.ITEM.getKey(item))))
                .toList();
    }

    public static void applySynced(List<SyncedEntry> entries) {
        Map<String, List<Item>> built = new HashMap<>();
        entries.forEach(entry -> BuiltInRegistries.ITEM.getOptional(entry.item()).ifPresent(item ->
                built.computeIfAbsent(entry.suffix(), ignored -> new java.util.ArrayList<>()).add(item)));
        built.replaceAll((suffix, items) -> List.copyOf(new LinkedHashSet<>(items)));
        activeIngredients = Map.copyOf(built);
    }

    public static void clearSynced() {
        activeIngredients = Map.of();
    }

    @Nullable
    public static Item ingredientFor(String suffix) {
        List<Item> items = ingredientsFor(suffix);
        return items.isEmpty() ? null : items.getFirst();
    }

    public record SyncedEntry(String suffix, ResourceLocation item) {}
}
