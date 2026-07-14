package com.alltuttasneeds.core.mixin;

import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TuttaBedBlock;
import com.alltuttasneeds.beds.config.TBConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BedRenderer.class)
public abstract class BedRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void alltuttasneeds$skipVanillaBedModel(BedBlockEntity blockEntity, float partialTick,
                                                      PoseStack poseStack, MultiBufferSource bufferSource,
                                                      int packedLight, int packedOverlay, CallbackInfo info) {
        if (!TBConfig.isModuleEnabled()) return;
        Block block = blockEntity.getBlockState().getBlock();
        if (block instanceof TuttaBedBlock || block instanceof LooseMattressBlock) {
            info.cancel();
        }
    }
}
