package com.alltuttasneeds.registry.compat.framework;

import com.alltuttasneeds.blocks.SecretDoorBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Shared registration logic for every {@link WoodFamily}, {@link SecretDoorFamily} and
 * {@link ExtraDoor}. All XContent classes delegate here instead of duplicating this logic.
 */
public final class CompatRegistrar {

    private CompatRegistrar() {}

    /**
     * Helper for mods that don't expose their {@code BlockSetType} directly.
     * Reads it back from the door block they already registered.
     */
    public static Supplier<BlockSetType> setTypeFromDoor(Supplier<? extends Block> door) {
        return () -> ((DoorBlock) door.get()).type();
    }

    /**
     * Registers every registrable {@link DoorVariant} in the family's display order.
     */
    public static void registerWoodFamily(
            DeferredRegister<Block> blocks, DeferredRegister<Item> items,
            Map<String, Supplier<? extends Block>> doors, Map<String, Supplier<Item>> doorItems,
            WoodFamily family
    ) {

        for (DoorVariant variant : family.displayOrder()) {
            if (!variant.isRegistrable()) continue;
            String name = family.registryName() + "_" + variant.suffix();
            registerDoor(blocks, items, doors, doorItems, name,
                    family.setType(), family.baseDoor(), variant.factory(), family.propertiesCustomizer());
        }
    }

    /** Registers a single bookshelf-disguised {@link SecretDoorFamily}. */
    public static void registerSecretDoor(
            DeferredRegister<Block> blocks, DeferredRegister<Item> items,
            Map<String, Supplier<? extends Block>> doors, Map<String, Supplier<Item>> doorItems,
            SecretDoorFamily secretDoor
    ) {
        String name = secretDoor.woodName() + "_bookshelf_door";
        if (doors.containsKey(name)) return;

        Supplier<Block> block = blocks.register(name, () -> {
            BlockSetType setType   = secretDoor.setType().get();
            BlockBehaviour.Properties props = BlockBehaviour.Properties
                    .ofFullCopy(secretDoor.bookshelf().get()).noOcclusion();
            return new SecretDoorBlock(setType, props, secretDoor.bookshelf());
        });

        doors.put(name, block);
        Supplier<Item> item = items.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        doorItems.put(name, item);
    }

    /** Registers a single hand-curated {@link ExtraDoor} (sliding / bars doors). */
    public static void registerExtraDoor(
            DeferredRegister<Block> blocks, DeferredRegister<Item> items,
            Map<String, Supplier<? extends Block>> doors, Map<String, Supplier<Item>> doorItems,
            ExtraDoor extra
    ) {
        if (doors.containsKey(extra.name())) return;

        Supplier<Block> block = blocks.register(extra.name(), () ->
                extra.factory().apply(extra.setType().get(), extra.properties().get()));

        doors.put(extra.name(), block);
        Supplier<Item> item = items.register(extra.name(), () -> new BlockItem(block.get(), new Item.Properties()));
        doorItems.put(extra.name(), item);
    }

    public static void registerWoodFamilies(
            DeferredRegister<Block> blocks, DeferredRegister<Item> items,
            Map<String, Supplier<? extends Block>> doors, Map<String, Supplier<Item>> doorItems,
            List<WoodFamily> families
    ) {
        for (WoodFamily family : families) registerWoodFamily(blocks, items, doors, doorItems, family);
    }

    public static void registerSecretDoors(
            DeferredRegister<Block> blocks, DeferredRegister<Item> items,
            Map<String, Supplier<? extends Block>> doors, Map<String, Supplier<Item>> doorItems,
            List<SecretDoorFamily> secretDoors
    ) {
        for (SecretDoorFamily secretDoor : secretDoors) registerSecretDoor(blocks, items, doors, doorItems, secretDoor);
    }

    public static void registerExtraDoors(
            DeferredRegister<Block> blocks, DeferredRegister<Item> items,
            Map<String, Supplier<? extends Block>> doors, Map<String, Supplier<Item>> doorItems,
            List<ExtraDoor> extraDoors
    ) {
        for (ExtraDoor extra : extraDoors) registerExtraDoor(blocks, items, doors, doorItems, extra);
    }

    private static void registerDoor(
            DeferredRegister<Block> blocks, DeferredRegister<Item> items,
            Map<String, Supplier<? extends Block>> doors, Map<String, Supplier<Item>> doorItems,
            String name, Supplier<BlockSetType> setTypeSupplier, Supplier<? extends Block> baseDoor,
            BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory,
            UnaryOperator<BlockBehaviour.Properties> propertiesCustomizer
    ) {
        if (doors.containsKey(name)) return;

        Supplier<Block> block = blocks.register(name, () -> {
            BlockSetType resolvedSetType = setTypeSupplier.get();
            BlockBehaviour.Properties props = propertiesCustomizer.apply(
                    BlockBehaviour.Properties.ofFullCopy(baseDoor.get()));
            return factory.apply(resolvedSetType, props);
        });

        doors.put(name, block);
        Supplier<Item> item = items.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        doorItems.put(name, item);
    }
}
