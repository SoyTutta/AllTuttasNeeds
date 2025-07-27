package com.alltuttasneeds;

import com.alltuttasneeds.registry.compat.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import com.alltuttasneeds.registry.TDContent;
import com.alltuttasneeds.registry.TDCreativeTab;

@Mod(AllTuttasNeeds.MODID)
public class AllTuttasNeeds {
    public static final String MODID = "alltuttasneeds";

    public AllTuttasNeeds(IEventBus modEventBus) {
        TDContent.BLOCKS.register(modEventBus);
        TDContent.ITEMS.register(modEventBus);
        if (Mods.NOMANSLAND.isLoaded()) {
            NMLContent.BLOCKS.register(modEventBus);
            NMLContent.ITEMS.register(modEventBus);
        }
        if (Mods.BLOCKBOX.isLoaded()) {
            BBContent.BLOCKS.register(modEventBus);
            BBContent.ITEMS.register(modEventBus);
        }
        if (Mods.NEWWORLD.isLoaded()) {
            NWContent.BLOCKS.register(modEventBus);
            NWContent.ITEMS.register(modEventBus);
        }
        if (Mods.MYNETHERSDELIGHT.isLoaded()) {
            MNDContent.BLOCKS.register(modEventBus);
            MNDContent.ITEMS.register(modEventBus);
        }
        if (Mods.ENDERSCAPE.isLoaded()) {
            ESContent.BLOCKS.register(modEventBus);
            ESContent.ITEMS.register(modEventBus);
        }
        if (Mods.ARTS_AND_CRAFTS.isLoaded()) {
            ACContent.BLOCKS.register(modEventBus);
            ACContent.ITEMS.register(modEventBus);
        }
        if (Mods.BIOMESOPLENTY.isLoaded()) {
            BoPContent.BLOCKS.register(modEventBus);
            BoPContent.ITEMS.register(modEventBus);
        }
        if (Mods.NATURES_SPIRIT.isLoaded()) {
            NSContent.BLOCKS.register(modEventBus);
            NSContent.ITEMS.register(modEventBus);
        }
        if (Mods.CREATE.isLoaded()) {
            CreateContent.BLOCKS.register(modEventBus);
            CreateContent.ITEMS.register(modEventBus);
        }
        if (Mods.MALUM.isLoaded()) {
            MalumContent.BLOCKS.register(modEventBus);
            MalumContent.ITEMS.register(modEventBus);
        }

        TDCreativeTab.TABS.register(modEventBus);
    }
}