package com.alltuttasneeds.beds;

import java.util.function.BooleanSupplier;

public record MattressMaterial(String id, BooleanSupplier enabled) {
    public boolean isEnabled() {
        return enabled.getAsBoolean();
    }
}
