package com.alltuttasneeds.doors.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class SecretDoorBlock extends DoorBlock {

    private final Supplier<Block> disguise;

    public SecretDoorBlock(BlockSetType type, Properties properties, Supplier<Block> disguise) {
        super(type, properties);
        this.disguise = disguise;
    }

    public Block getDisguiseBlock() {
        return disguise.get();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        Direction doorFacing = state.getValue(FACING);
        Direction hitFace = hitResult.getDirection();

        if (!state.getValue(OPEN) && hitFace != doorFacing) {
            return InteractionResult.FAIL;
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}