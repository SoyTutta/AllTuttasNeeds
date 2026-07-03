package com.alltuttasneeds.registry.compat.framework;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Declarative definition of a hand-curated door that doesn't follow the wood-family
 * pattern. Originally just sliding/bars doors (Tuttas Doors' iron bars, Create's
 * andesite/brass/copper bars, BlockBox's golden and weathering-copper bars, etc.), but
 * the type stays generic so other one-off door kinds can be added later without
 * reshaping the framework. Each entry usually needs its own colour/sound/factory, so
 * unlike {@link WoodFamily} there's no shared template.
 *
 * @param name              Full registry name, e.g. {@code "golden_bars_sliding_door"}.
 * @param setType           BlockSetType supplier.
 * @param properties        Properties supplier (each entry usually needs its own MapColor/SoundType/etc).
 * @param factory           Block factory; receives the resolved BlockSetType + Properties.
 * @param tags              Which generic tag buckets this door opts into (see {@link DoorTag}).
 * @param creativeTabAnchor Location of a pre-existing block this door should be slotted right
 *                          after in the creative tab (e.g. {@code minecraft:iron_bars} for the
 *                          iron bars sliding door). {@code null} when this entry has no natural
 *                          anchor — the creative tab handler then leaves it wherever the tab's
 *                          default ordering puts it. This is opt-in per entry rather than
 *                          derived from {@link #name()}, since future {@link ExtraDoor} kinds
 *                          won't necessarily be "bars" doors with a matching anchor block.
 */
public record ExtraDoor(
        String name,
        Supplier<BlockSetType> setType,
        Supplier<BlockBehaviour.Properties> properties,
        BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory,
        Set<DoorTag> tags,
        @Nullable ResourceLocation creativeTabAnchor
) {
    /** No creative-tab anchor — door is registered/tagged but not auto-slotted next to anything. */
    public static ExtraDoor of(String name, Supplier<BlockSetType> setType,
                               Supplier<BlockBehaviour.Properties> properties,
                               BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory,
                               DoorTag... tags) {
        return new ExtraDoor(name, setType, properties, factory, Set.of(tags), null);
    }

    /**
     * With an explicit creative-tab anchor — the door is inserted right after
     * {@code creativeTabAnchor} in every tab where that item appears.
     */
    public static ExtraDoor of(String name, Supplier<BlockSetType> setType,
                               Supplier<BlockBehaviour.Properties> properties,
                               BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory,
                               ResourceLocation creativeTabAnchor,
                               DoorTag... tags) {
        return new ExtraDoor(name, setType, properties, factory, Set.of(tags), creativeTabAnchor);
    }

    /**
     * Convenience for the common "bars sliding door" case: derives the anchor's registry
     * name by stripping the {@code "_sliding_door"} suffix off {@code doorName}
     * (e.g. {@code "copper_bars_sliding_door"} -> {@code "copper_bars"}), under the given
     * namespace. Only use this when the door actually follows that naming convention —
     * other door kinds should build their {@code ResourceLocation} directly, or pass
     * {@code null} via the no-anchor {@link #of} overload.
     */
    public static ResourceLocation barsAnchor(String namespace, String doorName) {
        String anchorName = doorName.endsWith("_sliding_door")
                ? doorName.substring(0, doorName.length() - "_sliding_door".length())
                : doorName;
        return ResourceLocation.fromNamespaceAndPath(namespace, anchorName);
    }
}