package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.MattressFamily;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import com.alltuttasneeds.beds.TBContent;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TBBlockLootTables extends BlockLootSubProvider {
    public TBBlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        if (TBContent.BED_FRAME != null) {
            dropHeadOnly(TBContent.BED_FRAME.get());
        }

        BedCompatRegistry.loaded().flatMap(compat -> compat.families().stream()).forEach(this::generateFamily);
    }

    private void generateFamily(MattressFamily family) {
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
        if (!blanket.isDirectApplyEnabled()) {
            dropHeadOnly(block);
            return;
        }

        LootTable.Builder table = LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(hasSilkTouch())
                        .add(LootItem.lootTableItem(block).when(headOnly(block))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(doesNotHaveSilkTouch())
                        .add(LootItem.lootTableItem(family.bedBare().get()).when(headOnly(block))));

        Item rawMaterial = DefaultBedIngredients.blanket(blanket.suffix(), color);
        if (rawMaterial != null) {
            table = table.withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(doesNotHaveSilkTouch())
                    .add(LootItem.lootTableItem(rawMaterial).when(headOnly(block))));
        }

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
