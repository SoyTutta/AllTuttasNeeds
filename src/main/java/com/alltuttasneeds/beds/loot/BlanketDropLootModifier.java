package com.alltuttasneeds.beds.loot;

import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.MattressFamily;
import com.alltuttasneeds.beds.block.TieredBedBlock;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public final class BlanketDropLootModifier extends LootModifier {
    public static final Supplier<MapCodec<BlanketDropLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(instance -> codecStart(instance)
                    .apply(instance, BlanketDropLootModifier::new)));

    public BlanketDropLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null
                || !(state.getBlock() instanceof TieredBedBlock bed)
                || !state.hasProperty(BedBlock.PART)
                || state.getValue(BedBlock.PART) != BedPart.HEAD
                || hasSilkTouch(context)) {
            return generatedLoot;
        }

        BlanketMaterial blanket = bed.blanketMaterial();
        DyeColor color = bed.color();
        if (blanket == null || color == null) return generatedLoot;

        Item blanketItem = blanket.associatedItemFor(color, bed.tier());
        if (blanketItem != null) {
            if (generatedLoot.stream().noneMatch(stack -> stack.is(blanketItem))) {
                generatedLoot.add(new ItemStack(blanketItem));
            }
            return generatedLoot;
        }

        Item bareBed = bareBedItem(bed);
        boolean replaced = false;
        if (bareBed != null) {
            for (int index = 0; index < generatedLoot.size(); index++) {
                ItemStack stack = generatedLoot.get(index);
                if (stack.is(bareBed)) {
                    generatedLoot.set(index, new ItemStack(bed.asItem(), stack.getCount()));
                    replaced = true;
                }
            }
        }
        if (!replaced && generatedLoot.stream().noneMatch(stack -> stack.is(bed.asItem()))) {
            generatedLoot.add(new ItemStack(bed.asItem()));
        }
        return generatedLoot;
    }

    private static Item bareBedItem(TieredBedBlock bed) {
        return BedCompatRegistry.loaded()
                .flatMap(compat -> compat.families().stream())
                .filter(family -> family.material().equals(bed.mattress()))
                .map(MattressFamily::bedBare)
                .map(block -> block.get().asItem())
                .findFirst()
                .orElse(null);
    }

    private static boolean hasSilkTouch(LootContext context) {
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool == null || tool.isEmpty()) return false;

        Holder<Enchantment> silkTouch = context.getLevel().registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.SILK_TOUCH);
        return tool.getEnchantmentLevel(silkTouch) > 0;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
