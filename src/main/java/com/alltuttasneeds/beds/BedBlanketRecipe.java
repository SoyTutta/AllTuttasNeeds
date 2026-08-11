package com.alltuttasneeds.beds;

import com.alltuttasneeds.beds.block.TieredBedBlock;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public final class BedBlanketRecipe extends CustomRecipe {
    public BedBlanketRecipe(CraftingBookCategory category) {
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
        ItemStack blanketStack = ItemStack.EMPTY;

        for (int index = 0; index < input.size(); index++) {
            ItemStack stack = input.getItem(index);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof BlockItem blockItem
                    && blockItem.getBlock() instanceof TieredBedBlock candidate
                    && candidate.tier() == BedTier.BASIC
                    && candidate.basicCover() == null
                    && candidate.blanketMaterial() == null) {
                if (bed != null) return null;
                bed = candidate;
            } else {
                if (!blanketStack.isEmpty()) return null;
                blanketStack = stack;
            }
        }

        if (bed == null || blanketStack.isEmpty()) return null;
        TieredBedBlock selectedBed = bed;
        MattressFamily family = BedCompatRegistry.loaded()
                .flatMap(compat -> compat.families().stream())
                .filter(candidate -> candidate.material().equals(selectedBed.mattress()))
                .findFirst()
                .orElse(null);
        return family == null ? null : matchingResult(family, blanketStack);
    }

    @Nullable
    private static Block matchingResult(MattressFamily family, ItemStack blanketStack) {
        BedDecorationResolver.Match match = BedDecorationResolver.resolveUnique(
                family, blanketStack, true, false);
        return match == null ? null : match.result();
    }

    @Override
    public RecipeSerializer<BedBlanketRecipe> getSerializer() {
        return TBContent.BED_BLANKET_SERIALIZER.get();
    }
}
