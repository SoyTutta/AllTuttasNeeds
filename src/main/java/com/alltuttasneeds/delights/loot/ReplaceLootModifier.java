package com.alltuttasneeds.delights.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public class ReplaceLootModifier extends LootModifier {
    private final Item replacedItem;
    private final Item newItem;
    private final EntityType<?> entity;
    private final List<Item> additionalItemsToRemove;

    public static final Supplier<MapCodec<ReplaceLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
                    .and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("replaces").forGetter(ReplaceLootModifier::getReplacedItem))
                    .and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ReplaceLootModifier::getNewItem))
                    .and(BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(ReplaceLootModifier::getEntity))
                    .and(BuiltInRegistries.ITEM.byNameCodec().listOf().optionalFieldOf("remove", List.of()).forGetter(ReplaceLootModifier::getAdditionalItemsToRemove))
                    .apply(inst, ReplaceLootModifier::new)));

    public ReplaceLootModifier(LootItemCondition[] conditionsIn, Item replacedItem, Item newItem, EntityType<?> entity, List<Item> additionalItemsToRemove) {
        super(conditionsIn);
        this.replacedItem = replacedItem;
        this.newItem = newItem;
        this.entity = entity;
        this.additionalItemsToRemove = additionalItemsToRemove;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity != null && entity.getType() == this.entity) {
            int amountOfItems = generatedLoot.stream()
                    .filter(itemStack -> itemStack.getItem() == replacedItem)
                    .mapToInt(ItemStack::getCount)
                    .sum();

            generatedLoot.add(new ItemStack(newItem, amountOfItems));
            generatedLoot.removeIf(itemStack -> itemStack.getItem() == replacedItem || additionalItemsToRemove.contains(itemStack.getItem()));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    public Item getReplacedItem() {
        return replacedItem;
    }

    public Item getNewItem() {
        return newItem;
    }

    public EntityType<?> getEntity() {
        return entity;
    }

    public List<Item> getAdditionalItemsToRemove() {
        return additionalItemsToRemove;
    }
}