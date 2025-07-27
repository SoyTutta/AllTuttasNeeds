package com.alltuttasneeds.core.data;

import com.alltuttasneeds.registry.TDContent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.*;
import java.util.function.Supplier;

public class TDItemsModels extends ItemModelProvider {
    public static final String GENERATED = "item/generated";

    public TDItemsModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "tuttasdoors", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerFromMap(TDContent.DOOR_ITEMS, "tuttasdoors");
    }

    private void registerFromMap(Map<String, Supplier<Item>> itemMap, String modNamespace) {
        for (Map.Entry<String, Supplier<Item>> entry : itemMap.entrySet()) {
            String name = entry.getKey();
            Item item = entry.getValue().get();
            itemGeneratedModel(item, resourceItem(name, modNamespace));
        }
    }

    public void itemGeneratedModel(Item item, ResourceLocation texture) {
        withExistingParent(itemName(item), GENERATED).texture("layer0", texture);
    }

    private String itemName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    private ResourceLocation resourceItem(String name, String modNamespace) {
        return ResourceLocation.fromNamespaceAndPath(modNamespace, "item/" + name);
    }
}