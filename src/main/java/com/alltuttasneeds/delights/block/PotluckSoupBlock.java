package com.alltuttasneeds.delights.block;

import com.alltuttasneeds.delights.DelightsItems;
import com.alltuttasneeds.delights.block.entity.PotluckSoupBlockEntity;
import com.alltuttasneeds.delights.potluck.PotluckRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.List;

public class PotluckSoupBlock extends BaseEntityBlock {
    public static final MapCodec<PotluckSoupBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    propertiesCodec(),
                    Codec.BOOL.fieldOf("elder").forGetter(PotluckSoupBlock::isElder)
            ).apply(instance, PotluckSoupBlock::new));
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final EnumProperty<PotluckPart> PART = EnumProperty.create("part", PotluckPart.class);
    public static final IntegerProperty FILL = IntegerProperty.create("fill", 0, 4);
    public static final BooleanProperty RAW = BooleanProperty.create("raw");
    public static final BooleanProperty TRAY = BooleanProperty.create("tray");
    public static final BooleanProperty FIRST = BooleanProperty.create("first");
    public static final BooleanProperty TAIL = BooleanProperty.create("tail");
    public static final IntegerProperty TAILS = IntegerProperty.create("tails", 0, 3);

    private static final VoxelShape NORMAL_SHAPE = Shapes.or(
            Block.box(1, 0, 1, 15, 3, 15),
            Block.box(1, 0, 1, 3, 14, 15),
            Block.box(13, 0, 1, 15, 14, 15),
            Block.box(3, 0, 1, 13, 14, 3),
            Block.box(3, 0, 13, 13, 14, 15));
    private static final VoxelShape ELDER_BOTTOM = Block.box(0, 0, 0, 16, 2, 16);
    private static final VoxelShape NORTH_WALL = Block.box(0, 0, 0, 16, 16, 2);
    private static final VoxelShape SOUTH_WALL = Block.box(0, 0, 14, 16, 16, 16);
    private static final VoxelShape WEST_WALL = Block.box(0, 0, 0, 2, 16, 16);
    private static final VoxelShape EAST_WALL = Block.box(14, 0, 0, 16, 16, 16);

    private final boolean elder;

    public PotluckSoupBlock(Properties properties, boolean elder) {
        super(properties);
        this.elder = elder;
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, PotluckPart.ORIGIN)
                .setValue(FILL, 4)
                .setValue(RAW, false)
                .setValue(TRAY, false)
                .setValue(FIRST, true)
                .setValue(TAIL, true)
                .setValue(TAILS, elder ? 3 : 1));
    }

    public boolean isElder() {
        return elder;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return elder ? getElderShape(state) : NORMAL_SHAPE;
    }

    private static VoxelShape getElderShape(BlockState state) {
        PotluckPart part = state.getValue(PART);
        PotluckPart lower = part.lowerPart();
        Direction facing = state.getValue(FACING);
        VoxelShape shape = part.isUpper() ? Shapes.empty() : ELDER_BOTTOM;
        if (lower == PotluckPart.ORIGIN || lower == PotluckPart.RIGHT) {
            shape = Shapes.or(shape, wall(facing.getOpposite()));
        }
        if (lower == PotluckPart.FRONT || lower == PotluckPart.DIAGONAL) {
            shape = Shapes.or(shape, wall(facing));
        }
        if (lower == PotluckPart.ORIGIN || lower == PotluckPart.FRONT) {
            shape = Shapes.or(shape, wall(facing.getCounterClockWise()));
        }
        if (lower == PotluckPart.RIGHT || lower == PotluckPart.DIAGONAL) {
            shape = Shapes.or(shape, wall(facing.getClockWise()));
        }
        return shape;
    }

    private static VoxelShape wall(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH_WALL;
            case SOUTH -> SOUTH_WALL;
            case WEST -> WEST_WALL;
            case EAST -> EAST_WALL;
            default -> Shapes.empty();
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        BlockPos origin = context.getClickedPos();
        BlockState state = defaultBlockState()
                .setValue(FACING, facing)
                .setValue(TRAY, isTrayHeatSource(context.getLevel(), origin));
        if (!elder) return hasSupport(context.getLevel(), origin) ? state : null;

        for (PotluckPart part : PotluckPart.values()) {
            BlockPos partPos = getPartPos(origin, facing, part);
            if (!context.getLevel().getWorldBorder().isWithinBounds(partPos)
                    || !context.getLevel().getBlockState(partPos).canBeReplaced(context)
                    || (!part.isUpper() && !hasSupport(context.getLevel(), partPos))) {
                return null;
            }
        }
        return state;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && elder) {
            Direction facing = state.getValue(FACING);
            for (PotluckPart part : PotluckPart.values()) {
                if (part == PotluckPart.ORIGIN) continue;
                BlockPos partPos = getPartPos(pos, facing, part);
                boolean tray = !part.isUpper() && isTrayHeatSource(level, partPos);
                level.setBlock(partPos, state.setValue(PART, part).setValue(TRAY, tray), 3);
            }
        }
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof PotluckSoupBlockEntity potluck) {
            potluck.initializeFullIfNeeded();
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        PotluckPart part = state.getValue(PART);
        if (!elder || !part.isUpper()) return hasSupport(level, pos);
        BlockState lower = level.getBlockState(pos.below());
        return lower.is(this) && lower.getValue(PART) == part.lowerPart()
                && lower.getValue(FACING) == state.getValue(FACING);
    }

    private static boolean hasSupport(LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState below = level.getBlockState(belowPos);
        return below.isFaceSturdy(level, belowPos, Direction.UP)
                || below.is(ModTags.Blocks.HEAT_SOURCES)
                || below.is(ModTags.Blocks.TRAY_HEAT_SOURCES);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART) == PotluckPart.ORIGIN ? new PotluckSoupBlockEntity(pos, state) : null;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        PotluckSoupBlockEntity potluck = getController(level, pos, state);
        return potluck == null ? ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
                : potluck.interact(player, hand, stack);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        PotluckSoupBlockEntity potluck = getController(level, pos, state);
        if (potluck == null || !potluck.interactEmptyHand(player)) return InteractionResult.PASS;
        return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        PotluckSoupBlockEntity potluck = getController(level, pos, state);
        if (potluck != null) {
            BlockPos origin = getOrigin(pos, state);
            double localY = entity.getY() - origin.getY();
            if (potluck.hasLiquidAt(localY)) {
                Vec3 motion = entity.getDeltaMovement();
                boolean submerged = entity.getEyeY() < origin.getY() + potluck.getLiquidHeight();
                double vertical = entity.isShiftKeyDown() ? -0.06D
                        : Math.max(motion.y * 0.82D, submerged ? 0.015D : -0.02D);
                entity.setDeltaMovement(motion.x * 0.82D,
                        vertical,
                        motion.z * 0.82D);
                if (submerged) entity.setSwimming(true);
                if (!potluck.isHeatedForEffects() && entity.isOnFire()) entity.clearFire();
                if (!level.isClientSide && potluck.isHeatedForEffects() && entity.tickCount % 10 == 0) {
                    entity.hurt(level.damageSources().hotFloor(), 1.0F);
                }
                if (!level.isClientSide && entity instanceof ItemEntity itemEntity) {
                    potluck.acceptThrownItem(itemEntity);
                }
            }
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        PotluckSoupBlockEntity potluck = getController(level, pos, state);
        if (potluck != null) potluck.scheduledTick();
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                   BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            PotluckSoupBlockEntity potluck = getController(level, pos, state);
            if (potluck != null) potluck.heatChanged();
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(PART) != PotluckPart.ORIGIN) return;
        PotluckSoupBlockEntity potluck = getController(level, pos, state);
        if (potluck == null || potluck.getLiquidUnits() <= 0) return;
        Bounds bounds = liquidBounds(pos, state);
        double height = pos.getY() + potluck.getLiquidHeight();
        for (PotluckRecipe.ParticleSetting setting : potluck.getAppearanceParticles()) {
            if (!potluck.allowsParticles(setting.state()) || random.nextFloat() >= setting.chance()) continue;
            for (int index = 0; index < setting.count(); index++) {
                double x = randomBetween(random, bounds.minX, bounds.maxX, setting.spread().x());
                double y = height + (random.nextDouble() - 0.5D) * setting.spread().y() * 2.0D;
                double z = randomBetween(random, bounds.minZ, bounds.maxZ, setting.spread().z());
                level.addParticle(setting.options(), x, y, z,
                        setting.velocity().x(), setting.velocity().y(), setting.velocity().z());
            }
        }
    }

    private static double randomBetween(RandomSource random, double min, double max, double spread) {
        double center = (min + max) * 0.5D;
        double radius = Math.min((max - min) * 0.5D, Math.max(0.0D, spread));
        return center + (random.nextDouble() - 0.5D) * radius * 2.0D;
    }

    private Bounds liquidBounds(BlockPos origin, BlockState state) {
        if (!elder) return new Bounds(origin.getX() + 0.2D, origin.getX() + 0.8D,
                origin.getZ() + 0.2D, origin.getZ() + 0.8D);
        Direction facing = state.getValue(FACING);
        int minX = origin.getX();
        int maxX = origin.getX();
        int minZ = origin.getZ();
        int maxZ = origin.getZ();
        for (PotluckPart part : new PotluckPart[]{PotluckPart.ORIGIN, PotluckPart.FRONT,
                PotluckPart.RIGHT, PotluckPart.DIAGONAL}) {
            BlockPos partPos = getPartPos(origin, facing, part);
            minX = Math.min(minX, partPos.getX());
            maxX = Math.max(maxX, partPos.getX());
            minZ = Math.min(minZ, partPos.getZ());
            maxZ = Math.max(maxZ, partPos.getZ());
        }
        return new Bounds(minX + 0.12D, maxX + 0.88D, minZ + 0.12D, maxZ + 0.88D);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (elder && state.getValue(PART) != PotluckPart.ORIGIN && !level.isClientSide) {
            BlockPos origin = getOrigin(pos, state);
            BlockState originState = level.getBlockState(origin);
            if (originState.is(this)) {
                if (!player.isCreative() && level.getBlockEntity(origin) instanceof PotluckSoupBlockEntity potluck) {
                    popResource(level, pos, potluck.createBlockItem());
                }
                removeOtherParts(level, origin, originState);
                level.removeBlock(origin, false);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock() && elder) {
            if (state.getValue(PART) == PotluckPart.ORIGIN) {
                removeOtherParts(level, pos, state);
            } else {
                BlockPos origin = getOrigin(pos, state);
                BlockState originState = level.getBlockState(origin);
                if (originState.is(this)) level.removeBlock(origin, false);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (state.getValue(PART) != PotluckPart.ORIGIN) return List.of();
        BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof PotluckSoupBlockEntity potluck) return List.of(potluck.createBlockItem());
        return List.of(new ItemStack(elder ? DelightsItems.ELDER_POTLUCK_SOUP.get() : DelightsItems.POTLUCK_SOUP.get()));
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        PotluckSoupBlockEntity potluck = getController(level, pos, state);
        return potluck == null ? 0 : potluck.getComparatorOutput();
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!canSurvive(state, level, pos)) return Blocks.AIR.defaultBlockState();
        if (direction == Direction.DOWN && !state.getValue(PART).isUpper()) {
            state = state.setValue(TRAY, isTrayHeatSource(level, pos));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, FILL, RAW, TRAY, FIRST, TAIL, TAILS);
    }

    private static boolean isTrayHeatSource(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(ModTags.Blocks.TRAY_HEAT_SOURCES);
    }

    public BlockPos getOrigin(BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        PotluckPart part = state.getValue(PART);
        BlockPos lowerPos = part.isUpper() ? pos.below() : pos;
        return switch (part.lowerPart()) {
            case ORIGIN -> lowerPos;
            case FRONT -> lowerPos.relative(facing.getOpposite());
            case RIGHT -> lowerPos.relative(facing.getCounterClockWise());
            case DIAGONAL -> lowerPos.relative(facing.getOpposite()).relative(facing.getCounterClockWise());
            default -> lowerPos;
        };
    }

    public static BlockPos getPartPos(BlockPos origin, Direction facing, PotluckPart part) {
        BlockPos lower = switch (part.lowerPart()) {
            case ORIGIN -> origin;
            case FRONT -> origin.relative(facing);
            case RIGHT -> origin.relative(facing.getClockWise());
            case DIAGONAL -> origin.relative(facing).relative(facing.getClockWise());
            default -> origin;
        };
        return part.isUpper() ? lower.above() : lower;
    }

    @Nullable
    public PotluckSoupBlockEntity getController(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = level.getBlockEntity(getOrigin(pos, state));
        return blockEntity instanceof PotluckSoupBlockEntity potluck ? potluck : null;
    }

    private void removeOtherParts(Level level, BlockPos origin, BlockState state) {
        Direction facing = state.getValue(FACING);
        for (PotluckPart part : PotluckPart.values()) {
            if (part == PotluckPart.ORIGIN) continue;
            BlockPos partPos = getPartPos(origin, facing, part);
            if (level.getBlockState(partPos).is(this)) level.removeBlock(partPos, false);
        }
    }

    private record Bounds(double minX, double maxX, double minZ, double maxZ) {}
}
