package com.alltuttasneeds.beds;

import com.alltuttasneeds.beds.block.TieredBedBlock;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public class BedDyeRecipe extends CustomRecipe {
    public BedDyeRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return resolve(input) != null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        Block result = resolve(input);
        return result == null ? ItemStack.EMPTY : new ItemStack(result);
    }

    @Nullable
    private static Block resolve(CraftingInput input) {
        TieredBedBlock bed = null;
        DyeColor dye = null;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof TieredBedBlock candidate
                    && candidate.tier() == BedTier.NORMAL && candidate.blanketMaterial() != null) {
                if (bed != null) return null;
                bed = candidate;
            } else if (stack.getItem() instanceof DyeItem dyeItem) {
                if (dye != null) return null;
                dye = dyeItem.getDyeColor();
            } else {
                return null;
            }
        }

        if (bed == null || dye == null || bed.color() == dye) return null;
        return recolor(bed, dye);
    }

    @Nullable
    private static Block recolor(TieredBedBlock bed, DyeColor color) {
        return BedCompatRegistry.loaded()
                .flatMap(compat -> compat.families().stream())
                .filter(family -> family.material().equals(bed.mattress()))
                .findFirst()
                .map(family -> sibling(family, bed, color))
                .orElse(null);
    }

    @Nullable
    private static Block sibling(MattressFamily family, TieredBedBlock bed, DyeColor color) {
        Map<DyeColor, Supplier<Block>> colors = family.bedBlankets().get(bed.blanketMaterial());
        if (colors == null) return null;

        Supplier<Block> result = colors.get(color);
        return result == null ? null : result.get();
    }

    @Override
    public RecipeSerializer<BedDyeRecipe> getSerializer() {
        return TBContent.BED_DYE_SERIALIZER.get();
    }
}
