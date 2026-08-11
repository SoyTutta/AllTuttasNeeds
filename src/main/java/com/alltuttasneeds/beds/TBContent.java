package com.alltuttasneeds.beds;

import com.alltuttasneeds.beds.block.BedFrameBlock;
import com.alltuttasneeds.beds.config.TBConfig;
import com.alltuttasneeds.beds.compat.BedModCompat;
import com.alltuttasneeds.beds.compat.BedRegistrar;
import com.alltuttasneeds.beds.item.BedBlanketItem;
import com.alltuttasneeds.beds.item.BedBlanketItem.BlanketKind;
import com.alltuttasneeds.core.Mods;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class TBContent implements BedModCompat {
    public static final TBContent INSTANCE = new TBContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "tuttasbeds");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "tuttasbeds");
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, "tuttasbeds");

    public static final Supplier<RecipeSerializer<BedDyeRecipe>> BED_DYE_SERIALIZER =
            TBConfig.moduleEnabled.get()
                    ? RECIPE_SERIALIZERS.register("bed_dye", () -> new SimpleCraftingRecipeSerializer<>(BedDyeRecipe::new))
                    : () -> { throw new IllegalStateException("Tutta's Beds is disabled"); };
    public static final Supplier<RecipeSerializer<BedBlanketRecipe>> BED_BLANKET_SERIALIZER =
            TBConfig.moduleEnabled.get()
                    ? RECIPE_SERIALIZERS.register("bed_blanket", () -> new SimpleCraftingRecipeSerializer<>(BedBlanketRecipe::new))
                    : () -> { throw new IllegalStateException("Tutta's Beds is disabled"); };

    private static final BlockBehaviour.Properties MATTRESS_PROPERTIES = BedRegistrar.createMattressProperties();
    private static final BlockBehaviour.Properties BED_PROPERTIES = BedRegistrar.createBedProperties();

    private static final BlockBehaviour.Properties FRAME_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .sound(SoundType.WOOD)
            .strength(2.0F)
            .noOcclusion();

    @Nullable
    public static final Supplier<Block> BED_FRAME = TBConfig.moduleEnabled.get()
            ? BLOCKS.register("bed_frame", () -> new BedFrameBlock(FRAME_PROPERTIES))
            : null;
    @Nullable
    public static final Supplier<Item> BED_FRAME_ITEM = BED_FRAME != null
            ? ITEMS.register("bed_frame", () -> new BlockItem(BED_FRAME.get(), new Item.Properties()))
            : null;

    private static final MattressMaterial WHEAT = new MattressMaterial("wheat",
            () -> TBConfig.moduleEnabled.get() && TBConfig.strawMattressEnabled.get());
    private static final MattressMaterial SOFT = new MattressMaterial("soft",
            () -> TBConfig.moduleEnabled.get() && TBConfig.softMattressEnabled.get());

    private static final CoverMaterial WHEAT_COVER = new CoverMaterial("wheat_cover",
            () -> TBConfig.moduleEnabled.get() && TBConfig.wheatCoverEnabled.get());
    private static final CoverMaterial LEATHER_COVER = new CoverMaterial("leather_cover",
            () -> TBConfig.moduleEnabled.get() && TBConfig.leatherCoverEnabled.get());

    private static final BlanketMaterial WOOL_BLANKET = new BlanketMaterial("wool_blanket", true,
            () -> TBConfig.moduleEnabled.get() && TBConfig.woolBlanketEnabled.get());
    private static final BlanketMaterial LEATHER_BLANKET = new BlanketMaterial("leather_blanket", false,
            () -> TBConfig.moduleEnabled.get() && TBConfig.leatherBlanketEnabled.get());

    private static final Map<DyeColor, Supplier<Item>> WOOL_BLANKET_ITEM_ENTRIES = new EnumMap<>(DyeColor.class);
    private static final Map<DyeColor, Supplier<Item>> LEATHER_BLANKET_ITEM_ENTRIES = new EnumMap<>(DyeColor.class);
    private static final Map<DyeColor, Supplier<Item>> DELUXE_WOOL_BLANKET_ITEM_ENTRIES = new EnumMap<>(DyeColor.class);

    public static final Map<DyeColor, Supplier<Item>> WOOL_BLANKET_ITEMS =
            Collections.unmodifiableMap(WOOL_BLANKET_ITEM_ENTRIES);
    public static final Map<DyeColor, Supplier<Item>> LEATHER_BLANKET_ITEMS =
            Collections.unmodifiableMap(LEATHER_BLANKET_ITEM_ENTRIES);
    public static final Map<DyeColor, Supplier<Item>> DELUXE_WOOL_BLANKET_ITEMS =
            Collections.unmodifiableMap(DELUXE_WOOL_BLANKET_ITEM_ENTRIES);

    private static final List<MattressMaterial> MATERIALS = List.of(WHEAT, SOFT);
    private static final List<CoverMaterial> COVERS = List.of(WHEAT_COVER, LEATHER_COVER);

    private static final List<BlanketMaterial> BLANKETS = List.of(LEATHER_BLANKET, WOOL_BLANKET);

    private static List<MattressFamily> families = List.of();

    private TBContent() {}

    public static void registerBlanketItemsForFamilies(List<MattressFamily> registeredFamilies) {
        if (!TBConfig.moduleEnabled.get() || !TBConfig.blanketItemsEnabled.get()) return;

        registerBlanketItemsIfSupported(WOOL_BLANKET_ITEM_ENTRIES, BlanketKind.WOOL, registeredFamilies);
        registerBlanketItemsIfSupported(LEATHER_BLANKET_ITEM_ENTRIES, BlanketKind.LEATHER, registeredFamilies);
        registerBlanketItemsIfSupported(DELUXE_WOOL_BLANKET_ITEM_ENTRIES, BlanketKind.DELUXE_WOOL, registeredFamilies);
    }

    private static void registerBlanketItemsIfSupported(Map<DyeColor, Supplier<Item>> items, BlanketKind kind,
                                                         List<MattressFamily> registeredFamilies) {
        if (!items.isEmpty() || !hasRegisteredVariant(kind, registeredFamilies)) return;

        for (DyeColor color : DyeColor.values()) {
            String id = color.getSerializedName() + "_" + kind.id();
            items.put(color, ITEMS.register(id, () -> new BedBlanketItem(kind, color, new Item.Properties())));
        }
    }

    private static boolean hasRegisteredVariant(BlanketKind kind, List<MattressFamily> registeredFamilies) {
        return registeredFamilies.stream().anyMatch(family -> {
            boolean hasBlanket = family.bedBlankets().keySet().stream()
                    .anyMatch(blanket -> blanket.suffix().equals(kind.blanketSuffix()));
            return hasBlanket && (!kind.isDeluxe() || !family.bedDeluxe().isEmpty());
        });
    }

    public static Stream<Supplier<Item>> blanketItems() {
        return Stream.of(WOOL_BLANKET_ITEMS, LEATHER_BLANKET_ITEMS, DELUXE_WOOL_BLANKET_ITEMS)
                .flatMap(items -> items.values().stream());
    }

    @Nullable
    public static Item blanketItem(BlanketMaterial blanket, DyeColor color, BedTier tier) {
        Supplier<Item> item;
        if (tier == BedTier.DELUXE) {
            item = DELUXE_WOOL_BLANKET_ITEMS.get(color);
        } else {
            item = switch (blanket.suffix()) {
                case "wool_blanket" -> WOOL_BLANKET_ITEMS.get(color);
                case "leather_blanket" -> LEATHER_BLANKET_ITEMS.get(color);
                default -> null;
            };
        }
        return item == null ? null : item.get();
    }

    @Override
    public Mods mod() {
        return Mods.ALLTUTTASNEEDS;
    }

    @Override
    public String namespace() {
        return "tuttasbeds";
    }

    @Override
    public DeferredRegister<Block> blocks() {
        return BLOCKS;
    }

    @Override
    public DeferredRegister<Item> items() {
        return ITEMS;
    }

    @Override
    public List<MattressMaterial> mattressMaterials() {
        return MATERIALS;
    }

    @Override
    public List<CoverMaterial> coverMaterials() {
        return COVERS;
    }

    @Override
    public List<BlanketMaterial> blanketMaterials() {
        return BLANKETS;
    }

    @Override
    public void registerFamilies(List<CoverMaterial> allCovers, List<BlanketMaterial> allBlankets) {
        if (!TBConfig.moduleEnabled.get()) return;

        families = BedRegistrar.registerFamilies(
                BLOCKS, ITEMS, MATERIALS, allCovers, allBlankets, MATTRESS_PROPERTIES, BED_PROPERTIES);
    }

    @Override
    public List<MattressFamily> families() {
        return families;
    }

    @Override
    public void registerToBus(IEventBus modEventBus) {
        BedModCompat.super.registerToBus(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        BedsLootModifiers.LOOT_MODIFIERS.register(modEventBus);
    }
}
