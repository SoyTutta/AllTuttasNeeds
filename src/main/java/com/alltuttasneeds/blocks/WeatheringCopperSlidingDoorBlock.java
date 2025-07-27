package com.alltuttasneeds.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class WeatheringCopperSlidingDoorBlock extends SlidingDoorBlock implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperSlidingDoorBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BlockSetType.CODEC.fieldOf("block_set_type")
                            .forGetter(WeatheringCopperSlidingDoorBlock::type),
                    WeatherState.CODEC.fieldOf("weathering_state")
                            .forGetter(WeatheringCopperSlidingDoorBlock::getAge),
                    propertiesCodec()
            ).apply(instance, WeatheringCopperSlidingDoorBlock::new)
    );

    private final WeatherState weatherState;

    public MapCodec<WeatheringCopperSlidingDoorBlock> codec() {
        return CODEC;
    }

    public WeatheringCopperSlidingDoorBlock(BlockSetType type, WeatherState state, Properties properties) {
        super(type, properties);
        this.weatherState = state;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            this.changeOverTime(state, level, pos, random);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}