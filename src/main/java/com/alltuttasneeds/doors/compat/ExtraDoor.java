package com.alltuttasneeds.doors.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public record ExtraDoor(
        String name,
        Supplier<BlockSetType> setType,
        Supplier<BlockBehaviour.Properties> properties,
        BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory,
        Set<DoorTag> tags,
        @Nullable ResourceLocation creativeTabAnchor
) {

    public static ExtraDoor of(String name, Supplier<BlockSetType> setType,
                               Supplier<BlockBehaviour.Properties> properties,
                               BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory,
                               DoorTag... tags) {
        return new ExtraDoor(name, setType, properties, factory, Set.of(tags), null);
    }

    public static ExtraDoor of(String name, Supplier<BlockSetType> setType,
                               Supplier<BlockBehaviour.Properties> properties,
                               BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory,
                               ResourceLocation creativeTabAnchor,
                               DoorTag... tags) {
        return new ExtraDoor(name, setType, properties, factory, Set.of(tags), creativeTabAnchor);
    }

    public static ResourceLocation barsAnchor(String namespace, String doorName) {
        String anchorName = doorName.endsWith("_sliding_door")
                ? doorName.substring(0, doorName.length() - "_sliding_door".length())
                : doorName;
        return ResourceLocation.fromNamespaceAndPath(namespace, anchorName);
    }
}