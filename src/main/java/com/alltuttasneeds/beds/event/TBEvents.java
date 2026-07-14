package com.alltuttasneeds.beds.event;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.beds.BedBlanketIngredients;
import com.alltuttasneeds.beds.BedCoverIngredients;
import com.alltuttasneeds.beds.BedTier;
import com.alltuttasneeds.beds.BedTierResolver;
import com.alltuttasneeds.beds.BunkBedPositions;
import com.alltuttasneeds.beds.block.TieredBedBlock;
import com.alltuttasneeds.beds.config.SleepEffectConfig;
import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerRespawnPositionEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;

import java.util.List;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID)
public final class TBEvents {
    private TBEvents() {}

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        if (!TBConfig.isModuleEnabled()) return;
        event.addListener(new BedCoverIngredients());
        event.addListener(new BedBlanketIngredients());
    }

    @SubscribeEvent
    public static void onPlayerSetSpawn(PlayerSetSpawnEvent event) {
        if (!TBConfig.isModuleEnabled()) return;
        BlockPos spawn = event.getNewSpawn();
        if (spawn == null) return;

        Block block = event.getEntity().getCommandSenderWorld().getBlockState(spawn).getBlock();
        if (isVanillaBed(block) && !TBConfig.vanillaBedsUseTierSpawnRules.get()) return;
        BedTier tier = BedTierResolver.resolve(block);
        if (tier != null && !TBConfig.setsSpawn(tier)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onCanPlayerSleep(CanPlayerSleepEvent event) {
        if (!TBConfig.isModuleEnabled()) return;
        if (!TBConfig.deluxeIgnoresNearbyMonsters.get() || event.getProblem() != BedSleepingProblem.NOT_SAFE) return;
        if (BedTierResolver.resolve(event.getState().getBlock()) == BedTier.DELUXE) {
            event.setProblem(null);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawnPosition(PlayerRespawnPositionEvent event) {
        if (!TBConfig.isModuleEnabled()) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        BlockPos respawnPos = player.getRespawnPosition();
        if (respawnPos == null) return;

        DimensionTransition transition = event.getDimensionTransition();
        ServerLevel level = transition.newLevel();
        if (player.getRespawnDimension() != level.dimension()) return;

        BlockState state = level.getBlockState(respawnPos);
        if (!(state.getBlock() instanceof TieredBedBlock) || !state.hasProperty(BedBlock.FACING)) return;

        List<BlockPos> beds = BunkBedPositions.collectStack(level, respawnPos);
        if (beds.size() == 1) return;

        Direction facing = state.getValue(BedBlock.FACING);
        BunkBedPositions.findStandUpPosition(player.getType(), level, beds, facing, player.getRespawnAngle())
                .ifPresent(position -> {
                    event.setDimensionTransition(new DimensionTransition(
                            level,
                            position,
                            transition.speed(),
                            transition.yRot(),
                            transition.xRot(),
                            false,
                            transition.postDimensionTransition()));
                    event.setCopyOriginalSpawnPosition(true);
                });
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (!TBConfig.isModuleEnabled()) return;
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        if (event.wakeImmediately() || !player.isSleepingLongEnough()) return;

        player.getSleepingPos().ifPresent(pos -> {
            BlockState state = player.level().getBlockState(pos);
            Block block = state.getBlock();
            if (isVanillaBed(block) && !TBConfig.vanillaBedsUseTierWakeEffects.get()) return;
            BedTier tier = BedTierResolver.resolve(block);
            if (tier == null) return;
            SleepEffectConfig effects = TBConfig.effectsFor(tier);

            effects.resolveEffect().ifPresent(effect ->
                    player.addEffect(new MobEffectInstance(effect, effects.durationTicks(), 0)));
        });
    }

    private static boolean isVanillaBed(Block block) {
        return block instanceof BedBlock
                && BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE);
    }

}
