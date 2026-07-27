package com.alltuttasneeds.delights.loot;

import com.alltuttasneeds.delights.DelightsItems;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class GuardianLootModifier extends LootModifier {
    public static final Supplier<MapCodec<GuardianLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(instance -> codecStart(instance)
                    .apply(instance, GuardianLootModifier::new)));

    public GuardianLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (!(entity instanceof Guardian guardian)) return generatedLoot;

        LivingEntity attacker = context.getParamOrNull(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity living
                ? living : null;
        boolean knife = attacker != null && ItemUtils.isKnife(attacker.getMainHandItem());
        int looting = getLooting(attacker);
        boolean elder = guardian instanceof ElderGuardian;

        int codCount = generatedLoot.stream()
                .filter(stack -> stack.is(Items.COD) || stack.is(Items.COOKED_COD))
                .mapToInt(ItemStack::getCount)
                .sum();
        boolean cooked = guardian.isOnFire() || generatedLoot.stream().anyMatch(stack -> stack.is(Items.COOKED_COD));
        generatedLoot.removeIf(stack -> stack.is(Items.COD) || stack.is(Items.COOKED_COD));

        if (knife && (elder || context.getRandom().nextFloat() < Math.min(1.0F, 0.33F + 0.10F * looting))) {
            generatedLoot.add(new ItemStack(elder ? DelightsItems.ELDER_GUARDIAN.get() : DelightsItems.GUARDIAN.get()));
            return generatedLoot;
        }

        Item slice = cooked
                ? (elder ? DelightsItems.COOKED_ELDER_GUARDIAN_SLICE.get() : DelightsItems.COOKED_GUARDIAN_SLICE.get())
                : (elder ? DelightsItems.RAW_ELDER_GUARDIAN_SLICE.get() : DelightsItems.RAW_GUARDIAN_SLICE.get());
        int slices = Math.max(1, codCount);
        float partChance = Math.min(1.0F, 0.50F + 0.10F * looting);
        if (context.getRandom().nextFloat() < partChance) slices++;
        if (context.getRandom().nextFloat() < partChance) slices++;
        if (slices > 0) generatedLoot.add(new ItemStack(slice, slices));

        boolean tailDrops = (knife && !elder) || context.getRandom().nextFloat() < partChance;
        if (tailDrops) {
            Item tail = cooked
                    ? (elder ? DelightsItems.SMOKED_ELDER_GUARDIAN_TAIL.get() : DelightsItems.SMOKED_GUARDIAN_TAIL.get())
                    : (elder ? DelightsItems.RAW_ELDER_GUARDIAN_TAIL.get() : DelightsItems.RAW_GUARDIAN_TAIL.get());
            generatedLoot.add(new ItemStack(tail));
        }
        return generatedLoot;
    }

    private static int getLooting(LivingEntity attacker) {
        if (attacker == null) return 0;
        Holder<Enchantment> looting = attacker.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.LOOTING);
        return EnchantmentHelper.getEnchantmentLevel(looting, attacker);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
