package com.alltuttasneeds.doors.compat;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.function.Supplier;

public record SecretDoorFamily(
        String woodName,
        Supplier<Block> bookshelf,
        Supplier<BlockSetType> setType,
        String ownerFamilyId
) {}
