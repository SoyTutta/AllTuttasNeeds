package com.alltuttasneeds.registry.compat.framework;

import com.alltuttasneeds.blocks.PetDoorBlock;
import com.alltuttasneeds.blocks.TransitDoorBlock;
import com.alltuttasneeds.registry.TDTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.function.BiFunction;

/**
 * Every "slot" a {@link WoodFamily} can fill, in the order it can appear in the
 * creative tab.
 * <p>
 * Registrable variants (DISCRETE, NORMAL, INDISCRETE, TRANSIT, PET) carry the
 * block factory and the Tuttas Doors tags they belong to, so registration,
 * tagging, blockstates/models and the creative tab are all driven by the same
 * single ordered list per family ({@link WoodFamily#displayOrder()}).
 * <p>
 * ORIGINAL and TRAPDOOR are "sentinels": we never register them ourselves,
 * they just tell the creative tab / tag generators to slot in the mod's own
 * pre-existing door/trapdoor item at that position. Which conceptual tier
 * (discrete/normal/indiscrete) the ORIGINAL door represents is derived
 * automatically — see {@link WoodFamily#impliedOriginalTier()}.
 */
public enum DoorVariant {
    DISCRETE("discrete_door", DoorBlock::new, TDTags.WOODEN_DISCRETE_DOORS, TDTags.WOODEN_DISCRETE_DOORS_ITEMS),
    NORMAL("normal_door", DoorBlock::new, TDTags.WOODEN_CLASSIC_DOORS, TDTags.WOODEN_CLASSIC_DOORS_ITEMS),
    INDISCRETE("indiscrete_door", DoorBlock::new, TDTags.WOODEN_INDISCRETE_DOORS, TDTags.WOODEN_INDISCRETE_DOORS_ITEMS),
    TRANSIT("transit_door", TransitDoorBlock::new, TDTags.WOODEN_TRANSIT_DOORS, TDTags.WOODEN_TRANSIT_DOORS_ITEMS),
    PET("pet_door", PetDoorBlock::new, TDTags.WOODEN_PET_DOORS, TDTags.WOODEN_PET_DOORS_ITEMS),

    /** Sentinel: the mod's own original door. Never registered by us. */
    ORIGINAL(null, null, null, null),
    /** Sentinel: the mod's own original trapdoor, looked up as "&lt;wood&gt;_trapdoor". */
    TRAPDOOR(null, null, null, null);

    private final String suffix;
    private final BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory;
    private final TagKey<Block> blockTag;
    private final TagKey<Item> itemTag;

    DoorVariant(String suffix, BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory,
                TagKey<Block> blockTag, TagKey<Item> itemTag) {
        this.suffix = suffix;
        this.factory = factory;
        this.blockTag = blockTag;
        this.itemTag = itemTag;
    }

    /** True for DISCRETE/NORMAL/INDISCRETE/TRANSIT/PET; false for the ORIGINAL/TRAPDOOR sentinels. */
    public boolean isRegistrable() {
        return suffix != null;
    }

    public String suffix() {
        return suffix;
    }

    public BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory() {
        return factory;
    }

    public TagKey<Block> blockTag() {
        return blockTag;
    }

    public TagKey<Item> itemTag() {
        return itemTag;
    }
}
