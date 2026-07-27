package com.alltuttasneeds.delights.block;

import com.alltuttasneeds.delights.block.entity.SnowGolemFeastBlockEntity;
import com.alltuttasneeds.delights.DelightsTextUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SnowGolemFeastBlock extends PieBlock implements EntityBlock {

    public static final BooleanProperty PUMPKIN = BooleanProperty.create("pumpkin");
    private final Supplier<Item> IceCreamContainer;

    public SnowGolemFeastBlock(Properties properties, Supplier<Item> pieSlice,
                               Supplier<Item> IceCreamContainer) {
        super(properties, pieSlice);
        this.IceCreamContainer = IceCreamContainer;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(BITES, 0)
                .setValue(PUMPKIN, true));
    }

    protected static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D)
    };

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
                               CollisionContext context) {
        return SHAPES[state.getValue(BITES)];
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SnowGolemFeastBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof SnowGolemFeastBlockEntity be) {
                net.minecraft.network.chat.Component customName = stack.get(DataComponents.CUSTOM_NAME);
                if (customName != null) {
                    be.setCustomName(customName);
                }
                be.ensureFaceAssigned();
            }
        }
    }

    public ItemStack getIceContainerItem(BlockState state) {
        return new ItemStack(this.IceCreamContainer.get());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level,
                                           BlockPos pos, Player player, InteractionHand hand,
                                           BlockHitResult hit) {
        if (heldStack.is(Items.NAME_TAG) && heldStack.has(DataComponents.CUSTOM_NAME)) {
            if (!level.isClientSide
                    && level.getBlockEntity(pos) instanceof SnowGolemFeastBlockEntity be) {
                be.setCustomName(heldStack.get(DataComponents.CUSTOM_NAME));
                if (!player.getAbilities().instabuild) {
                    heldStack.shrink(1);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (ItemUtils.isKnife(player.getMainHandItem()) && state.getValue(PUMPKIN)) {
            return this.cutSlice(level, pos, state, player, heldStack.getItem());
        }
        if (heldStack.is(net.neoforged.neoforge.common.Tags.Items.TOOLS_SHEAR)
                && state.getValue(PUMPKIN)) {
            return this.chopPumpkin(level, pos, state, player);
        }
        if (heldStack.getItem() == this.IceCreamContainer.get() && !state.getValue(PUMPKIN)) {
            return this.cutSlice(level, pos, state, player, heldStack.getItem());
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        ItemStack iceCreamContainer = this.getIceContainerItem(state);
        ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (state.getValue(PUMPKIN)) {
            if (level.isClientSide) {
                if (this.consumeBite(level, pos, state, player).consumesAction()) {
                    return InteractionResult.SUCCESS;
                }
                if (heldStack.isEmpty()) {
                    return InteractionResult.CONSUME;
                }
            }
            return this.consumeBite(level, pos, state, player);
        } else if (heldStack.getItem() == this.IceCreamContainer.get()) {
            if (this.consumeBitePumpkin(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }
        } else {
            player.displayClientMessage(DelightsTextUtils.block(
                    "snowgolemfeast.use_container",
                    iceCreamContainer.getHoverName()), true);
        }
        return InteractionResult.FAIL;
    }

    protected InteractionResult consumeBitePumpkin(Level level, BlockPos pos,
                                                   BlockState state, Player playerIn) {
        ItemStack heldStack = playerIn.getItemInHand(InteractionHand.MAIN_HAND);
        if (!playerIn.canEat(false)) {
            return InteractionResult.PASS;
        }
        if (heldStack.getItem() == this.IceCreamContainer.get()) {
            heldStack.shrink(1);
        }
        ItemStack sliceStack = this.getPieSliceItem();
        FoodProperties sliceFood = sliceStack.getItem().getFoodProperties(sliceStack, playerIn);
        if (sliceFood != null) {
            playerIn.getFoodData().eat(sliceFood);
            for (FoodProperties.PossibleEffect effect : sliceFood.effects()) {
                if (!level.isClientSide && effect != null
                        && level.random.nextFloat() < effect.probability()) {
                    playerIn.addEffect(effect.effect());
                }
            }
        }
        int bites = state.getValue(BITES);
        if (bites < this.getMaxBites() - 1) {
            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
        } else {
            level.removeBlock(pos, false);
        }
        level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    protected ItemInteractionResult chopPumpkin(Level level, BlockPos pos,
                                                BlockState state, Player player) {
        int bites = this.getMaxBites() - state.getValue(BITES);
        if (state.getValue(PUMPKIN)) {
            level.setBlock(pos, state.setValue(PUMPKIN, false), 3);
            Direction direction = player.getDirection().getOpposite();
            for (int i = 0; i < bites; i++) {
                ItemUtils.spawnItemEntity(level, ModItems.PUMPKIN_SLICE.get().getDefaultInstance(),
                        pos.getX() + 0.5F, pos.getY() + 0.3, pos.getZ() + 0.5F,
                        direction.getStepX() * 0.15, 0.05, direction.getStepZ() * 0.15);
            }
            level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.PLAYERS, 0.8F, 0.8F);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PUMPKIN);
    }
}
