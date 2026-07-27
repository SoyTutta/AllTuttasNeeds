package com.alltuttasneeds.delights;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.delights.compat.BrewinAndChewinCompat;
import com.alltuttasneeds.delights.compat.MinersDelightCompat;
import com.alltuttasneeds.delights.compat.MyNethersDelightCompat;
import com.alltuttasneeds.delights.compat.SpawnCompat;
import com.alltuttasneeds.delights.config.DelightGroup;
import com.alltuttasneeds.delights.config.DelightsConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.registry.ModItems;

public class DelightsCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DelightsModule.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TUTTAS_DELIGHTS_TAB =
            TABS.register("tuttasdelights", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.tuttasdelights"))
                    .icon(DelightsCreativeTab::resolveIcon)
                    .displayItems((parameters, output) -> {
                        addSucklingPig(output);
                        addAncient(output);
                        addPotato(output);
                        addCreeper(output);
                        addSlime(output);
                        addFrozenTreats(output);
                        addFrog(output);
                        addExtraMeals(output);
                        addBreaded(output);
                        addSandwichPortions(output);
                        addUndead(output);
                    })
                    .build());

    private static ItemStack resolveIcon() {
        if (DelightsConfig.isGroupEnabled(DelightGroup.SUCKLING_PIG)) {
            return DelightsItems.SUCKLING_PIG_WITH_VEGETABLES.get().getDefaultInstance();
        }
        if (DelightsConfig.isGroupEnabled(DelightGroup.ANCIENT)) {
            return DelightsItems.ANCIENT_SOFT_BOILED_EGG_AND_BREADS.get().getDefaultInstance();
        }
        if (DelightsConfig.isGroupEnabled(DelightGroup.POTATO)) {
            return DelightsItems.POTATO_OMELET.get().getDefaultInstance();
        }
        if (DelightsConfig.isGroupEnabled(DelightGroup.CREEPER)) {
            return DelightsItems.MINT_ROULETTE.get().getDefaultInstance();
        }
        if (DelightsConfig.isGroupEnabled(DelightGroup.SLIME)) {
            return DelightsItems.SLIME_JELLY.get().getDefaultInstance();
        }
        if (DelightsConfig.isGroupEnabled(DelightGroup.FROZEN_TREATS)) {
            return DelightsItems.ICE_CREAM_IN_A_PUMPKIN_HEAD.get().getDefaultInstance();
        }
        if (DelightsConfig.isGroupEnabled(DelightGroup.FROG)) {
            return DelightsItems.FROGGLE_RICE_CHOWDER.get().getDefaultInstance();
        }
        if (DelightsConfig.isGroupEnabled(DelightGroup.EXTRA_MEALS)) {
            return DelightsItems.HONEY_WINGS.get().getDefaultInstance();
        }
        if (DelightsConfig.isGroupEnabled(DelightGroup.BREADED)) {
            return DelightsItems.BREADED_MEAT_SANDWICH.get().getDefaultInstance();
        }
        if (DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)) {
            return ModItems.HAMBURGER.get().getDefaultInstance();
        }
        if (DelightsConfig.isGroupEnabled(DelightGroup.UNDEAD)) {
            return DelightsItems.DISGUSTING_STEW.get().getDefaultInstance();
        }
        return ModItems.COOKING_POT.get().getDefaultInstance();
    }

    private static void addFrozenTreats(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.FROZEN_TREATS)) return;
        output.accept(ModItems.MELON_POPSICLE.get());
        output.accept(DelightsItems.GLISTERING_MELON_POPSICLE.get());
        output.accept(DelightsItems.COCOA_POPSICLE.get());
        output.accept(DelightsItems.MINT_POPSICLE.get());
        output.accept(DelightsItems.SWEET_BERRIES_POPSICLE.get());
        output.accept(DelightsItems.GLOW_BERRIES_POPSICLE.get());
        output.accept(DelightsItems.APPLE_POPSICLE.get());
        output.accept(DelightsItems.GOLDEN_APPLE_POPSICLE.get());
        output.accept(DelightsItems.TORCH_POPSICLE.get());
        output.accept(DelightsItems.ICE_CREAM_IN_A_PUMPKIN_HEAD.get());
        output.accept(DelightsItems.PUMPKIN_HEAD_SLICE.get());
        if (Mods.MYNETHERSDELIGHT.isLoaded()) {
            MyNethersDelightCompat.addFrozenTreats(output);
        }
    }

    private static void addSandwichPortions(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)) return;
        output.accept(ModItems.HAMBURGER.get());
        output.accept(DelightsItems.HALF_A_HAMBURGER.get());
        output.accept(ModItems.EGG_SANDWICH.get());
        output.accept(DelightsItems.HALF_A_EGG_SANDWICH.get());
        output.accept(ModItems.CHICKEN_SANDWICH.get());
        output.accept(DelightsItems.HALF_A_CHICKEN_SANDWICH.get());
        output.accept(ModItems.BACON_SANDWICH.get());
        output.accept(DelightsItems.HALF_A_BACON_SANDWICH.get());
        if (Mods.MYNETHERSDELIGHT.isLoaded()) {
            MyNethersDelightCompat.addSandwichPortions(output);
        }
        if (Mods.BREWIN_AND_CHEWIN.isLoaded()) {
            BrewinAndChewinCompat.addSandwichPortions(output);
        }
        if (Mods.MINERS_DELIGHT.isLoaded()) {
            MinersDelightCompat.addSandwichPortions(output);
        }
        if (Mods.SPAWN.isLoaded()) {
            SpawnCompat.addSandwichPortions(output);
        }
    }

    private static void addBreaded(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.BREADED)) return;
        output.accept(DelightsItems.RAW_BREADED_MEAT.get());
        output.accept(DelightsItems.COOKED_BREADED_MEAT.get());
        output.accept(DelightsItems.BREADED_MEAT_SANDWICH.get());
        output.accept(DelightsItems.HALF_A_BREADED_MEAT_SANDWICH.get());
    }

    private static void addPotato(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.POTATO)) return;
        output.accept(DelightsItems.POTATO_SLICES.get());
        output.accept(DelightsItems.BAKED_POTATO_SLICES.get());
        output.accept(DelightsItems.FRIES_POTATOES.get());
        output.accept(DelightsItems.POTATO_OMELET.get());
        output.accept(DelightsItems.NAPOLITANA_WITH_FRIES_POTATOES.get());
        output.accept(DelightsItems.BREADED_MEAT_WITH_PASTA.get());
        output.accept(DelightsItems.BREADED_MEAT_WITH_MASHED_POTATO.get());
        output.accept(DelightsItems.MASHED_POTATO_WITH_MEATBALLS.get());
        if (Mods.MYNETHERSDELIGHT.isLoaded()) {
            MyNethersDelightCompat.addPotato(output);
        }
    }

    private static void addSucklingPig(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.SUCKLING_PIG)) return;
        output.accept(DelightsItems.RAW_SUCKLING_PIG.get());
        output.accept(DelightsItems.SUCKLING_PIG_WITH_VEGETABLES.get());
        output.accept(DelightsItems.PLATE_OF_MUZZLE_WITH_VEGETABLES.get());
        output.accept(DelightsItems.PLATE_OF_HAM_WITH_VEGETABLES.get());
        output.accept(DelightsItems.PLATE_OF_SUCKLING_PIG_WITH_VEGETABLES.get());
    }

    private static void addFrog(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.FROG)) return;
        if (!Mods.NOMANSLAND.isLoaded()) {
            output.accept(DelightsItems.RAW_FROG_LEG.get());
            output.accept(DelightsItems.COOKED_FROG_LEG.get());
        }
        output.accept(DelightsItems.FROGGLE_RICE_CHOWDER.get());
        output.accept(DelightsItems.FROGGLE_SANDWICH.get());
        output.accept(DelightsItems.HALF_A_FROGGLE_SANDWICH.get());
        output.accept(DelightsItems.FROG_LEG_ON_A_STICK.get());
    }

    private static void addExtraMeals(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.EXTRA_MEALS)) return;
        output.accept(DelightsItems.COOKED_PASTA.get());
        output.accept(DelightsItems.HONEY_WINGS.get());
        output.accept(DelightsItems.BEET_AND_EGG_SALAD.get());
        output.accept(DelightsItems.PASTA_WITH_BEET.get());
        output.accept(DelightsItems.BEET_JUICE.get());
    }

    private static void addSlime(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.SLIME)) return;
        output.accept(DelightsItems.SLIMECUBE.get());
        output.accept(DelightsItems.SLIME_DUMPLINGS.get());
        output.accept(DelightsItems.STICKY_GREEN_NOODLES.get());
        output.accept(DelightsItems.SLIME_JELLY.get());
        output.accept(DelightsItems.SLIME_JELLY_SLICE.get());
        output.accept(DelightsItems.SQUISHMALLOW.get());
        output.accept(DelightsItems.SMOKED_SQUISHMALLOW.get());
        output.accept(DelightsItems.MINTMALLOW_BIT.get());
    }

    private static void addCreeper(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.CREEPER)) return;
        output.accept(DelightsItems.MINT_LETTUCE.get());
        output.accept(DelightsItems.SWEET_TRIGGER.get());
        output.accept(DelightsItems.CREEPER_SALAD.get());
        output.accept(DelightsItems.MINT_ROULETTE.get());
    }

    private static void addAncient(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.ANCIENT)) return;
        output.accept(DelightsItems.POT_SLICE.get());
        output.accept(DelightsItems.ANCIENT_BOILED_EGG.get());
        output.accept(DelightsItems.ANCIENT_RIBS.get());
        output.accept(DelightsItems.COOKED_ANCIENT_RIBS.get());
        output.accept(DelightsItems.RIBS_WITH_EGGS.get());
        output.accept(DelightsItems.ANCIENT_RIBS_WITH_SEEDS.get());
        output.accept(DelightsItems.ANCIENT_BREAKFAST.get());
        output.accept(DelightsItems.PASTA_WITH_POT.get());
        output.accept(DelightsItems.POT_WITH_HONEY_ON_A_STICK.get());
        output.accept(DelightsItems.ANCIENT_SALAD.get());
        output.accept(DelightsItems.ANCIENT_EGG_SANDWICH.get());
        output.accept(DelightsItems.HALF_A_ANCIENT_EGG_SANDWICH.get());
        output.accept(DelightsItems.ANCIENT_SOFT_BOILED_EGG_AND_BREADS.get());
        output.accept(DelightsItems.PLATE_OF_ANCIENT_SOFT_BOILED_EGG_AND_BREAD.get());
    }

    private static void addUndead(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.UNDEAD)) return;
        output.accept(DelightsItems.BROKEN_BONES.get());
        output.accept(DelightsItems.ROTTEN_HAM.get());
        output.accept(DelightsItems.ROTTEN_BACON.get());
        output.accept(DelightsItems.ROTTEN_MINCED_BEEF.get());
        output.accept(DelightsItems.ROTTEN_MUTTON_CHOPS.get());
        output.accept(DelightsItems.ROTTEN_CHICKEN_CUTS.get());
        output.accept(DelightsItems.ROTTEN_SAUSAGE.get());
        output.accept(DelightsItems.ROTTEN_MEAT_ON_A_BONE.get());
        output.accept(DelightsItems.DISGUSTING_STEW.get());
    }

}
