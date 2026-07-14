package com.alltuttasneeds;

import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import com.alltuttasneeds.core.condition.ATNConditions;
import com.alltuttasneeds.core.config.ATNConfig;
import com.alltuttasneeds.beds.TBCreativeTab;
import com.alltuttasneeds.doors.TDCreativeTab;
import com.alltuttasneeds.doors.compat.CompatRegistry;
import com.alltuttasneeds.doors.config.TDConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AllTuttasNeeds.MODID)
public class AllTuttasNeeds {
    public static final String MODID = "alltuttasneeds";

    public AllTuttasNeeds(IEventBus modEventBus, ModContainer modContainer) {
        ATNConfig.register(modContainer);
        ATNConditions.register(modEventBus);

        CompatRegistry.loaded().forEach(compat -> compat.registerToBus(modEventBus));
        if (TDConfig.anySetEnabled()) {
            TDCreativeTab.TABS.register(modEventBus);
        }

        BedCompatRegistry.registerFamilies();
        BedCompatRegistry.loaded().forEach(compat -> compat.registerToBus(modEventBus));
        TBCreativeTab.TABS.register(modEventBus);
    }
}
