package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import com.alltuttasneeds.beds.TBContent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class TBBlockTags extends BlockTagsProvider {
    public TBBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "tuttasbeds", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        var beds = tag(BlockTags.BEDS);

        if (TBContent.BED_FRAME != null) {
            beds.addOptional(BuiltInRegistries.BLOCK.getKey(TBContent.BED_FRAME.get()));
        }

        BedCompatRegistry.loaded().flatMap(compat -> compat.families().stream()).forEach(family -> {
            beds.addOptional(BuiltInRegistries.BLOCK.getKey(family.looseMattress().get()));
            family.looseMattressCovers().values().forEach(block ->
                    beds.addOptional(BuiltInRegistries.BLOCK.getKey(block.get())));

            beds.addOptional(BuiltInRegistries.BLOCK.getKey(family.bedBare().get()));
            family.bedBasicCovers().values().forEach(block ->
                    beds.addOptional(BuiltInRegistries.BLOCK.getKey(block.get())));

            family.bedBlankets().values().forEach(colors -> colors.values().forEach(block ->
                    beds.addOptional(BuiltInRegistries.BLOCK.getKey(block.get()))));
            family.bedDeluxe().values().forEach(block ->
                    beds.addOptional(BuiltInRegistries.BLOCK.getKey(block.get())));
        });
    }
}
