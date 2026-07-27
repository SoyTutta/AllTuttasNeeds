package com.alltuttasneeds.delights;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class DelightsCommonSetup {

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(DelightsCommonSetup::registerDispenserBehaviors);
    }

    public static void registerDispenserBehaviors() {
    }
}

