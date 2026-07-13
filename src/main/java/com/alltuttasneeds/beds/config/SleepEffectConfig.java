package com.alltuttasneeds.beds.config;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Optional;

public final class SleepEffectConfig {
    public final ModConfigSpec.BooleanValue enabled;
    public final ModConfigSpec.ConfigValue<String> effectId;
    public final ModConfigSpec.IntValue durationSeconds;

    private SleepEffectConfig(ModConfigSpec.BooleanValue enabled, ModConfigSpec.ConfigValue<String> effectId, ModConfigSpec.IntValue durationSeconds) {
        this.enabled = enabled;
        this.effectId = effectId;
        this.durationSeconds = durationSeconds;
    }

    static SleepEffectConfig define(ModConfigSpec.Builder builder, String section, boolean defaultEnabled, String defaultEffectId, int defaultDurationSeconds) {
        builder.push(section);
        ModConfigSpec.BooleanValue enabled = builder
                .comment("Whether waking up from this applies an effect at all.")
                .define("effectEnabled", defaultEnabled);
        ModConfigSpec.ConfigValue<String> effectId = builder
                .comment("Registry name of the effect to apply, e.g. \"minecraft:regeneration\". Any effect, from any mod, works.")
                .define("effectId", defaultEffectId);
        ModConfigSpec.IntValue durationSeconds = builder
                .comment("Duration of the effect, in seconds.")
                .defineInRange("effectDurationSeconds", defaultDurationSeconds, 0, Integer.MAX_VALUE);
        builder.pop();
        return new SleepEffectConfig(enabled, effectId, durationSeconds);
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
}
