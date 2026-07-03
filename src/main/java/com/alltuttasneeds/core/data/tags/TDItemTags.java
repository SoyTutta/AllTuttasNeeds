package com.alltuttasneeds.core.data.tags;

import com.alltuttasneeds.registry.TDTags;
import com.alltuttasneeds.registry.compat.framework.CompatRegistry;
import com.alltuttasneeds.registry.compat.framework.DoorTag;
import com.alltuttasneeds.registry.compat.framework.DoorVariant;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class TDItemTags extends ItemTagsProvider {
    public TDItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, "tuttasdoors", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        registerModTags();
        registerMinecraftTags();
    }

    private void registerModTags() {
        registerSecretDoorTags();
        registerWoodFamilyTags();
        registerExtraDoorTags();

        tag(TDTags.DISCRETE_DOORS_ITEMS).addTag(TDTags.WOODEN_DISCRETE_DOORS_ITEMS);
        tag(TDTags.CLASSIC_DOORS_ITEMS).addTag(TDTags.WOODEN_CLASSIC_DOORS_ITEMS);
        tag(TDTags.INDISCRETE_DOORS_ITEMS).addTag(TDTags.WOODEN_INDISCRETE_DOORS_ITEMS);
        tag(TDTags.TRANSIT_DOORS_ITEMS).addTag(TDTags.WOODEN_TRANSIT_DOORS_ITEMS);
        tag(TDTags.PET_DOORS_ITEMS).addTag(TDTags.WOODEN_PET_DOORS_ITEMS);
        tag(TDTags.SECRET_DOORS_ITEMS).addTag(TDTags.WOODEN_SECRET_DOORS_ITEMS);
    }

    private void registerSecretDoorTags() {
        var secretDoors = tag(TDTags.WOODEN_SECRET_DOORS_ITEMS);
        CompatRegistry.loaded().forEach(compat ->
                compat.secretDoorFamilies().forEach(secret -> {
                    ResourceLocation location = compat.registryLocation(secret.woodName() + "_bookshelf_door");
                    secretDoors.addOptional(location);
                }));
    }

    private void registerWoodFamilyTags() {
        CompatRegistry.loaded().forEach(compat -> compat.woodFamilies().forEach(family -> {
            for (DoorVariant variant : family.displayOrder()) {
                if (variant.isRegistrable()) {
                    tag(variant.itemTag()).addOptional(
                            compat.registryLocation(family.registryName() + "_" + variant.suffix()));
                } else if (variant == DoorVariant.ORIGINAL) {
                    DoorVariant impliedTier = family.impliedOriginalTier();
                    if (impliedTier != null) {
                        tag(impliedTier.itemTag()).addOptional(family.originalLocation());
                    }
                }
            }
        }));
    }

    private void registerExtraDoorTags() {
        CompatRegistry.loaded().forEach(compat -> compat.extraDoors().forEach(extra -> {
            ResourceLocation location = compat.registryLocation(extra.name());
            if (extra.tags().contains(DoorTag.SLIDING)) tag(TDTags.SLIDING_DOORS_ITEMS).addOptional(location);
            if (extra.tags().contains(DoorTag.PIGLIN_LOVED)) tag(ItemTags.PIGLIN_LOVED).addOptional(location);
        }));
    }

    private void registerMinecraftTags() {
        tag(ItemTags.WOODEN_DOORS)
                .addTag(TDTags.WOODEN_SECRET_DOORS_ITEMS)
                .addTag(TDTags.WOODEN_DISCRETE_DOORS_ITEMS)
                .addTag(TDTags.WOODEN_CLASSIC_DOORS_ITEMS)
                .addTag(TDTags.WOODEN_INDISCRETE_DOORS_ITEMS)
                .addTag(TDTags.WOODEN_TRANSIT_DOORS_ITEMS);

        tag(ItemTags.WOODEN_TRAPDOORS).addTag(TDTags.WOODEN_PET_DOORS_ITEMS);

        tag(ItemTags.DOORS)
                .addTag(ItemTags.WOODEN_DOORS)
                .addTag(TDTags.SLIDING_DOORS_ITEMS);

        registerNonFlammableWoodTag();
    }

    private void registerNonFlammableWoodTag() {
        var nonFlammableWood = tag(ItemTags.NON_FLAMMABLE_WOOD);
        CompatRegistry.loaded().forEach(compat -> compat.woodFamilies().forEach(family -> {
            if (!family.nonFlammable()) return;
            for (DoorVariant variant : family.displayOrder()) {
                if (variant.isRegistrable()) {
                    nonFlammableWood.addOptional(
                            compat.registryLocation(family.registryName() + "_" + variant.suffix()));
                } else if (variant == DoorVariant.ORIGINAL && !family.originalLocation().getNamespace().equals("minecraft")) {
                    nonFlammableWood.addOptional(family.originalLocation());
                }
            }
        }));
    }
}