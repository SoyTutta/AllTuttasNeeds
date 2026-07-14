package com.alltuttasneeds.doors.event;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.doors.block.PetDoorBlock;
import com.alltuttasneeds.doors.block.TransitDoorBlock;
import com.alltuttasneeds.doors.config.TDConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.WeakHashMap;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID)
public final class DoorAutomaticOpeningEvents {
    private static final WeakHashMap<Player, Vec3> LAST_PLAYER_POSITIONS = new WeakHashMap<>();

    private DoorAutomaticOpeningEvents() {}

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (!TDConfig.isModuleEnabled()) return;
        Entity entity = event.getEntity();
        if (!(entity.level() instanceof ServerLevel level) || entity.isSpectator()) return;
        if (!TDConfig.transitAutomaticOpeningEnabled.get() && !TDConfig.petAutomaticOpeningEnabled.get()) return;

        AABB contactBounds = entity.getBoundingBox().inflate(0.1D);
        Vec3 movement = getPlayerMovement(entity);
        boolean approaching = movement != null && horizontalSpeedSqr(movement) > 1.0E-4D;
        AABB bounds = approaching ? contactBounds.inflate(1.0D, 0.0D, 1.0D) : contactBounds;
        int minX = Mth.floor(bounds.minX);
        int minY = Mth.floor(bounds.minY);
        int minZ = Mth.floor(bounds.minZ);
        int maxX = Mth.floor(bounds.maxX);
        int maxY = Mth.floor(bounds.maxY);
        int maxZ = Mth.floor(bounds.maxZ);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    cursor.set(x, y, z);
                    BlockState state = level.getBlockState(cursor);
                    if (state.getBlock() instanceof TransitDoorBlock transitDoor) {
                        DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
                        if (half == DoubleBlockHalf.UPPER && y - 1 >= minY) continue;
                        BlockPos doorPos = half == DoubleBlockHalf.UPPER ? cursor.below() : cursor.immutable();
                        if (isContactingOrApproaching(entity, movement, contactBounds,
                                new AABB(doorPos.getX(), doorPos.getY(), doorPos.getZ(),
                                        doorPos.getX() + 1.0D, doorPos.getY() + 2.0D, doorPos.getZ() + 1.0D))) {
                            openTransitDoor(level, doorPos, entity, transitDoor);
                        }
                    } else if (state.getBlock() instanceof PetDoorBlock petDoor) {
                        BlockPos doorPos = cursor.immutable();
                        if (isContactingOrApproaching(entity, movement, contactBounds, new AABB(doorPos))) {
                            openPetDoor(level, doorPos, state, entity, petDoor);
                        }
                    }
                }
            }
        }
    }

    private static void openTransitDoor(ServerLevel level, BlockPos pos, Entity entity, TransitDoorBlock door) {
        if (!TDConfig.transitAutomaticOpeningEnabled.get() || !isTransitEntityEligible(entity)) return;

        BlockState state = level.getBlockState(pos);
        if (!state.is(door)) return;
        if (!TDConfig.shouldAutomaticallyClose(state.getValue(BlockStateProperties.POWERED))) return;

        if (!state.getValue(DoorBlock.OPEN)) {
            door.setOpen(null, level, state, pos, true);
        }
        if (TDConfig.transitAutomaticClosingEnabled.get()) {
            level.scheduleTick(pos, door, TDConfig.automaticClosingDelay());
        }
    }

    private static boolean isTransitEntityEligible(Entity entity) {
        if (entity.isCrouching() || entity instanceof ItemEntity) return false;
        if (!(entity instanceof Animal animal)) return true;
        return !animal.getPassengers().isEmpty() || animal instanceof TamableAnimal tamable && tamable.isTame();
    }

    private static boolean isContactingOrApproaching(Entity entity, Vec3 movement, AABB contactBounds, AABB doorBounds) {
        if (contactBounds.intersects(doorBounds)) return true;
        if (!(entity instanceof Player) || movement == null) return false;

        double targetX = (doorBounds.minX + doorBounds.maxX) * 0.5D - entity.getX();
        double targetZ = (doorBounds.minZ + doorBounds.maxZ) * 0.5D - entity.getZ();
        double distanceSqr = targetX * targetX + targetZ * targetZ;
        if (distanceSqr > 2.25D) return false;

        double dot = movement.x * targetX + movement.z * targetZ;
        return dot > 0.0D && dot * dot >= horizontalSpeedSqr(movement) * distanceSqr * 0.5D;
    }

    private static Vec3 getPlayerMovement(Entity entity) {
        if (!(entity instanceof Player player)) return null;
        Vec3 current = entity.position();
        Vec3 previous = LAST_PLAYER_POSITIONS.put(player, current);
        return previous == null ? null : current.subtract(previous);
    }

    private static double horizontalSpeedSqr(Vec3 movement) {
        return movement.x * movement.x + movement.z * movement.z;
    }

    private static void openPetDoor(ServerLevel level, BlockPos pos, BlockState state, Entity entity, PetDoorBlock door) {
        if (!TDConfig.petAutomaticOpeningEnabled.get()) return;
        if (!TDConfig.shouldAutomaticallyClose(state.getValue(BlockStateProperties.POWERED))) return;
        if (entity instanceof Player player && state.getValue(PetDoorBlock.HALF) == Half.TOP
                && !player.isCrouching() && !player.isSwimming()
                && player.getBoundingBox().maxY > pos.getY() + 1.0D) {
            return;
        }

        if (!state.getValue(PetDoorBlock.OPEN)) {
            door.setOpen(null, level, state, pos, true);
        }
        if (TDConfig.petAutomaticClosingEnabled.get()) {
            level.scheduleTick(pos, door, TDConfig.automaticClosingDelay());
        }
    }
}
