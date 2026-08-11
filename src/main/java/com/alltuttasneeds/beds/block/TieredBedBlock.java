package com.alltuttasneeds.beds.block;

import com.alltuttasneeds.beds.BedTier;
import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.MattressMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public class TieredBedBlock extends AbstractTuttaBedBlock {
    private final MattressMaterial mattress;
    private final BedTier tier;
    @Nullable
    private final CoverMaterial basicCover;
    @Nullable
    private final BlanketMaterial blanketMaterial;
    @Nullable
    private final DyeColor color;
    private final Map<CoverMaterial, Supplier<Block>> coverResults;
    private final Map<BlanketMaterial, Map<DyeColor, Supplier<Block>>> blanketResults;

    public TieredBedBlock(MattressMaterial mattress, BedTier tier, @Nullable CoverMaterial basicCover,
                          @Nullable BlanketMaterial blanketMaterial, @Nullable DyeColor color,
                          Map<CoverMaterial, Supplier<Block>> coverResults,
                          Map<BlanketMaterial, Map<DyeColor, Supplier<Block>>> blanketResults,
                          Properties properties) {
        super(color != null ? color : DyeColor.WHITE, properties);
        this.mattress = mattress;
        this.tier = tier;
        this.basicCover = basicCover;
        this.blanketMaterial = blanketMaterial;
        this.color = color;
        this.coverResults = coverResults;
        this.blanketResults = blanketResults;
    }

    public MattressMaterial mattress() {
        return mattress;
    }

    @Override
    public BedTier tier() {
        return tier;
    }

    @Nullable
    public CoverMaterial basicCover() {
        return basicCover;
    }

    @Nullable
    public BlanketMaterial blanketMaterial() {
        return blanketMaterial;
    }

    @Nullable
    public DyeColor color() {
        return color;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (BedCombining.isOccupied(level, pos, state)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof LooseMattressBlock replacementMattress
                && BedCombining.swapMattress(level, pos, state, this, replacementMattress, player, stack)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (BedCombining.applyDecoration(level, pos, state, this, player, stack)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
