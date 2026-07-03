package com.alltuttasneeds.registry.compat.framework;

import com.alltuttasneeds.blocks.SlidingDoorBlock;
import com.alltuttasneeds.blocks.WeatheringCopperSlidingDoorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Builds the 8-variant copper weathering chain (4 oxidation stages x waxed/unwaxed) shared
 * by every "copper bars sliding door" in the pack, generated from
 * {@link WeatheringCopper.WeatherState} the same way vanilla copper blocks are.
 * <p>
 * One call produces both the {@link ExtraDoor} registrations and the oxidation/waxing
 * {@link Link}s consumed by {@code DataMaps} and {@code CraftingRecipes} — a compat only
 * states its namespace and base {@link BlockBehaviour.Properties} once, instead of the
 * chain being re-derived independently in every place that needs it.
 */
public final class WeatheringDoorChain {

    private WeatheringDoorChain() {}

    private static final String BASE_NAME = "copper_bars_sliding_door";

    /** One oxidation step: {@code from} ages into {@code to} ({@code null} at full oxidation); {@code waxed} is from's waxed counterpart. */
    public record Link(String from, @Nullable String to, String waxed) {}

    public record Chain(List<ExtraDoor> doors, List<Link> oxidationLinks) {}

    public static Chain of(String namespace, Supplier<BlockBehaviour.Properties> basePropertiesTemplate) {
        List<ExtraDoor> doors = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        for (WeatheringCopper.WeatherState state : WeatheringCopper.WeatherState.values()) {
            String unwaxedName = variantName(state, false);
            String waxedName = variantName(state, true);

            doors.add(door(namespace, unwaxedName, basePropertiesTemplate, state, false));
            doors.add(door(namespace, waxedName, basePropertiesTemplate, state, true));

            String next = state == WeatheringCopper.WeatherState.OXIDIZED ? null : variantName(next(state), false);
            links.add(new Link(unwaxedName, next, waxedName));
        }

        return new Chain(List.copyOf(doors), List.copyOf(links));
    }

    private static ExtraDoor door(String namespace, String fullName,
                                   Supplier<BlockBehaviour.Properties> basePropertiesTemplate,
                                   WeatheringCopper.WeatherState state, boolean waxed) {
        Supplier<BlockBehaviour.Properties> properties = () ->
                basePropertiesTemplate.get().mapColor(mapColorFor(state));

        BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory = waxed
                ? SlidingDoorBlock::new
                : (type, props) -> new WeatheringCopperSlidingDoorBlock(type, state, props);

        return ExtraDoor.of(fullName, () -> BlockSetType.COPPER, properties, factory,
                ExtraDoor.barsAnchor(namespace, fullName),
                DoorTag.SLIDING, DoorTag.NEEDS_STONE_TOOL);
    }

    private static WeatheringCopper.WeatherState next(WeatheringCopper.WeatherState state) {
        return WeatheringCopper.WeatherState.values()[state.ordinal() + 1];
    }

    private static String variantName(WeatheringCopper.WeatherState state, boolean waxed) {
        String prefix = waxed ? "waxed_" : "";
        String base = switch (state) {
            case UNAFFECTED -> waxed ? "waxed" : "copper";
            case EXPOSED    -> prefix + "exposed";
            case WEATHERED  -> prefix + "weathered";
            case OXIDIZED   -> prefix + "oxidized";
        };
        return base.equals("copper") ? BASE_NAME : base + "_" + BASE_NAME;
    }

    private static MapColor mapColorFor(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case EXPOSED   -> Blocks.EXPOSED_COPPER.defaultMapColor();
            case WEATHERED -> Blocks.WEATHERED_COPPER.defaultMapColor();
            case OXIDIZED  -> Blocks.OXIDIZED_COPPER.defaultMapColor();
            default        -> Blocks.COPPER_BLOCK.defaultMapColor();
        };
    }
}
