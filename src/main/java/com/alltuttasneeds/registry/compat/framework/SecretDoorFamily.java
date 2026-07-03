package com.alltuttasneeds.registry.compat.framework;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.function.Supplier;

/**
 * Declarative definition of a "secret" door disguised as a bookshelf.
 *
 * @param woodName      Base name; registry name becomes {@code "<woodName>_bookshelf_door"}.
 * @param bookshelf     The bookshelf block this door disguises itself as.
 * @param setType       BlockSetType used for sounds / open-close behaviour.
 * @param ownerFamilyId The creative-tab {@link WoodFamily#familyId()} this secret door slots
 *                      into (it's shown right before that family's own variants). It may belong
 *                      to a different mod than the one registering it — e.g. NoMansLand provides
 *                      secret doors for several vanilla wood families.
 */
public record SecretDoorFamily(
        String woodName,
        Supplier<Block> bookshelf,
        Supplier<BlockSetType> setType,
        String ownerFamilyId
) {}
