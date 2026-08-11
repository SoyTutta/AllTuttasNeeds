package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.MattressFamily;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import com.alltuttasneeds.beds.TBContent;
import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.core.condition.ModuleEnabledCondition;
import com.alltuttasneeds.core.datagen.ConditionalBlockLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ItemExistsCondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TBBlockLootTables extends BlockLootSubProvider implements ConditionalBlockLootTableProvider.SubProvider {
    private final Map<ResourceKey<LootTable>, List<ICondition>> conditions = new HashMap<>();

    public TBBlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        if (TBContent.BED_FRAME != null) {
            Block frame = TBContent.BED_FRAME.get();
            registerConditions(frame, Mods.ALLTUTTASNEEDS);
            dropHeadOnly(frame);
        }

        BedCompatRegistry.loaded().forEach(compat -> compat.families().forEach(family ->
                generateFamily(family, compat.mod())));
    }

    private void generateFamily(MattressFamily family, Mods owner) {
        registerFamilyConditions(family, owner);
        dropHeadOnly(family.looseMattress().get());
        family.looseMattressCovers().values().forEach(block ->
                generateCoveredBlock(block.get(), family.looseMattress().get()));

        dropHeadOnly(family.bedBare().get());
        family.bedBasicCovers().values().forEach(block ->
                generateCoveredBlock(block.get(), family.bedBare().get()));

        family.bedBlankets().forEach((blanket, colors) ->
                colors.forEach((color, block) -> generateBlanketedBed(family, blanket, color, block.get())));

        if (!family.bedDeluxe().isEmpty()) {
            family.bedBlankets().keySet().stream()
                    .filter(BlanketMaterial::supportsDeluxe)
                    .findFirst()
                    .ifPresent(deluxeBlanket -> family.bedDeluxe().forEach((color, block) ->
                            generateBlanketedBed(family, deluxeBlanket, color, block.get())));
        }
    }

    private void registerFamilyConditions(MattressFamily family, Mods owner) {
        registerConditions(family.looseMattress().get(), owner);
        family.looseMattressCovers().values().forEach(block -> registerConditions(block.get(), owner));
        registerConditions(family.bedBare().get(), owner);
        family.bedBasicCovers().values().forEach(block -> registerConditions(block.get(), owner));
        family.bedBlankets().values().forEach(colors -> colors.values().forEach(block ->
                registerConditions(block.get(), owner)));
        family.bedDeluxe().values().forEach(block -> registerConditions(block.get(), owner));
    }

    private void registerConditions(Block block, Mods owner) {
        List<ICondition> tableConditions = new ArrayList<>();
        tableConditions.add(ModuleEnabledCondition.BEDS);
        tableConditions.add(new ItemExistsCondition(BuiltInRegistries.ITEM.getKey(block.asItem())));
        if (owner != Mods.ALLTUTTASNEEDS) {
            tableConditions.add(new ModLoadedCondition(owner.id()));
        }
        conditions.put(block.getLootTable(), List.copyOf(tableConditions));
    }

    @Override
    public List<ICondition> conditions(ResourceKey<LootTable> table) {
        return conditions.getOrDefault(table, List.of());
    }

    private void generateCoveredBlock(Block covered, Block bare) {
        add(covered, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(hasSilkTouch())
                        .add(LootItem.lootTableItem(covered).when(headOnly(covered))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(doesNotHaveSilkTouch())
                        .add(LootItem.lootTableItem(bare).when(headOnly(covered)))));
    }

    private void generateBlanketedBed(MattressFamily family, BlanketMaterial blanket, DyeColor color, Block block) {
        LootTable.Builder table = LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(hasSilkTouch())
                        .add(LootItem.lootTableItem(block).when(headOnly(block))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(doesNotHaveSilkTouch())
                        .add(LootItem.lootTableItem(family.bedBare().get()).when(headOnly(block))));

        add(block, table);
    }

    private void dropHeadOnly(Block block) {
        add(block, createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
    }

    private static LootItemBlockStatePropertyCondition.Builder headOnly(Block block) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BedBlock.PART, BedPart.HEAD));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> blocks = new ArrayList<>();
        if (TBContent.BED_FRAME != null) blocks.add(TBContent.BED_FRAME.get());

        BedCompatRegistry.loaded().flatMap(compat -> compat.families().stream()).forEach(family -> {
            blocks.add(family.looseMattress().get());
            family.looseMattressCovers().values().forEach(b -> blocks.add(b.get()));
            blocks.add(family.bedBare().get());
            family.bedBasicCovers().values().forEach(b -> blocks.add(b.get()));
            family.bedBlankets().values().forEach(colors -> colors.values().forEach(b -> blocks.add(b.get())));
            family.bedDeluxe().values().forEach(b -> blocks.add(b.get()));
        });

        return blocks;
    }
}
