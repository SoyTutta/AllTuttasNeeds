package com.alltuttasneeds.doors.compat;

import com.alltuttasneeds.doors.block.PetDoorBlock;
import com.alltuttasneeds.doors.block.TransitDoorBlock;
import com.alltuttasneeds.doors.TDTags;
import com.alltuttasneeds.doors.config.DoorSet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.function.BiFunction;

public enum DoorVariant {
    DISCRETE("discrete_door", DoorBlock::new, TDTags.WOODEN_DISCRETE_DOORS, TDTags.WOODEN_DISCRETE_DOORS_ITEMS, DoorSet.CONSISTENT),
    NORMAL("normal_door", DoorBlock::new, TDTags.WOODEN_CLASSIC_DOORS, TDTags.WOODEN_CLASSIC_DOORS_ITEMS, DoorSet.CONSISTENT),
    INDISCRETE("indiscrete_door", DoorBlock::new, TDTags.WOODEN_INDISCRETE_DOORS, TDTags.WOODEN_INDISCRETE_DOORS_ITEMS, DoorSet.CONSISTENT),
    TRANSIT("transit_door", TransitDoorBlock::new, TDTags.WOODEN_TRANSIT_DOORS, TDTags.WOODEN_TRANSIT_DOORS_ITEMS, DoorSet.TRANSIT),
    PET("pet_door", PetDoorBlock::new, TDTags.WOODEN_PET_DOORS, TDTags.WOODEN_PET_DOORS_ITEMS, DoorSet.PET),

    ORIGINAL(null, null, null, null, null),

    TRAPDOOR(null, null, null, null, null);

    private final String suffix;
    private final BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory;
    private final TagKey<Block> blockTag;
    private final TagKey<Item> itemTag;
    private final DoorSet set;

    DoorVariant(String suffix, BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory,
                TagKey<Block> blockTag, TagKey<Item> itemTag, DoorSet set) {
        this.suffix = suffix;
        this.factory = factory;
        this.blockTag = blockTag;
        this.itemTag = itemTag;
        this.set = set;
    }

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

    public DoorSet set() {
        return set;
    }
}

