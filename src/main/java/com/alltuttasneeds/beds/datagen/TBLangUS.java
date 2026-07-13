package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.ArrayList;
import java.util.List;

public class TBLangUS extends LanguageProvider {
    private final List<Block> allBlocks = new ArrayList<>();

    public TBLangUS(PackOutput output) {
        super(output, "tuttasbeds", "en_us");

        BedCompatRegistry.loaded().flatMap(compat -> compat.blocks().getEntries().stream())
                .forEach(entry -> allBlocks.add(entry.get()));
    }

    @Override
    protected void addTranslations() {
        allBlocks.forEach(block -> add(block.getDescriptionId(), TBLangNames.english(block)));

        add("alltuttasneeds.itemGroup.tuttasbeds", "Tutta's Beds");
        add("alltuttasneeds.tooltip.beds.basic", "Simple Bed: A Humble Rest.");
        add("alltuttasneeds.tooltip.beds.low", "Cozy Bed: Warm Through the Night.");
        add("alltuttasneeds.tooltip.beds.normal", "Comfortable Bed: Rest Until Morning.");
        add("alltuttasneeds.tooltip.beds.deluxe", "Deluxe Bed: Sleep Like Royalty.");
    }
}
