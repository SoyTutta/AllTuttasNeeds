package com.alltuttasneeds.doors.client;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.doors.compat.CompatRegistry;
import com.alltuttasneeds.doors.compat.DoorVariant;
import com.alltuttasneeds.doors.compat.ExtraDoor;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.SecretDoorFamily;
import com.alltuttasneeds.doors.compat.WoodFamily;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID)
public final class TDCreativeModeTabHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    private TDCreativeModeTabHandler() {}

    @SubscribeEvent
    public static void onBuildContents(final BuildCreativeModeTabContentsEvent event) {
        CompatRegistry.loaded().forEach(compat -> {
            for (WoodFamily family : compat.woodFamilies()) {
                injectWoodFamily(event, family, compat);
            }
            for (SecretDoorFamily secret : compat.secretDoorFamilies()) {
                injectSecretDoor(event, secret, compat);
            }
            for (ExtraDoor extra : compat.extraDoors()) {
                injectExtraDoor(event, extra, compat);
            }
        });
    }

    private static void injectWoodFamily(
            BuildCreativeModeTabContentsEvent event,
            WoodFamily family,
            ModCompat compat
    ) {
        Item baseDoorItem = family.baseDoor().get().asItem();
        ItemStack doorAnchor = baseDoorItem.getDefaultInstance();

        ResourceLocation trapdoorLoc = family.trapdoorLocation();
        Item trapdoorItem = BuiltInRegistries.ITEM.getOptional(trapdoorLoc).orElse(null);
        ItemStack trapdoorAnchor = trapdoorItem != null ? trapdoorItem.getDefaultInstance() : null;

        ResourceLocation fenceGateLoc = ResourceLocation.fromNamespaceAndPath(
                family.familyNamespace(), family.registryName() + "_fence_gate");
        Item fenceGateItem = BuiltInRegistries.ITEM.getOptional(fenceGateLoc).orElse(null);

        boolean doorInTab      = anchorPresent(event, doorAnchor);
        boolean trapdoorInTab  = trapdoorAnchor != null && anchorPresent(event, trapdoorAnchor);
        boolean fenceGateInTab = fenceGateItem != null && anchorPresent(event, fenceGateItem.getDefaultInstance());

        if (!doorInTab && !trapdoorInTab && !fenceGateInTab) {
            LOGGER.debug("[Tuttas Doors] SKIP tab={} — no anchor for family {}",
                    event.getTabKey().location(), family.familyId());
            return;
        }

        List<DoorVariant> order = family.displayOrder();
        int originalIdx = order.indexOf(DoorVariant.ORIGINAL);
        int trapdoorIdx = order.indexOf(DoorVariant.TRAPDOOR);

        List<ItemStack> beforeDoor = new ArrayList<>();
        List<ItemStack> afterDoor = new ArrayList<>();
        List<ItemStack> afterTrapdoor = new ArrayList<>();

        for (int i = 0; i < order.size(); i++) {
            DoorVariant variant = order.get(i);
            if (!variant.isRegistrable()) continue;

            String key = family.registryName() + "_" + variant.suffix();
            Supplier<Item> supplier = compat.doorItems().get(key);
            if (supplier == null) {
                LOGGER.warn("[Tuttas Doors] MISSING key '{}' in doorItems for compat '{}' (family: {})",
                        key, compat.namespace(), family.familyId());
                continue;
            }
            Item item = supplier.get();
            if (item == null) continue;

            ItemStack stack = item.getDefaultInstance();

            if (trapdoorIdx >= 0 && i > trapdoorIdx) {
                afterTrapdoor.add(stack);
            } else if (doorInTab && originalIdx >= 0 && i < originalIdx) {
                beforeDoor.add(stack);
            } else {
                afterDoor.add(stack);
            }
        }

        if (doorInTab && !beforeDoor.isEmpty()) {
            for (ItemStack stack : beforeDoor) {
                insertBefore(event, doorAnchor, stack);
            }
        }

        if (!afterDoor.isEmpty()) {
            ItemStack anchor = doorInTab      ? doorAnchor
                    : trapdoorInTab  ? trapdoorAnchor
                    : fenceGateInTab ? fenceGateItem.getDefaultInstance()
                    : null;
            if (anchor != null) {
                for (int i = afterDoor.size() - 1; i >= 0; i--) {
                    insertAfter(event, anchor, afterDoor.get(i));
                }
            }
        }

        if (!afterTrapdoor.isEmpty()) {
            ItemStack anchor = trapdoorInTab  ? trapdoorAnchor
                    : doorInTab      ? doorAnchor
                    : fenceGateInTab ? fenceGateItem.getDefaultInstance()
                    : null;
            if (anchor != null) {
                for (int i = afterTrapdoor.size() - 1; i >= 0; i--) {
                    insertAfter(event, anchor, afterTrapdoor.get(i));
                }
            }
        }
    }

    private static void injectSecretDoor(
            BuildCreativeModeTabContentsEvent event,
            SecretDoorFamily secret,
            ModCompat compat
    ) {
        Item bookshelfItem = secret.bookshelf().get().asItem();
        ItemStack anchor = bookshelfItem.getDefaultInstance();

        if (!anchorPresent(event, anchor)) return;

        String name = secret.woodName() + "_bookshelf_door";
        Supplier<Item> supplier = compat.doorItems().get(name);
        if (supplier == null) return;

        Item doorItem = supplier.get();
        if (doorItem != null) {
            insertAfter(event, anchor, doorItem.getDefaultInstance());
        }
    }

    private static void injectExtraDoor(
            BuildCreativeModeTabContentsEvent event,
            ExtraDoor extra,
            ModCompat compat
    ) {
        ResourceLocation anchorLoc = extra.creativeTabAnchor();
        if (anchorLoc == null) return;

        Item anchorItem = BuiltInRegistries.ITEM.getOptional(anchorLoc).orElse(null);
        if (anchorItem == null) return;

        ItemStack anchor = anchorItem.getDefaultInstance();
        if (!anchorPresent(event, anchor)) return;

        Supplier<Item> supplier = compat.doorItems().get(extra.name());
        if (supplier == null) return;

        Item doorItem = supplier.get();
        if (doorItem != null) {
            insertAfter(event, anchor, doorItem.getDefaultInstance());
        }
    }

    @Nullable
    private static CreativeModeTab.TabVisibility resolveAnchorVisibility(
            BuildCreativeModeTabContentsEvent event, ItemStack anchor
    ) {
        boolean inParent = tabContains(event.getParentEntries(), anchor);
        boolean inSearch = tabContains(event.getSearchEntries(), anchor);
        if (inParent && inSearch) return CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
        if (inParent) return CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
        if (inSearch) return CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY;
        return null;
    }

    private static boolean anchorPresent(BuildCreativeModeTabContentsEvent event, ItemStack anchor) {
        return tabContains(event.getParentEntries(), anchor) || tabContains(event.getSearchEntries(), anchor);
    }

    private static boolean visibilityIncludesParent(CreativeModeTab.TabVisibility visibility) {
        return visibility == CreativeModeTab.TabVisibility.PARENT_TAB_ONLY
                || visibility == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
    }

    private static boolean visibilityIncludesSearch(CreativeModeTab.TabVisibility visibility) {
        return visibility == CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY
                || visibility == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
    }

    private static boolean alreadyPresentFor(
            BuildCreativeModeTabContentsEvent event, ItemStack stack, CreativeModeTab.TabVisibility visibility
    ) {
        if (visibilityIncludesParent(visibility) && tabContains(event.getParentEntries(), stack)) return true;
        return visibilityIncludesSearch(visibility) && tabContains(event.getSearchEntries(), stack);
    }

    private static void insertAfter(
            BuildCreativeModeTabContentsEvent event,
            ItemStack anchor,
            ItemStack newItem
    ) {
        CreativeModeTab.TabVisibility visibility = resolveAnchorVisibility(event, anchor);
        if (visibility == null) {
            LOGGER.warn("[Tuttas Doors] tab={} | anchor {} no longer present when inserting {} — skipping",
                    event.getTabKey().location(),
                    BuiltInRegistries.ITEM.getKey(anchor.getItem()),
                    BuiltInRegistries.ITEM.getKey(newItem.getItem()));
            return;
        }
        if (alreadyPresentFor(event, newItem, visibility)) return;
        event.insertAfter(anchor, newItem, visibility);
    }

    private static void insertBefore(
            BuildCreativeModeTabContentsEvent event,
            ItemStack anchor,
            ItemStack newItem
    ) {
        CreativeModeTab.TabVisibility visibility = resolveAnchorVisibility(event, anchor);
        if (visibility == null) {
            LOGGER.warn("[Tuttas Doors] tab={} | anchor {} no longer present when inserting {} — skipping",
                    event.getTabKey().location(),
                    BuiltInRegistries.ITEM.getKey(anchor.getItem()),
                    BuiltInRegistries.ITEM.getKey(newItem.getItem()));
            return;
        }
        if (alreadyPresentFor(event, newItem, visibility)) return;
        event.insertBefore(anchor, newItem, visibility);
    }

    private static boolean tabContains(Iterable<ItemStack> entries, ItemStack target) {
        for (ItemStack stack : entries) {
            if (ItemStack.isSameItemSameComponents(stack, target)) return true;
        }
        return false;
    }
}
