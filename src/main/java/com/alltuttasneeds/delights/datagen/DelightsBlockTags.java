package com.alltuttasneeds.delights.datagen;

import com.alltuttasneeds.delights.DelightsModule;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DelightsBlockTags extends BlockTagsProvider {
    private static final List<String> FEASTS = List.of(
            "suckling_pig_with_vegetables_block",
            "potato_and_meat_pie_block",
            "ancient_soft-boiled_egg_and_breads_block",
            "ice_cream_in_a_pumpkin_head_block",
            "mint_roulette_block",
            "slime_jelly_block"
    );

    public DelightsBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                             @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DelightsModule.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE);
        var feasts = tag(ModTags.Blocks.FEASTS);
        var mineableWithKnife = tag(ModTags.Blocks.MINEABLE_WITH_KNIFE);
        for (String path : FEASTS) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(DelightsModule.MODID, path);
            feasts.addOptional(id);
            mineableWithKnife.addOptional(id);
        }
        tag(ModTags.Blocks.PIES).addOptional(
                ResourceLocation.fromNamespaceAndPath(DelightsModule.MODID, "potato_and_meat_pie_block"));
    }
}
