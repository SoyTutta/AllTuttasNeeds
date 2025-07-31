package com.alltuttasneeds.core.data.langs;

import com.alltuttasneeds.registry.TDContent;
import com.alltuttasneeds.registry.compat.*;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ESLang extends LanguageProvider {

    private static final Map<String, String> DOOR_TYPE_MAP = new LinkedHashMap<>();
    static {
        DOOR_TYPE_MAP.put("_bookshelf_door", "Puerta de librería");
        DOOR_TYPE_MAP.put("_discrete_door", "Puerta");
        DOOR_TYPE_MAP.put("_indiscrete_door", "Puerta");
        DOOR_TYPE_MAP.put("_transit_door", "Puerta");
        DOOR_TYPE_MAP.put("_pet_door", "Puerta");
        DOOR_TYPE_MAP.put("_normal_door", "Puerta");
    }

    private static final Map<String, String> METAL_MATERIAL_TRANSLATION_MAP = Map.ofEntries(
            Map.entry("iron", "hierro"),
            Map.entry("golden", "oro"),
            Map.entry("copper", "cobre"),
            Map.entry("exposed_copper", "cobre expuesto"),
            Map.entry("weathered_copper", "cobre desgastado"),
            Map.entry("oxidized_copper", "cobre oxidado"),
            Map.entry("waxed_copper", "cobre encerado"),
            Map.entry("waxed_exposed_copper", "cobre expuesto y encerado"),
            Map.entry("waxed_weathered_copper", "cobre desgastado y encerado"),
            Map.entry("waxed_oxidized_copper", "cobre oxidado y encerado"),
            Map.entry("andesite", "andesita"),
            Map.entry("brass", "latón")
    );

    private static final Map<String, String> MATERIAL_TRANSLATION_MAP = new LinkedHashMap<>();
    static {
        MATERIAL_TRANSLATION_MAP.put("oak", "roble");
        MATERIAL_TRANSLATION_MAP.put("spruce", "abeto");
        MATERIAL_TRANSLATION_MAP.put("birch", "abedul");
        MATERIAL_TRANSLATION_MAP.put("jungle", "jungla");
        MATERIAL_TRANSLATION_MAP.put("acacia", "acacia");
        MATERIAL_TRANSLATION_MAP.put("dark_oak", "roble oscuro");
        MATERIAL_TRANSLATION_MAP.put("mangrove", "mangle");
        MATERIAL_TRANSLATION_MAP.put("cherry", "cerezo");
        MATERIAL_TRANSLATION_MAP.put("crimson", "carmesí");
        MATERIAL_TRANSLATION_MAP.put("warped", "distorsionado");
        MATERIAL_TRANSLATION_MAP.put("bamboo", "bambú");

        MATERIAL_TRANSLATION_MAP.put("powdery", "cañon polvoriento");

        MATERIAL_TRANSLATION_MAP.put("veiled", "velada");
        MATERIAL_TRANSLATION_MAP.put("celestial", "celestial");
        MATERIAL_TRANSLATION_MAP.put("murublight", "murutación");

        MATERIAL_TRANSLATION_MAP.put("pine", "pino");
        MATERIAL_TRANSLATION_MAP.put("redwood", "secoya");
        MATERIAL_TRANSLATION_MAP.put("mahogany", "caoba");
        MATERIAL_TRANSLATION_MAP.put("jacaranda", "jacarandá");
        MATERIAL_TRANSLATION_MAP.put("palm", "palmera");
        MATERIAL_TRANSLATION_MAP.put("willow", "sauce");
        MATERIAL_TRANSLATION_MAP.put("dead", "muerta");
        MATERIAL_TRANSLATION_MAP.put("magic", "mágica");
        MATERIAL_TRANSLATION_MAP.put("umbran", "sombría");
        MATERIAL_TRANSLATION_MAP.put("hellbark", "corteza infernal");

        MATERIAL_TRANSLATION_MAP.put("aspen", "álamo temblón");
        MATERIAL_TRANSLATION_MAP.put("cedar", "cedro");
        MATERIAL_TRANSLATION_MAP.put("coconut", "coco");
        MATERIAL_TRANSLATION_MAP.put("cypress", "ciprés");
        MATERIAL_TRANSLATION_MAP.put("fir", "pícea");
        MATERIAL_TRANSLATION_MAP.put("ghaf", "ghaf");
        MATERIAL_TRANSLATION_MAP.put("joshua", "joshua");
        MATERIAL_TRANSLATION_MAP.put("larch", "alerce");
        MATERIAL_TRANSLATION_MAP.put("maple", "arce");
        MATERIAL_TRANSLATION_MAP.put("olive", "olivo");
        MATERIAL_TRANSLATION_MAP.put("palo_verde", "palo verde");
        MATERIAL_TRANSLATION_MAP.put("saxaul", "saxaúl");
        MATERIAL_TRANSLATION_MAP.put("sugi", "sugi");
        MATERIAL_TRANSLATION_MAP.put("wisteria", "glicina");

        MATERIAL_TRANSLATION_MAP.put("walnut", "nogal");
    }

    private final List<DeferredRegister<Block>> allBlockRegistries = new ArrayList<>();

    public ESLang(PackOutput output) {
        super(output, "tuttasdoors", "es_es");

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

        add("alltuttasneeds:tooltip.tuttasdoors.secret", "Puerta Secreta: Oculta por fuera, evidente por dentro");

        add("alltuttasneeds:tooltip.tuttasdoors.discrete", "Puerta Discreta: Máxima privacidad.");
        add("alltuttasneeds:tooltip.tuttasdoors.indiscrete", "Puerta Indiscreta: Vista completa al exterior.");
        add("alltuttasneeds:tooltip.tuttasdoors.normal", "Puerta Clásica: Ligera vista al exterior.");

        add("alltuttasneeds:tooltip.tuttasdoors.transit", "Puerta de Tránsito: Acceso sin manos.");
        add("alltuttasneeds:tooltip.tuttasdoors.pet", "Puerta para Mascotas: Acceso para compañeros.");

        add("alltuttasneeds:tooltip.tuttasdoors.sliding", "Puerta Corrediza: Se desliza por un riel.");
    }

    private void addBlockTranslation(Block block) {
        String descriptionId = block.getDescriptionId();
        String path = block.builtInRegistryHolder().key().location().getPath();

        String metalSuffix = "_bars_sliding_door";
        if (path.endsWith(metalSuffix)) {
            String materialKey = path.substring(0, path.length() - metalSuffix.length());
            if (METAL_MATERIAL_TRANSLATION_MAP.containsKey(materialKey)) {
                String translatedMaterial = METAL_MATERIAL_TRANSLATION_MAP.get(materialKey);
                String finalName = "Puerta de barrotes de " + translatedMaterial;
                add(descriptionId, finalName);
                return;
            }
        }

        for (Map.Entry<String, String> entry : DOOR_TYPE_MAP.entrySet()) {
            String suffix = entry.getKey();
            if (path.endsWith(suffix)) {
                String materialKey = path.substring(0, path.length() - suffix.length());
                String translatedMaterial = MATERIAL_TRANSLATION_MAP.getOrDefault(materialKey, toTitleCase(materialKey, "_"));
                String doorType = entry.getValue();
                String finalName = (doorType + " de " + translatedMaterial).trim();
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