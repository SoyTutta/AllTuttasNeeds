package com.alltuttasneeds.delights;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.delights.DelightsCommonSetup;
import com.alltuttasneeds.delights.DelightsPotions;
import com.alltuttasneeds.delights.DelightsBlockEntities;
import com.alltuttasneeds.delights.DelightsBlocks;
import com.alltuttasneeds.delights.DelightsCreativeTab;
import com.alltuttasneeds.delights.DelightsItems;
import com.alltuttasneeds.delights.DelightsLootModifiers;
import com.alltuttasneeds.delights.compat.BrewinAndChewinCompat;
import com.alltuttasneeds.delights.compat.MinersDelightCompat;
import com.alltuttasneeds.delights.compat.MyNethersDelightCompat;
import com.alltuttasneeds.delights.compat.SpawnCompat;
import com.alltuttasneeds.delights.config.DelightsConfig;
import com.alltuttasneeds.delights.config.DelightGroup;
import net.neoforged.bus.api.IEventBus;

public final class DelightsModule {
    public static final String MODID = "tuttasdelights";

    private DelightsModule() {}

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(DelightsCommonSetup::init);

        DelightsIngredientTypes.INGREDIENT_TYPES.register(modEventBus);
        DelightsItems.ITEMS.register(modEventBus);
        DelightsBlocks.BLOCKS.register(modEventBus);
        if (DelightsConfig.isGroupEnabled(DelightGroup.FROZEN_TREATS)) {
            DelightsBlockEntities.FROZEN_BLOCK_ENTITIES.register(modEventBus);
        }
        DelightsLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        if (DelightsConfig.isGroupEnabled(DelightGroup.FROG)) {
            DelightsPotions.register(modEventBus);
        }
        if (DelightsConfig.anyGroupEnabled()) {
            DelightsCreativeTab.TABS.register(modEventBus);
        }

        if (Mods.MYNETHERSDELIGHT.isLoaded()
                && (DelightsConfig.isGroupEnabled(DelightGroup.POTATO)
                || DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS))) {
            MyNethersDelightCompat.ITEMS.register(modEventBus);
        }
        if (Mods.BREWIN_AND_CHEWIN.isLoaded()
                && DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)) {
            BrewinAndChewinCompat.ITEMS.register(modEventBus);
        }
        if (Mods.MINERS_DELIGHT.isLoaded()
                && DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)) {
            MinersDelightCompat.ITEMS.register(modEventBus);
        }
        if (Mods.SPAWN.isLoaded()
                && DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)) {
            SpawnCompat.ITEMS.register(modEventBus);
        }
    }
}
