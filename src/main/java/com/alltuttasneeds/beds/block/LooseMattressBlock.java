package com.alltuttasneeds.beds.block;

import com.alltuttasneeds.beds.BedTier;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.MattressMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public class LooseMattressBlock extends BedBlock {
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D);

    private final MattressMaterial material;
    @Nullable
    private final CoverMaterial cover;
    private final Map<CoverMaterial, Supplier<Block>> coverResults;
    private final Supplier<Block> frameResult;

    public LooseMattressBlock(MattressMaterial material, @Nullable CoverMaterial cover,
                               Map<CoverMaterial, Supplier<Block>> coverResults, Supplier<Block> frameResult,
                               Properties properties) {
        super(DyeColor.WHITE, properties);
        this.material = material;
        this.cover = cover;
        this.coverResults = coverResults;
        this.frameResult = frameResult;
    }

    public MattressMaterial material() {
        return material;
    }

    @Nullable
    public CoverMaterial cover() {
        return cover;
    }

    public Supplier<Block> frameResult() {
        return frameResult;
    }

    public BedTier tier() {
        return cover == null ? BedTier.BASIC : BedTier.LOW;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return super.canSurvive(state, level, pos) && !(level.getBlockState(pos.below()).getBlock() instanceof LooseMattressBlock);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos().below()).getBlock() instanceof LooseMattressBlock) {
            return null;
        }
        return super.getStateForPlacement(context);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (cover != null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        CoverMaterial usedCover = null;
        for (Map.Entry<CoverMaterial, Supplier<Block>> entry : coverResults.entrySet()) {
            CoverMaterial candidate = entry.getKey();
            if (!candidate.isDirectApplyEnabled()) continue;
            Item ingredient = candidate.ingredient();
            if (ingredient != null && stack.is(ingredient)) {
                usedCover = candidate;
                break;
            }
        }
        if (usedCover == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        BedCombining.replaceBothParts(level, pos, state, coverResults.get(usedCover).get(), player, stack);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide
                && BedCombining.combineMattressOntoFrame(level, pos, state, this, frameResult.get())) {
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hit);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.hasProperty(FACING)) return state;
        Direction facing = state.getValue(FACING);
        BedPart part = state.getValue(PART);
        Direction pairDirection = part == BedPart.HEAD ? facing.getOpposite() : facing;

        if (direction == pairDirection) {
            boolean validPair = neighborState.getBlock() instanceof LooseMattressBlock
                    && neighborState.hasProperty(FACING) && neighborState.getValue(FACING) == facing
                    && neighborState.hasProperty(PART) && neighborState.getValue(PART) != part;
            return validPair ? state.setValue(OCCUPIED, neighborState.getValue(OCCUPIED)) : Blocks.AIR.defaultBlockState();
        }
        return state;
    }
}
