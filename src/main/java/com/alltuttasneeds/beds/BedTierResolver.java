package com.alltuttasneeds.beds;

import com.alltuttasneeds.beds.block.BedFrameBlock;
import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TieredBedBlock;
import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public final class BedTierResolver {
    private BedTierResolver() {}

    @Nullable
    public static BedTier resolve(Block block) {
        if (!(block instanceof BedBlock)) return null;

        BedTier override = configuredTier(block);
        if (override != null) return override;

        if (block instanceof BedFrameBlock) return BedTier.BASIC;
        if (block instanceof LooseMattressBlock mattress) {
            return mattress.cover() == null ? BedTier.BASIC : BedTier.LOW;
        }
        if (block instanceof TieredBedBlock bed) {
            if (bed.tier() == BedTier.DELUXE) return BedTier.DELUXE;
            if (bed.blanketMaterial() != null) return BedTier.NORMAL;
            return bed.basicCover() == null ? BedTier.BASIC : BedTier.LOW;
        }
        return BedTier.BASIC;
    }

    @Nullable
    private static BedTier configuredTier(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        if (matches(id, TBConfig.deluxeTierGameplay.blockOverrides.get())) return BedTier.DELUXE;
        if (matches(id, TBConfig.normalTierGameplay.blockOverrides.get())) return BedTier.NORMAL;
        if (matches(id, TBConfig.lowTierGameplay.blockOverrides.get())) return BedTier.LOW;
        if (matches(id, TBConfig.basicTierGameplay.blockOverrides.get())) return BedTier.BASIC;
        return null;
    }

    private static boolean matches(ResourceLocation id, Iterable<? extends String> patterns) {
        String fullId = id.toString();
        String namespaceWildcard = id.getNamespace() + ":*";
        for (String pattern : patterns) {
            if (pattern.equals(fullId) || pattern.equals(namespaceWildcard)) return true;
        }
        return false;
    }
}
