package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.core.condition.DoorSetEnabledCondition;
import com.alltuttasneeds.core.condition.ModuleEnabledCondition;
import com.alltuttasneeds.doors.block.PetDoorBlock;
import com.alltuttasneeds.doors.block.TransitDoorBlock;
import com.alltuttasneeds.doors.compat.CompatRegistry;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.config.DoorSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockLootTables extends BlockLootSubProvider {
    private final Map<ResourceKey<LootTable>, List<ICondition>> conditions = new HashMap<>();

    public BlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        CompatRegistry.loaded().forEach(this::generate);
    }

    private void generate(ModCompat compat) {
        Set<String> secretDoors = new HashSet<>();
        compat.secretDoorFamilies().forEach(secret ->
                secretDoors.add(secret.woodName() + "_bookshelf_door"));
        Set<String> extraDoors = new HashSet<>();
        compat.extraDoors().forEach(extra -> extraDoors.add(extra.name()));

        compat.doors().forEach((name, supplier) -> {
            Block door = supplier.get();
            DoorSet set = secretDoors.contains(name)
                    ? DoorSet.SECRET
                    : extraDoors.contains(name)
                    ? DoorSet.LATERAL
                    : door instanceof PetDoorBlock
                    ? DoorSet.PET
                    : door instanceof TransitDoorBlock
                    ? DoorSet.TRANSIT
                    : DoorSet.CONSISTENT;

            List<ICondition> tableConditions = new ArrayList<>();
            tableConditions.add(ModuleEnabledCondition.DOORS);
            tableConditions.add(DoorSetEnabledCondition.forSet(set));
            if (compat.mod() != Mods.ALLTUTTASNEEDS) {
                tableConditions.add(new ModLoadedCondition(compat.mod().id()));
            }
            if (set == DoorSet.SECRET) {
                compat.secretDoorDependencies().stream()
                        .filter(dependency -> dependency != Mods.ALLTUTTASNEEDS)
                        .map(dependency -> new ModLoadedCondition(dependency.id()))
                        .forEach(tableConditions::add);
            }
            conditions.put(door.getLootTable(), List.copyOf(tableConditions));

            if (door instanceof PetDoorBlock) {
                dropSelf(door);
            } else {
                add(door, this::createDoorTable);
            }
        });
    }

    public List<ICondition> conditions(ResourceKey<LootTable> table) {
        return conditions.getOrDefault(table, List.of());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> doors = new ArrayList<>();
        CompatRegistry.loaded().forEach(compat ->
                compat.doors().values().forEach(supplier -> doors.add(supplier.get())));
        return doors;
    }
}
