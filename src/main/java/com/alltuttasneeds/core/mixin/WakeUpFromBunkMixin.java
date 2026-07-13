package com.alltuttasneeds.core.mixin;

import com.alltuttasneeds.beds.BunkBedPositions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class WakeUpFromBunkMixin extends Entity {
    private WakeUpFromBunkMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "stopSleeping", at = @At("HEAD"), cancellable = true)
    private void alltuttasneeds$wakeBesideLowestBed(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Optional<BlockPos> sleepingPos = entity.getSleepingPos();
        if (sleepingPos.isEmpty() || !this.level().hasChunkAt(sleepingPos.get())) return;

        List<BlockPos> beds = BunkBedPositions.collectStack(this.level(), sleepingPos.get());
        if (beds.size() == 1) return;

        BlockPos originalPos = sleepingPos.get();
        BlockState originalState = this.level().getBlockState(originalPos);
        if (!originalState.isBed(this.level(), originalPos, entity)) return;

        Direction originalFacing = originalState.getValue(BedBlock.FACING);
        originalState.setBedOccupied(this.level(), originalPos, entity, false);
        Vec3 standUp = BunkBedPositions.findStandUpPosition(entity.getType(), this.level(), beds, originalFacing, entity.getYRot())
                .orElseGet(() -> Vec3.atBottomCenterOf(originalPos.above()).add(0.0D, 0.1D, 0.0D));
        Vec3 lookDirection = Vec3.atBottomCenterOf(originalPos).subtract(standUp).normalize();
        float rotation = (float) Mth.wrapDegrees(Mth.atan2(lookDirection.z, lookDirection.x) * 180.0F / Math.PI - 90.0D);

        entity.setPos(standUp.x, standUp.y, standUp.z);
        entity.setYRot(rotation);
        entity.setXRot(0.0F);
        Vec3 position = entity.position();
        entity.setPose(Pose.STANDING);
        entity.setPos(position.x, position.y, position.z);
        entity.clearSleepingPos();
        ci.cancel();
    }

}
