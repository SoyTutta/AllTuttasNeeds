package com.alltuttasneeds.beds.item;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

public final class BedBlanketItem extends Item {
    private final BlanketKind kind;
    private final DyeColor color;

    public BedBlanketItem(BlanketKind kind, DyeColor color, Properties properties) {
        super(properties);
        this.kind = kind;
        this.color = color;
    }

    public BlanketKind kind() {
        return kind;
    }

    public DyeColor color() {
        return color;
    }

    public enum BlanketKind {
        WOOL("wool_blanket", "wool_blanket", false),
        LEATHER("leather_blanket", "leather_blanket", false),
        DELUXE_WOOL("deluxe_wool_blanket", "wool_blanket", true);

        private final String id;
        private final String blanketSuffix;
        private final boolean deluxe;

        BlanketKind(String id, String blanketSuffix, boolean deluxe) {
            this.id = id;
            this.blanketSuffix = blanketSuffix;
            this.deluxe = deluxe;
        }

        public String id() {
            return id;
        }

        public String blanketSuffix() {
            return blanketSuffix;
        }

        public boolean isDeluxe() {
            return deluxe;
        }
    }
}
