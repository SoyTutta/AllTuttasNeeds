package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.doors.compat.CompatRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class USLang extends LanguageProvider {

    private static final Set<String> INHERITED_DOOR_SUFFIXES = Set.of(
            "_discrete_door", "_normal_door", "_indiscrete_door", "_transit_door", "_pet_door"
    );

    private final List<DeferredHolder<Block, ? extends Block>> allBlocks = new ArrayList<>();

    public USLang(PackOutput output) {
        super(output, "tuttasdoors", "en_us");

        CompatRegistry.loaded().forEach(compat ->
                compat.blocks().getEntries().forEach(allBlocks::add));
    }

    @Override
    protected void addTranslations() {
        allBlocks.stream()
                .map(DeferredHolder::get)
                .forEach(this::addBlockTranslation);

        add("alltuttasneeds.itemGroup.tuttasdoors", "Tutta's Doors");

        add("alltuttasneeds.tooltip.style.secret", "Secret Door: Hidden Entrance, Obvious Exit.");

        add("alltuttasneeds.tooltip.style.discreet", "Discreet Door: Full Privacy.");
        add("alltuttasneeds.tooltip.style.indiscreet", "Indiscreet Door: Open View.");
        add("alltuttasneeds.tooltip.style.normal", "Classic Door: Peek Outside.");

        add("alltuttasneeds.tooltip.style.transit", "Transit Door: Hands-Free Access.");
        add("alltuttasneeds.tooltip.style.pet", "Pet Door: Companion Access.");

        add("alltuttasneeds.tooltip.style.sliding", "Sliding Door: Lateral Track.");
    }

    private void addBlockTranslation(Block block) {
        String descriptionId = block.getDescriptionId();
        String path = block.builtInRegistryHolder().key().location().getPath();

        if (INHERITED_DOOR_SUFFIXES.stream().anyMatch(path::endsWith)) return;

        String slidingSuffix = "_sliding_door";
        if (path.endsWith(slidingSuffix)) {
            String materialName = path.substring(0, path.length() - slidingSuffix.length());
            add(descriptionId, toTitleCase(materialName, "_") + " Door");
            return;
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
