package com.alltuttasneeds;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ATNTextUtils {
    public ATNTextUtils() {
    }
    public static MutableComponent getTranslation(String key, Object... args) {
        return Component.translatable("alltuttasneeds." + key, args);
    }
}