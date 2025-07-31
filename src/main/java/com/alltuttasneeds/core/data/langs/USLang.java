package com.alltuttasneeds.core.data.langs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alltuttasneeds.registry.TDContent;
import com.alltuttasneeds.registry.compat.*;
import com.alltuttasneeds.registry.compat.NWContent;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class USLang extends LanguageProvider {

    private static final Map<String, String> DOOR_TYPE_MAP = new LinkedHashMap<>();
    static {
        DOOR_TYPE_MAP.put("_discrete_door", "Door");
        DOOR_TYPE_MAP.put("_indiscrete_door", "Door");
        DOOR_TYPE_MAP.put("_transit_door", "Door");
        DOOR_TYPE_MAP.put("_pet_door", "Door");
        DOOR_TYPE_MAP.put("_normal_door", "Door");
        DOOR_TYPE_MAP.put("_sliding_door", "Door");
        DOOR_TYPE_MAP.put("_boards", "Board");
    }

    private final List<DeferredRegister<Block>> allBlockRegistries = new ArrayList<>();

    public USLang(PackOutput output) {
        super(output, "tuttasdoors", "en_us");

        allBlockRegistries.add(TDContent.BLOCKS);

        if (Mods.NOMANSLAND.isLoaded()) {
            allBlockRegistries.add(NMLContent.BLOCKS);
        }
        if (Mods.BLOCKBOX.isLoaded()) {
            allBlockRegistries.add(BBContent.BLOCKS);
        }
        if (Mods.NEWWORLD.isLoaded()) {
            allBlockRegistries.add(NWContent.BLOCKS);
        }
        if (Mods.MYNETHERSDELIGHT.isLoaded()) {
            allBlockRegistries.add(MNDContent.BLOCKS);
        }
        if (Mods.ENDERSCAPE.isLoaded()) {
            allBlockRegistries.add(ESContent.BLOCKS);
        }
        if (Mods.ARTS_AND_CRAFTS.isLoaded()) {
            allBlockRegistries.add(ACContent.BLOCKS);
        }
        if (Mods.BIOMESOPLENTY.isLoaded()) {
            allBlockRegistries.add(BoPContent.BLOCKS);
        }
        if (Mods.NATURES_SPIRIT.isLoaded()) {
            allBlockRegistries.add(NSContent.BLOCKS);
        }
        if (Mods.CREATE.isLoaded()) {
            allBlockRegistries.add(CreateContent.BLOCKS);
        }
        if (Mods.MALUM.isLoaded()) {
            allBlockRegistries.add(MalumContent.BLOCKS);
        }
    }

    @Override
    protected void addTranslations() {
        allBlockRegistries.stream()
                .flatMap(deferredRegister -> deferredRegister.getEntries().stream())
                .map(DeferredHolder::get)
                .forEach(this::addBlockTranslation);

        add("alltuttasneeds.itemGroup.tuttasdoors", "Tutta's Doors");

        add("alltuttasneeds.tooltip.style.secret", "Secret Door: Hidden Entrance, Obvious Exit.");

        add("alltuttasneeds.tooltip.style.discreet", "Discreet Door: Full Privacy.");
        add("alltuttasneeds.tooltip.style.indiscreet", "Indiscreet Door: Open View.");
        add("alltuttasneeds.tooltip.style.normal", "Classic Door: Peek Outside.");

        add("alltuttasneeds.tooltip.style.transit", "Transit Door: Hands-Free Access.");
        add("alltuttasneeds.tooltip.style.pet", "Pet Door: Companion Access.");

        add("alltuttasneeds.tooltip.style.sliding", "Sliding Door: Lateral Track");
    }

    private void addBlockTranslation(Block block) {
        String descriptionId = block.getDescriptionId();
        String path = block.builtInRegistryHolder().key().location().getPath();

        for (Map.Entry<String, String> entry : DOOR_TYPE_MAP.entrySet()) {
            String suffix = entry.getKey();
            if (path.endsWith(suffix)) {
                String materialName = path.substring(0, path.length() - suffix.length());
                String formattedMaterial = toTitleCase(materialName, "_");
                String doorType = entry.getValue();

                String finalName = (formattedMaterial + " " + doorType).trim();
                add(descriptionId, finalName);
                return;
            }
        }

        add(descriptionId, toTitleCase(path, "_"));
    }

    public static String toTitleCase(String givenString, String regex) {
        if (givenString == null || givenString.isEmpty()) return "";
        String[] stringArray = givenString.split(regex);
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : stringArray) {
            if (!string.isEmpty()) {
                stringBuilder.append(Character.toUpperCase(string.charAt(0))).append(string.substring(1)).append(" ");
            }
        }
        return stringBuilder.toString().trim();
    }
}