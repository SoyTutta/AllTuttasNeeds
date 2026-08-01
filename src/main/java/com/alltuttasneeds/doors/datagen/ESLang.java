package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.doors.compat.CompatRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ESLang extends LanguageProvider {

    private static final Set<String> INHERITED_DOOR_SUFFIXES = Set.of(
            "_discrete_door", "_normal_door", "_indiscrete_door", "_transit_door", "_pet_door"
    );

    private static final String BOOKSHELF_DOOR_SUFFIX = "_bookshelf_door";

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

    private static final Map<String, String> MATERIAL_TRANSLATION_MAP = Map.ofEntries(
            Map.entry("acacia", "acacia"),
            Map.entry("ashroot", "raíz de fresno"),
            Map.entry("aspen", "álamo temblón"),
            Map.entry("azalea", "azalea"),
            Map.entry("bamboo", "bambú"),
            Map.entry("birch", "abedul"),
            Map.entry("cherry", "cerezo"),
            Map.entry("crimson", "carmesí"),
            Map.entry("dark_oak", "roble oscuro"),
            Map.entry("driftwood", "madera flotante"),
            Map.entry("fir", "pícea"),
            Map.entry("gourdrot", "tallo de calabaza"),
            Map.entry("grimwood", "madera lúgubre"),
            Map.entry("jungle", "jungla"),
            Map.entry("kousa", "cornejo kousa"),
            Map.entry("laurel", "laurel"),
            Map.entry("mangrove", "mangle"),
            Map.entry("maple", "arce"),
            Map.entry("morado", "morado"),
            Map.entry("oak", "roble"),
            Map.entry("pine", "pino"),
            Map.entry("plum", "ciruelo"),
            Map.entry("red_bamboo", "bambú rojo"),
            Map.entry("river", "ribera"),
            Map.entry("rosewood", "palisandro"),
            Map.entry("spruce", "abeto"),
            Map.entry("walnut", "nogal"),
            Map.entry("warped", "distorsionada"),
            Map.entry("willow", "sauce"),
            Map.entry("wisteria", "glicina"),
            Map.entry("yucca", "yuca")
    );

    private static final Set<String> DIRECT_MATERIAL_MODIFIERS = Set.of(
            "crimson", "warped"
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

        if (INHERITED_DOOR_SUFFIXES.stream().anyMatch(path::endsWith)) return;

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

        if (path.endsWith(BOOKSHELF_DOOR_SUFFIX)) {
            String materialKey = path.substring(0, path.length() - BOOKSHELF_DOOR_SUFFIX.length());
            String translatedMaterial = MATERIAL_TRANSLATION_MAP.getOrDefault(materialKey, toWords(materialKey, "_"));
            String connector = DIRECT_MATERIAL_MODIFIERS.contains(materialKey) ? " " : " de ";
            add(descriptionId, "Puerta de librería" + connector + translatedMaterial);
            return;
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
