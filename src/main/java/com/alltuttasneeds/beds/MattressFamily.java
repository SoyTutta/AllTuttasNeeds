package com.alltuttasneeds.beds;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.Supplier;

public record MattressFamily(
        MattressMaterial material,
        Supplier<Block> looseMattress,
        Map<CoverMaterial, Supplier<Block>> looseMattressCovers,
        Supplier<Block> bedBare,
        Map<CoverMaterial, Supplier<Block>> bedBasicCovers,
        Map<BlanketMaterial, Map<DyeColor, Supplier<Block>>> bedBlankets,
        Map<DyeColor, Supplier<Block>> bedDeluxe
) {
}
