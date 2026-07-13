package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.BedModelNaming;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import com.alltuttasneeds.beds.TBContent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class TBItemsModels extends ItemModelProvider {
    private static final ResourceLocation FRAME_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("tuttasbeds", "block/frame/" + DyeColor.BROWN.getSerializedName());

    public TBItemsModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "tuttasbeds", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        if (TBContent.BED_FRAME_ITEM != null) {
            registerFrame(TBContent.BED_FRAME_ITEM.get(), TBContent.BED_FRAME.get());
        }

        BedCompatRegistry.loaded().flatMap(compat -> compat.families().stream()).forEach(family -> {
            registerMattress(family.looseMattress());
            family.looseMattressCovers().values().forEach(this::registerMattress);

            registerBed(family.bedBare());
            family.bedBasicCovers().values().forEach(this::registerBed);

            family.bedBlankets().values().forEach(colors -> colors.values().forEach(this::registerBed));
            family.bedDeluxe().values().forEach(this::registerBed);
        });
    }

    private void registerFrame(Item item, Block block) {
        withExistingParent(itemName(item), modLoc("item/template/frame")).texture("0", BedModelNaming.blockTexture(block));
    }

    private void registerMattress(Supplier<Block> block) {
        withExistingParent(itemName(block.get().asItem()), modLoc("item/template/mattress"))
                .texture("0", BedModelNaming.blockTexture(block.get()));
    }

    private void registerBed(Supplier<Block> block) {
        withExistingParent(itemName(block.get().asItem()), modLoc("item/template/bed"))
                .texture("0", BedModelNaming.blockTexture(block.get()))
                .texture("frame", FRAME_TEXTURE);
    }

    private String itemName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).toString();
    }
}