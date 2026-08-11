package com.alltuttasneeds.beds.block;

import com.alltuttasneeds.beds.BedTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractTuttaBedBlock extends BedBlock implements TuttaBedBlock {
    private static final VoxelShape BODY = Block.box(0.0D, 3.0D, 0.0D, 16.0D, 9.0D, 16.0D);
    private static final Map<ShapeKey, VoxelShape> SHAPES = new ConcurrentHashMap<>();

    protected AbstractTuttaBedBlock(DyeColor color, Properties properties) {
        super(color, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, BedPart.FOOT)
                .setValue(OCCUPIED, false)
                .setValue(BedStateProperties.BED_POSITION, BedPosition.SINGLE)
                .setValue(BedStateProperties.BUNK, false));
    }

    public abstract BedTier tier();

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BedCombining.preserveHeadDropWhenBreakingFoot(level, pos, state, player);
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED, BedStateProperties.BED_POSITION, BedStateProperties.BUNK);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        ShapeKey key = new ShapeKey(
                state.getValue(FACING),
                state.getValue(PART),
                state.getValue(BedStateProperties.BED_POSITION),
                state.getValue(BedStateProperties.BUNK));
        return SHAPES.computeIfAbsent(key, AbstractTuttaBedBlock::createShape);
    }

    private static VoxelShape createShape(ShapeKey key) {
        VoxelShape shape = BODY;
        if (key.position == BedPosition.CENTER) return shape;

        Direction end = key.part == BedPart.HEAD ? key.facing : key.facing.getOpposite();
        if (key.position == BedPosition.SINGLE || key.position == BedPosition.LEFT) {
            shape = addPost(shape, end, key.facing.getCounterClockWise(), key.bunk);
        }
        if (key.position == BedPosition.SINGLE || key.position == BedPosition.RIGHT) {
            shape = addPost(shape, end, key.facing.getClockWise(), key.bunk);
        }
        return shape.optimize();
    }

    private static VoxelShape addPost(VoxelShape shape, Direction end, Direction side, boolean bunk) {
        double minX = end == Direction.WEST || side == Direction.WEST ? 0.0D : 13.0D;
        double minZ = end == Direction.NORTH || side == Direction.NORTH ? 0.0D : 13.0D;
        shape = Shapes.or(shape, Block.box(minX, 0.0D, minZ, minX + 3.0D, 3.0D, minZ + 3.0D));
        return bunk
                ? Shapes.or(shape, Block.box(minX, 9.0D, minZ, minX + 3.0D, 16.0D, minZ + 3.0D))
                : shape;
    }

    private record ShapeKey(Direction facing, BedPart part, BedPosition position, boolean bunk) {}

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return null;
        BlockPos pos = context.getClickedPos();
        return state
                .setValue(BedStateProperties.BED_POSITION, computeBedPosition(state, context.getLevel(), pos))
                .setValue(BedStateProperties.BUNK, isBunkMatch(state, context.getLevel().getBlockState(pos.above())));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.hasProperty(FACING)) return state;
        Direction facing = state.getValue(FACING);
        BedPart part = state.getValue(PART);
        Direction pairDirection = part == BedPart.HEAD ? facing.getOpposite() : facing;

        if (direction == pairDirection) {
            if (!isPair(state, neighborState, facing, part)) {
                return Blocks.AIR.defaultBlockState();
            }
            return state.setValue(OCCUPIED, neighborState.getValue(OCCUPIED));
        }

        if (direction == Direction.UP) {
            boolean bunk = isBunkMatch(state, neighborState);
            return state.getValue(BedStateProperties.BUNK) == bunk ? state : state.setValue(BedStateProperties.BUNK, bunk);
        }

        if (direction == facing.getClockWise() || direction == facing.getCounterClockWise()) {
            return state.setValue(BedStateProperties.BED_POSITION, computeBedPosition(state, level, pos));
        }
        return state;
    }

    private static boolean isPair(BlockState state, BlockState neighbor, Direction facing, BedPart part) {
        return neighbor.is(state.getBlock())
                && neighbor.hasProperty(FACING) && neighbor.getValue(FACING) == facing
                && neighbor.hasProperty(PART) && neighbor.getValue(PART) != part;
    }

    private static BedPosition computeBedPosition(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        Direction right = facing.getClockWise();
        boolean hasRight = isFamilyNeighbor(state, level.getBlockState(pos.relative(right)));
        boolean hasLeft = isFamilyNeighbor(state, level.getBlockState(pos.relative(right.getOpposite())));
        if (hasRight && hasLeft) return BedPosition.CENTER;
        if (hasRight) return BedPosition.LEFT;
        if (hasLeft) return BedPosition.RIGHT;
        return BedPosition.SINGLE;
    }

    private static boolean isFamilyNeighbor(BlockState bed, BlockState neighbor) {
        if (!(neighbor.getBlock() instanceof TuttaBedBlock) || !neighbor.hasProperty(FACING) || !neighbor.hasProperty(PART)) {
            return false;
        }
        Direction facing = bed.getValue(FACING);
        BedPart part = bed.getValue(PART);
        Direction neighborFacing = neighbor.getValue(FACING);
        BedPart neighborPart = neighbor.getValue(PART);
        return neighborFacing == facing && neighborPart == part
                || neighborFacing == facing.getOpposite() && neighborPart == opposite(part);
    }

    private static boolean isBunkMatch(BlockState below, BlockState above) {
        if (!(above.getBlock() instanceof TuttaBedBlock)) return false;
        if (!above.hasProperty(FACING) || !above.hasProperty(PART)) return false;

        Direction belowFacing = below.getValue(FACING);
        BedPart belowPart = below.getValue(PART);
        Direction aboveFacing = above.getValue(FACING);
        BedPart abovePart = above.getValue(PART);

        boolean sameWay = aboveFacing == belowFacing && abovePart == belowPart;
        boolean reversed = aboveFacing == belowFacing.getOpposite() && abovePart == opposite(belowPart);
        return sameWay || reversed;
    }

    private static BedPart opposite(BedPart part) {
        return part == BedPart.HEAD ? BedPart.FOOT : BedPart.HEAD;
    }
}
