package com.alltuttasneeds.core.data;

import com.alltuttasneeds.blocks.PetDoorBlock;
import com.alltuttasneeds.registry.TDContent;
import com.alltuttasneeds.registry.compat.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.core.HolderLookup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class BlockLootTables extends BlockLootSubProvider {
    public BlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        for (Block door : getKnownBlocks()) {
            if (door instanceof PetDoorBlock) {
                dropSelf(door);
            } else {
                add(door, this::createDoorTable);
            }
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> doors = new ArrayList<>();

        for (Supplier<? extends Block> sup : TDContent.DOORS.values()) {
            doors.add(sup.get());
        }

        if (Mods.BLOCKBOX.isLoaded()) {
            for (Supplier<? extends Block> sup : BBContent.DOORS.values()) {
                doors.add(sup.get());
            }
        }
        if (Mods.NOMANSLAND.isLoaded()) {
            for (Supplier<? extends Block> sup : NMLContent.DOORS.values()) {
                doors.add(sup.get());
            }
        }
        if (Mods.NEWWORLD.isLoaded()) {
            for (Supplier<? extends Block> sup : NWContent.DOORS.values()) {
                doors.add(sup.get());
            }
        }
        if (Mods.MYNETHERSDELIGHT.isLoaded()) {
            for (Supplier<? extends Block> sup : MNDContent.DOORS.values()) {
                doors.add(sup.get());
            }
        }
        if (Mods.ENDERSCAPE.isLoaded()) {
            for (Supplier<? extends Block> sup : ESContent.DOORS.values()) {
                doors.add(sup.get());
            }
        }
        if (Mods.ARTS_AND_CRAFTS.isLoaded()) {
            for (Supplier<? extends Block> sup : ACContent.DOORS.values()) {
                doors.add(sup.get());
            }
        }
        if (Mods.BIOMESOPLENTY.isLoaded()) {
            for (Supplier<? extends Block> sup : BoPContent.DOORS.values()) {
                doors.add(sup.get());
            }
        }
        if (Mods.NATURES_SPIRIT.isLoaded()) {
            for (Supplier<? extends Block> sup : NSContent.DOORS.values()) {
                doors.add(sup.get());
            }
        }
        if (Mods.CREATE.isLoaded()) {
            for (Supplier<? extends Block> sup : CreateContent.DOORS.values()) {
                doors.add(sup.get());
            }
        }
        if (Mods.MALUM.isLoaded()) {
            for (Supplier<? extends Block> sup : MalumContent.DOORS.values()) {
                doors.add(sup.get());
            }
        }

        return doors;
    }
}