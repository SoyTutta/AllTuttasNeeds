package com.alltuttasneeds.beds;

import com.alltuttasneeds.core.Mods;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.List;

public record BedColor(String id, DyeColor vanillaColor, ResourceLocation woolItem,
                       @Nullable ResourceLocation dyeItem, @Nullable Mods requiredMod) {
    private static final List<DyeColor> VANILLA_DYE_COLORS = List.of(
            DyeColor.WHITE,
            DyeColor.ORANGE,
            DyeColor.MAGENTA,
            DyeColor.LIGHT_BLUE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.PINK,
            DyeColor.GRAY,
            DyeColor.LIGHT_GRAY,
            DyeColor.CYAN,
            DyeColor.PURPLE,
            DyeColor.BLUE,
            DyeColor.BROWN,
            DyeColor.GREEN,
            DyeColor.RED,
            DyeColor.BLACK);
    private static final List<BedColor> VANILLA = VANILLA_DYE_COLORS.stream()
            .map(BedColor::vanilla)
            .toList();

    public static BedColor vanilla(DyeColor color) {
        if (!VANILLA_DYE_COLORS.contains(color)) {
            throw new IllegalArgumentException("Not a vanilla dye color: " + color.getSerializedName());
        }
        Item dye = DyeItem.byColor(color);
        return new BedColor(
                color.getSerializedName(),
                color,
                BuiltInRegistries.ITEM.getKey(WoolColors.woolItem(color)),
                dye == null ? null : BuiltInRegistries.ITEM.getKey(dye),
                null);
    }

    public static List<BedColor> vanillaColors() {
        return VANILLA;
    }

    public static List<DyeColor> vanillaDyeColors() {
        return VANILLA_DYE_COLORS;
    }

    @Nullable
    public static BedColor fromVanilla(DyeColor color) {
        return VANILLA.stream().filter(candidate -> candidate.vanillaColor() == color).findFirst().orElse(null);
    }

    public Item woolIngredient() {
        return BuiltInRegistries.ITEM.get(woolItem);
    }

    @Nullable
    public Item dyeIngredient() {
        return dyeItem == null ? null : BuiltInRegistries.ITEM.getOptional(dyeItem).orElse(null);
    }

    public boolean isAvailable() {
        return requiredMod == null || requiredMod.isLoaded();
    }
}
