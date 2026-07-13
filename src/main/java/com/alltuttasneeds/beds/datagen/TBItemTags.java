package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.TBContent;
import com.alltuttasneeds.beds.TBTags;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BedBlock;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class TBItemTags extends ItemTagsProvider {
    public TBItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                      CompletableFuture<TagLookup<Block>> blockTags,
                      @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, "tuttasbeds", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        copy(BlockTags.BEDS, ItemTags.BEDS);

        if (TBContent.BED_FRAME != null) tag(TBTags.BASIC_TIER_BEDS).add(TBContent.BED_FRAME.get().asItem());

        BuiltInRegistries.ITEM.stream()
                .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("minecraft"))
                .filter(item -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof BedBlock)
                .forEach(item -> tag(TBTags.BASIC_TIER_BEDS).add(item));

        BedCompatRegistry.loaded().flatMap(compat -> compat.families().stream()).forEach(family -> {
            tag(TBTags.BASIC_TIER_BEDS).add(family.looseMattress().get().asItem());
            family.looseMattressCovers().values().forEach(block ->
                    tag(TBTags.LOW_TIER_BEDS).add(block.get().asItem()));

            tag(TBTags.BASIC_TIER_BEDS).add(family.bedBare().get().asItem());
            family.bedBasicCovers().values().forEach(block ->
                    tag(TBTags.LOW_TIER_BEDS).add(block.get().asItem()));
            family.bedBlankets().values().forEach(colors -> colors.values().forEach(block ->
                    tag(TBTags.NORMAL_TIER_BEDS).add(block.get().asItem())));
            family.bedDeluxe().values().forEach(block ->
                    tag(TBTags.DELUXE_TIER_BEDS).add(block.get().asItem()));
        });
    }
}
