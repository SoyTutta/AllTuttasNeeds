package com.alltuttasneeds.delights.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Map;

import static java.util.Map.entry;

public class DelightsLangES extends LanguageProvider {
    private static final Map<String, String> TRANSLATIONS = Map.ofEntries(
            entry("block.tuttasdelights.ancient_soft-boiled_egg_and_breads_block", "Huevo ancestral pochado con panes"),
            entry("block.tuttasdelights.feast.need_knives", "Cuchillo"),
            entry("block.tuttasdelights.feast.space_required", "Necesitas más espacio para servir esto."),
            entry("block.tuttasdelights.feast.use_alt_container", "Necesitas un %s o un %s para comer esto."),
            entry("block.tuttasdelights.ice_cream_in_a_pumpkin_head_block", "Helado en cabeza de calabaza"),
            entry("block.tuttasdelights.meat_excess.use_container", "¡Esto necesita algunas verduras más!"),
            entry("block.tuttasdelights.mint_roulette_block", "Ruleta de menta"),
            entry("block.tuttasdelights.needheat.use_container", "Necesitas una fuente de calor para cocinar."),
            entry("block.tuttasdelights.potato_and_meat_pie_block", "Pastel de patata y carne"),
            entry("block.tuttasdelights.slime_jelly_block", "Gelatina de slime"),
            entry("block.tuttasdelights.snowgolemfeast.use_container", "Necesitas un %s para comer esto."),
            entry("block.tuttasdelights.suckling_pig_with_vegetables_block", "Cochinillo con verduras"),
            entry("block.tuttasdelights.tasteless.use_container", "Queda algo de sopa. ¡Prueba añadir agua!"),
            entry("block.tuttasdelights.veggies_excess.use_container", "¡Esto necesita un poco más de carne!"),
            entry("item.brewinandchewin.half_a_ham_and_cheese_sandwich", "Medio sándwich de jamón y queso"),
            entry("item.minecraft.lingering_potion.effect.froggy_potion", "Poción persistente del ritmo del sapo"),
            entry("item.minecraft.lingering_potion.effect.long_froggy_potion", "Poción persistente del ritmo del sapo"),
            entry("item.minecraft.lingering_potion.effect.strong_froggy_potion", "Poción persistente del ritmo del sapo"),
            entry("item.minecraft.potion.effect.froggy_potion", "Poción del ritmo del sapo"),
            entry("item.minecraft.potion.effect.long_froggy_potion", "Poción del ritmo del sapo"),
            entry("item.minecraft.potion.effect.strong_froggy_potion", "Poción del ritmo del sapo"),
            entry("item.minecraft.splash_potion.effect.froggy_potion", "Poción arrojadiza del ritmo del sapo"),
            entry("item.minecraft.splash_potion.effect.long_froggy_potion", "Poción arrojadiza del ritmo del sapo"),
            entry("item.minecraft.splash_potion.effect.strong_froggy_potion", "Poción arrojadiza del ritmo del sapo"),
            entry("item.minecraft.tipped_arrow.effect.froggy_potion", "Flecha del ritmo del sapo"),
            entry("item.minecraft.tipped_arrow.effect.long_froggy_potion", "Flecha del ritmo del sapo"),
            entry("item.minecraft.tipped_arrow.effect.strong_froggy_potion", "Flecha del ritmo del sapo"),
            entry("item.minersdelight.half_a_caveburger", "Media hamburguesa cavernaria"),
            entry("item.minersdelight.half_a_insect_sandwich", "Medio sándwich de insectos"),
            entry("item.minersdelight.half_a_squid_sandwich", "Medio sándwich de calamar"),
            entry("item.minersdelight.half_a_vegan_hamburger", "Media hamburguesa vegana"),
            entry("item.mynethersdelight.half_a_chilidog", "Medio chilidog"),
            entry("item.mynethersdelight.half_a_hotdog", "Medio hotdog"),
            entry("item.mynethersdelight.half_a_hotdog_with_fries_potatoes", "Medio hotdog con patatas fritas"),
            entry("item.mynethersdelight.half_a_hotdog_with_mixed_salad", "Medio hotdog con ensalada mixta"),
            entry("item.mynethersdelight.half_a_hotdog_with_nether_salad", "Medio hotdog con ensalada del Nether"),
            entry("item.mynethersdelight.half_a_nether_burger", "Media hamburguesa del Nether"),
            entry("item.mynethersdelight.hotdog_with_fries_potatoes", "Hotdog con patatas fritas"),
            entry("item.spawn.half_a_tuna_sandwich", "Medio sándwich de atún"),
            entry("item.tuttasdelights.ancient_boiled_egg", "Huevo ancestral cocido"),
            entry("item.tuttasdelights.ancient_breakfast", "Desayuno ancestral"),
            entry("item.tuttasdelights.ancient_egg_sandwich", "Sándwich de huevo ancestral"),
            entry("item.tuttasdelights.ancient_ribs", "Costillas ancestrales"),
            entry("item.tuttasdelights.ancient_ribs_with_seeds", "Costillas ancestrales con semillas"),
            entry("item.tuttasdelights.ancient_salad", "Ensalada ancestral"),
            entry("item.tuttasdelights.apple_popsicle", "Palito de manzana"),
            entry("item.tuttasdelights.baked_potato_slices", "Rodajas de patata horneadas"),
            entry("item.tuttasdelights.beet_and_egg_salad", "Ensalada de remolacha y huevo"),
            entry("item.tuttasdelights.beet_juice", "Zumo de remolacha"),
            entry("item.tuttasdelights.breaded_meat_sandwich", "Sándwich de carne empanada"),
            entry("item.tuttasdelights.breaded_meat_with_mashed_potato", "Carne empanada con puré de patata"),
            entry("item.tuttasdelights.breaded_meat_with_pasta", "Carne empanada con pasta"),
            entry("item.tuttasdelights.broken_bones", "Huesos rotos"),
            entry("item.tuttasdelights.cocoa_popsicle", "Palito de cacao"),
            entry("item.tuttasdelights.cooked_ancient_ribs", "Costillas ancestrales cocinadas"),
            entry("item.tuttasdelights.cooked_breaded_meat", "Carne empanada cocinada"),
            entry("item.tuttasdelights.cooked_frog_leg", "Anca de rana cocinada"),
            entry("item.tuttasdelights.cooked_pasta", "Pasta cocinada"),
            entry("item.tuttasdelights.creeper_salad", "Ensalada de creeper"),
            entry("item.tuttasdelights.disgusting_stew", "Estofado repugnante"),
            entry("item.tuttasdelights.fries_potatoes", "Patatas fritas"),
            entry("item.tuttasdelights.frog_legs_on_a_stick", "Ancas de rana en un palo"),
            entry("item.tuttasdelights.froggle_rice_chowder", "Sopa de arroz y rana"),
            entry("item.tuttasdelights.froggle_sandwich", "Sándwich de rana"),
            entry("item.tuttasdelights.glistering_melon_popsicle", "Palito de sandía reluciente"),
            entry("item.tuttasdelights.glow_berries_popsicle", "Palito de bayas luminosas"),
            entry("item.tuttasdelights.golden_apple_popsicle", "Palito de manzana dorada"),
            entry("item.tuttasdelights.half_a_ancient_egg_sandwich", "Medio sándwich de huevo ancestral"),
            entry("item.tuttasdelights.half_a_bacon_sandwich", "Medio sándwich de panceta"),
            entry("item.tuttasdelights.half_a_breaded_meat_sandwich", "Medio sándwich de carne empanada"),
            entry("item.tuttasdelights.half_a_chicken_sandwich", "Medio sándwich de pollo"),
            entry("item.tuttasdelights.half_a_egg_sandwich", "Medio sándwich de huevo"),
            entry("item.tuttasdelights.half_a_froggle_sandwich", "Medio sándwich de rana"),
            entry("item.tuttasdelights.half_a_hamburger", "Media hamburguesa"),
            entry("item.tuttasdelights.honey_wings", "Alitas con miel"),
            entry("item.tuttasdelights.mashed_potato_with_meatballs", "Puré de patata con albóndigas"),
            entry("item.tuttasdelights.mint_lettuce", "Lechuga de menta"),
            entry("item.tuttasdelights.mint_popsicle", "Palito de menta"),
            entry("item.tuttasdelights.mintmallow_bite", "Bocado de malvavisco de menta"),
            entry("item.tuttasdelights.napolitana_with_fries_potatoes", "Napolitana con patatas fritas"),
            entry("item.tuttasdelights.pasta_with_beet", "Pasta con remolacha"),
            entry("item.tuttasdelights.pasta_with_pot", "Pasta con vaina"),
            entry("item.tuttasdelights.plate_of_ancient_soft-boiled_egg_and_bread", "Plato de huevo ancestral pochado con pan"),
            entry("item.tuttasdelights.plate_of_ham_with_vegetables", "Plato de jamón con verduras"),
            entry("item.tuttasdelights.plate_of_muzzle_with_vegetables", "Plato de morro con verduras"),
            entry("item.tuttasdelights.plate_of_potato_and_meat_pie", "Plato de pastel de patata y carne"),
            entry("item.tuttasdelights.plate_of_suckling_pig_with_vegetables", "Plato de cochinillo con verduras"),
            entry("item.tuttasdelights.pot_slice", "Rodaja de vaina"),
            entry("item.tuttasdelights.pot_with_honey_on_a_stick", "Vaina con miel en un palo"),
            entry("item.tuttasdelights.potato_omelet", "Tortilla de patata"),
            entry("item.tuttasdelights.potato_slices", "Rodajas de patata"),
            entry("item.tuttasdelights.pumpkin_head_slice", "Rodaja de cabeza de calabaza"),
            entry("item.tuttasdelights.raw_breaded_meat", "Carne empanada cruda"),
            entry("item.tuttasdelights.raw_frog_leg", "Anca de rana cruda"),
            entry("item.tuttasdelights.raw_suckling_pig", "Cochinillo crudo"),
            entry("item.tuttasdelights.ribs_with_eggs", "Costillas con huevos"),
            entry("item.tuttasdelights.rotten_bacon", "Panceta podrida"),
            entry("item.tuttasdelights.rotten_chicken_cuts", "Cortes de pollo podridos"),
            entry("item.tuttasdelights.rotten_ham", "Jamón podrido"),
            entry("item.tuttasdelights.rotten_meat_on_a_bone", "Carne podrida en un hueso"),
            entry("item.tuttasdelights.rotten_minced_beef", "Carne picada podrida"),
            entry("item.tuttasdelights.rotten_mutton_chops", "Chuletas de cordero podridas"),
            entry("item.tuttasdelights.rotten_sausage", "Salchicha podrida"),
            entry("item.tuttasdelights.slime_dumplings", "Dumplings de slime"),
            entry("item.tuttasdelights.slime_jelly_slice", "Rodaja de gelatina de slime"),
            entry("item.tuttasdelights.slime_jelly_slice.effect.empty", "Rodaja de gelatina de slime"),
            entry("item.tuttasdelights.slimecube", "Cubo de slime"),
            entry("item.tuttasdelights.smoked_squishmallow", "Malvavisco de slime ahumado"),
            entry("item.tuttasdelights.squishmallow", "Malvavisco de slime"),
            entry("item.tuttasdelights.sticky_green_noodles", "Fideos verdes pegajosos"),
            entry("item.tuttasdelights.sweet_berries_popsicle", "Palito de bayas dulces"),
            entry("item.tuttasdelights.sweet_trigger", "Gatillo dulce"),
            entry("item.tuttasdelights.torch_popsicle", "Palito de antorcha"),
            entry("itemGroup.tuttasdelights", "Tutta's Delights"),
            entry("tooltip.tuttasdelights.rotten_meat_on_a_bone", "Garras insalubres")
    );

    public DelightsLangES(PackOutput output) {
        super(output, "tuttasdelights", "es_es");
    }

    @Override
    protected void addTranslations() {
        TRANSLATIONS.forEach(this::add);
    }

    @Override
    public String getName() {
        return "Tutta's Delights Spanish translations";
    }
}
