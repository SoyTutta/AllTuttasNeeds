package com.alltuttasneeds.delights.face;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class SnowGolemFaceRegistry extends SimpleJsonResourceReloadListener {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();

    public static final SnowGolemFaceRegistry INSTANCE = new SnowGolemFaceRegistry();
    public static final ResourceLocation DEFAULT_FACE =
            ResourceLocation.fromNamespaceAndPath("tuttasdelights", "default");

    private static volatile Map<String, ResourceLocation> names = Map.of();
    private static volatile Set<ResourceLocation> knownFaces = Set.of(DEFAULT_FACE);
    private static volatile List<WeightedFace> randomFaces = List.of(new WeightedFace(DEFAULT_FACE, 1));
    private static volatile int totalWeight = 1;

    private SnowGolemFaceRegistry() {
        super(GSON, "snow_golem_feast_faces");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager,
                         ProfilerFiller profiler) {
        Map<String, ResourceLocation> loadedNames = new HashMap<>();
        Set<ResourceLocation> loadedFaces = new LinkedHashSet<>();
        Map<ResourceLocation, Integer> weights = new HashMap<>();

        resources.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(ResourceLocation::toString)))
                .forEach(entry -> loadRule(entry.getKey(), entry.getValue(), loadedNames, loadedFaces, weights));

        loadedFaces.add(DEFAULT_FACE);
        List<WeightedFace> loadedRandomFaces = new ArrayList<>();
        int loadedTotalWeight = 0;
        for (Map.Entry<ResourceLocation, Integer> entry : weights.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(ResourceLocation::toString)))
                .toList()) {
            int available = Integer.MAX_VALUE - loadedTotalWeight;
            int weight = Math.min(entry.getValue(), available);
            if (weight <= 0) continue;
            loadedRandomFaces.add(new WeightedFace(entry.getKey(), weight));
            loadedTotalWeight += weight;
            if (loadedTotalWeight == Integer.MAX_VALUE) break;
        }

        if (loadedRandomFaces.isEmpty()) {
            loadedRandomFaces = List.of(new WeightedFace(DEFAULT_FACE, 1));
            loadedTotalWeight = 1;
        }

        names = Map.copyOf(loadedNames);
        knownFaces = Set.copyOf(loadedFaces);
        randomFaces = List.copyOf(loadedRandomFaces);
        totalWeight = loadedTotalWeight;
        LOGGER.info("Loaded {} Snow Golem Feast face names and {} random appearances",
                names.size(), randomFaces.size());
    }

    private static void loadRule(ResourceLocation ruleId, JsonElement element,
                                 Map<String, ResourceLocation> loadedNames,
                                 Set<ResourceLocation> loadedFaces,
                                 Map<ResourceLocation, Integer> weights) {
        try {
            JsonObject json = element.getAsJsonObject();
            ResourceLocation appearance = ruleId;
            if (json.has("appearance")) {
                appearance = ResourceLocation.parse(json.get("appearance").getAsString());
            }

            int randomWeight = json.has("random_weight") ? json.get("random_weight").getAsInt() : 0;
            if (randomWeight < 0) {
                throw new IllegalArgumentException("random_weight cannot be negative");
            }

            loadedFaces.add(appearance);
            if (randomWeight > 0) {
                weights.merge(appearance, randomWeight, (left, right) -> {
                    long sum = (long) left + right;
                    return (int) Math.min(sum, Integer.MAX_VALUE);
                });
            }

            JsonArray ruleNames = json.has("names") ? json.getAsJsonArray("names") : new JsonArray();
            for (JsonElement nameElement : ruleNames) {
                String name = normalizeName(nameElement.getAsString());
                if (name.isEmpty()) continue;
                ResourceLocation previous = loadedNames.putIfAbsent(name, appearance);
                if (previous != null && !previous.equals(appearance)) {
                    LOGGER.warn("Snow Golem Feast name '{}' from {} already points to {}; keeping the first mapping",
                            nameElement.getAsString(), ruleId, previous);
                }
            }
        } catch (RuntimeException exception) {
            LOGGER.error("Could not load Snow Golem Feast face rule {}", ruleId, exception);
        }
    }

    @Nullable
    public static ResourceLocation getFaceForName(@Nullable String name) {
        if (name == null) return null;
        return names.get(normalizeName(name));
    }

    public static ResourceLocation getRandomFace(RandomSource random) {
        int target = random.nextInt(totalWeight);
        for (WeightedFace face : randomFaces) {
            target -= face.weight();
            if (target < 0) return face.id();
        }
        return DEFAULT_FACE;
    }

    public static boolean isKnown(ResourceLocation face) {
        return knownFaces.contains(face);
    }

    private static String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }

    private record WeightedFace(ResourceLocation id, int weight) {}
}
