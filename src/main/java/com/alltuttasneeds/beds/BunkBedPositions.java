package com.alltuttasneeds.beds;

import com.alltuttasneeds.beds.block.TuttaBedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class BunkBedPositions {
    private BunkBedPositions() {}

    public static List<BlockPos> collectStack(Level level, BlockPos topPos) {
        List<BlockPos> beds = new ArrayList<>();
        beds.add(topPos);
        BlockPos currentPos = topPos;
        BlockState current = level.getBlockState(currentPos);

        while (current.getBlock() instanceof TuttaBedBlock) {
            BlockPos belowPos = currentPos.below();
            BlockState below = level.getBlockState(belowPos);
            if (!isStackMatch(current, below)) break;
            beds.add(belowPos);
            currentPos = belowPos;
            current = below;
        }
        return beds;
    }

    public static Optional<Vec3> findStandUpPosition(EntityType<?> entityType, Level level, List<BlockPos> beds,
                                                      Direction fallbackFacing, float rotation) {
        for (int index = beds.size() - 1; index >= 0; index--) {
            BlockPos pos = beds.get(index);
            BlockState state = level.getBlockState(pos);
            Direction facing = state.hasProperty(BedBlock.FACING) ? state.getValue(BedBlock.FACING) : fallbackFacing;
            Optional<Vec3> standUp = BedBlock.findStandUpPosition(entityType, level, pos, facing, rotation);
            if (standUp.isPresent()) return standUp;
        }
        return Optional.empty();
    }

    private static boolean isStackMatch(BlockState upper, BlockState lower) {
        if (!(lower.getBlock() instanceof TuttaBedBlock)
                || !upper.hasProperty(BedBlock.FACING) || !upper.hasProperty(BedBlock.PART)
                || !lower.hasProperty(BedBlock.FACING) || !lower.hasProperty(BedBlock.PART)) {
            return false;
        }
        Direction upperFacing = upper.getValue(BedBlock.FACING);
        BedPart upperPart = upper.getValue(BedBlock.PART);
        Direction lowerFacing = lower.getValue(BedBlock.FACING);
        BedPart lowerPart = lower.getValue(BedBlock.PART);
        return lowerFacing == upperFacing && lowerPart == upperPart
                || lowerFacing == upperFacing.getOpposite() && lowerPart != upperPart;
    }
}
