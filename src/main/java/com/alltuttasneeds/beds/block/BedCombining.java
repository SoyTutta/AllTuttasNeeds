package com.alltuttasneeds.beds.block;

import com.alltuttasneeds.beds.BedTier;
import com.alltuttasneeds.beds.BedDecorationResolver;
import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.MattressFamily;
import com.alltuttasneeds.beds.MattressMaterial;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

final class BedCombining {
    private static final int PAIRED_UPDATE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_SUPPRESS_DROPS;

    private BedCombining() {}

    static void preserveHeadDropWhenBreakingFoot(Level level, BlockPos pos, BlockState state, Player player) {
        if (level.isClientSide || player.isCreative() || state.getValue(BedBlock.PART) != BedPart.FOOT) return;

        Direction facing = state.getValue(BedBlock.FACING);
        BlockPos headPos = pos.relative(facing);
        BlockState headState = level.getBlockState(headPos);
        if (!headState.is(state.getBlock())
                || !headState.hasProperty(BedBlock.FACING) || headState.getValue(BedBlock.FACING) != facing
                || !headState.hasProperty(BedBlock.PART) || headState.getValue(BedBlock.PART) != BedPart.HEAD) {
            return;
        }

        BlockEntity blockEntity = headState.hasBlockEntity() ? level.getBlockEntity(headPos) : null;
        Block.dropResources(headState, level, headPos, blockEntity, player, player.getMainHandItem());
        level.setBlock(headPos, Blocks.AIR.defaultBlockState(), PAIRED_UPDATE_FLAGS);
    }

    static boolean isOccupied(Level level, BlockPos pos, BlockState state) {
        if (state.hasProperty(BedBlock.OCCUPIED) && state.getValue(BedBlock.OCCUPIED)) return true;
        if (!state.hasProperty(BedBlock.FACING) || !state.hasProperty(BedBlock.PART)) return false;

        Direction facing = state.getValue(BedBlock.FACING);
        BlockPos otherPos = state.getValue(BedBlock.PART) == BedPart.HEAD
                ? pos.relative(facing.getOpposite())
                : pos.relative(facing);
        BlockState otherState = level.getBlockState(otherPos);
        return otherState.hasProperty(BedBlock.OCCUPIED) && otherState.getValue(BedBlock.OCCUPIED);
    }

    static void replaceBothParts(Level level, BlockPos clickedPos, BlockState clickedState, Block result, Player player, ItemStack usedStack) {
        if (level.isClientSide) return;

        Direction facing = clickedState.getValue(BedBlock.FACING);
        BedPart part = clickedState.getValue(BedBlock.PART);
        BlockPos headPos = part == BedPart.HEAD ? clickedPos : clickedPos.relative(facing);
        BlockPos footPos = part == BedPart.HEAD ? clickedPos.relative(facing.getOpposite()) : clickedPos;

        BlockState headState = level.getBlockState(headPos);
        BlockState footState = level.getBlockState(footPos);
        setPair(level,
                headPos, resultState(result, headState, facing, BedPart.HEAD),
                footPos, resultState(result, footState, facing, BedPart.FOOT));
        level.playSound(null, clickedPos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

        if (!player.getAbilities().instabuild) usedStack.shrink(1);
    }

    @Nullable
    static Block coverResult(Map<CoverMaterial, Supplier<Block>> coverResults, @Nullable CoverMaterial currentCover,
                             ItemStack usedStack) {
        CoverMaterial match = null;
        Block result = null;
        for (Map.Entry<CoverMaterial, Supplier<Block>> entry : coverResults.entrySet()) {
            CoverMaterial candidate = entry.getKey();
            if (!candidate.isDirectApplyEnabled() || !candidate.matches(usedStack)) continue;
            if (match != null) return null;
            match = candidate;
            if (candidate != currentCover) result = entry.getValue().get();
        }
        return result;
    }

    static boolean combineMattressOntoFrame(Level level, BlockPos mattressPos, BlockState mattressState, LooseMattressBlock mattress, Block result) {
        if (isOccupied(level, mattressPos, mattressState)) return false;

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

        setPair(level,
                mattressPos, Blocks.AIR.defaultBlockState(),
                otherMattressPos, Blocks.AIR.defaultBlockState());
        setPair(level,
                framePos, resultState(result, frameState, facing, part),
                otherFramePos, resultState(result, otherFrameState, facing, part == BedPart.HEAD ? BedPart.FOOT : BedPart.HEAD));
        level.playSound(null, mattressPos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
    }

    static boolean swapMattress(Level level, BlockPos pos, BlockState state, TieredBedBlock currentBed,
                                LooseMattressBlock replacementMattress, Player player, ItemStack usedStack) {
        if (replacementMattress.material().equals(currentBed.mattress())) return false;

        MattressFamily currentFamily = findFamily(currentBed.mattress());
        MattressFamily replacementFamily = findFamily(replacementMattress.material());
        if (currentFamily == null || replacementFamily == null) return false;

        Block result = replacementBed(replacementFamily, currentBed, replacementMattress);
        if (result == null) return false;

        replaceBothParts(level, pos, state, result, player, usedStack);
        if (!level.isClientSide && !player.getAbilities().instabuild) {
            Block.popResource(level, pos, new ItemStack(currentFamily.looseMattress().get()));
        }
        return true;
    }

    static boolean applyDecoration(Level level, BlockPos pos, BlockState state, TieredBedBlock currentBed,
                                   Player player, ItemStack usedStack) {
        MattressFamily family = findFamily(currentBed.mattress());
        if (family == null) return false;

        boolean includeCovers = (currentBed.tier() == BedTier.BASIC || currentBed.tier() == BedTier.LOW)
                && currentBed.blanketMaterial() == null;
        BedDecorationResolver.Match match = BedDecorationResolver.resolveUnique(
                family, usedStack, includeCovers, true);
        if (match == null || match.result() == currentBed) return false;

        Item previousBlanket = null;
        if (match.kind() == BedDecorationResolver.Kind.BLANKET) {
            previousBlanket = blanketItem(currentBed);
            if (currentBed.blanketMaterial() != null && previousBlanket == null) return false;
        }

        replaceBothParts(level, pos, state, match.result(), player, usedStack);
        if (!level.isClientSide && !player.getAbilities().instabuild && previousBlanket != null) {
            Block.popResource(level, pos, new ItemStack(previousBlanket));
        }
        return true;
    }

    @Nullable
    private static Block replacementBed(MattressFamily family, TieredBedBlock currentBed,
                                        LooseMattressBlock replacementMattress) {
        if (currentBed.blanketMaterial() != null) {
            DyeColor color = currentBed.color();
            if (color == null) return null;

            if (currentBed.tier() == BedTier.DELUXE) {
                return get(family.bedDeluxe().get(color));
            }

            Map<DyeColor, Supplier<Block>> colors = family.bedBlankets().get(currentBed.blanketMaterial());
            return colors == null ? null : get(colors.get(color));
        }

        if (replacementMattress.cover() != null) {
            return get(family.bedBasicCovers().get(replacementMattress.cover()));
        }
        return family.bedBare().get();
    }

    @Nullable
    private static MattressFamily findFamily(MattressMaterial material) {
        return BedCompatRegistry.loaded()
                .flatMap(compat -> compat.families().stream())
                .filter(family -> family.material().equals(material))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    private static Item blanketItem(TieredBedBlock bed) {
        DyeColor color = bed.color();
        if (color == null || bed.blanketMaterial() == null) return null;
        return bed.blanketMaterial().associatedItemFor(color, bed.tier());
    }

    @Nullable
    private static Block get(@Nullable Supplier<Block> block) {
        return block == null ? null : block.get();
    }

    private static boolean isMatchingFrame(BlockState state, Direction facing, BedPart part) {
        return state.getBlock() instanceof BedFrameBlock
                && state.hasProperty(BedBlock.FACING) && state.getValue(BedBlock.FACING) == facing
                && state.hasProperty(BedBlock.PART) && state.getValue(BedBlock.PART) == part;
    }

    private static void setPair(Level level, BlockPos firstPos, BlockState firstState,
                                BlockPos secondPos, BlockState secondState) {
        BlockState previousFirst = level.getBlockState(firstPos);
        BlockState previousSecond = level.getBlockState(secondPos);

        level.setBlock(firstPos, firstState, PAIRED_UPDATE_FLAGS);
        level.setBlock(secondPos, secondState, PAIRED_UPDATE_FLAGS);

        notifyPairChange(level, firstPos, previousFirst, firstState);
        notifyPairChange(level, secondPos, previousSecond, secondState);
    }

    private static void notifyPairChange(Level level, BlockPos pos, BlockState previous, BlockState current) {
        level.blockUpdated(pos, previous.getBlock());
        previous.updateIndirectNeighbourShapes(level, pos, Block.UPDATE_CLIENTS);
        current.updateNeighbourShapes(level, pos, Block.UPDATE_CLIENTS);
        current.updateIndirectNeighbourShapes(level, pos, Block.UPDATE_CLIENTS);
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
