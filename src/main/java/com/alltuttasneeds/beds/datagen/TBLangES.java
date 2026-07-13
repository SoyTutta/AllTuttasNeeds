package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.ArrayList;
import java.util.List;

public class TBLangES extends LanguageProvider {
    private final List<Block> allBlocks = new ArrayList<>();

    public TBLangES(PackOutput output) {
        super(output, "tuttasbeds", "es_es");

        BedCompatRegistry.loaded().flatMap(compat -> compat.blocks().getEntries().stream())
                .forEach(entry -> allBlocks.add(entry.get()));
    }

    @Override
    protected void addTranslations() {
        allBlocks.forEach(block -> add(block.getDescriptionId(), TBLangNames.spanish(block)));

        add("alltuttasneeds.itemGroup.tuttasbeds", "Tutta's Beds");
        add("alltuttasneeds.tooltip.beds.basic", "Cama sencilla: un descanso humilde.");
        add("alltuttasneeds.tooltip.beds.low", "Cama acogedora: calidez toda la noche.");
        add("alltuttasneeds.tooltip.beds.normal", "Cama cómoda: descansa hasta el alba.");
        add("alltuttasneeds.tooltip.beds.deluxe", "Cama deluxe: duerme como la realeza.");
    }
}
