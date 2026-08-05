package com.alltuttasneeds.core.mixin;

import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TuttaBedBlock;
import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "fr.madu59.obe.client.renderer.blockentity.BlockEntityModelsManager", remap = false)
public abstract class OptimizedBlockEntitiesCompatMixin {

    @Inject(
            method = "getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;Lnet/minecraft/client/resources/model/BakedModel;Ljava/lang/String;)Lnet/minecraft/client/resources/model/BakedModel;",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void alltuttasneeds$keepOriginalBedModel(BlockState state, RandomSource random,
                                                      BakedModel originalModel, String group,
                                                      CallbackInfoReturnable<BakedModel> cir) {
        if (!TBConfig.isModuleEnabled()) return;
        Block block = state.getBlock();
        // OBE otherwise composites its vanilla bed mesh over our baked block model.
        if (block instanceof TuttaBedBlock || block instanceof LooseMattressBlock) {
            cir.setReturnValue(originalModel);
        }
    }
}
