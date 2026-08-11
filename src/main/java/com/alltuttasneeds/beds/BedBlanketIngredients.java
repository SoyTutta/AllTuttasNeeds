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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BedBlanketIngredients extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(BedBlanketIngredients.class);

    private static volatile Map<String, Map<DyeColor, Item>> loadedIngredients = Map.of();
    private static volatile Map<String, RecipeItems> loadedRecipeItems = Map.of();
    private static volatile Map<String, Map<DyeColor, Item>> activeIngredients = Map.of();
    private static volatile Map<String, RecipeItems> activeRecipeItems = Map.of();

    public BedBlanketIngredients() {
        super(new Gson(), "bed_blankets");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<String, Map<DyeColor, Item>> built = new HashMap<>();
        Map<String, RecipeItems> builtRecipeItems = new HashMap<>();
        Set<String> duplicateSuffixes = duplicateSuffixes(resources);
        resources.forEach((id, json) -> {
            if (duplicateSuffixes.contains(id.getPath())) return;
            try {
                JsonObject root = json.getAsJsonObject();
                built.put(id.getPath(), readItems(
                        id, GsonHelper.getAsJsonObject(root, "colors", new JsonObject()), "ingredient"));

                JsonObject recipeRoot = GsonHelper.getAsJsonObject(root, "recipe_items", new JsonObject());
                Map<DyeColor, Item> normal = readItems(
                        id, GsonHelper.getAsJsonObject(recipeRoot, "normal", new JsonObject()), "normal item");
                Map<DyeColor, Item> deluxe = readItems(
                        id, GsonHelper.getAsJsonObject(recipeRoot, "deluxe", new JsonObject()), "deluxe item");
                if (!normal.isEmpty() || !deluxe.isEmpty()) {
                    builtRecipeItems.put(id.getPath(), new RecipeItems(normal, deluxe));
                }
            } catch (Exception e) {
                LOGGER.error("Failed to parse bed blanket ingredient {}", id, e);
            }
        });
        loadedIngredients = Map.copyOf(built);
        loadedRecipeItems = Map.copyOf(builtRecipeItems);
        activeIngredients = loadedIngredients;
        activeRecipeItems = loadedRecipeItems;
    }

    private static Set<String> duplicateSuffixes(Map<ResourceLocation, JsonElement> resources) {
        Map<String, ResourceLocation> sources = new HashMap<>();
        Set<String> duplicates = new HashSet<>();
        resources.keySet().forEach(id -> {
            ResourceLocation previous = sources.putIfAbsent(id.getPath(), id);
            if (previous != null) {
                duplicates.add(id.getPath());
                LOGGER.error("Duplicate bed blanket suffix {} in {} and {}; ignoring this suffix", id.getPath(), previous, id);
            }
        });
        return duplicates;
    }

    private static Map<DyeColor, Item> readItems(ResourceLocation source, JsonObject colors, String purpose) {
        Map<DyeColor, Item> byColor = new EnumMap<>(DyeColor.class);
        for (Map.Entry<String, JsonElement> entry : colors.entrySet()) {
            DyeColor color = DyeColor.byName(entry.getKey(), null);
            if (color == null) {
                LOGGER.warn("Bed blanket {} {} has unknown colour {}", purpose, source, entry.getKey());
                continue;
            }
            ResourceLocation itemId = ResourceLocation.parse(entry.getValue().getAsString());
            Item item = BuiltInRegistries.ITEM.getOptional(itemId).orElse(null);
            if (item == null) {
                LOGGER.warn("Bed blanket {} {} references unknown item {}", purpose, source, itemId);
                continue;
            }
            byColor.put(color, item);
        }
        return Map.copyOf(byColor);
    }

    @Nullable
    public static Item itemFor(String suffix, DyeColor color) {
        Map<DyeColor, Item> byColor = activeIngredients.get(suffix);
        return byColor == null ? null : byColor.get(color);
    }

    @Nullable
    public static DyeColor colorFor(String suffix, ItemStack stack) {
        Map<DyeColor, Item> byColor = activeIngredients.get(suffix);
        if (byColor == null) return null;
        for (Map.Entry<DyeColor, Item> entry : byColor.entrySet()) {
            if (stack.is(entry.getValue())) return entry.getKey();
        }
        return null;
    }

    @Nullable
    public static Item recipeItemFor(String suffix, DyeColor color, BedTier tier) {
        RecipeItems items = activeRecipeItems.get(suffix);
        if (items == null) return null;
        return (tier == BedTier.DELUXE ? items.deluxe() : items.normal()).get(color);
    }

    @Nullable
    public static Item associatedItemFor(String suffix, DyeColor color, BedTier tier) {
        Item override = recipeItemFor(suffix, color, tier);
        if (override != null) return override;
        return tier == BedTier.NORMAL ? itemFor(suffix, color) : null;
    }

    public static List<SyncedIngredient> syncedIngredients() {
        return loadedIngredients.entrySet().stream()
                .flatMap(entry -> entry.getValue().entrySet().stream().map(colorEntry ->
                        new SyncedIngredient(entry.getKey(), colorEntry.getKey(),
                                BuiltInRegistries.ITEM.getKey(colorEntry.getValue()))))
                .toList();
    }

    public static List<SyncedRecipeItem> syncedRecipeItems() {
        return loadedRecipeItems.entrySet().stream().flatMap(entry -> {
            var normal = entry.getValue().normal().entrySet().stream().map(colorEntry ->
                    new SyncedRecipeItem(entry.getKey(), colorEntry.getKey(), BedTier.NORMAL,
                            BuiltInRegistries.ITEM.getKey(colorEntry.getValue())));
            var deluxe = entry.getValue().deluxe().entrySet().stream().map(colorEntry ->
                    new SyncedRecipeItem(entry.getKey(), colorEntry.getKey(), BedTier.DELUXE,
                            BuiltInRegistries.ITEM.getKey(colorEntry.getValue())));
            return java.util.stream.Stream.concat(normal, deluxe);
        }).toList();
    }

    public static void applySynced(List<SyncedIngredient> syncedIngredients,
                                   List<SyncedRecipeItem> syncedRecipeItems) {
        Map<String, Map<DyeColor, Item>> builtIngredients = new HashMap<>();
        syncedIngredients.forEach(entry -> BuiltInRegistries.ITEM.getOptional(entry.item()).ifPresent(item ->
                builtIngredients.computeIfAbsent(entry.suffix(), ignored -> new EnumMap<>(DyeColor.class))
                        .put(entry.color(), item)));

        Map<String, Map<DyeColor, Item>> normal = new HashMap<>();
        Map<String, Map<DyeColor, Item>> deluxe = new HashMap<>();
        syncedRecipeItems.forEach(entry -> BuiltInRegistries.ITEM.getOptional(entry.item()).ifPresent(item -> {
            Map<String, Map<DyeColor, Item>> target = entry.tier() == BedTier.DELUXE ? deluxe : normal;
            target.computeIfAbsent(entry.suffix(), ignored -> new EnumMap<>(DyeColor.class))
                    .put(entry.color(), item);
        }));

        Map<String, RecipeItems> builtRecipeItems = new HashMap<>();
        Set<String> suffixes = new HashSet<>(normal.keySet());
        suffixes.addAll(deluxe.keySet());
        suffixes.forEach(suffix -> builtRecipeItems.put(suffix, new RecipeItems(
                Map.copyOf(normal.getOrDefault(suffix, Map.of())),
                Map.copyOf(deluxe.getOrDefault(suffix, Map.of())))));

        builtIngredients.replaceAll((suffix, items) -> Map.copyOf(items));
        activeIngredients = Map.copyOf(builtIngredients);
        activeRecipeItems = Map.copyOf(builtRecipeItems);
    }

    public static void clearSynced() {
        activeIngredients = Map.of();
        activeRecipeItems = Map.of();
    }

    public record SyncedIngredient(String suffix, DyeColor color, ResourceLocation item) {}

    public record SyncedRecipeItem(String suffix, DyeColor color, BedTier tier, ResourceLocation item) {}

    private record RecipeItems(Map<DyeColor, Item> normal, Map<DyeColor, Item> deluxe) {}
}
