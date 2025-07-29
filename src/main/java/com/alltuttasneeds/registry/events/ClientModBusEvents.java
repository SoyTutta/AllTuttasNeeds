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

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents {

    private record CompatModule(Supplier<Boolean> isLoaded, Map<String, Supplier<? extends Block>> doorMap) {}

    private static final List<CompatModule> STANDARD_COMPAT_MODULES = List.of(
            new CompatModule(() -> true, TDContent.DOORS),
            new CompatModule(Mods.NOMANSLAND::isLoaded, NMLContent.DOORS),
            new CompatModule(Mods.BLOCKBOX::isLoaded, BBContent.DOORS),
            new CompatModule(Mods.NEWWORLD::isLoaded, NWContent.DOORS),
            new CompatModule(Mods.MYNETHERSDELIGHT::isLoaded, MNDContent.DOORS),
            new CompatModule(Mods.ENDERSCAPE::isLoaded, ESContent.DOORS),
            new CompatModule(Mods.ARTS_AND_CRAFTS::isLoaded, ACContent.DOORS),
            new CompatModule(Mods.NATURES_SPIRIT::isLoaded, NSContent.DOORS),
            new CompatModule(Mods.CREATE::isLoaded, CreateContent.DOORS),
            new CompatModule(Mods.MALUM::isLoaded, MalumContent.DOORS)
    );

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderType cutoutLayer = RenderType.cutout();
            RenderType translucentLayer = RenderType.translucent();

            for (CompatModule module : STANDARD_COMPAT_MODULES) {
                if (module.isLoaded().get() && module.doorMap() != null) {
                    module.doorMap().values().forEach(blockSupplier ->
                            ItemBlockRenderTypes.setRenderLayer(blockSupplier.get(), cutoutLayer)
                    );
                }
            }

            if (Mods.BIOMESOPLENTY.isLoaded() && BoPContent.DOORS != null) {
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
}