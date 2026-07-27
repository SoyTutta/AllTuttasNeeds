package com.alltuttasneeds.core.condition;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.delights.config.DelightsConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;

public record DelightRecipeEnabledCondition(String option) implements ICondition {
    public static final MapCodec<DelightRecipeEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("option").forGetter(DelightRecipeEnabledCondition::option)
    ).apply(instance, DelightRecipeEnabledCondition::new));

    public static final DelightRecipeEnabledCondition CHEESE_WEDGES =
            new DelightRecipeEnabledCondition("cheese_wedges");

    @Override
    public boolean test(IContext context) {
        return switch (option) {
            case "cheese_wedges" -> DelightsConfig.useCheeseWedges() && Mods.BREWIN_AND_CHEWIN.isLoaded();
            default -> false;
        };
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
