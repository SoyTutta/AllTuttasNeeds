package com.alltuttasneeds.core.mixin;

import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TuttaBedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeBedMixin {

    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void alltuttasneeds$allowTuttaBeds(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if ((BlockEntityType<?>) (Object) this != BlockEntityType.BED) return;

        Block block = state.getBlock();
        if (block instanceof TuttaBedBlock || block instanceof LooseMattressBlock) {
            cir.setReturnValue(true);
        }
    }
}