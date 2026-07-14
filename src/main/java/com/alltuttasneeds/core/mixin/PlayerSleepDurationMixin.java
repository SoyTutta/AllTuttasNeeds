package com.alltuttasneeds.core.mixin;

import com.alltuttasneeds.beds.BedTier;
import com.alltuttasneeds.beds.BedTierResolver;
import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerSleepDurationMixin {
    @Unique
    private int alltuttasneeds$sleepTicks;

    @Inject(method = "tick", at = @At("TAIL"))
    private void alltuttasneeds$countSleepTicks(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!player.isSleeping() || !alltuttasneeds$usesTieredSleepDuration(player)) {
            alltuttasneeds$sleepTicks = 0;
            return;
        }
        alltuttasneeds$sleepTicks++;
    }

    @Inject(method = "isSleepingLongEnough", at = @At("HEAD"), cancellable = true)
    private void alltuttasneeds$applyTieredSleepDuration(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (!alltuttasneeds$usesTieredSleepDuration(player)) return;
        if (!player.isSleeping()) {
            cir.setReturnValue(false);
            return;
        }

        cir.setReturnValue(alltuttasneeds$sleepTicks >= alltuttasneeds$requiredSleepTicks(player));
    }

    @Inject(method = "getSleepTimer", at = @At("HEAD"), cancellable = true)
    private void alltuttasneeds$syncSleepOverlay(CallbackInfoReturnable<Integer> cir) {
        Player player = (Player) (Object) this;
        if (!player.isSleeping() || !alltuttasneeds$usesTieredSleepDuration(player)) return;

        int requiredTicks = alltuttasneeds$requiredSleepTicks(player);
        cir.setReturnValue(Mth.clamp(Mth.floor(alltuttasneeds$sleepTicks * 100.0D / requiredTicks), 0, 100));
    }

    @Unique
    private static int alltuttasneeds$requiredSleepTicks(Player player) {
        BedTier tier = player.getSleepingPos()
                .map(pos -> BedTierResolver.resolve(player.level().getBlockState(pos).getBlock()))
                .orElse(BedTier.BASIC);
        return Math.max(1, Mth.ceil(100.0D * TBConfig.sleepDurationMultiplier(tier)));
    }

    @Unique
    private static boolean alltuttasneeds$usesTieredSleepDuration(Player player) {
        if (!TBConfig.isModuleEnabled() || !TBConfig.tieredSleepDurationEnabled.get()) return false;
        return player.getSleepingPos()
                .map(pos -> player.level().getBlockState(pos).getBlock())
                .filter(block -> BedTierResolver.resolve(block) != null)
                .map(block -> TBConfig.vanillaBedsUseTieredSleepDuration.get()
                        || !alltuttasneeds$isVanillaBed(block))
                .orElse(false);
    }

    @Unique
    private static boolean alltuttasneeds$isVanillaBed(Block block) {
        return block instanceof BedBlock
                && BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE);
    }
}
