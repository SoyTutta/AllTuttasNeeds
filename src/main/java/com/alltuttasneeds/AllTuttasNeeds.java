package com.alltuttasneeds;

import com.alltuttasneeds.registry.TDCreativeTab;
import com.alltuttasneeds.registry.compat.framework.CompatRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(AllTuttasNeeds.MODID)
public class AllTuttasNeeds {
    public static final String MODID = "alltuttasneeds";

    public AllTuttasNeeds(IEventBus modEventBus) {
        CompatRegistry.loaded().forEach(compat -> compat.registerToBus(modEventBus));
        TDCreativeTab.TABS.register(modEventBus);
    }
}
