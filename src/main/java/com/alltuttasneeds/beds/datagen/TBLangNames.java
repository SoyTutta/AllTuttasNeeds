package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.MattressMaterial;
import com.alltuttasneeds.beds.block.BedFrameBlock;
import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TieredBedBlock;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.Locale;

final class TBLangNames {
    private TBLangNames() {}

    static String english(Block block) {
        if (block instanceof BedFrameBlock) return "Bed Frame";

        if (block instanceof LooseMattressBlock mattress) {
            String base = materialEn(mattress.material()) + " Mattress";
            return mattress.cover() == null ? base : base + " with " + coverEn(mattress.cover()) + " Cover";
        }

        if (block instanceof TieredBedBlock bed) {
            String material = materialEn(bed.mattress());
            return switch (bed.tier()) {
                case LOW -> bed.basicCover() == null
                        ? material + " Bed"
                        : material + " Bed with " + coverEn(bed.basicCover()) + " Cover";
                case NORMAL -> colorEn(bed.color()) + " " + material + " Bed" + normalSuffixEn(bed.blanketMaterial());
                case DELUXE -> colorEn(bed.color()) + " " + material + " Deluxe Bed";
                default -> material + " Bed";
            };
        }

        return block.getDescriptionId();
    }

    static String spanish(Block block) {
        if (block instanceof BedFrameBlock) return "Marco de cama";

        if (block instanceof LooseMattressBlock mattress) {
            String base = "Colchón " + materialEs(mattress.material());
            return mattress.cover() == null ? base : base + " con cubierta de " + coverEs(mattress.cover());
        }

        if (block instanceof TieredBedBlock bed) {
            String material = materialEs(bed.mattress());
            return switch (bed.tier()) {
                case LOW -> bed.basicCover() == null
                        ? "Cama " + material
                        : "Cama " + material + " con cubierta de " + coverEs(bed.basicCover());
                case NORMAL -> "Cama " + material + " " + colorEs(bed.color(), true) + normalSuffixEs(bed.blanketMaterial());
                case DELUXE -> "Cama " + material + " " + colorEs(bed.color(), true) + " delux";
                default -> "Cama " + material;
            };
        }

        return block.getDescriptionId();
    }

    private static String normalSuffixEn(BlanketMaterial blanket) {
        return blanket != null && !blanket.supportsDeluxe() ? " (Leather)" : "";
    }

    private static String normalSuffixEs(BlanketMaterial blanket) {
        return blanket != null && !blanket.supportsDeluxe() ? " (cuero)" : "";
    }

    private static String materialEn(MattressMaterial material) {
        return switch (material.id()) {
            case "wheat" -> "Wheat";
            case "straw" -> "Straw";
            case "soft" -> "Soft";
            case "canvas" -> "Canvas";
            default -> capitalize(material.id());
        };
    }

    private static String materialEs(MattressMaterial material) {
        return switch (material.id()) {
            case "wheat" -> "de trigo";
            case "straw" -> "de paja";
            case "soft" -> "suave";
            case "canvas" -> "de lona";
            default -> words(material.id());
        };
    }

    private static String coverEn(CoverMaterial cover) {
        if (cover.suffix().startsWith("wheat")) return "Wheat";
        if (cover.suffix().startsWith("leather")) return "Leather";
        if (cover.suffix().startsWith("canvas")) return "Canvas";
        return capitalize(cover.suffix().replace("_cover", ""));
    }

    private static String coverEs(CoverMaterial cover) {
        if (cover.suffix().startsWith("wheat")) return "trigo";
        if (cover.suffix().startsWith("leather")) return "cuero";
        if (cover.suffix().startsWith("canvas")) return "lona";
        return words(cover.suffix().replace("_cover", ""));
    }

    private static String colorEn(DyeColor color) {
        if (color == null) return "";
        String name = color.getSerializedName().replace('_', ' ');
        return capitalize(name);
    }

    private static String colorEs(DyeColor color, boolean feminine) {
        if (color == null) return "";
        return switch (color) {
            case WHITE -> feminine ? "blanca" : "blanco";
            case ORANGE -> "naranja";
            case MAGENTA -> "magenta";
            case LIGHT_BLUE -> "azul claro";
            case YELLOW -> feminine ? "amarilla" : "amarillo";
            case LIME -> "lima";
            case PINK -> "rosa";
            case GRAY -> "gris";
            case LIGHT_GRAY -> "gris claro";
            case CYAN -> "cian";
            case PURPLE -> feminine ? "morada" : "morado";
            case BLUE -> "azul";
            case BROWN -> "marrón";
            case GREEN -> "verde";
            case RED -> feminine ? "roja" : "rojo";
            case BLACK -> feminine ? "negra" : "negro";
        };
    }

    private static String words(String text) {
        return text.replace('_', ' ').toLowerCase(Locale.ROOT);
    }

    private static String capitalize(String text) {
        if (text.isEmpty()) return text;
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
