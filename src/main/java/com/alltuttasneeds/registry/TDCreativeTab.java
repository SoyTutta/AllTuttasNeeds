package com.alltuttasneeds.registry;

import com.alltuttasneeds.ATNTextUtils;
import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.registry.compat.framework.CompatRegistry;
import com.alltuttasneeds.registry.compat.framework.DoorVariant;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.WoodFamily;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class TDCreativeTab {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AllTuttasNeeds.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TUTTAS_DOORS = TABS.register("tuttasdoors",
            () -> CreativeModeTab.builder()
                    .title(ATNTextUtils.getTranslation("itemGroup.tuttasdoors"))
                    .icon(() -> {
                        Item icon = BuiltInRegistries.ITEM.get(TDContent.ICON_ITEM.getKey().location());
                        return icon != null ? new ItemStack(icon) : ItemStack.EMPTY;
                    })
                    .displayItems((parameters, output) -> {
                        Map<String, List<Supplier<Item>>> bookshelfDoorsByFamily = collectBookshelfDoors();

                        Set<String> shownFamilies = new HashSet<>();
                        CompatRegistry.loaded().forEach(compat -> compat.woodFamilies().forEach(family -> {
                            if (!shownFamilies.add(family.familyId())) return;

                            bookshelfDoorsByFamily.getOrDefault(family.familyId(), List.of())
                                    .forEach(door -> accept(output, door));

                            for (DoorVariant variant : family.displayOrder()) {
                                accept(output, resolveVariantItem(family, compat, variant));
                            }
                        }));

                        CompatRegistry.loaded().forEach(compat ->
                                compat.extraDoors().forEach(extra ->
                                        accept(output, compat.doorItems().get(extra.name()))));
                    })
                    .build()
    );

    /**
     * Multiple compats can declare a secret door for the same family (e.g. NoMansLand and
     * Woodworks both provide one for {@code "minecraft:spruce"}); all of them should show
     * up if their mods are loaded, hence a list per family instead of a single supplier.
     */
    private static Map<String, List<Supplier<Item>>> collectBookshelfDoors() {
        Map<String, List<Supplier<Item>>> byFamily = new LinkedHashMap<>();
        CompatRegistry.loaded().forEach(compat -> compat.secretDoorFamilies().forEach(secret -> {
            Supplier<Item> doorItem = compat.doorItems().get(secret.woodName() + "_bookshelf_door");
            if (doorItem == null) return;
            byFamily.computeIfAbsent(secret.ownerFamilyId(), key -> new ArrayList<>()).add(doorItem);
        }));
        return byFamily;
    }

    private static Supplier<Item> resolveVariantItem(WoodFamily family, ModCompat compat, DoorVariant variant) {
        if (variant == DoorVariant.ORIGINAL) {
            return () -> family.baseDoor().get().asItem();
        }
        if (variant == DoorVariant.TRAPDOOR) {
            ResourceLocation rl = family.trapdoorLocation();
            Item item = BuiltInRegistries.ITEM.getOptional(rl).orElse(null);
            if (item == null) {
                LOGGER.warn("[Tuttas Doors] Missing trapdoor: {}", rl);
                return null;
            }
            return () -> item;
        }
        return compat.doorItems().get(family.registryName() + "_" + variant.suffix());
    }

    private static void accept(CreativeModeTab.Output output, Supplier<? extends ItemLike> item) {
        if (item != null && item.get() != null) {
            output.accept(item.get());
        }
    }
}
