package com.alltuttasneeds.core.mixin;

import com.alltuttasneeds.beds.block.BedFrameBlock;
import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityBedHeightMixin extends Entity {
    private LivingEntityBedHeightMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "setPosToBed", cancellable = true)
    private void alltuttasneeds$setPosToReducedHeightBed(BlockPos pos, CallbackInfo info) {
        if (!TBConfig.isModuleEnabled()) return;
        Block block = this.level().getBlockState(pos).getBlock();
        if (block instanceof BedFrameBlock || block instanceof LooseMattressBlock) {
            this.setPos(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
            info.cancel();
        }
    }
}
