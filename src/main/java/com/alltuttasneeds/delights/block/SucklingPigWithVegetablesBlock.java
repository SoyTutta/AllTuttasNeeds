package com.alltuttasneeds.delights.block;

import com.alltuttasneeds.delights.DelightsTextUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.DoubleBlockCombiner.BlockType;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SucklingPigWithVegetablesBlock extends ThreefoldFeastBlock{
    public static final IntegerProperty EXTRASERVINGS = IntegerProperty.create("servings", 0, 5);
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    protected static final VoxelShape PLATE_SHAPE_NORTH = Block.box(1, 0, 0, 15, 2, 15);
    protected static final VoxelShape PLATE_SHAPE_SOUTH = Block.box(1, 0, 1, 15, 2, 16);
    protected static final VoxelShape PLATE_SHAPE_WEST = Block.box(0, 0, 1, 15, 2, 15);
    protected static final VoxelShape PLATE_SHAPE_EAST = Block.box(1, 0, 1, 16, 2, 15);
    protected static final VoxelShape[] TOP_SHAPES_NORTH = new VoxelShape[]{
            Block.box(0.0F, 0.0F, 16.0F, 0.0F, 0.0F, 16.0F),
            Block.box(0.0F, 0.0F, 16.0F, 0.0F, 0.0F, 16.0F),
            Block.box(0.0F, 0.0F, 16.0F, 0.0F, 0.0F, 16.0F),
            Shapes.or(Block.box(8.0F, 2.0F, 0.0F, 13.0F, 10.0F, 6.0F), Block.box(10.0F, 2.0F, 4.0F, 14.0F, 6.0F, 10.0F)),
            Shapes.or(Block.box(3.0F, 2.0F, 0.0F, 13.0F, 10.0F, 6.0F), Block.box(10.0F, 2.0F, 4.0F, 14.0F, 6.0F, 10.0F), Block.box(2.0F, 2.0F, 4.0F, 6.0F, 6.0F, 10.0F)),
            Shapes.or(Block.box(3.0F, 2.0F, 0.0F, 13.0F, 10.0F, 6.0F), Block.box(10.0F, 2.0F, 4.0F, 14.0F, 6.0F, 10.0F), Block.box(2.0F, 2.0F, 4.0F, 6.0F, 6.0F, 10.0F), Block.box(6.0F, 5.0F, 13.0F, 10.0F, 8.0F, 14.0F), Block.box(4.0F, 3.0F, 5.0F, 12.0F, 11.0F, 13.0F)),
            Shapes.or(Block.box(3.0F, 2.0F, 0.0F, 13.0F, 10.0F, 6.0F), Block.box(10.0F, 2.0F, 4.0F, 14.0F, 6.0F, 10.0F), Block.box(2.0F, 2.0F, 4.0F, 6.0F, 6.0F, 10.0F), Block.box(6.0F, 5.0F, 13.0F, 10.0F, 8.0F, 14.0F), Block.box(4.0F, 3.0F, 5.0F, 12.0F, 11.0F, 13.0F))
    };
    protected static final VoxelShape[] BOTTOM_SHAPES_NORTH = new VoxelShape[]{
            Block.box(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
            Shapes.or(Block.box(3, 2, 6, 8, 10, 15), Block.box(2, 2, 3, 6, 6, 9)),
            Shapes.or(Block.box(3, 2, 6, 13, 10, 15), Block.box(10, 2, 3, 14, 6, 9), Block.box(2, 2, 3, 6, 6, 9)),
            Shapes.or(Block.box(8, 2, 15, 13, 10, 16), Block.box(3, 2, 6, 13, 10, 15), Block.box(10, 2, 3, 14, 6, 9), Block.box(2, 2, 3, 6, 6, 9)),
            Shapes.or(Block.box(3.0F, 2.0F, 10.0F, 13.0F, 10.0F, 16.0F), Block.box(2.0F, 2.0F, 6.0F, 6.0F, 6.0F, 12.0F), Block.box(10.0F, 2.0F, 6.0F, 14.0F, 6.0F, 12.0F)),
            Shapes.or(Block.box(3, 2, 6, 13, 10, 16), Block.box(10, 2, 3, 14, 6, 9), Block.box(2, 2, 3, 6, 6, 9)),
            Shapes.or(Block.box(3, 2, 6, 13, 10, 16), Block.box(10, 2, 3, 14, 6, 9), Block.box(2, 2, 3, 6, 6, 9))
    };
    protected static final VoxelShape[] TOP_SHAPES_SOUTH = new VoxelShape[]{
            Block.box(16.0F, 0.0F, 0.0F, 16.0F, 0.0F, 0.0F),
            Block.box(16.0F, 0.0F, 0.0F, 16.0F, 0.0F, 0.0F),
            Block.box(16.0F, 0.0F, 0.0F, 16.0F, 0.0F, 0.0F),
            Shapes.or(Block.box(3.0F, 2.0F, 10.0F, 8.0F, 10.0F, 16.0F), Block.box(2.0F, 2.0F, 6.0F, 6.0F, 6.0F, 12.0F)),
            Shapes.or(Block.box(3.0F, 2.0F, 10.0F, 13.0F, 10.0F, 16.0F), Block.box(2.0F, 2.0F, 6.0F, 6.0F, 6.0F, 12.0F), Block.box(10.0F, 2.0F, 6.0F, 14.0F, 6.0F, 12.0F)),
            Shapes.or(Block.box(3.0F, 2.0F, 10.0F, 13.0F, 10.0F, 16.0F), Block.box(2.0F, 2.0F, 6.0F, 6.0F, 6.0F, 12.0F), Block.box(10.0F, 2.0F, 6.0F, 14.0F, 6.0F, 12.0F), Block.box(6.0F, 5.0F, 2.0F, 10.0F, 8.0F, 3.0F), Block.box(4.0F, 3.0F, 3.0F, 12.0F, 11.0F, 11.0F)),
            Shapes.or(Block.box(3.0F, 2.0F, 10.0F, 13.0F, 10.0F, 16.0F), Block.box(2.0F, 2.0F, 6.0F, 6.0F, 6.0F, 12.0F), Block.box(10.0F, 2.0F, 6.0F, 14.0F, 6.0F, 12.0F), Block.box(6.0F, 5.0F, 2.0F, 10.0F, 8.0F, 3.0F), Block.box(4.0F, 3.0F, 3.0F, 12.0F, 11.0F, 11.0F))
    };
    protected static final VoxelShape[] BOTTOM_SHAPES_SOUTH = new VoxelShape[]{
            Block.box(0.0F, 0.0F, 16.0F, 0.0F, 0.0F, 16.0F),
            Shapes.or(Block.box(8, 2, 1, 13, 10, 10), Block.box(10, 2, 7, 14, 6, 13)),
            Shapes.or(Block.box(3, 2, 1, 13, 10, 10), Block.box(2, 2, 7, 6, 6, 13), Block.box(10, 2, 7, 14, 6, 13)),
            Shapes.or(Block.box(3, 2, 0, 8, 10, 1), Block.box(3, 2, 1, 13, 10, 10), Block.box(2, 2, 7, 6, 6, 13), Block.box(10, 2, 7, 14, 6, 13)),
            Shapes.or(Block.box(3.0F, 2.0F, 0.0F, 13.0F, 10.0F, 6.0F), Block.box(10.0F, 2.0F, 4.0F, 14.0F, 6.0F, 10.0F), Block.box(2.0F, 2.0F, 4.0F, 6.0F, 6.0F, 10.0F)),
            Shapes.or(Block.box(3, 2, 0, 13, 10, 10), Block.box(2, 2, 7, 6, 6, 13), Block.box(10, 2, 7, 14, 6, 13)),
            Shapes.or(Block.box(3, 2, 0, 13, 10, 10), Block.box(2, 2, 7, 6, 6, 13), Block.box(10, 2, 7, 14, 6, 13))
    };
    protected static final VoxelShape[] TOP_SHAPES_EAST = new VoxelShape[]{
            Block.box(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
            Block.box(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),

            Block.box(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
            Shapes.or(Block.box(10, 2, 8, 16, 10, 13), Block.box(6, 2, 10, 12, 6, 14)),

            Shapes.or(Block.box(10.0F, 2.0F, 3.0F, 16.0F, 10.0F, 13.0F), Block.box(6.0F, 2.0F, 2.0F, 12.0F, 6.0F, 6.0F), Block.box(6.0F, 2.0F, 10.0F, 12.0F, 6.0F, 14.0F)),
            Shapes.or(Block.box(10.0F, 2.0F, 3.0F, 16.0F, 10.0F, 13.0F), Block.box(6.0F, 2.0F, 2.0F, 12.0F, 6.0F, 6.0F), Block.box(6.0F, 2.0F, 10.0F, 12.0F, 6.0F, 14.0F), Block.box(2.0F, 5.0F, 6.0F, 3.0F, 8.0F, 10.0F), Block.box(3.0F, 3.0F, 4.0F, 11.0F, 11.0F, 12.0F)),
            Shapes.or(Block.box(10.0F, 2.0F, 3.0F, 16.0F, 10.0F, 13.0F), Block.box(6.0F, 2.0F, 2.0F, 12.0F, 6.0F, 6.0F), Block.box(6.0F, 2.0F, 10.0F, 12.0F, 6.0F, 14.0F), Block.box(2.0F, 5.0F, 6.0F, 3.0F, 8.0F, 10.0F), Block.box(3.0F, 3.0F, 4.0F, 11.0F, 11.0F, 12.0F))
    };
    protected static final VoxelShape[] BOTTOM_SHAPES_EAST = new VoxelShape[]{
            Block.box(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
            Shapes.or(Block.box(1, 2, 3, 10, 10, 8), Block.box(7, 2, 2, 13, 6, 6)),
            Shapes.or(Block.box(1, 2, 3, 10, 10, 13), Block.box(7, 2, 10, 13, 6, 14), Block.box(7, 2, 2, 13, 6, 6)),
            Shapes.or(Block.box(1, 2, 3, 10, 10, 13), Block.box(0, 2, 8, 1, 10, 13), Block.box(7, 2, 10, 13, 6, 14), Block.box(7, 2, 2, 13, 6, 6)),
            Shapes.or(Block.box(0, 2, 3, 10, 10, 13), Block.box(7, 2, 2, 13, 6, 6), Block.box(7, 2, 10, 13, 6, 14)),
            Shapes.or(Block.box(0, 2, 3, 10, 10, 13), Block.box(7, 2, 2, 13, 6, 6), Block.box(7, 2, 10, 13, 6, 14)),
            Shapes.or(Block.box(0, 2, 3, 10, 10, 13), Block.box(7, 2, 2, 13, 6, 6), Block.box(7, 2, 10, 13, 6, 14))
    };
    protected static final VoxelShape[] TOP_SHAPES_WEST = new VoxelShape[]{
            Block.box(16.0F, 0.0F, 16.0F, 16.0F, 0.0F, 16.0F),
            Block.box(16.0F, 0.0F, 16.0F, 16.0F, 0.0F, 16.0F),
            Block.box(16.0F, 0.0F, 16.0F, 16.0F, 0.0F, 16.0F),
            Shapes.or(Block.box(0.0F, 2.0F, 3.0F, 6.0F, 10.0F, 8.0F), Block.box(4.0F, 2.0F, 2.0F, 8.0F, 6.0F, 6.0F)),
            Shapes.or(Block.box(0.0F, 2.0F, 3.0F, 6.0F, 10.0F, 13.0F), Block.box(4.0F, 2.0F, 2.0F, 8.0F, 6.0F, 6.0F), Block.box(4.0F, 2.0F, 10.0F, 8.0F, 6.0F, 14.0F)),
            Shapes.or(Block.box(0.0F, 2.0F, 3.0F, 6.0F, 10.0F, 13.0F), Block.box(4.0F, 2.0F, 2.0F, 8.0F, 6.0F, 6.0F), Block.box(4.0F, 2.0F, 10.0F, 8.0F, 6.0F, 14.0F), Block.box(13.0F, 5.0F, 6.0F, 14.0F, 8.0F, 10.0F), Block.box(5.0F, 3.0F, 4.0F, 13.0F, 11.0F, 12.0F))
    };
    protected static final VoxelShape[] BOTTOM_SHAPES_WEST = new VoxelShape[]{
            Block.box(16.0F, 0.0F, 0.0F, 16.0F, 0.0F, 0.0F),
            Shapes.or(Block.box(3, 2, 10, 9, 6, 14), Block.box(6, 2, 8, 15, 10, 13)),
            Shapes.or(Block.box(6, 2, 3, 15, 10, 13), Block.box(3, 2, 10, 9, 6, 14), Block.box(3, 2, 2, 9, 6, 6)),
            Shapes.or(Block.box(3, 2, 10, 9, 6, 14), Block.box(3, 2, 2, 9, 6, 6), Block.box(6, 2, 3, 15, 10, 13), Block.box(15, 2, 3, 16, 10, 8)),
            Shapes.or(Block.box(6, 2, 3, 16, 10, 13), Block.box(3, 2, 2, 9, 6, 6), Block.box(3, 2, 10, 9, 6, 14)),
            Shapes.or(Block.box(6, 2, 3, 16, 10, 13), Block.box(3, 2, 2, 9, 6, 6), Block.box(3, 2, 10, 9, 6, 14)),
            Shapes.or(Block.box(6, 2, 3, 16, 10, 13), Block.box(3, 2, 2, 9, 6, 6), Block.box(3, 2, 10, 9, 6, 14))
    };

    public SucklingPigWithVegetablesBlock(Properties properties, Supplier<Item> altServingOneItem, int altServingOneCount, Supplier<Item> altServingTwoItem, int altServingTwoCount, Supplier<Item> servingItem, boolean hasLeftovers) {
        super(properties, altServingOneItem, altServingOneCount, altServingTwoItem, altServingTwoCount, servingItem, hasLeftovers);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.HEAD).setValue(FACING, Direction.NORTH).setValue(getServingsProperty(), getMaxServings()));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        BedPart part = state.getValue(PART);
        int servings = state.getValue(EXTRASERVINGS);

        VoxelShape topShapes = switch (direction) {
            case NORTH -> Shapes.or(PLATE_SHAPE_NORTH, TOP_SHAPES_NORTH[servings]);
            case SOUTH -> Shapes.or(PLATE_SHAPE_SOUTH, TOP_SHAPES_SOUTH[servings]);
            case EAST -> Shapes.or(PLATE_SHAPE_EAST, TOP_SHAPES_EAST[servings]);
            case WEST -> Shapes.or(PLATE_SHAPE_WEST, TOP_SHAPES_WEST[servings]);
            default -> Shapes.or(PLATE_SHAPE_NORTH, TOP_SHAPES_NORTH[servings]);
        };
        VoxelShape bottomShapes = switch (direction) {
            case NORTH -> Shapes.or(PLATE_SHAPE_SOUTH, BOTTOM_SHAPES_NORTH[servings]);
            case SOUTH -> Shapes.or(PLATE_SHAPE_NORTH, BOTTOM_SHAPES_SOUTH[servings]);
            case EAST -> Shapes.or(PLATE_SHAPE_WEST, BOTTOM_SHAPES_EAST[servings]);
            case WEST -> Shapes.or(PLATE_SHAPE_EAST, BOTTOM_SHAPES_WEST[servings]);
            default -> Shapes.or(PLATE_SHAPE_SOUTH, BOTTOM_SHAPES_NORTH[servings]);
        };
        return part == BedPart.HEAD ? topShapes : bottomShapes;
    }

    private static Direction getDirectionToOther(BedPart part, Direction direction) {
        return part == BedPart.HEAD ? direction : direction.getOpposite();
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing != getDirectionToOther(stateIn.getValue(PART), stateIn.getValue(FACING))) {
            return !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        } else {
            return stateIn.canSurvive(worldIn, currentPos) && facingState.is(this) && facingState.getValue(PART) != stateIn.getValue(PART) ? stateIn : Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            BedPart bedpart = state.getValue(PART);

            if (bedpart == BedPart.FOOT) {
                BlockPos blockpos = pos.relative(getDirectionToOther(bedpart, state.getValue(FACING)));
                BlockState blockstate = level.getBlockState(blockpos);

                if (blockstate.is(this) && blockstate.getValue(PART) == BedPart.HEAD) {
                    level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }

        super.playerWillDestroy(level, pos, state, player);
        return state;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        BlockPos clickedPos = context.getClickedPos();
        BlockPos adjacentPos = clickedPos.relative(direction);
        Level level = context.getLevel();

        if (level.getBlockState(adjacentPos).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(adjacentPos)) {
            if (canSurvive(level.getBlockState(adjacentPos), level, adjacentPos) &&
                    canSurvive(this.defaultBlockState(), level, clickedPos)) {
                return this.defaultBlockState().setValue(FACING, direction);
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isClientSide) {
            BlockPos facingPos = pos.relative(state.getValue(FACING));
            worldIn.setBlock(facingPos, state.setValue(PART, BedPart.FOOT), 3);
            worldIn.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(worldIn, pos, 3);
        }
    }

    public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
        BedPart bedpart = state.getValue(PART);
        return bedpart == BedPart.FOOT ? BlockType.FIRST : BlockType.SECOND;
    }

    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return blockState.getValue(this.getServingsProperty());
    }

    @Override
    public IntegerProperty getServingsProperty() {
        return EXTRASERVINGS;
    }

    @Override
    public int getMaxServings() {
        return 5;
    }

    @Override
    protected ItemInteractionResult takeServing(LevelAccessor level, BlockPos pos, BlockState state, Player player, InteractionHand hand) {
        int servings = state.getValue(getServingsProperty());

        if (servings == 0) {
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
            level.destroyBlock(pos, true);
            return ItemInteractionResult.SUCCESS;
        }

        ItemStack servingOne = this.getaltServingOneItem(state);
        ItemStack servingTwo = this.getaltServingTwoItem(state);
        ItemStack servingThree = this.getServingItem(state);

        if (servings > 0) {
            ItemStack selectedServing = determineServing(servingOne, servingTwo, servingThree, pos, state, player);
            if (selectedServing != null && processServing(level, pos, state, player, hand, selectedServing, servings)) {
                BlockPos pairPos = pos.relative(getDirectionToOther(state.getValue(PART), state.getValue(FACING)));
                BlockState pairState = level.getBlockState(pairPos);

                level.setBlock(pos, state.setValue(this.getServingsProperty(), servings - 1), 3);
                if (pairState.is(this)) {
                    level.setBlock(pairPos, pairState.setValue(this.getServingsProperty(), servings - 1), 3);
                }

                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected ItemStack determineServing(ItemStack servingOne, ItemStack servingTwo, ItemStack servingThree, BlockPos pos, BlockState state, Player player) {
        int maxServings = getMaxServings();
        int servings = state.getValue(getServingsProperty());

        if (servings == maxServings) {
            return servingOne;
        }
        if (servings <= 2) {
            return servingTwo;
        }
        return servingThree;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParams) {
        List<ItemStack> drops = new ArrayList(super.getDrops(state, lootParams));
        if (!lootParams.getLevel().isClientSide) {
            BedPart part = state.getValue(PART);
            int serving = state.getValue(EXTRASERVINGS);
            int bonemealCount = 0;
            if (part == BedPart.HEAD) {
                bonemealCount = serving == 3 ? 1 : (serving <= 2 ? 2 : 0);
            } else if (part == BedPart.FOOT) {
                bonemealCount = serving == 1 ? 1 : (serving <= 0 ? 2 : 0);
            }

            if (bonemealCount > 0) {
                drops.add(new ItemStack(Items.BONE_MEAL, bonemealCount));
            }
        }

        return drops;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, EXTRASERVINGS, PART);
    }
}
