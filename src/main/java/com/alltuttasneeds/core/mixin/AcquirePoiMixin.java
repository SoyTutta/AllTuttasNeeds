package com.alltuttasneeds.core.mixin;

import com.alltuttasneeds.beds.block.BedFrameBlock;
import com.alltuttasneeds.beds.block.BedStateProperties;
import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TieredBedBlock;
import com.alltuttasneeds.beds.block.TuttaBedBlock;
import com.alltuttasneeds.beds.config.TBConfig;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.AcquirePoi;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(AcquirePoi.class)
public abstract class AcquirePoiMixin {
    @Inject(method = "findPathToPois", at = @At("HEAD"), cancellable = true)
    private static void alltuttasneeds$prioritizeBeds(Mob mob,
                                                      Set<Pair<Holder<PoiType>, BlockPos>> pois,
                                                      CallbackInfoReturnable<Path> cir) {
        if (!TBConfig.isModuleEnabled() || !TBConfig.villagersCanUseTuttaBeds.get()) return;
        Map<Integer, Set<BlockPos>> bedsByPriority = new HashMap<>();

        for (Pair<Holder<PoiType>, BlockPos> poi : pois) {
            BlockPos pos = poi.getSecond();
            BlockState state = mob.level().getBlockState(pos);
            BedBlock block = state.getBlock() instanceof BedBlock bed ? bed : null;
            if (block == null) continue;
            BlockState below = mob.level().getBlockState(pos.below());
            if (block instanceof TuttaBedBlock
                    && below.hasProperty(BedStateProperties.BUNK)
                    && below.getValue(BedStateProperties.BUNK)) {
                continue;
            }
            bedsByPriority.computeIfAbsent(priorityOf(block), ignored -> new HashSet<>()).add(pos);
        }

        for (int priority = 0; priority <= 6; priority++) {
            Set<BlockPos> candidates = bedsByPriority.get(priority);
            if (candidates == null) continue;
            Path path = mob.getNavigation().createPath(candidates, 1);
            if (path != null && path.canReach()) {
                cir.setReturnValue(path);
                return;
            }
        }
    }

    private static int priorityOf(BedBlock block) {
        if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals("minecraft")) return 0;
        if (block instanceof TieredBedBlock bed) {
            if (bed.blanketMaterial() != null) return 1;
            if (bed.basicCover() != null) return 2;
            return 3;
        }
        if (block instanceof LooseMattressBlock mattress) return mattress.cover() != null ? 4 : 5;
        if (block instanceof BedFrameBlock) return 5;
        return block instanceof TuttaBedBlock ? 3 : 5;
    }
}
