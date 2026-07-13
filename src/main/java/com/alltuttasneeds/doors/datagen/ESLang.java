package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.doors.compat.CompatRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
            Map.entry("brass", "latón"),
            Map.entry("entwine", "entrelazados"),
            Map.entry("sunmetal", "metal solar"),
            Map.entry("silver", "plata"),
            Map.entry("tin", "estaño")
    );

    private static final Set<String> DIRECT_METAL_MODIFIERS = Set.of("entwine");

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
        MATERIAL_TRANSLATION_MAP.put("warped", "distorsionada");
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

        MATERIAL_TRANSLATION_MAP.put("ashroot", "raíz de fresno");
        MATERIAL_TRANSLATION_MAP.put("gourdrot", "tallo de calabaza");
        MATERIAL_TRANSLATION_MAP.put("red_bamboo", "bambú rojo");

        MATERIAL_TRANSLATION_MAP.put("azalea", "azalea");
        MATERIAL_TRANSLATION_MAP.put("cork", "corcho");
        MATERIAL_TRANSLATION_MAP.put("date", "palmera datilera");
        MATERIAL_TRANSLATION_MAP.put("driftwood", "madera flotante");
        MATERIAL_TRANSLATION_MAP.put("empyreal", "empírea");
        MATERIAL_TRANSLATION_MAP.put("grimwood", "madera lúgubre");
        MATERIAL_TRANSLATION_MAP.put("kousa", "cornejo kousa");
        MATERIAL_TRANSLATION_MAP.put("laurel", "laurel");
        MATERIAL_TRANSLATION_MAP.put("morado", "morado");
        MATERIAL_TRANSLATION_MAP.put("pale_oak", "roble pálido");
        MATERIAL_TRANSLATION_MAP.put("plum", "ciruelo");
        MATERIAL_TRANSLATION_MAP.put("river", "ribera");
        MATERIAL_TRANSLATION_MAP.put("rosewood", "palisandro");
        MATERIAL_TRANSLATION_MAP.put("rotten", "podrida");
        MATERIAL_TRANSLATION_MAP.put("twisted", "retorcida");
        MATERIAL_TRANSLATION_MAP.put("walnut", "nogal");
        MATERIAL_TRANSLATION_MAP.put("yucca", "yuca");

        MATERIAL_TRANSLATION_MAP.put("runewood", "madera rúnica");
        MATERIAL_TRANSLATION_MAP.put("runewood_boards", "tablas de madera rúnica");
        MATERIAL_TRANSLATION_MAP.put("bolted_runewood", "madera rúnica reforzada");
        MATERIAL_TRANSLATION_MAP.put("bolted_runewood_boards", "tablas de madera rúnica reforzadas");
        MATERIAL_TRANSLATION_MAP.put("soulwood", "madera de alma");
        MATERIAL_TRANSLATION_MAP.put("soulwood_boards", "tablas de madera de alma");
        MATERIAL_TRANSLATION_MAP.put("bolted_soulwood", "madera de alma reforzada");
        MATERIAL_TRANSLATION_MAP.put("bolted_soulwood_boards", "tablas de madera de alma reforzadas");
    }

    private static final Set<String> DIRECT_MATERIAL_MODIFIERS = Set.of(
            "crimson", "warped", "veiled", "celestial", "dead",
            "magic", "umbran", "empyreal", "rotten", "twisted"
    );

    private final List<DeferredHolder<Block, ? extends Block>> allBlocks = new ArrayList<>();

    public ESLang(PackOutput output) {
        super(output, "tuttasdoors", "es_es");

        CompatRegistry.loaded().forEach(compat ->
                compat.blocks().getEntries().forEach(allBlocks::add));
    }

    @Override
    protected void addTranslations() {
        allBlocks.stream()
                .map(DeferredHolder::get)
                .forEach(this::addBlockTranslation);

        add("alltuttasneeds.itemGroup.tuttasdoors", "Tutta's Doors");

        add("alltuttasneeds.tooltip.style.secret", "Puerta secreta: oculta por fuera, evidente por dentro.");

        add("alltuttasneeds.tooltip.style.discreet", "Puerta discreta: máxima privacidad.");
        add("alltuttasneeds.tooltip.style.indiscreet", "Puerta indiscreta: vista completa al exterior.");
        add("alltuttasneeds.tooltip.style.normal", "Puerta clásica: ligera vista al exterior.");

        add("alltuttasneeds.tooltip.style.transit", "Puerta de tránsito: acceso sin manos.");
        add("alltuttasneeds.tooltip.style.pet", "Puerta para mascotas: acceso para compañeros.");

        add("alltuttasneeds.tooltip.style.sliding", "Puerta corrediza: se desliza por un riel.");
    }

    private void addBlockTranslation(Block block) {
        String descriptionId = block.getDescriptionId();
        String path = block.builtInRegistryHolder().key().location().getPath();

        String metalSuffix = "_bars_sliding_door";
        if (path.endsWith(metalSuffix)) {
            String materialKey = path.substring(0, path.length() - metalSuffix.length());
            if (METAL_MATERIAL_TRANSLATION_MAP.containsKey(materialKey)) {
                String translatedMaterial = METAL_MATERIAL_TRANSLATION_MAP.get(materialKey);
                String connector = DIRECT_METAL_MODIFIERS.contains(materialKey) ? " " : " de ";
                String finalName = "Puerta de barrotes" + connector + translatedMaterial;
                add(descriptionId, finalName);
                return;
            }
        }

        for (Map.Entry<String, String> entry : DOOR_TYPE_MAP.entrySet()) {
            String suffix = entry.getKey();
            if (path.endsWith(suffix)) {
                String materialKey = path.substring(0, path.length() - suffix.length());
                String translatedMaterial = MATERIAL_TRANSLATION_MAP.getOrDefault(materialKey, toWords(materialKey, "_"));
                String doorType = entry.getValue();
                String connector = DIRECT_MATERIAL_MODIFIERS.contains(materialKey) ? " " : " de ";
                String finalName = (doorType + connector + translatedMaterial).trim();
                add(descriptionId, finalName);
                return;
            }
        }

        add(descriptionId, toSentenceCase(path, "_"));
    }

    private static String toWords(String givenString, String regex) {
        if (givenString == null || givenString.isEmpty()) return "";
        return String.join(" ", givenString.split(regex)).toLowerCase(Locale.ROOT);
    }

    private static String toSentenceCase(String givenString, String regex) {
        String words = toWords(givenString, regex);
        return words.isEmpty() ? words : Character.toUpperCase(words.charAt(0)) + words.substring(1);
    }
}
