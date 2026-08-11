package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.TBContent;
import com.alltuttasneeds.beds.TBTags;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider.IntrinsicTagAppender;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
        var beds = tag(ItemTags.BEDS);
        var basicBeds = tag(TBTags.BASIC_TIER_BEDS);

        if (TBContent.BED_FRAME != null) {
            var frameId = BuiltInRegistries.ITEM.getKey(TBContent.BED_FRAME.get().asItem());
            beds.addOptional(frameId);
            basicBeds.addOptional(frameId);
        }

        BuiltInRegistries.ITEM.stream()
                .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("minecraft"))
                .filter(item -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof BedBlock)
                .forEach(item -> {
                    beds.add(item);
                    basicBeds.add(item);
                });

        BedCompatRegistry.loaded().flatMap(compat -> compat.families().stream()).forEach(family -> {
            beds.addOptional(BuiltInRegistries.ITEM.getKey(family.looseMattress().get().asItem()));
            basicBeds.addOptional(BuiltInRegistries.ITEM.getKey(family.looseMattress().get().asItem()));
            family.looseMattressCovers().values().forEach(block ->
                    addOptionalBed(beds, TBTags.LOW_TIER_BEDS, block.get()));

            beds.addOptional(BuiltInRegistries.ITEM.getKey(family.bedBare().get().asItem()));
            basicBeds.addOptional(BuiltInRegistries.ITEM.getKey(family.bedBare().get().asItem()));
            family.bedBasicCovers().values().forEach(block ->
                    addOptionalBed(beds, TBTags.LOW_TIER_BEDS, block.get()));
            family.bedBlankets().values().forEach(colors -> colors.values().forEach(block ->
                    addOptionalBed(beds, TBTags.NORMAL_TIER_BEDS, block.get())));
            family.bedDeluxe().values().forEach(block ->
                    addOptionalBed(beds, TBTags.DELUXE_TIER_BEDS, block.get()));
        });
    }

    private void addOptionalBed(IntrinsicTagAppender<Item> beds,
                                TagKey<Item> tier,
                                Block block) {
        var id = BuiltInRegistries.ITEM.getKey(block.asItem());
        beds.addOptional(id);
        tag(tier).addOptional(id);
    }
}
