package com.alltuttasneeds.delights.config;

public enum DelightGroup {
    SUCKLING_PIG("sucklingPig"),
    ANCIENT("ancient"),
    POTATO("potato"),
    CREEPER("creeper"),
    SLIME("slime"),
    FROZEN_TREATS("frozenTreats"),
    FROG("frog"),
    EXTRA_MEALS("extraMeals"),
    BREADED("breaded"),
    SANDWICH_PORTIONS("sandwichPortions"),
    UNDEAD("undead"),
    // Under development. Kept internal so it cannot be exposed through the startup config.
    GUARDIAN("guardian", false);

    private final String configKey;
    private final boolean configurable;

    DelightGroup(String configKey) {
        this(configKey, true);
    }

    DelightGroup(String configKey, boolean configurable) {
        this.configKey = configKey;
        this.configurable = configurable;
    }

    public String configKey() {
        return configKey;
    }

    public boolean configurable() {
        return configurable;
    }
}
