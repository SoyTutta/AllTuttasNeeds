package com.alltuttasneeds.delights;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class DelightsTextUtils {
    public DelightsTextUtils() {
    }
    public static MutableComponent getTranslation(String key, Object... args) {
        return Component.translatable("tuttasdelights." + key, args);
    }

    public static MutableComponent getTextWithType(String translationType, String translationKey, Object... args) {
        return Component.translatable(translationType + ".tuttasdelights." + translationKey, args);
    }

    public static MutableComponent block(String key, Object... args) {
        return getTextWithType("block", key, args);
    }

    public static MutableComponent item(String key, Object... args) {
        return getTextWithType("item", key, args);
    }

    public static MutableComponent tooltip(String key, Object... args) {
        return getTextWithType("tooltip", key, args);
    }
}
