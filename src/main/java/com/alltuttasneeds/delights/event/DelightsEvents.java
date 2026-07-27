package com.alltuttasneeds.delights.event;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.delights.config.DelightsConfig;
import com.alltuttasneeds.delights.DelightsPotions;
import com.alltuttasneeds.delights.DelightsItems;
import com.alltuttasneeds.delights.DelightsBlocks;
import com.alltuttasneeds.delights.block.SnowGolemFeastBlock;
import com.alltuttasneeds.delights.block.entity.SnowGolemFeastBlockEntity;
import com.alltuttasneeds.delights.config.DelightGroup;
import com.alltuttasneeds.delights.face.SnowGolemFaceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import vectorwing.farmersdelight.common.block.PieBlock;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID)
public class DelightsEvents {

    private static boolean isProcessingBoneAttack = false;

    @SubscribeEvent
    public static void onLivingDamage(LivingIncomingDamageEvent event) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.UNDEAD)
                || isProcessingBoneAttack
                || !(event.getSource().getEntity() instanceof Player player)
                || player.level().isClientSide) return;

        ItemStack weapon = player.getMainHandItem();
        if (!weapon.is(DelightsItems.ROTTEN_MEAT_ON_A_BONE.get())) return;

        float newDamage = event.getAmount() + 2;
        event.setCanceled(true);

        Holder<DamageType> mobAttack = player.level().registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.MOB_ATTACK);

        isProcessingBoneAttack = true;
        try {
            Zombie zombie = new Zombie(player.level());
            boolean hurt = event.getEntity().hurt(
                    new DamageSource(mobAttack, zombie, zombie, player.position()), newDamage);
            if (hurt
                    && event.getEntity().isDeadOrDying()
                    && !player.getAbilities().instabuild
                    && player.getMainHandItem() == weapon
                    && weapon.is(DelightsItems.ROTTEN_MEAT_ON_A_BONE.get())) {
                weapon.shrink(1);
            }
        } finally {
            isProcessingBoneAttack = false;
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.POTATO)
                || !(event.getEntity() instanceof Player player)
                || !"SoyTutta".equals(player.getGameProfile().getName())
                || player.getRandom().nextFloat() >= 0.15F) return;

        event.getDrops().add(new ItemEntity(
                player.level(),
                player.getX(),
                player.getY(),
                player.getZ(),
                new ItemStack(DelightsItems.POTATO_AND_MEAT_PIE.get())));
    }

    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.FROG)) return;

        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, DelightsItems.RAW_FROG_LEG.get(), DelightsPotions.FROGGY_POTION);
        builder.addMix(DelightsPotions.FROGGY_POTION, Items.REDSTONE, DelightsPotions.LONG_FROGGY_POTION);
        builder.addMix(DelightsPotions.FROGGY_POTION, Items.GLOWSTONE_DUST, DelightsPotions.STRONG_FROGGY_POTION);
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        if (DelightsConfig.isGroupEnabled(DelightGroup.FROZEN_TREATS)) {
            event.addListener(SnowGolemFaceRegistry.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void onLightningRemoved(EntityLeaveLevelEvent event) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.FROZEN_TREATS)
                || !(event.getLevel() instanceof ServerLevel level)
                || !(event.getEntity() instanceof LightningBolt lightning)
                || lightning.getRemovalReason() != Entity.RemovalReason.DISCARDED) return;

        BlockPos pos = BlockPos.containing(
                lightning.getX(), lightning.getY() - 1.0E-6, lightning.getZ());
        var state = level.getBlockState(pos);
        if (!state.is(DelightsBlocks.ICE_CREAM_IN_A_PUMPKIN_HEAD_BLOCK.get())
                || state.getValue(PieBlock.BITES) != 0
                || !(level.getBlockEntity(pos) instanceof SnowGolemFeastBlockEntity feast)) return;

        Component customName = feast.getCustomName();
        boolean hasPumpkin = state.getValue(SnowGolemFeastBlock.PUMPKIN);
        SnowGolem golem = EntityType.SNOW_GOLEM.create(level);
        if (golem == null) return;

        golem.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                level.random.nextFloat() * 360.0F, 0.0F);
        golem.setPumpkin(hasPumpkin);
        if (customName != null) {
            golem.setCustomName(customName);
            golem.setPersistenceRequired();
        }

        level.removeBlock(pos, false);
        level.addFreshEntity(golem);
    }
}
