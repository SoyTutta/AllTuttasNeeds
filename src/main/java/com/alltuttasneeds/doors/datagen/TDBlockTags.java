package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.doors.TDTags;
import com.alltuttasneeds.doors.compat.CompatRegistry;
import com.alltuttasneeds.doors.compat.DoorTag;
import com.alltuttasneeds.doors.compat.DoorVariant;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class TDBlockTags extends BlockTagsProvider {
    public TDBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "tuttasdoors", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        registerSecretDoorTags();
        registerWoodFamilyTags();
        registerExtraDoorTags();
        registerMinecraftTags();
    }

    private void registerSecretDoorTags() {
        var bookshelfDoors = tag(TDTags.BOOKSHELF_DOORS);
        CompatRegistry.loaded().forEach(compat ->
                compat.secretDoorFamilies().forEach(secret ->
                        bookshelfDoors.addOptional(
                                compat.registryLocation(secret.woodName() + "_bookshelf_door"))));
        tag(TDTags.WOODEN_SECRET_DOORS).addTag(TDTags.BOOKSHELF_DOORS);
    }

    private void registerWoodFamilyTags() {
        CompatRegistry.loaded().forEach(compat -> compat.woodFamilies().forEach(family -> {
            for (DoorVariant variant : family.displayOrder()) {
                if (variant.isRegistrable()) {
                    tag(variant.blockTag()).addOptional(
                            compat.registryLocation(family.registryName() + "_" + variant.suffix()));
                } else if (variant == DoorVariant.ORIGINAL) {
                    DoorVariant impliedTier = family.impliedOriginalTier();
                    if (impliedTier != null) {
                        tag(impliedTier.blockTag()).addOptional(family.originalLocation());
                    }
                }
            }
        }));
    }

    private void registerExtraDoorTags() {
        CompatRegistry.loaded().forEach(compat -> compat.extraDoors().forEach(extra -> {
            ResourceLocation location = compat.registryLocation(extra.name());
            if (extra.tags().contains(DoorTag.SLIDING)) tag(TDTags.SLIDING_DOORS).addOptional(location);
            if (extra.tags().contains(DoorTag.GUARDED_BY_PIGLINS)) tag(BlockTags.GUARDED_BY_PIGLINS).addOptional(location);
            if (extra.tags().contains(DoorTag.NEEDS_STONE_TOOL)) tag(BlockTags.NEEDS_STONE_TOOL).addOptional(location);
            if (extra.tags().contains(DoorTag.NEEDS_IRON_TOOL)) tag(BlockTags.NEEDS_IRON_TOOL).addOptional(location);
        }));
    }

    private void registerMinecraftTags() {
        tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).addTag(TDTags.BOOKSHELF_DOORS);
        tag(BlockTags.WOODEN_DOORS)
                .addTag(TDTags.WOODEN_SECRET_DOORS)
                .addTag(TDTags.WOODEN_DISCRETE_DOORS)
                .addTag(TDTags.WOODEN_CLASSIC_DOORS)
                .addTag(TDTags.WOODEN_INDISCRETE_DOORS)
                .addTag(TDTags.WOODEN_TRANSIT_DOORS)
                .addTag(TDTags.WOODEN_PET_DOORS);

        tag(BlockTags.DOORS)
                .addTag(BlockTags.WOODEN_DOORS)
                .addTag(TDTags.SLIDING_DOORS);

        tag(BlockTags.WALLS).addTag(TDTags.SLIDING_DOORS);

        tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(TDTags.WOODEN_SECRET_DOORS)
                .addTag(TDTags.WOODEN_DISCRETE_DOORS)
                .addTag(TDTags.WOODEN_CLASSIC_DOORS)
                .addTag(TDTags.WOODEN_INDISCRETE_DOORS)
                .addTag(TDTags.WOODEN_TRANSIT_DOORS)
                .addTag(TDTags.WOODEN_PET_DOORS);

        tag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(TDTags.SLIDING_DOORS);
    }
}