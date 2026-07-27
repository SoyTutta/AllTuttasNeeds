package com.alltuttasneeds.delights.potluck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class PotluckMobIngredientRegistry extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();
    public static final PotluckMobIngredientRegistry INSTANCE = new PotluckMobIngredientRegistry();

    private static volatile List<MobIngredient> ingredients = List.of();

    private PotluckMobIngredientRegistry() {
        super(GSON, "potluck_mob_ingredients");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager,
                         ProfilerFiller profiler) {
        List<MobIngredient> loaded = new ArrayList<>();
        resources.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(ResourceLocation::toString)))
                .forEach(entry -> {
                    try {
                        JsonObject json = entry.getValue().getAsJsonObject();
                        String requirement = GsonHelper.getAsString(json, "requirement");
                        double weight = GsonHelper.getAsDouble(json, "weight");
                        if (!(weight > 0.0D)) throw new IllegalArgumentException("weight must be positive");
                        ResourceLocation entity = json.has("entity")
                                ? ResourceLocation.parse(GsonHelper.getAsString(json, "entity")) : null;
                        TagKey<EntityType<?>> tag = json.has("entity_tag")
                                ? TagKey.create(Registries.ENTITY_TYPE,
                                ResourceLocation.parse(GsonHelper.getAsString(json, "entity_tag"))) : null;
                        if ((entity == null) == (tag == null)) {
                            throw new IllegalArgumentException("Exactly one of entity or entity_tag is required");
                        }
                        loaded.add(new MobIngredient(requirement, weight, entity, tag));
                    } catch (RuntimeException exception) {
                        LOGGER.error("Could not load Potluck mob ingredient {}", entry.getKey(), exception);
                    }
                });
        ingredients = List.copyOf(loaded);
        LOGGER.info("Loaded {} Potluck mob ingredients", ingredients.size());
    }

    @Nullable
    public static Contribution getContribution(LivingEntity entity) {
        for (MobIngredient ingredient : ingredients) {
            if (ingredient.matches(entity)) return new Contribution(ingredient.requirement(), ingredient.weight());
        }
        return null;
    }

    private record MobIngredient(String requirement, double weight, @Nullable ResourceLocation entity,
                                 @Nullable TagKey<EntityType<?>> tag) {
        private boolean matches(LivingEntity living) {
            ResourceLocation entityId = EntityType.getKey(living.getType());
            return entity != null ? entity.equals(entityId) : living.getType().is(tag);
        }
    }

    public record Contribution(String requirement, double weight) {}
}
