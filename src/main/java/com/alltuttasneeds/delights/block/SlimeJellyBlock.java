package com.alltuttasneeds.delights.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.function.Supplier;

public class SlimeJellyBlock extends HalfTransparentBlock {
    public static final BooleanProperty SECOND_JELLY = BooleanProperty.create("second_jelly");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 3);

    protected static final VoxelShape[][] SHAPE_BY_BITE = new VoxelShape[][]{
            {
            Block.box(1, 0, 1, 15, 8, 15),
            Shapes.or(Block.box(8, 0, 1, 15, 8, 8), Block.box(1, 0, 8, 8, 8, 15), Block.box(1, 0, 1, 8, 8, 8)),
            Shapes.or(Block.box(8, 0, 1, 15, 8, 8), Block.box(1, 0, 1, 8, 8, 8)),
            Shapes.or(Block.box(8, 0, 1, 15, 8, 8))
    },
    {
            Block.box(1, 0, 1, 15, 8, 15),
            Shapes.or(Block.box(8, 0, 8, 15, 8, 15), Block.box(1, 0, 1, 8, 8, 8), Block.box(8, 0, 1, 15, 8, 8)),
            Shapes.or(Block.box(8, 0, 8, 15, 8, 15), Block.box(8, 0, 1, 15, 8, 8)),
            Shapes.or(Block.box(8, 0, 8, 15, 8, 15))
    },{
            Block.box(1, 0, 1, 15, 8, 15),
            Shapes.or(Block.box(1, 0, 8, 8, 8, 15), Block.box(8, 0, 1, 15, 8, 8), Block.box(8, 0, 8, 15, 8, 15)),
            Shapes.or(Block.box(1, 0, 8, 8, 8, 15), Block.box(8, 0, 8, 15, 8, 15)),
            Shapes.or(Block.box(1, 0, 8, 8, 8, 15))
    },{
            Block.box(1, 0, 1, 15, 8, 15),
            Shapes.or(Block.box(1, 0, 1, 8, 8, 8), Block.box(8, 0, 8, 15, 8, 15), Block.box(1, 0, 8, 8, 8, 15)),
            Shapes.or(Block.box(1, 0, 1, 8, 8, 8), Block.box(1, 0, 8, 8, 8, 15)),
            Shapes.or(Block.box(1, 0, 1, 8, 8, 8))
    }
    };

    protected static final VoxelShape[][] SECOND_SHAPE_BY_BITE = new VoxelShape[][]{
    {
            Block.box(1, 0, 1, 15, 16, 15),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(8, 8, 1, 15, 16, 8), Block.box(1, 8, 8, 8, 16, 15), Block.box(1, 8, 1, 8, 16, 8)),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(8, 8, 1, 15, 16, 8), Block.box(1, 8, 1, 8, 16, 8)),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(1, 8, 1, 8, 16, 8))
    },{
            Block.box(1, 0, 1, 15, 16, 15),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(8, 8, 8, 15, 16, 15), Block.box(1, 8, 1, 8, 16, 8), Block.box(8, 8, 1, 15, 16, 8)),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(8, 8, 8, 15, 16, 15), Block.box(8, 8, 1, 15, 16, 8)),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(8, 8, 1, 15, 16, 8))
    },{
            Block.box(1, 0, 1, 15, 16, 15),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(8, 8, 8, 15, 16, 15), Block.box(8, 8, 1, 15, 16, 8), Block.box(1, 8, 8, 8, 16, 15)),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(8, 8, 8, 15, 16, 15), Block.box(1, 8, 8, 8, 16, 15)),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(8, 8, 8, 15, 16, 15))
    },{
            Block.box(1, 0, 1, 15, 16, 15),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(1, 8, 1, 8, 16, 8), Block.box(8, 8, 8, 15, 16, 15), Block.box(1, 8, 8, 8, 16, 15)),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(1, 8, 1, 8, 16, 8), Block.box(1, 8, 8, 8, 16, 15)),
            Shapes.or(Block.box(1, 0, 1, 15, 8, 15), Block.box(1, 8, 8, 8, 16, 15))
    }
    };

    public final Supplier<Item> jellySlice;

    public SlimeJellyBlock(Properties properties, Supplier<Item> pieSlice) {
        super(properties);
        this.jellySlice = pieSlice;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BITES, 0)
                .setValue(SECOND_JELLY, false));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    public ItemStack getPieSliceItem() {
        return new ItemStack(this.jellySlice.get());
    }

    public int getMaxBites() {
        return 4;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean isSecondCake = state.getValue(SECOND_JELLY);
        int direction = state.getValue(FACING).get2DDataValue();
        int bites = state.getValue(BITES);
        if (isSecondCake) {
            return SECOND_SHAPE_BY_BITE[direction][bites];
        } else {
            return SHAPE_BY_BITE[direction][bites];
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    @Override
    public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockState blockAtPos = level.getBlockState(pos);
        if (Block.byItem(heldStack.getItem()) == blockAtPos.getBlock()) {
            return secondCake(level, pos, state, player);
        }

        if (ItemUtils.isKnife(player.getMainHandItem())) {
            return cutSlice(level, pos, state, player);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            if (consumeBite(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return consumeBite(level, pos, state, player);
    }

    protected ItemInteractionResult secondCake(Level level, BlockPos pos, BlockState state, Player player) {
        ItemStack heldStack = player.getMainHandItem();
        if (state.getValue(BITES) == 0 && !state.getValue(SECOND_JELLY)) {
            if (!player.isCreative()) {
                heldStack.shrink(1);
            }
            level.playSound(null, pos, SoundEvents.MAGMA_CUBE_SQUISH_SMALL, SoundSource.PLAYERS, 0.8F, 0.8F);
            level.setBlock(pos, state.setValue(SECOND_JELLY, true), 3);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    protected ItemInteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Player player) {
        int bites = state.getValue(BITES);
        if (bites < getMaxBites() - 1) {
            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
        } else if (state.getValue(SECOND_JELLY)){
            level.setBlock(pos, state.setValue(BITES, 0)
                    .setValue(SECOND_JELLY, false), 3);
        } else {
            level.removeBlock(pos, false);
        }

        Direction direction = player.getDirection().getOpposite();
        ItemUtils.spawnItemEntity(level, this.getPieSliceItem(), pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
                direction.getStepX() * 0.15, 0.05, direction.getStepZ() * 0.15);
        level.playSound(null, pos, SoundEvents.MAGMA_CUBE_SQUISH, SoundSource.PLAYERS, 0.8F, 0.8F);
        return ItemInteractionResult.SUCCESS;
    }

    protected InteractionResult consumeBite(Level level, BlockPos pos, BlockState state, Player playerIn) {
        if (!playerIn.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            ItemStack sliceStack = this.getPieSliceItem();
            FoodProperties sliceFood = sliceStack.getItem().getFoodProperties(sliceStack, playerIn);

            if (sliceFood != null) {
                playerIn.getFoodData().eat(sliceFood);
                for (FoodProperties.PossibleEffect effect : sliceFood.effects()) {
                    if (!level.isClientSide && effect != null && level.random.nextFloat() < effect.probability()) {
                        playerIn.addEffect(effect.effect());
                    }
                }
            }

            int bites = state.getValue(BITES);
            if (bites < getMaxBites() - 1) {
                level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            } else if (state.getValue(SECOND_JELLY)){
                level.setBlock(pos, state.setValue(BITES, 0)
                        .setValue(SECOND_JELLY, false), 3);
            } else {
                level.removeBlock(pos, false);
            }
            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity.isSuppressingBounce()) {
            super.fallOn(level, state, pos, entity, fallDistance);
        } else {
            entity.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
        }
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(level, entity);
        } else {
            this.bounceUp(entity);
        }
    }

    private void bounceUp(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y < 0.0) {
            double d0 = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        double d0 = Math.abs(entity.getDeltaMovement().y);
        if (d0 < 0.1 && !entity.isSteppingCarefully()) {
            double d1 = 0.4 + d0 * 0.2;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0, d1));
        }

        super.stepOn(level, pos, state, entity);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BITES, SECOND_JELLY);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return getMaxBites() - blockState.getValue(BITES);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }
}