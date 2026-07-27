package com.alltuttasneeds.delights.potluck;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryOps;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record PotluckRecipe(ResourceLocation id, PotluckSize size, int initialServings, int cookTime,
                            double refillMultiplier, List<Requirement> requirements, ItemStack result,
                            Ingredient servingContainer, Ingredient returnableDishes,
                            List<SpecialServing> specialServings, Appearance appearance) {
    public PotluckRecipe {
        requirements = List.copyOf(requirements);
        result = result.copy();
        specialServings = List.copyOf(specialServings);
    }

    public boolean fits(int capacity) {
        return size == PotluckSize.ANY || size.capacity == capacity;
    }

    public int initialRegularServings() {
        int specials = specialServings.stream().mapToInt(SpecialServing::initialCount).sum();
        return Math.max(0, initialServings - specials);
    }

    public static PotluckRecipe fromJson(ResourceLocation id, JsonObject json, HolderLookup.Provider registries) {
        PotluckSize size = PotluckSize.byName(GsonHelper.getAsString(json, "size", "any"));
        int initialServings = GsonHelper.getAsInt(json, "initial_servings", size.capacity > 0 ? size.capacity : 4);
        if (initialServings < 1 || initialServings > 64) {
            throw new IllegalArgumentException("initial_servings must be between 1 and 64");
        }
        int cookTime = Math.max(1, GsonHelper.getAsInt(json, "cook_time", 200));
        double refillMultiplier = GsonHelper.getAsDouble(json, "refill_multiplier", 0.5D);
        if (!(refillMultiplier > 0.0D)) throw new IllegalArgumentException("refill_multiplier must be positive");

        JsonArray requirementArray = GsonHelper.getAsJsonArray(json, "requirements");
        if (requirementArray.isEmpty() || requirementArray.size() > 6) {
            throw new IllegalArgumentException("requirements must contain between 1 and 6 entries");
        }
        List<Requirement> requirements = new ArrayList<>();
        for (JsonElement element : requirementArray) {
            JsonObject requirementJson = element.getAsJsonObject();
            String key = GsonHelper.getAsString(requirementJson, "key");
            double requiredWeight = GsonHelper.getAsDouble(requirementJson, "weight");
            if (!(requiredWeight > 0.0D)) throw new IllegalArgumentException("Requirement weight must be positive: " + key);
            JsonArray inputsJson = GsonHelper.getAsJsonArray(requirementJson, "inputs");
            List<WeightedIngredient> inputs = new ArrayList<>();
            for (JsonElement inputElement : inputsJson) {
                JsonObject inputJson = inputElement.getAsJsonObject();
                double weight = GsonHelper.getAsDouble(inputJson, "weight", 1.0D);
                if (!(weight > 0.0D)) throw new IllegalArgumentException("Input weight must be positive: " + key);
                inputs.add(new WeightedIngredient(parseIngredient(inputJson), weight));
            }
            if (inputs.isEmpty()) throw new IllegalArgumentException("Requirement has no inputs: " + key);
            requirements.add(new Requirement(key, requiredWeight, inputs));
        }

        ItemStack result = parseStack(GsonHelper.getAsJsonObject(json, "result"));
        Ingredient servingContainer = json.has("serving_container")
                ? parseIngredient(json.getAsJsonObject("serving_container"))
                : Ingredient.of(net.minecraft.world.item.Items.BOWL);
        Ingredient returnableDishes = json.has("returnable_dishes")
                ? parseIngredientList(json.getAsJsonArray("returnable_dishes"))
                : Ingredient.of(result.getItem());

        List<SpecialServing> specialServings = new ArrayList<>();
        JsonArray specialsJson = json.has("special_servings") ? json.getAsJsonArray("special_servings") : new JsonArray();
        for (JsonElement element : specialsJson) {
            JsonObject specialJson = element.getAsJsonObject();
            String key = GsonHelper.getAsString(specialJson, "key");
            ItemStack specialResult = parseStack(GsonHelper.getAsJsonObject(specialJson, "result"));
            ServingOrder order = ServingOrder.byName(GsonHelper.getAsString(specialJson, "order", "last"));
            int initialCount = Math.max(0, GsonHelper.getAsInt(specialJson, "initial_count", 0));
            int maxCount = Math.max(initialCount, GsonHelper.getAsInt(specialJson, "max_count", initialCount));
            boolean repeatable = GsonHelper.getAsBoolean(specialJson, "repeatable", false);
            Ingredient trigger = specialJson.has("trigger")
                    ? parseIngredient(specialJson.getAsJsonObject("trigger")) : Ingredient.EMPTY;
            specialServings.add(new SpecialServing(key, specialResult, order, initialCount, maxCount, repeatable, trigger));
        }

        JsonObject appearanceJson = json.has("appearance") ? json.getAsJsonObject("appearance") : new JsonObject();
        ResourceLocation texture = ResourceLocation.parse(GsonHelper.getAsString(appearanceJson, "texture",
                "tuttasdelights:block/potluck_soup_block"));
        int color = parseColor(GsonHelper.getAsString(appearanceJson, "color", "#FFFFFFFF"));
        List<ParticleSetting> particles = new ArrayList<>();
        JsonArray particlesJson = appearanceJson.has("particles")
                ? appearanceJson.getAsJsonArray("particles") : new JsonArray();
        for (JsonElement element : particlesJson) {
            JsonObject particleJson = element.getAsJsonObject();
            ResourceLocation type = ResourceLocation.parse(GsonHelper.getAsString(particleJson, "type"));
            JsonObject optionsJson = particleJson.has("options")
                    ? particleJson.getAsJsonObject("options").deepCopy() : new JsonObject();
            optionsJson.addProperty("type", type.toString());
            ParticleOptions options = ParticleTypes.CODEC
                    .parse(RegistryOps.create(JsonOps.INSTANCE, registries), optionsJson)
                    .getOrThrow(message -> new IllegalArgumentException("Invalid particle options: " + message));
            int count = Math.max(1, Math.min(8, GsonHelper.getAsInt(particleJson, "count", 1)));
            float chance = Math.max(0.0F, Math.min(1.0F, GsonHelper.getAsFloat(particleJson, "chance", 0.1F)));
            Vec3 spread = parseVec3(particleJson, "spread", new Vec3(0.35D, 0.05D, 0.35D));
            Vec3 velocity = parseVec3(particleJson, "velocity", new Vec3(0.0D, 0.02D, 0.0D));
            ParticleState state = ParticleState.byName(GsonHelper.getAsString(particleJson, "state", "heated"));
            particles.add(new ParticleSetting(options, count, chance, spread, velocity, state));
        }
        return new PotluckRecipe(id, size, initialServings, cookTime, refillMultiplier, requirements, result,
                servingContainer, returnableDishes, specialServings,
                new Appearance(texture, color, particles));
    }

    private static Ingredient parseIngredientList(JsonArray array) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (JsonElement element : array) ingredients.add(parseIngredient(element.getAsJsonObject()));
        return Ingredient.of(ingredients.stream().flatMap(ingredient -> java.util.Arrays.stream(ingredient.getItems())));
    }

    private static Ingredient parseIngredient(JsonObject json) {
        if (json.has("item")) {
            Item item = getItem(ResourceLocation.parse(GsonHelper.getAsString(json, "item")));
            return Ingredient.of(item);
        }
        if (json.has("tag")) {
            return Ingredient.of(ItemTags.create(ResourceLocation.parse(GsonHelper.getAsString(json, "tag"))));
        }
        throw new IllegalArgumentException("Ingredient needs item or tag");
    }

    private static ItemStack parseStack(JsonObject json) {
        Item item = getItem(ResourceLocation.parse(GsonHelper.getAsString(json, "item")));
        int count = Math.max(1, GsonHelper.getAsInt(json, "count", 1));
        return new ItemStack(item, count);
    }

    private static Item getItem(ResourceLocation id) {
        return BuiltInRegistries.ITEM.getOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("Unknown item " + id));
    }

    private static int parseColor(String value) {
        String normalized = value.startsWith("#") ? value.substring(1) : value;
        long parsed = Long.parseUnsignedLong(normalized, 16);
        if (normalized.length() <= 6) parsed |= 0xFF000000L;
        return (int) parsed;
    }

    private static Vec3 parseVec3(JsonObject parent, String name, Vec3 fallback) {
        if (!parent.has(name)) return fallback;
        JsonObject json = parent.getAsJsonObject(name);
        return new Vec3(GsonHelper.getAsDouble(json, "x", fallback.x()),
                GsonHelper.getAsDouble(json, "y", fallback.y()),
                GsonHelper.getAsDouble(json, "z", fallback.z()));
    }

    public enum PotluckSize {
        ANY("any", -1), NORMAL("normal", 4), ELDER("elder", 12);

        private final String name;
        private final int capacity;

        PotluckSize(String name, int capacity) {
            this.name = name;
            this.capacity = capacity;
        }

        private static PotluckSize byName(String name) {
            for (PotluckSize value : values()) if (value.name.equals(name)) return value;
            throw new IllegalArgumentException("Unknown Potluck size " + name);
        }
    }

    public enum ServingOrder {
        FIRST, LAST;

        private static ServingOrder byName(String name) {
            return switch (name) {
                case "first" -> FIRST;
                case "last" -> LAST;
                default -> throw new IllegalArgumentException("Unknown serving order " + name);
            };
        }
    }

    public enum ParticleState {
        ALWAYS, READY, RAW, COOKING, HEATED;

        private static ParticleState byName(String name) {
            try {
                return valueOf(name.toUpperCase(java.util.Locale.ROOT));
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException("Unknown particle state " + name);
            }
        }
    }

    public record WeightedIngredient(Ingredient ingredient, double weight) {
        public boolean matches(ItemStack stack) {
            return ingredient.test(stack);
        }
    }

    public record Requirement(String key, double requiredWeight, List<WeightedIngredient> inputs) {
        public Requirement {
            inputs = List.copyOf(inputs);
        }

        public double weightFor(ItemStack stack) {
            double weight = 0.0D;
            for (WeightedIngredient input : inputs) {
                if (input.matches(stack)) weight = Math.max(weight, input.weight());
            }
            return weight;
        }
    }

    public record SpecialServing(String key, ItemStack result, ServingOrder order, int initialCount,
                                 int maxCount, boolean repeatable, Ingredient trigger) {
        public SpecialServing {
            result = result.copy();
        }
    }

    public record Appearance(ResourceLocation texture, int color, List<ParticleSetting> particles) {
        public Appearance {
            particles = List.copyOf(particles);
        }
    }

    public record ParticleSetting(ParticleOptions options, int count, float chance, Vec3 spread,
                                  Vec3 velocity, ParticleState state) {}

    public record Vec3(double x, double y, double z) {}
}
