package com.alltuttasneeds.beds;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public final class BedDecorationResolver {
    private BedDecorationResolver() {}

    @Nullable
    public static Match resolveUnique(MattressFamily family, ItemStack stack,
                                      boolean includeCovers, boolean directInteraction) {
        Match match = null;

        if (includeCovers) {
            for (var entry : family.bedBasicCovers().entrySet()) {
                CoverMaterial cover = entry.getKey();
                if ((directInteraction && !cover.isDirectApplyEnabled()) || !cover.matches(stack)) continue;
                if (match != null) return null;
                match = new Match(entry.getValue().get(), Kind.COVER);
            }
        }

        for (var blanketEntry : family.bedBlankets().entrySet()) {
            BlanketMaterial blanket = blanketEntry.getKey();
            for (var colorEntry : blanketEntry.getValue().entrySet()) {
                Item item = blanket.associatedItemFor(colorEntry.getKey(), BedTier.NORMAL);
                if (item == null || !stack.is(item)) continue;
                if (match != null) return null;
                match = new Match(colorEntry.getValue().get(), Kind.BLANKET);
            }
        }

        BlanketMaterial deluxeBlanket = family.bedBlankets().keySet().stream()
                .filter(BlanketMaterial::supportsDeluxe)
                .findFirst()
                .orElse(null);
        if (deluxeBlanket == null) return match;

        for (var entry : family.bedDeluxe().entrySet()) {
            Item item = deluxeBlanket.associatedItemFor(entry.getKey(), BedTier.DELUXE);
            if (item == null || !stack.is(item)) continue;
            if (match != null) return null;
            match = new Match(entry.getValue().get(), Kind.BLANKET);
        }
        return match;
    }

    public enum Kind {
        COVER,
        BLANKET
    }

    public record Match(Block result, Kind kind) {}
}
