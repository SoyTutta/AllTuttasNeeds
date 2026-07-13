package com.alltuttasneeds.beds.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

final class BedCombining {
    private BedCombining() {}

    static void replaceBothParts(Level level, BlockPos clickedPos, BlockState clickedState, Block result, Player player, ItemStack usedStack) {
        Direction facing = clickedState.getValue(BedBlock.FACING);
        BedPart part = clickedState.getValue(BedBlock.PART);
        BlockPos headPos = part == BedPart.HEAD ? clickedPos : clickedPos.relative(facing);
        BlockPos footPos = part == BedPart.HEAD ? clickedPos.relative(facing.getOpposite()) : clickedPos;

        level.setBlock(headPos, resultState(result, level.getBlockState(headPos), facing, BedPart.HEAD), Block.UPDATE_ALL);
        level.setBlock(footPos, resultState(result, level.getBlockState(footPos), facing, BedPart.FOOT), Block.UPDATE_ALL);
        level.playSound(null, clickedPos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

        if (!player.getAbilities().instabuild) usedStack.shrink(1);
    }

    static boolean combineMattressOntoFrame(Level level, BlockPos mattressPos, BlockState mattressState, LooseMattressBlock mattress, Block result) {
        Direction facing = mattressState.getValue(BedBlock.FACING);
        BedPart part = mattressState.getValue(BedBlock.PART);
        BlockPos otherMattressPos = part == BedPart.HEAD ? mattressPos.relative(facing.getOpposite()) : mattressPos.relative(facing);
        BlockState otherMattressState = level.getBlockState(otherMattressPos);

        if (!(otherMattressState.getBlock() instanceof LooseMattressBlock otherMattress)) return false;
        if (otherMattress.material() != mattress.material() || otherMattress.cover() != mattress.cover()) return false;
        if (!otherMattressState.hasProperty(BedBlock.FACING) || otherMattressState.getValue(BedBlock.FACING) != facing) return false;

        BlockPos framePos = mattressPos.below();
        BlockPos otherFramePos = otherMattressPos.below();
        BlockState frameState = level.getBlockState(framePos);
        BlockState otherFrameState = level.getBlockState(otherFramePos);

        if (!isMatchingFrame(frameState, facing, part) || !isMatchingFrame(otherFrameState, facing, part == BedPart.HEAD ? BedPart.FOOT : BedPart.HEAD)) {
            return false;
        }

        level.setBlock(mattressPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        level.setBlock(otherMattressPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        level.setBlock(framePos, resultState(result, frameState, facing, part), Block.UPDATE_ALL);
        level.setBlock(otherFramePos, resultState(result, otherFrameState, facing, part == BedPart.HEAD ? BedPart.FOOT : BedPart.HEAD), Block.UPDATE_ALL);
        level.playSound(null, mattressPos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
    }

    private static boolean isMatchingFrame(BlockState state, Direction facing, BedPart part) {
        return state.getBlock() instanceof BedFrameBlock
                && state.hasProperty(BedBlock.FACING) && state.getValue(BedBlock.FACING) == facing
                && state.hasProperty(BedBlock.PART) && state.getValue(BedBlock.PART) == part;
    }

    private static BlockState resultState(Block result, BlockState previous, Direction facing, BedPart part) {
        BlockState state = result.defaultBlockState().setValue(BedBlock.FACING, facing).setValue(BedBlock.PART, part);
        if (state.hasProperty(BedStateProperties.BED_POSITION) && previous.hasProperty(BedStateProperties.BED_POSITION)) {
            state = state.setValue(BedStateProperties.BED_POSITION, previous.getValue(BedStateProperties.BED_POSITION));
        }
        if (state.hasProperty(BedStateProperties.BUNK) && previous.hasProperty(BedStateProperties.BUNK)) {
            state = state.setValue(BedStateProperties.BUNK, previous.getValue(BedStateProperties.BUNK));
        }
        return state;
    }
}
