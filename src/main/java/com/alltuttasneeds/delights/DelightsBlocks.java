package com.alltuttasneeds.delights;

import com.alltuttasneeds.delights.block.*;
import com.alltuttasneeds.delights.config.DelightGroup;
import com.alltuttasneeds.delights.config.DelightsConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;

import static com.alltuttasneeds.delights.config.DelightGroup.*;

public class DelightsBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "tuttasdelights");
    // Feasts
    public static final Supplier<Block> ANCIENT_SOFT_BOILED_EGG_AND_BREADS_BLOCK = register(ANCIENT, "ancient_soft-boiled_egg_and_breads_block",
            () -> new AncientSoftBoiledEggBlock(Block.Properties.ofFullCopy(Blocks.CAKE), DelightsItems.PLATE_OF_ANCIENT_SOFT_BOILED_EGG_AND_BREAD,4, DelightsItems.ANCIENT_EGG_SANDWICH, true));
    public static final Supplier<Block> POTATO_AND_MEAT_PIE_BLOCK = register(POTATO, "potato_and_meat_pie_block",
            () -> new PotatoAndMeatPieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), DelightsItems.PLATE_OF_POTATO_AND_MEAT_PIE, true));
    public static final Supplier<Block> MINT_ROULETTE_BLOCK = register(CREEPER, "mint_roulette_block",
            () -> new MintRouletteBlock(Block.Properties.ofFullCopy(Blocks.CAKE), DelightsItems.SWEET_TRIGGER, true));
    public static final Supplier<Block> ICE_CREAM_IN_A_PUMPKIN_HEAD_BLOCK = register(FROZEN_TREATS, "ice_cream_in_a_pumpkin_head_block",
            () -> new SnowGolemFeastBlock(Block.Properties.ofFullCopy(Blocks.CAKE), DelightsItems.PUMPKIN_HEAD_SLICE, ModItems.PUMPKIN_SLICE));
    public static final Supplier<Block> SUCKLING_PIG_WITH_VEGETABLES_BLOCK = register(SUCKLING_PIG, "suckling_pig_with_vegetables_block",
            () -> new SucklingPigWithVegetablesBlock(Block.Properties.ofFullCopy(Blocks.CAKE), DelightsItems.PLATE_OF_MUZZLE_WITH_VEGETABLES, 1, DelightsItems.PLATE_OF_HAM_WITH_VEGETABLES, 2, DelightsItems.PLATE_OF_SUCKLING_PIG_WITH_VEGETABLES,true));

    public static final Supplier<Block> SLIME_JELLY_BLOCK = register(SLIME, "slime_jelly_block",
            () -> new SlimeJellyBlock(Block.Properties.ofFullCopy(Blocks.SLIME_BLOCK), DelightsItems.SLIME_JELLY_SLICE));
    public static final Supplier<Block> POTLUCK_SOUP_BLOCK = register(GUARDIAN, "potluck_soup_block",
            () -> new PotluckSoupBlock(Block.Properties.ofFullCopy(Blocks.CAULDRON).noOcclusion(), false));
    public static final Supplier<Block> ELDER_POTLUCK_SOUP_BLOCK = register(GUARDIAN, "elder_potluck_soup_block",
            () -> new PotluckSoupBlock(Block.Properties.ofFullCopy(Blocks.CAULDRON).noOcclusion(), true));

    private static Supplier<Block> register(DelightGroup group, String name, Supplier<Block> block) {
        if (DelightsConfig.isGroupEnabled(group)) {
            return BLOCKS.register(name, block);
        }
        return () -> {
            throw new IllegalStateException("Tutta's Delights content group is disabled: " + group.configKey());
        };
    }

}
