package com.alltuttasneeds.core.condition;

import com.alltuttasneeds.delights.config.DelightGroup;
import com.alltuttasneeds.delights.config.DelightsConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.Locale;

public record DelightGroupEnabledCondition(String group) implements ICondition {
    public static final MapCodec<DelightGroupEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("group").forGetter(DelightGroupEnabledCondition::group)
    ).apply(instance, DelightGroupEnabledCondition::new));

    public DelightGroupEnabledCondition(DelightGroup group) {
        this(group.name().toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean test(IContext context) {
        try {
            return DelightsConfig.isGroupEnabled(DelightGroup.valueOf(group.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
