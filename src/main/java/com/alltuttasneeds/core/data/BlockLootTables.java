package com.alltuttasneeds.core.data;

import com.alltuttasneeds.blocks.PetDoorBlock;
import com.alltuttasneeds.registry.compat.framework.CompatRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        CompatRegistry.loaded().forEach(compat ->
                compat.doors().values().forEach(supplier -> doors.add(supplier.get())));
        return doors;
    }
}