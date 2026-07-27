package com.alltuttasneeds.delights.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.function.Supplier;

public class MintRouletteBlock extends FeastBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    protected static final VoxelShape[] FEAST_SHAPES= new VoxelShape[]{
            Block.box(4, 0, 4, 12, 3, 12),
            Block.box(4, 0, 4, 12, 5, 12),
            Block.box(4, 0, 4, 12, 5, 12),
            Block.box(4, 0, 4, 12, 7, 12),
            Block.box(4, 0, 4, 12, 9, 12)
};

    public MintRouletteBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
        super(properties, servingItem, hasLeftovers);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(getServingsProperty(), getMaxServings()).setValue(ACTIVE,true));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return FEAST_SHAPES[state.getValue(SERVINGS)];
    }

    @Override
    protected ItemInteractionResult takeServing(LevelAccessor level, BlockPos pos, BlockState state, Player player, InteractionHand hand) {
        int servings = state.getValue(getServingsProperty());

        ItemStack serving = this.getServingItem(state);
        ItemStack heldStack = player.getItemInHand(hand);

       if (state.getValue(ACTIVE)) {
            if (ItemUtils.isKnife(player.getMainHandItem()) || heldStack.is(net.neoforged.neoforge.common.Tags.Items.TOOLS_SHEAR)) {
                level.setBlock(pos, state.setValue(ACTIVE, false), 3);
                level.playSound(null, pos, SoundEvents.BOGGED_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
       }
       if (state.getValue(ACTIVE) && servings > 0) {
            if (!serving.hasCraftingRemainingItem() || ItemStack.isSameItem(heldStack, serving.getCraftingRemainingItem())) {
                if (level instanceof ServerLevel serverLevel) {
                    if (serverLevel.random.nextFloat() < (0.10 + (0.10f * servings))) {
                        Creeper creeper = EntityType.CREEPER.create(serverLevel);
                        if (creeper != null) {
                            serverLevel.explode(null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 2.0F, false, Level.ExplosionInteraction.NONE);
                            player.hurt(serverLevel.damageSources().mobAttack(creeper), 20.0F);

                            creeper.remove(Entity.RemovalReason.DISCARDED);
                            level.destroyBlock(pos, false);
                        }
                        level.playSound(null, pos, SoundEvents.CREEPER_DEATH, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }
        }

        return super.takeServing(level, pos, state, player, hand);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }
}