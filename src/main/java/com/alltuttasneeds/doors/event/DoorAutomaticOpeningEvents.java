package com.alltuttasneeds.doors.event;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.doors.block.PetDoorBlock;
import com.alltuttasneeds.doors.block.TransitDoorBlock;
import com.alltuttasneeds.doors.config.TDConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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
        if (!(entity instanceof Player player)
                || !(entity.level() instanceof ServerLevel level)
                || entity.isSpectator()) return;
        if (!TDConfig.transitAutomaticOpeningEnabled.get() && !TDConfig.petAutomaticOpeningEnabled.get()) return;

        AABB contactBounds = entity.getBoundingBox().inflate(0.1D);
        Vec3 movement = getPlayerMovement(player);
        if (movement == null || horizontalSpeedSqr(movement) <= 1.0E-4D) return;
        AABB bounds = contactBounds.inflate(1.0D, 0.0D, 1.0D);
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
                        if (isContactingOrApproaching(player, movement, contactBounds,
                                new AABB(doorPos.getX(), doorPos.getY(), doorPos.getZ(),
                                        doorPos.getX() + 1.0D, doorPos.getY() + 2.0D, doorPos.getZ() + 1.0D))) {
                            transitDoor.tryOpenAutomatically(level, doorPos, level.getBlockState(doorPos), player);
                        }
                    } else if (state.getBlock() instanceof PetDoorBlock petDoor) {
                        BlockPos doorPos = cursor.immutable();
                        if (isContactingOrApproaching(player, movement, contactBounds, new AABB(doorPos))) {
                            petDoor.tryOpenAutomatically(level, doorPos, state, player);
                        }
                    }
                }
            }
        }
    }

    private static boolean isContactingOrApproaching(Player player, Vec3 movement, AABB contactBounds, AABB doorBounds) {
        if (contactBounds.intersects(doorBounds)) return true;

        double targetX = (doorBounds.minX + doorBounds.maxX) * 0.5D - player.getX();
        double targetZ = (doorBounds.minZ + doorBounds.maxZ) * 0.5D - player.getZ();
        double distanceSqr = targetX * targetX + targetZ * targetZ;
        if (distanceSqr > 2.25D) return false;

        double dot = movement.x * targetX + movement.z * targetZ;
        return dot > 0.0D && dot * dot >= horizontalSpeedSqr(movement) * distanceSqr * 0.5D;
    }

    private static Vec3 getPlayerMovement(Player player) {
        Vec3 current = player.position();
        Vec3 previous = LAST_PLAYER_POSITIONS.put(player, current);
        return previous == null ? null : current.subtract(previous);
    }

    private static double horizontalSpeedSqr(Vec3 movement) {
        return movement.x * movement.x + movement.z * movement.z;
    }
}
