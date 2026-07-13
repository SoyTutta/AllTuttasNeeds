package com.alltuttasneeds.beds.compat;

import com.alltuttasneeds.beds.BedTier;
import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.MattressFamily;
import com.alltuttasneeds.beds.MattressMaterial;
import com.alltuttasneeds.beds.block.BedFrameBlock;
import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TieredBedBlock;
import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class BedRegistrar {
    private BedRegistrar() {}

    public static BlockBehaviour.Properties createMattressProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOL)
                .sound(SoundType.WOOL)
                .strength(0.2F)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .ignitedByLava();
    }

    public static BlockBehaviour.Properties createBedProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOL)
                .sound(SoundType.WOOD)
                .strength(0.2F)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .ignitedByLava();
    }

    public static List<MattressFamily> registerFamilies(
            DeferredRegister<Block> blocks, DeferredRegister<Item> items,
            List<MattressMaterial> materials, List<CoverMaterial> covers, List<BlanketMaterial> blankets,
            BlockBehaviour.Properties mattressProperties, BlockBehaviour.Properties bedProperties
    ) {
        List<MattressFamily> families = new ArrayList<>();
        for (MattressMaterial material : materials) {
            if (material.isEnabled()) {
                families.add(registerFamily(blocks, items, material, covers, blankets, mattressProperties, bedProperties));
            }
        }
        return List.copyOf(families);
    }

    public static MattressFamily registerFamily(
            DeferredRegister<Block> blocks, DeferredRegister<Item> items,
            MattressMaterial material, List<CoverMaterial> covers, List<BlanketMaterial> blankets,
            BlockBehaviour.Properties mattressProperties, BlockBehaviour.Properties bedProperties
    ) {
        String id = material.id();

        Map<BlanketMaterial, Map<DyeColor, Supplier<Block>>> bedBlankets = new LinkedHashMap<>();
        Map<DyeColor, Supplier<Block>> deluxe = new EnumMap<>(DyeColor.class);

        for (BlanketMaterial blanket : blankets) {
            if (!blanket.isEnabled()) continue;

            Map<DyeColor, Supplier<Block>> colors = new EnumMap<>(DyeColor.class);
            for (DyeColor color : DyeColor.values()) {
                colors.put(color, registerBlockItem(blocks, items, id + "_bed_" + color.getSerializedName() + "_" + blanket.suffix(),
                        () -> new TieredBedBlock(material, BedTier.NORMAL, null, blanket, color, Map.of(), Map.of(), bedProperties)));
            }
            bedBlankets.put(blanket, colors);

            if (blanket.supportsDeluxe() && TBConfig.deluxeTierEnabled.get()) {
                for (DyeColor color : DyeColor.values()) {
                    deluxe.put(color, registerBlockItem(blocks, items, id + "_bed_" + color.getSerializedName() + "_deluxe",
                            () -> new TieredBedBlock(material, BedTier.DELUXE, null, blanket, color, Map.of(), Map.of(), bedProperties)));
                }
            }
        }

        Map<CoverMaterial, Supplier<Block>> bedCovers = new LinkedHashMap<>();
        for (CoverMaterial cover : covers) {
            if (!cover.isEnabled()) continue;
            bedCovers.put(cover, registerBlockItem(blocks, items, id + "_bed_" + cover.suffix(),
                    () -> new TieredBedBlock(material, BedTier.LOW, cover, null, null, Map.of(), Map.of(), bedProperties)));
        }

        Supplier<Block> bedBare = blocks.register(id + "_bed",
                () -> new TieredBedBlock(material, BedTier.BASIC, null, null, null, bedCovers, bedBlankets, bedProperties));
        items.register(id + "_bed", () -> new BlockItem(bedBare.get(), new Item.Properties()));

        Map<CoverMaterial, Supplier<Block>> mattressCovers = new LinkedHashMap<>();
        for (CoverMaterial cover : covers) {
            if (!cover.isEnabled()) continue;
            Supplier<Block> coveredBed = bedCovers.get(cover);
            mattressCovers.put(cover, registerBlockItem(blocks, items, id + "_mattress_" + cover.suffix(),
                    () -> new LooseMattressBlock(material, cover, Map.of(), coveredBed, mattressProperties),
                    item -> BedFrameBlock.registerMattressResult(item, coveredBed)));
        }

        Supplier<Block> looseMattress = blocks.register(id + "_mattress",
                () -> new LooseMattressBlock(material, null, mattressCovers, bedBare, mattressProperties));
        Supplier<Item> looseMattressItem = items.register(id + "_mattress",
                () -> new BlockItem(looseMattress.get(), new Item.Properties()));

        BedFrameBlock.registerMattressResult(looseMattressItem, bedBare);

        return new MattressFamily(material, looseMattress, mattressCovers, bedBare, bedCovers, bedBlankets, deluxe);
    }

    private static Supplier<Block> registerBlockItem(DeferredRegister<Block> blocks, DeferredRegister<Item> items,
                                                     String name, Supplier<? extends Block> factory) {
        return registerBlockItem(blocks, items, name, factory, item -> {});
    }

    private static Supplier<Block> registerBlockItem(DeferredRegister<Block> blocks, DeferredRegister<Item> items,
                                                     String name, Supplier<? extends Block> factory,
                                                     Consumer<Supplier<Item>> onItemRegistered) {
        Supplier<Block> block = blocks.register(name, factory);
        Supplier<Item> item = items.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        onItemRegistered.accept(item);
        return block;
    }
}
