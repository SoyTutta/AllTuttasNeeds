package com.alltuttasneeds.delights.client;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.delights.client.renderer.*;
import com.alltuttasneeds.delights.config.DelightsConfig;
import com.alltuttasneeds.delights.DelightsBlockEntities;
import com.alltuttasneeds.delights.config.DelightGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID, value = Dist.CLIENT)
public class DelightsClientEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (DelightsConfig.isGroupEnabled(DelightGroup.FROZEN_TREATS)) {
            BlockEntityRenderers.register(
                    DelightsBlockEntities.SNOW_GOLEM_FEAST_BE.get(),
                    SnowGolemFeastRenderer::new);
        }
    }

    @SubscribeEvent
    public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.FROZEN_TREATS)) return;
        event.register(FaceModelLoader.ID, new FaceModelLoader());
    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.FROZEN_TREATS)) return;

        FaceNameRegistry.reload(Minecraft.getInstance().getResourceManager());
        FaceNameRegistry.registerModels(event);
    }

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.FROZEN_TREATS)) return;
        FaceNameRegistry.bakeModels(event);
    }
}
