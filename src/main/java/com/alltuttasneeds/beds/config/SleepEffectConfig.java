package com.alltuttasneeds.beds.config;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SleepEffectConfig {
    public final ModConfigSpec.BooleanValue enabled;
    public final ModConfigSpec.ConfigValue<String> effectId;
    public final ModConfigSpec.IntValue durationSeconds;
    public final ModConfigSpec.ConfigValue<List<? extends String>> additionalEffects;

    private SleepEffectConfig(ModConfigSpec.BooleanValue enabled, ModConfigSpec.ConfigValue<String> effectId,
                              ModConfigSpec.IntValue durationSeconds,
                              ModConfigSpec.ConfigValue<List<? extends String>> additionalEffects) {
        this.enabled = enabled;
        this.effectId = effectId;
        this.durationSeconds = durationSeconds;
        this.additionalEffects = additionalEffects;
    }

    static SleepEffectConfig define(ModConfigSpec.Builder builder, String section, boolean defaultEnabled, String defaultEffectId, int defaultDurationSeconds) {
        builder.push(section);
        ModConfigSpec.BooleanValue enabled = builder
                .comment("Whether waking up from this applies any configured effects.")
                .worldRestart()
                .define("effectEnabled", defaultEnabled);
        ModConfigSpec.ConfigValue<String> effectId = builder
                .comment("Registry name of the primary effect, e.g. \"minecraft:regeneration\". Any effect, from any mod, works.")
                .worldRestart()
                .define("effectId", defaultEffectId);
        ModConfigSpec.IntValue durationSeconds = builder
                .comment("Duration of the primary effect, in seconds.")
                .worldRestart()
                .defineInRange("effectDurationSeconds", defaultDurationSeconds, 0, Integer.MAX_VALUE / 20);
        ModConfigSpec.ConfigValue<List<? extends String>> additionalEffects = builder
                .comment("Additional effects applied after waking, each with its own duration.",
                        "Use \"effect_id=duration_seconds\", for example \"minecraft:absorption=10\".",
                        "Effects from any loaded mod work. Invalid or unavailable entries are ignored.")
                .worldRestart()
                .defineList("additionalEffects", List.of(), value -> value instanceof String);
        builder.pop();
        return new SleepEffectConfig(enabled, effectId, durationSeconds, additionalEffects);
    }

    public Optional<Holder<MobEffect>> resolveEffect() {
        if (!enabled.get()) return Optional.empty();

        ResourceLocation id = ResourceLocation.tryParse(effectId.get());
        if (id == null) return Optional.empty();

        MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(id);
        return effect == null ? Optional.empty() : Optional.of(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect));
    }

    public int durationTicks() {
        return durationSeconds.get() * 20;
    }

    public List<ResolvedEffect> resolveEffects() {
        if (!enabled.get()) return List.of();

        List<ResolvedEffect> resolved = new ArrayList<>();
        resolveEffect().ifPresent(effect -> resolved.add(new ResolvedEffect(effect, durationTicks())));
        for (String entry : additionalEffects.get()) {
            resolveAdditionalEffect(entry).ifPresent(resolved::add);
        }
        return List.copyOf(resolved);
    }

    private static Optional<ResolvedEffect> resolveAdditionalEffect(String entry) {
        int separator = entry.lastIndexOf('=');
        if (separator <= 0 || separator == entry.length() - 1) return Optional.empty();

        ResourceLocation id = ResourceLocation.tryParse(entry.substring(0, separator).trim());
        if (id == null) return Optional.empty();

        int durationSeconds;
        try {
            durationSeconds = Integer.parseInt(entry.substring(separator + 1).trim());
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
        if (durationSeconds < 0 || durationSeconds > Integer.MAX_VALUE / 20) return Optional.empty();

        MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(id);
        if (effect == null) return Optional.empty();
        return Optional.of(new ResolvedEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect), durationSeconds * 20));
    }

    public record ResolvedEffect(Holder<MobEffect> effect, int durationTicks) {}
}
