package com.alltuttasneeds.beds;

import com.alltuttasneeds.beds.block.BedFrameBlock;
import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TieredBedBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;

public final class BedModelNaming {
    private BedModelNaming() {}

    public static String type(Block block) {
        if (block instanceof BedFrameBlock) return "frame";

        if (block instanceof LooseMattressBlock mattress) {
            String id = mattress.material().id() + "_mattress";
            return mattress.cover() == null ? id : id + "_" + mattress.cover().suffix();
        }

        TieredBedBlock bed = (TieredBedBlock) block;
        String id = bed.mattress().id() + "_bed";
        return switch (bed.tier()) {
            case LOW -> bed.basicCover() == null ? id : id + "_" + bed.basicCover().suffix();
            case NORMAL -> id + "_" + (bed.blanketMaterial() != null ? bed.blanketMaterial().suffix() : "wool_blanket");
            case DELUXE -> id + "_deluxe";
            case BASIC -> id;
        };
    }

    public static DyeColor color(Block block) {
        if (block instanceof BedFrameBlock || block instanceof LooseMattressBlock) return DyeColor.BROWN;
        if (block instanceof TieredBedBlock bed && bed.blanketMaterial() == null) return DyeColor.BROWN;
        return block instanceof BedBlock bed ? bed.getColor() : DyeColor.BROWN;
    }

    public static ResourceLocation blockTexture(Block block) {
        return ResourceLocation.fromNamespaceAndPath("tuttasbeds", "block/" + type(block) + "/" + color(block).getSerializedName());
    }
}
