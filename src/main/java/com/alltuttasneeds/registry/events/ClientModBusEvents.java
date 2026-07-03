package com.alltuttasneeds.registry.events;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.registry.compat.framework.CompatRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderType cutout = RenderType.cutout();
            RenderType translucent = RenderType.translucent();

            CompatRegistry.loaded().forEach(compat ->
                    compat.doors().forEach((name, blockSupplier) -> {
                        RenderType layer = "translucent".equals(compat.renderTypeKey(name)) ? translucent : cutout;
                        ItemBlockRenderTypes.setRenderLayer(blockSupplier.get(), layer);
                    }));
        });
    }
}
