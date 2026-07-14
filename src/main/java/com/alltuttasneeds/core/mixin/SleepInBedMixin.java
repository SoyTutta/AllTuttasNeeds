package com.alltuttasneeds.core.mixin;

import com.alltuttasneeds.beds.block.BedFrameBlock;
import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TuttaBedBlock;
import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.SleepInBed;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SleepInBed.class)
public abstract class SleepInBedMixin {
    @Inject(method = "checkExtraStartConditions", at = @At("HEAD"), cancellable = true)
    private void alltuttasneeds$respectVillagerBedConfig(ServerLevel level, LivingEntity entity,
                                                         CallbackInfoReturnable<Boolean> cir) {
        if (!TBConfig.isModuleEnabled()) return;
        if (TBConfig.villagersCanUseTuttaBeds.get()) return;
        entity.getBrain().getMemory(MemoryModuleType.HOME).ifPresent(home -> {
            Block block = level.getBlockState(home.pos()).getBlock();
            if (block instanceof TuttaBedBlock || block instanceof LooseMattressBlock) {
                cir.setReturnValue(false);
            }
        });
    }

    @Inject(method = "canStillUse", at = @At("HEAD"), cancellable = true)
    private void alltuttasneeds$allowReducedHeightBeds(ServerLevel level, LivingEntity entity, long gameTime,
                                                       CallbackInfoReturnable<Boolean> cir) {
        if (!TBConfig.isModuleEnabled()) return;
        Optional<GlobalPos> home = entity.getBrain().getMemory(MemoryModuleType.HOME);
        if (home.isEmpty() || home.get().dimension() != level.dimension()) return;

        BlockPos pos = home.get().pos();
        Block block = level.getBlockState(pos).getBlock();
        if (!(block instanceof BedFrameBlock) && !(block instanceof LooseMattressBlock)) return;
        if (!TBConfig.villagersCanUseTuttaBeds.get()) {
            cir.setReturnValue(false);
            return;
        }

        cir.setReturnValue(entity.getBrain().isActive(Activity.REST)
                && pos.closerToCenterThan(entity.position(), 1.14D));
    }
}
