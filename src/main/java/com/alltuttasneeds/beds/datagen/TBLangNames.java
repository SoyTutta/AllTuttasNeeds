package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.BedColor;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.MattressMaterial;
import com.alltuttasneeds.beds.block.BedFrameBlock;
import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TieredBedBlock;
import com.alltuttasneeds.beds.item.BedBlanketItem.BlanketKind;
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
                case NORMAL -> colorEn(bed.bedColor()) + " " + material + " Bed" + normalSuffixEn(bed.blanketMaterial());
                case DELUXE -> colorEn(bed.bedColor()) + " " + material + " Deluxe Bed";
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
                case NORMAL -> "Cama " + material + " " + colorEs(bed.bedColor(), true) + normalSuffixEs(bed.blanketMaterial());
                case DELUXE -> "Cama " + material + " " + colorEs(bed.bedColor(), true) + " delux";
                default -> "Cama " + material;
            };
        }

        return block.getDescriptionId();
    }

    static String english(BlanketKind kind, DyeColor color) {
        String material = switch (kind) {
            case WOOL -> "Wool Blanket";
            case LEATHER -> "Leather Blanket";
            case DELUXE_WOOL -> "Deluxe Wool Blanket";
        };
        return colorEn(color) + " " + material;
    }

    static String spanish(BlanketKind kind, DyeColor color) {
        String material = switch (kind) {
            case WOOL -> "Manta de lana";
            case LEATHER -> "Manta de cuero";
            case DELUXE_WOOL -> "Manta de lana deluxe";
        };
        return material + " " + colorEs(color, true);
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

    private static String colorEn(BedColor color) {
        if (color == null) return "";
        return color.id().equals("bleached") ? "Bleached" : colorEn(color.vanillaColor());
    }

    private static String colorEs(DyeColor color, boolean feminine) {
        if (color == null) return "";
        return switch (color.getSerializedName()) {
            case "white" -> feminine ? "blanca" : "blanco";
            case "orange" -> "naranja";
            case "magenta" -> "magenta";
            case "light_blue" -> "azul claro";
            case "yellow" -> feminine ? "amarilla" : "amarillo";
            case "lime" -> "lima";
            case "pink" -> "rosa";
            case "gray" -> "gris";
            case "light_gray" -> "gris claro";
            case "cyan" -> "cian";
            case "purple" -> feminine ? "morada" : "morado";
            case "blue" -> "azul";
            case "brown" -> "marrón";
            case "green" -> "verde";
            case "red" -> feminine ? "roja" : "rojo";
            case "black" -> feminine ? "negra" : "negro";
            default -> words(color.getSerializedName());
        };
    }

    private static String colorEs(BedColor color, boolean feminine) {
        if (color == null) return "";
        return color.id().equals("bleached")
                ? (feminine ? "blanqueada" : "blanqueado")
                : colorEs(color.vanillaColor(), feminine);
    }

    private static String words(String text) {
        return text.replace('_', ' ').toLowerCase(Locale.ROOT);
    }

    private static String capitalize(String text) {
        if (text.isEmpty()) return text;
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
