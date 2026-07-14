package com.alltuttasneeds.core.condition;

import com.alltuttasneeds.beds.config.TBConfig;
import com.alltuttasneeds.doors.config.TDConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;

public record ModuleEnabledCondition(String module) implements ICondition {
    public static final MapCodec<ModuleEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("module").forGetter(ModuleEnabledCondition::module)
    ).apply(instance, ModuleEnabledCondition::new));

    public static final ModuleEnabledCondition DOORS = new ModuleEnabledCondition("doors");

    @Override
    public boolean test(IContext context) {
        return switch (module) {
            case "doors" -> TDConfig.isModuleEnabled();
            case "beds" -> TBConfig.isModuleEnabled();
            default -> false;
        };
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
