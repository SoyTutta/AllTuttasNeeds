package com.alltuttasneeds.doors.client;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.doors.compat.CompatRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID, value = Dist.CLIENT)
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
