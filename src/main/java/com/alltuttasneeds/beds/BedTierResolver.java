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
        if (!TBConfig.isModuleEnabled()) return null;
        if (!(block instanceof BedBlock)) return null;

        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        BedTier tier = configuredTier(id);
        if (tier == null) tier = automaticTier(block);
        return isExcluded(id, tier) ? null : tier;
    }

    private static BedTier automaticTier(Block block) {
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
    private static BedTier configuredTier(ResourceLocation id) {
        if (matches(id, TBConfig.deluxeTierGameplay.blockOverrides.get()) && !isExcluded(id, BedTier.DELUXE)) {
            return BedTier.DELUXE;
        }
        if (matches(id, TBConfig.normalTierGameplay.blockOverrides.get()) && !isExcluded(id, BedTier.NORMAL)) {
            return BedTier.NORMAL;
        }
        if (matches(id, TBConfig.lowTierGameplay.blockOverrides.get()) && !isExcluded(id, BedTier.LOW)) {
            return BedTier.LOW;
        }
        if (matches(id, TBConfig.basicTierGameplay.blockOverrides.get()) && !isExcluded(id, BedTier.BASIC)) {
            return BedTier.BASIC;
        }
        return null;
    }

    private static boolean isExcluded(ResourceLocation id, BedTier tier) {
        Iterable<? extends String> exclusions = switch (tier) {
            case BASIC -> TBConfig.basicTierGameplay.blockExclusions.get();
            case LOW -> TBConfig.lowTierGameplay.blockExclusions.get();
            case NORMAL -> TBConfig.normalTierGameplay.blockExclusions.get();
            case DELUXE -> TBConfig.deluxeTierGameplay.blockExclusions.get();
        };
        return matches(id, exclusions);
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
