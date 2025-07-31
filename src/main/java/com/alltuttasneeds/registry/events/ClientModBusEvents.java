package com.alltuttasneeds.registry.events;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.registry.TDContent;
import com.alltuttasneeds.registry.compat.Mods;
import com.alltuttasneeds.registry.compat.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;
import java.util.function.Supplier;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderType cutoutLayer = RenderType.cutout();
            RenderType translucentLayer = RenderType.translucent();

            setRenderLayers(TDContent.DOORS, cutoutLayer);

            if (Mods.NOMANSLAND.isLoaded()) {
                setRenderLayers(NMLContent.DOORS, cutoutLayer);
            }
            if (Mods.BLOCKBOX.isLoaded()) {
                setRenderLayers(BBContent.DOORS, cutoutLayer);
            }
            if (Mods.NEWWORLD.isLoaded()) {
                setRenderLayers(NWContent.DOORS, cutoutLayer);
            }
            if (Mods.MYNETHERSDELIGHT.isLoaded()) {
                setRenderLayers(MNDContent.DOORS, cutoutLayer);
            }
            if (Mods.ENDERSCAPE.isLoaded()) {
                setRenderLayers(ESContent.DOORS, cutoutLayer);
            }
            if (Mods.ARTS_AND_CRAFTS.isLoaded()) {
                setRenderLayers(ACContent.DOORS, cutoutLayer);
            }
            if (Mods.NATURES_SPIRIT.isLoaded()) {
                setRenderLayers(NSContent.DOORS, cutoutLayer);
            }
            if (Mods.CREATE.isLoaded()) {
                setRenderLayers(CreateContent.DOORS, cutoutLayer);
            }
            if (Mods.MALUM.isLoaded()) {
                setRenderLayers(MalumContent.DOORS, cutoutLayer);
            }

            if (Mods.BIOMESOPLENTY.isLoaded()) {
                BoPContent.DOORS.forEach((name, blockSupplier) -> {
                    if (name.startsWith("magic_")) {
                        ItemBlockRenderTypes.setRenderLayer(blockSupplier.get(), translucentLayer);
                    } else {
                        ItemBlockRenderTypes.setRenderLayer(blockSupplier.get(), cutoutLayer);
                    }
                });
            }
        });
    }

    private static void setRenderLayers(Map<String, Supplier<? extends Block>> doorMap, RenderType renderType) {
        if (doorMap != null) {
            doorMap.values().forEach(blockSupplier -> ItemBlockRenderTypes.setRenderLayer(blockSupplier.get(), renderType));
        }
    }
}