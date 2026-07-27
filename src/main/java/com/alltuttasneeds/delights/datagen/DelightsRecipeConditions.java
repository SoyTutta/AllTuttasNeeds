package com.alltuttasneeds.delights.datagen;

import com.alltuttasneeds.core.condition.DelightGroupEnabledCondition;
import com.alltuttasneeds.delights.config.DelightGroup;
import net.minecraft.data.recipes.RecipeOutput;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.Arrays;

public final class DelightsRecipeConditions {
    private DelightsRecipeConditions() {}

    public static RecipeOutput groups(RecipeOutput output, DelightGroup... groups) {
        ICondition[] conditions = Arrays.stream(groups)
                .distinct()
                .map(DelightGroupEnabledCondition::new)
                .toArray(ICondition[]::new);
        return output.withConditions(conditions);
    }
}
