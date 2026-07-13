package com.alltuttasneeds.beds.block;

import com.alltuttasneeds.beds.BedTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class BedFrameBlock extends AbstractTuttaBedBlock {
    private static final VoxelShape PLATFORM = Block.box(0.0D, 3.0D, 0.0D, 16.0D, 5.0D, 16.0D);
    private static final Map<ShapeKey, VoxelShape> SHAPES = new ConcurrentHashMap<>();

    private static final Map<Supplier<Item>, Supplier<Block>> MATTRESS_RESULTS = new HashMap<>();

    public BedFrameBlock(Properties properties) {
        super(DyeColor.WHITE, properties);
    }

    public static void registerMattressResult(Supplier<Item> mattressItem, Supplier<Block> resultingBed) {
        MATTRESS_RESULTS.put(mattressItem, resultingBed);
    }

    @Override
    public BedTier tier() {
        return BedTier.BASIC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        ShapeKey key = new ShapeKey(
                state.getValue(FACING),
                state.getValue(PART),
                state.getValue(BedStateProperties.BED_POSITION),
                state.getValue(BedStateProperties.BUNK));
        return SHAPES.computeIfAbsent(key, BedFrameBlock::createShape);
    }

    private static VoxelShape createShape(ShapeKey key) {
        VoxelShape shape = PLATFORM;
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
                ? Shapes.or(shape, Block.box(minX, 5.0D, minZ, minX + 3.0D, 16.0D, minZ + 3.0D))
                : shape;
    }

    private record ShapeKey(Direction facing, BedPart part, BedPosition position, boolean bunk) {}

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockState(pos).getBlock() instanceof BedFrameBlock)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        Supplier<Block> result = null;
        for (Map.Entry<Supplier<Item>, Supplier<Block>> entry : MATTRESS_RESULTS.entrySet()) {
            if (stack.is(entry.getKey().get())) {
                result = entry.getValue();
                break;
            }
        }
        if (result == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        BedCombining.replaceBothParts(level, pos, state, result.get(), player, stack);
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }
}
