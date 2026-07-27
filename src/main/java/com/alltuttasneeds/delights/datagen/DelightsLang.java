package com.alltuttasneeds.delights.datagen;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.alltuttasneeds.delights.DelightsBlocks;
import com.alltuttasneeds.delights.DelightsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class DelightsLang extends LanguageProvider {
    private final Set<String> addedKeys = new HashSet<>();

    public DelightsLang(PackOutput output) {
        super(output, "tuttasdelights", "en_us");
    }

    @Override
    protected void addTranslations() {
        Set<Supplier<Block>> blocks = DelightsBlocks.BLOCKS.getEntries().stream()
                .map(entry -> (Supplier<Block>) entry::get)
                .collect(Collectors.toSet());
        Set<Supplier<Item>> items = DelightsItems.ITEMS.getEntries().stream()
                .map(entry -> (Supplier<Item>) entry::get)
                .collect(Collectors.toSet());

        blocks.forEach((b) -> {
            String descriptionId = b.get().getDescriptionId();
            if (addedKeys.add(descriptionId)) {
                String name = descriptionId.replaceFirst("block\\.tuttasdelights\\.", "");
                name = formatDisplayName(name).replaceFirst(" Block$", "");
                this.add(descriptionId, name);
            }
        });

        items.removeIf((i) -> i.get() instanceof BlockItem);
        items.forEach((i) -> {
            String descriptionId = i.get().getDescriptionId();
            if (addedKeys.add(descriptionId)) {
                String name = descriptionId.replaceFirst("item\\.tuttasdelights\\.", "");
                name = formatDisplayName(name)
                        .replaceFirst("^Half a (Ancient|Egg)", "Half an $1");
                this.add(descriptionId, name);
            }
        });

        addCustomTranslations();
    }

    private void addCustomTranslations() {
        this.add("itemGroup.tuttasdelights", "Tutta's Delights");

        this.add("item.minecraft.potion.effect.froggy_potion", "Potion of the Toad's Tempo");
        this.add("item.minecraft.potion.effect.long_froggy_potion", "Potion of the Toad's Tempo");
        this.add("item.minecraft.potion.effect.strong_froggy_potion", "Potion of the Toad's Tempo");
        this.add("item.minecraft.splash_potion.effect.froggy_potion", "Splash Potion of the Toad's Tempo");
        this.add("item.minecraft.splash_potion.effect.long_froggy_potion", "Splash Potion of the Toad's Tempo");
        this.add("item.minecraft.splash_potion.effect.strong_froggy_potion", "Splash Potion of the Toad's Tempo");
        this.add("item.minecraft.lingering_potion.effect.froggy_potion", "Lingering Potion of the Toad's Tempo");
        this.add("item.minecraft.lingering_potion.effect.long_froggy_potion", "Lingering Potion of the Toad's Tempo");
        this.add("item.minecraft.lingering_potion.effect.strong_froggy_potion", "Lingering Potion of the Toad's Tempo");

        this.add("item.minecraft.tipped_arrow.effect.froggy_potion", "Arrow of the Toad's Tempo");
        this.add("item.minecraft.tipped_arrow.effect.long_froggy_potion", "Arrow of the Toad's Tempo");
        this.add("item.minecraft.tipped_arrow.effect.strong_froggy_potion", "Arrow of the Toad's Tempo");

        this.add("item.tuttasdelights.slime_jelly_slice.effect.empty", "Slime Jelly Slice");
        this.add("block.tuttasdelights.meat_excess.use_container", "This needs a few more veggies!");
        this.add("block.tuttasdelights.veggies_excess.use_container", "This needs a little more meat!");
        this.add("block.tuttasdelights.tasteless.use_container", "There's some leftover soup. Try adding water!");
        this.add("block.tuttasdelights.needheat.use_container", "You need a heat source for cooking.");
        this.add("block.tuttasdelights.feast.use_alt_container", "You need a %s or a %s to eat this.");
        this.add("block.tuttasdelights.feast.need_knives", "Knife");
        this.add("block.tuttasdelights.feast.space_required", "You need more space to serve this.");
        this.add("block.tuttasdelights.snowgolemfeast.use_container", "You need a %s to eat this.");
        this.add("tooltip.tuttasdelights.rotten_meat_on_a_bone", "Unhealthy Claws");

        this.add("item.mynethersdelight.half_a_nether_burger", "Half a Nether Burger");
        this.add("item.mynethersdelight.half_a_hotdog", "Half a Hotdog");
        this.add("item.mynethersdelight.hotdog_with_fries_potatoes", "Hotdog with Fries Potatoes");
        this.add("item.mynethersdelight.half_a_hotdog_with_fries_potatoes", "Half a Hotdog with Fries Potatoes");
        this.add("item.mynethersdelight.half_a_hotdog_with_mixed_salad", "Half a Hotdog with Mixed Salad");
        this.add("item.mynethersdelight.half_a_hotdog_with_nether_salad", "Half a Hotdog with Nether Salad");
        this.add("item.mynethersdelight.half_a_chilidog", "Half a Chilidog");
        this.add("item.brewinandchewin.half_a_ham_and_cheese_sandwich", "Half a Ham and Cheese Sandwich");
        this.add("item.minersdelight.half_a_vegan_hamburger", "Half a Vegan Hamburger");
        this.add("item.minersdelight.half_a_caveburger", "Half a Caveburger");
        this.add("item.minersdelight.half_a_insect_sandwich", "Half an Insect Sandwich");
        this.add("item.minersdelight.half_a_squid_sandwich", "Half a Squid Sandwich");
        this.add("item.spawn.half_a_tuna_sandwich", "Half a Tuna Sandwich");
    }

    @Override
    public String getName() {
        return "Lang Entries";
    }

    public static String toTitleCase(String givenString, String regex) {
        if (givenString == null || givenString.isEmpty()) {
            return givenString;
        }

        String[] stringArray = givenString.split(regex);
        StringBuilder stringBuilder = new StringBuilder();
        String[] var4 = stringArray;
        int var5 = stringArray.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String string = var4[var6];
            if (string.length() > 0) {
                stringBuilder.append(Character.toUpperCase(string.charAt(0))).append(string.substring(1)).append(regex);
            }
        }

        return stringBuilder.toString().trim().replaceAll(regex, " ").substring(0, stringBuilder.length() - 1);
    }

    private String formatDisplayName(String name) {
        return toTitleCase(this.correctBlockItemName(name), "_")
                .replaceAll("\\bA\\b", "a")
                .replaceAll("\\bAn\\b", "an")
                .replaceAll("\\bAnd\\b", "and")
                .replaceAll("\\bIn\\b", "in")
                .replaceAll("\\bOf\\b", "of")
                .replaceAll("\\bOn\\b", "on")
                .replaceAll("\\bWith\\b", "with")
                .replaceFirst("^Long ", "")
                .replaceFirst("^Strong ", "")
                .replaceAll("\\bToads\\b", "Toad's");
    }

    public String correctBlockItemName(String name) {
        if (!name.endsWith("_bricks") && name.contains("bricks")) {
            name = name.replaceFirst("bricks", "brick");
        }

        if ((name.contains("_fence") || name.contains("_button")) && name.contains("planks")) {
            name = name.replaceFirst("_planks", "");
        }

        return name;
    }
}
