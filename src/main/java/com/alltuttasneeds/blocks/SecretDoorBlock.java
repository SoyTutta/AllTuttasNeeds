package com.alltuttasneeds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;

public class SecretDoorBlock extends DoorBlock {

    public SecretDoorBlock(BlockSetType type, Properties properties) {
        super(type, properties);
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