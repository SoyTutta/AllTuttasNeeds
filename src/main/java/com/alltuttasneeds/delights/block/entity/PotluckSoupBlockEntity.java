package com.alltuttasneeds.delights.block.entity;

import com.alltuttasneeds.delights.DelightsBlockEntities;
import com.alltuttasneeds.delights.DelightsItems;
import com.alltuttasneeds.delights.block.PotluckPart;
import com.alltuttasneeds.delights.block.PotluckSoupBlock;
import com.alltuttasneeds.delights.potluck.PotluckMobIngredientRegistry;
import com.alltuttasneeds.delights.potluck.PotluckRecipe;
import com.alltuttasneeds.delights.potluck.PotluckRecipeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryOps;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PotluckSoupBlockEntity extends BlockEntity {
    private static final int DATA_VERSION = 2;
    private static final int ASSEMBLY_DELAY = 10;
    private static final ResourceLocation GUARDIAN_RECIPE =
            ResourceLocation.fromNamespaceAndPath("tuttasdelights", "guardian");
    private static final ResourceLocation ELDER_RECIPE =
            ResourceLocation.fromNamespaceAndPath("tuttasdelights", "elder_guardian");

    private boolean initialized;
    private boolean initialSpecialsClaimed;
    @Nullable private ResourceLocation recipeId;
    @Nullable private ResourceLocation armedRecipeId;
    private int selectedRecipeIndex;
    private int readyRegular;
    private int pendingRegular;
    private int water;
    private int remainingCookTicks = 200;
    private long completionGameTime = -1L;
    private boolean assemblyScheduled;
    private final Map<String, Integer> readySpecials = new LinkedHashMap<>();
    private final Map<String, Integer> pendingSpecials = new LinkedHashMap<>();
    private final Map<String, Integer> rawSpecials = new LinkedHashMap<>();
    private final Map<String, Double> credits = new HashMap<>();
    private ResourceLocation appearanceTexture =
            ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still");
    private int appearanceColor = 0xCC3F76E4;
    private List<PotluckRecipe.ParticleSetting> appearanceParticles = List.of();

    private final ItemStackHandler ingredients = new ItemStackHandler(6) {
        @Override
        protected void onContentsChanged(int slot) {
            sync();
        }
    };
    private final ItemStackHandler remainderOutput = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            sync();
        }
    };
    private final IItemHandler inputHandler = new InputHandler();
    private final IItemHandler outputHandler = new OutputHandler();

    public PotluckSoupBlockEntity(BlockPos pos, BlockState state) {
        super(DelightsBlockEntities.POTLUCK_SOUP_BE.get(), pos, state);
    }

    public void initializeFullIfNeeded() {
        if (initialized) return;
        initialized = true;
        initialSpecialsClaimed = true;
        PotluckRecipe recipe = PotluckRecipeRegistry.get(defaultRecipeId());
        if (recipe != null) {
            selectRecipe(recipe);
            readyRegular = recipe.initialRegularServings();
            for (PotluckRecipe.SpecialServing special : recipe.specialServings()) {
                putCount(readySpecials, special.key(), special.initialCount());
            }
        } else {
            recipeId = defaultRecipeId();
            appearanceTexture = ResourceLocation.fromNamespaceAndPath("tuttasdelights",
                    isElder() ? "block/elder_potluck_soup_block" : "block/potluck_soup_block");
            appearanceColor = 0xFFFFFFFF;
            readyRegular = isElder() ? 8 : 2;
            readySpecials.put("first_plate", 1);
            readySpecials.put("tail", isElder() ? 3 : 1);
        }
        sync();
    }

    public IItemHandler getInputHandler() {
        return inputHandler;
    }

    public IItemHandler getRemainderOutput() {
        return outputHandler;
    }

    public ItemInteractionResult interact(Player player, InteractionHand hand, ItemStack heldStack) {
        if (level == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (heldStack.isEmpty()) return selectOrConfirmRecipe(player);

        PotluckRecipe returnedRecipe = PotluckRecipeRegistry.forReturnedDish(heldStack, capacity());
        if (returnedRecipe != null) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            if (returnServing(returnedRecipe)) {
                if (!player.getAbilities().instabuild) {
                    ItemStack remainder = getRemainder(heldStack);
                    heldStack.shrink(1);
                    giveRemainder(remainder, InsertionSource.HAND, player);
                }
                level.playSound(null, worldPosition, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 0.8F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.CONSUME;
        }

        if (isAcceptedInput(heldStack)) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            boolean creative = player.getAbilities().instabuild;
            if (insertOne(heldStack, InsertionSource.HAND, creative ? null : player)) {
                if (!creative) heldStack.shrink(1);
                level.playSound(null, worldPosition, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.8F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.CONSUME;
        }

        PotluckRecipe recipe = getRecipe();
        if (recipe != null && recipe.servingContainer().test(heldStack)) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            if (isServingBlocked()) {
                player.displayClientMessage(Component.translatable("block.tuttasdelights.potluck_soup.preparing"), true);
                return ItemInteractionResult.CONSUME;
            }
            ItemStack serving = takeServing(recipe);
            if (serving.isEmpty()) {
                player.displayClientMessage(Component.translatable("block.tuttasdelights.potluck_soup.empty"), true);
                return ItemInteractionResult.CONSUME;
            }
            giveServing(player, hand, heldStack, serving);
            level.playSound(null, worldPosition, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 0.8F, 1.0F);
            sync();
            clearRecipeIfSpent();
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private ItemInteractionResult selectOrConfirmRecipe(Player player) {
        if (level == null || hasPreparedContent()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        List<PotluckRecipe> candidates = getCandidates();
        if (candidates.isEmpty()) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("block.tuttasdelights.potluck_soup.no_recipe"), true);
            }
            return ItemInteractionResult.SUCCESS;
        }
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;

        int armedIndex = indexOf(candidates, armedRecipeId);
        if (player.isShiftKeyDown()) {
            selectedRecipeIndex = armedIndex < 0 ? 0 : (armedIndex + 1) % candidates.size();
            armedRecipeId = candidates.get(selectedRecipeIndex).id();
            showSelectedRecipe(player, candidates.get(selectedRecipeIndex));
            sync();
            return ItemInteractionResult.SUCCESS;
        }
        if (armedIndex < 0) {
            selectedRecipeIndex = Math.min(selectedRecipeIndex, candidates.size() - 1);
            armedRecipeId = candidates.get(selectedRecipeIndex).id();
            showSelectedRecipe(player, candidates.get(selectedRecipeIndex));
            sync();
            return ItemInteractionResult.SUCCESS;
        }

        PotluckRecipe recipe = candidates.get(armedIndex);
        if (!startInitialBatch(recipe)) {
            armedRecipeId = null;
            player.displayClientMessage(Component.translatable("block.tuttasdelights.potluck_soup.recipe_changed"), true);
            sync();
            return ItemInteractionResult.CONSUME;
        }
        player.displayClientMessage(Component.translatable("block.tuttasdelights.potluck_soup.recipe_confirmed",
                recipe.result().getHoverName()), true);
        return ItemInteractionResult.SUCCESS;
    }

    public boolean interactEmptyHand(Player player) {
        return selectOrConfirmRecipe(player) != ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private void showSelectedRecipe(Player player, PotluckRecipe recipe) {
        player.displayClientMessage(Component.translatable("block.tuttasdelights.potluck_soup.recipe_selected",
                recipe.result().getHoverName()), true);
    }

    private int indexOf(List<PotluckRecipe> candidates, @Nullable ResourceLocation id) {
        if (id == null) return -1;
        for (int index = 0; index < candidates.size(); index++) {
            if (candidates.get(index).id().equals(id)) return index;
        }
        return -1;
    }

    public void acceptThrownItem(ItemEntity entity) {
        if (level == null || level.isClientSide || !entity.isAlive()) return;
        ItemStack stack = entity.getItem();
        PotluckRecipe returnedRecipe = PotluckRecipeRegistry.forReturnedDish(stack, capacity());
        boolean accepted = returnedRecipe != null
                ? returnServing(returnedRecipe)
                : insertOne(stack, InsertionSource.THROWN, null);
        if (!accepted) return;
        if (returnedRecipe != null) giveRemainder(getRemainder(stack), InsertionSource.THROWN, null);
        stack.shrink(1);
        if (stack.isEmpty()) entity.discard();
        else entity.setItem(stack);
    }

    public boolean acceptMobContribution(PotluckMobIngredientRegistry.Contribution contribution) {
        PotluckRecipe recipe = getRecipe();
        if (level == null || level.isClientSide || recipe == null || liquidUnits() <= 0
                || preparedServings() >= capacity()) return false;
        PotluckRecipe.Requirement requirement = findRequirement(recipe, contribution.requirement());
        if (requirement == null) return false;
        credits.merge(requirement.key(), contribution.weight(), Double::sum);
        scheduleAssembly();
        sync();
        return true;
    }

    public void scheduledTick() {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (pendingServings() == 0) {
            assemblyScheduled = false;
            assembleRefillBatch();
            return;
        }
        long now = serverLevel.getGameTime();
        if (!isHeated()) {
            if (completionGameTime >= 0L) remainingCookTicks = (int) Math.max(1L, completionGameTime - now);
            completionGameTime = -1L;
            sync();
            return;
        }
        if (completionGameTime > now) {
            serverLevel.scheduleTick(worldPosition, getBlockState().getBlock(), (int) (completionGameTime - now));
            return;
        }

        readyRegular += pendingRegular;
        pendingRegular = 0;
        for (Map.Entry<String, Integer> entry : pendingSpecials.entrySet()) {
            readySpecials.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
        pendingSpecials.clear();
        remainingCookTicks = getRecipe() == null ? 200 : getRecipe().cookTime();
        completionGameTime = -1L;
        sync();
        scheduleAssembly();
    }

    public void heatChanged() {
        if (level == null || level.isClientSide || pendingServings() == 0) return;
        if (isHeated()) scheduleCooking();
        else if (completionGameTime >= 0L) {
            remainingCookTicks = (int) Math.max(1L, completionGameTime - level.getGameTime());
            completionGameTime = -1L;
            sync();
        }
    }

    public int getComparatorOutput() {
        if (isServingBlocked()) return 0;
        int ready = readyServings();
        return ready == 0 ? 0 : Math.max(1, (int) Math.floor(15.0D * ready / capacity()));
    }

    public ItemStack createBlockItem() {
        ItemStack stack = new ItemStack(isElder() ? DelightsItems.ELDER_POTLUCK_SOUP.get() : DelightsItems.POTLUCK_SOUP.get());
        if (level != null) saveToItem(stack, level.registryAccess());
        return stack;
    }

    @Nullable
    public PotluckRecipe getRecipe() {
        return recipeId == null ? null : PotluckRecipeRegistry.get(recipeId);
    }

    public ResourceLocation getAppearanceTexture() {
        return appearanceTexture;
    }

    public int getAppearanceColor() {
        return appearanceColor;
    }

    public List<PotluckRecipe.ParticleSetting> getAppearanceParticles() {
        return appearanceParticles;
    }

    public int getLiquidUnits() {
        return liquidUnits();
    }

    public int getCapacity() {
        return capacity();
    }

    public boolean isCooking() {
        return pendingServings() > 0;
    }

    public boolean isHeatedForEffects() {
        return isHeated();
    }

    public boolean allowsParticles(PotluckRecipe.ParticleState state) {
        return switch (state) {
            case ALWAYS -> liquidUnits() > 0;
            case READY -> readyServings() > 0 && pendingServings() == 0;
            case RAW -> water > 0 || !ingredientsEmpty();
            case COOKING -> pendingServings() > 0;
            case HEATED -> isHeated() && liquidUnits() > 0;
        };
    }

    public double getLiquidHeight() {
        if (liquidUnits() <= 0) return 0.0D;
        double fraction = (double) liquidUnits() / capacity();
        return isElder() ? 0.125D + 1.625D * fraction : 0.25D + 0.5D * fraction;
    }

    public boolean hasLiquidAt(double localY) {
        if (liquidUnits() <= 0) return false;
        return localY <= getLiquidHeight();
    }

    private void giveServing(Player player, InteractionHand hand, ItemStack container, ItemStack serving) {
        if (player.getAbilities().instabuild) return;
        container.shrink(1);
        if (container.isEmpty()) player.setItemInHand(hand, serving);
        else if (!player.addItem(serving)) player.drop(serving, false);
    }

    private ItemStack takeServing(PotluckRecipe recipe) {
        for (PotluckRecipe.SpecialServing special : recipe.specialServings()) {
            if (special.order() == PotluckRecipe.ServingOrder.FIRST && takeCount(readySpecials, special.key())) {
                return special.result().copy();
            }
        }
        if (readyRegular > 0) {
            readyRegular--;
            return recipe.result().copy();
        }
        for (PotluckRecipe.SpecialServing special : recipe.specialServings()) {
            if (special.order() == PotluckRecipe.ServingOrder.LAST && takeCount(readySpecials, special.key())) {
                return special.result().copy();
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean returnServing(PotluckRecipe returnedRecipe) {
        if (preparedServings() + water >= capacity() || pendingServings() > 0) return false;
        if (recipeId == null) {
            if (!canChangeRecipe()) return false;
            selectRecipe(returnedRecipe);
        } else if (!recipeId.equals(returnedRecipe.id())) {
            return false;
        }
        readyRegular++;
        sync();
        return true;
    }

    private boolean insertOne(ItemStack stack, InsertionSource source, @Nullable Player player) {
        if (stack.isEmpty()) return false;
        int waterUnits = getWaterUnits(stack);
        if (waterUnits > 0) {
            if (liquidUnits() + waterUnits > capacity()) return false;
            ItemStack remainder = getRemainder(stack);
            if (source == InsertionSource.HOPPER && !canStoreRemainder(remainder)) return false;
            water += waterUnits;
            giveRemainder(remainder, source, player);
            armedRecipeId = null;
            scheduleAssembly();
            sync();
            return true;
        }

        PotluckRecipe recipe = getRecipe();
        if (!matchesAnyRequirement(stack, recipe)) return false;
        ItemStack remainder = getRemainder(stack);
        if (source == InsertionSource.HOPPER && !canStoreRemainder(remainder)) return false;
        if (!insertIngredientStack(stack)) return false;
        if (recipe != null) reserveSpecial(stack, recipe);
        giveRemainder(remainder, source, player);
        armedRecipeId = null;
        scheduleAssembly();
        sync();
        return true;
    }

    private boolean insertIngredientStack(ItemStack source) {
        ItemStack one = source.copyWithCount(1);
        for (int slot = 0; slot < ingredients.getSlots(); slot++) {
            one = ingredients.insertItem(slot, one, false);
            if (one.isEmpty()) return true;
        }
        return false;
    }

    private boolean isAcceptedInput(ItemStack stack) {
        return getWaterUnits(stack) > 0 || matchesAnyRequirement(stack, getRecipe());
    }

    private boolean matchesAnyRequirement(ItemStack stack, @Nullable PotluckRecipe selected) {
        if (selected != null) {
            for (PotluckRecipe.Requirement requirement : selected.requirements()) {
                if (requirement.weightFor(stack) > 0.0D) return true;
            }
            return false;
        }
        for (PotluckRecipe recipe : PotluckRecipeRegistry.forCapacity(capacity())) {
            for (PotluckRecipe.Requirement requirement : recipe.requirements()) {
                if (requirement.weightFor(stack) > 0.0D) return true;
            }
        }
        return false;
    }

    private void reserveSpecial(ItemStack stack, PotluckRecipe recipe) {
        for (PotluckRecipe.SpecialServing special : recipe.specialServings()) {
            if (!special.repeatable() || special.trigger().isEmpty() || !special.trigger().test(stack)) continue;
            int stored = count(readySpecials, special.key()) + count(pendingSpecials, special.key())
                    + count(rawSpecials, special.key());
            if (stored < special.maxCount()) putCount(rawSpecials, special.key(), 1);
        }
    }

    private List<PotluckRecipe> getCandidates() {
        List<PotluckRecipe> candidates = new ArrayList<>();
        for (PotluckRecipe recipe : PotluckRecipeRegistry.forCapacity(capacity())) {
            if (canStartInitial(recipe)) candidates.add(recipe);
        }
        return candidates;
    }

    private boolean canStartInitial(PotluckRecipe recipe) {
        int servings = Math.min(water, capacity() - preparedServings());
        for (PotluckRecipe.Requirement requirement : recipe.requirements()) {
            servings = Math.min(servings, (int) Math.floor((availableWeight(requirement) + 1.0E-7D)
                    / refillCost(recipe, requirement)));
        }
        return servings > 0;
    }

    private boolean startInitialBatch(PotluckRecipe recipe) {
        if (!canStartInitial(recipe)) return false;
        selectRecipe(recipe);
        reserveSpecialsFromInventory(recipe);
        int servings = possibleRefillServings(recipe);
        if (servings <= 0) return false;
        for (PotluckRecipe.Requirement requirement : recipe.requirements()) {
            consumeWeight(requirement, refillCost(recipe, requirement) * servings);
        }
        water -= servings;
        int specials = 0;
        for (PotluckRecipe.SpecialServing special : recipe.specialServings()) {
            if (!special.repeatable() && !initialSpecialsClaimed) {
                int count = Math.min(special.initialCount(), servings - specials);
                putCount(pendingSpecials, special.key(), count);
                specials += count;
            } else if (special.repeatable()) {
                int count = Math.min(count(rawSpecials, special.key()), servings - specials);
                putCount(rawSpecials, special.key(), -count);
                putCount(pendingSpecials, special.key(), count);
                specials += count;
            }
        }
        initialSpecialsClaimed = true;
        pendingRegular = servings - specials;
        armedRecipeId = null;
        remainingCookTicks = recipe.cookTime();
        completionGameTime = -1L;
        sync();
        scheduleCooking();
        return true;
    }

    private void assembleRefillBatch() {
        PotluckRecipe recipe = getRecipe();
        if (recipe == null || pendingServings() > 0) return;
        int servings = possibleRefillServings(recipe);
        if (servings <= 0) {
            sync();
            return;
        }
        for (PotluckRecipe.Requirement requirement : recipe.requirements()) {
            double cost = refillCost(recipe, requirement) * servings;
            consumeWeight(requirement, cost);
        }
        water -= servings;
        int specials = 0;
        for (PotluckRecipe.SpecialServing special : recipe.specialServings()) {
            if (!special.repeatable()) continue;
            int selected = Math.min(count(rawSpecials, special.key()), servings - specials);
            if (selected > 0) {
                putCount(rawSpecials, special.key(), -selected);
                putCount(pendingSpecials, special.key(), selected);
                specials += selected;
            }
        }
        pendingRegular = servings - specials;
        remainingCookTicks = recipe.cookTime();
        completionGameTime = -1L;
        sync();
        scheduleCooking();
    }

    private int possibleRefillServings(PotluckRecipe recipe) {
        int servings = Math.min(water, capacity() - preparedServings());
        for (PotluckRecipe.Requirement requirement : recipe.requirements()) {
            double cost = refillCost(recipe, requirement);
            servings = Math.min(servings, (int) Math.floor((availableWeight(requirement) + 1.0E-7D) / cost));
        }
        return Math.max(0, servings);
    }

    private double refillCost(PotluckRecipe recipe, PotluckRecipe.Requirement requirement) {
        return requirement.requiredWeight() / recipe.initialServings() * recipe.refillMultiplier();
    }

    private double availableWeight(PotluckRecipe.Requirement requirement) {
        double weight = credits.getOrDefault(requirement.key(), 0.0D);
        for (int slot = 0; slot < ingredients.getSlots(); slot++) {
            ItemStack stack = ingredients.getStackInSlot(slot);
            weight += requirement.weightFor(stack) * stack.getCount();
        }
        return weight;
    }

    private void consumeWeight(PotluckRecipe.Requirement requirement, double requested) {
        double credit = credits.getOrDefault(requirement.key(), 0.0D);
        double remaining = requested;
        if (credit > 0.0D) {
            double used = Math.min(credit, remaining);
            credit -= used;
            remaining -= used;
        }
        for (int slot = 0; slot < ingredients.getSlots() && remaining > 1.0E-7D; slot++) {
            ItemStack stack = ingredients.getStackInSlot(slot);
            double itemWeight = requirement.weightFor(stack);
            while (!stack.isEmpty() && itemWeight > 0.0D && remaining > 1.0E-7D) {
                ingredients.extractItem(slot, 1, false);
                remaining -= itemWeight;
                stack = ingredients.getStackInSlot(slot);
            }
        }
        credit += Math.max(0.0D, -remaining);
        if (credit > 1.0E-7D) credits.put(requirement.key(), credit);
        else credits.remove(requirement.key());
    }

    private void selectRecipe(PotluckRecipe recipe) {
        recipeId = recipe.id();
        appearanceTexture = recipe.appearance().texture();
        appearanceColor = recipe.appearance().color();
        appearanceParticles = recipe.appearance().particles();
    }

    private void reserveSpecialsFromInventory(PotluckRecipe recipe) {
        for (PotluckRecipe.SpecialServing special : recipe.specialServings()) {
            if (!special.repeatable() || special.trigger().isEmpty()) continue;
            int matches = 0;
            for (int slot = 0; slot < ingredients.getSlots(); slot++) {
                ItemStack stack = ingredients.getStackInSlot(slot);
                if (special.trigger().test(stack)) matches += stack.getCount();
            }
            int stored = count(readySpecials, special.key()) + count(pendingSpecials, special.key())
                    + count(rawSpecials, special.key());
            putCount(rawSpecials, special.key(), Math.min(matches, Math.max(0, special.maxCount() - stored)));
        }
    }

    private void scheduleAssembly() {
        if (!(level instanceof ServerLevel serverLevel) || pendingServings() > 0 || recipeId == null
                || assemblyScheduled) return;
        assemblyScheduled = true;
        serverLevel.scheduleTick(worldPosition, getBlockState().getBlock(), ASSEMBLY_DELAY);
    }

    private void scheduleCooking() {
        if (!(level instanceof ServerLevel serverLevel) || pendingServings() == 0 || !isHeated()) return;
        long now = serverLevel.getGameTime();
        if (completionGameTime < now) completionGameTime = now + Math.max(1, remainingCookTicks);
        serverLevel.scheduleTick(worldPosition, getBlockState().getBlock(), (int) Math.max(1L, completionGameTime - now));
        setChanged();
    }

    private boolean isHeated() {
        if (level == null) return false;
        BlockState state = getBlockState();
        Direction facing = state.getValue(PotluckSoupBlock.FACING);
        PotluckPart[] parts = isElder()
                ? new PotluckPart[]{PotluckPart.ORIGIN, PotluckPart.FRONT, PotluckPart.RIGHT, PotluckPart.DIAGONAL}
                : new PotluckPart[]{PotluckPart.ORIGIN};
        for (PotluckPart part : parts) {
            BlockPos below = PotluckSoupBlock.getPartPos(worldPosition, facing, part).below();
            BlockState heat = level.getBlockState(below);
            if (heat.is(ModTags.Blocks.HEAT_SOURCES)
                    && (!heat.hasProperty(BlockStateProperties.LIT) || heat.getValue(BlockStateProperties.LIT))) {
                return true;
            }
        }
        return false;
    }

    private boolean isServingBlocked() {
        PotluckRecipe recipe = getRecipe();
        return pendingServings() > 0 || (recipe != null && possibleRefillServings(recipe) > 0);
    }

    private boolean hasPreparedContent() {
        return readyServings() + pendingServings() > 0;
    }

    private boolean canChangeRecipe() {
        return !hasPreparedContent() && water == 0 && ingredientsEmpty() && credits.isEmpty()
                && rawSpecials.isEmpty();
    }

    private void clearRecipeIfSpent() {
        PotluckRecipe current = getRecipe();
        if (hasPreparedContent() || (current != null && possibleRefillServings(current) > 0)) return;
        recipeId = null;
        armedRecipeId = null;
        selectedRecipeIndex = 0;
        appearanceTexture = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still");
        appearanceColor = 0xCC3F76E4;
        appearanceParticles = List.of();
        sync();
    }

    private boolean ingredientsEmpty() {
        for (int slot = 0; slot < ingredients.getSlots(); slot++) {
            if (!ingredients.getStackInSlot(slot).isEmpty()) return false;
        }
        return true;
    }

    private int readyServings() {
        return readyRegular + sum(readySpecials);
    }

    private int pendingServings() {
        return pendingRegular + sum(pendingSpecials);
    }

    private int preparedServings() {
        return readyServings() + pendingServings();
    }

    private int liquidUnits() {
        return Math.min(capacity(), preparedServings() + water);
    }

    private int capacity() {
        return isElder() ? 12 : 4;
    }

    private boolean isElder() {
        return getBlockState().getBlock() instanceof PotluckSoupBlock block && block.isElder();
    }

    private ResourceLocation defaultRecipeId() {
        return isElder() ? ELDER_RECIPE : GUARDIAN_RECIPE;
    }

    @Nullable
    private static PotluckRecipe.Requirement findRequirement(PotluckRecipe recipe, String key) {
        for (PotluckRecipe.Requirement requirement : recipe.requirements()) {
            if (requirement.key().equals(key)) return requirement;
        }
        return null;
    }

    private static int getWaterUnits(ItemStack stack) {
        if (stack.is(Items.WATER_BUCKET)) return 3;
        return stack.has(DataComponents.POTION_CONTENTS)
                && stack.get(DataComponents.POTION_CONTENTS).is(Potions.WATER) ? 1 : 0;
    }

    private static ItemStack getRemainder(ItemStack stack) {
        if (stack.is(Items.WATER_BUCKET)) return new ItemStack(Items.BUCKET);
        if (getWaterUnits(stack) > 0) return new ItemStack(Items.GLASS_BOTTLE);
        return stack.hasCraftingRemainingItem() ? stack.getCraftingRemainingItem().copy() : ItemStack.EMPTY;
    }

    private void giveRemainder(ItemStack remainder, InsertionSource source, @Nullable Player player) {
        if (remainder.isEmpty()) return;
        if (source == InsertionSource.HOPPER) {
            storeRemainder(remainder);
        } else if (source == InsertionSource.HAND && player != null) {
            if (!player.addItem(remainder)) player.drop(remainder, false);
        } else if (source == InsertionSource.THROWN && level != null) {
            Direction facing = getBlockState().getValue(PotluckSoupBlock.FACING).getOpposite();
            ItemEntity returned = new ItemEntity(level,
                    worldPosition.getX() + 0.5D + facing.getStepX() * 0.7D,
                    worldPosition.getY() + 0.8D,
                    worldPosition.getZ() + 0.5D + facing.getStepZ() * 0.7D,
                    remainder.copy());
            returned.setDeltaMovement(facing.getStepX() * 0.12D, 0.12D, facing.getStepZ() * 0.12D);
            level.addFreshEntity(returned);
        }
    }

    private boolean canStoreRemainder(ItemStack remainder) {
        if (remainder.isEmpty()) return true;
        ItemStack left = remainder.copy();
        for (int slot = 0; slot < remainderOutput.getSlots(); slot++) {
            left = remainderOutput.insertItem(slot, left, true);
            if (left.isEmpty()) return true;
        }
        return false;
    }

    private void storeRemainder(ItemStack remainder) {
        ItemStack left = remainder.copy();
        for (int slot = 0; slot < remainderOutput.getSlots() && !left.isEmpty(); slot++) {
            left = remainderOutput.insertItem(slot, left, false);
        }
    }

    private static int sum(Map<String, Integer> values) {
        int sum = 0;
        for (int value : values.values()) sum += value;
        return sum;
    }

    private static int count(Map<String, Integer> values, String key) {
        return values.getOrDefault(key, 0);
    }

    private static void putCount(Map<String, Integer> values, String key, int amount) {
        int updated = values.getOrDefault(key, 0) + amount;
        if (updated > 0) values.put(key, updated);
        else values.remove(key);
    }

    private static boolean takeCount(Map<String, Integer> values, String key) {
        if (count(values, key) <= 0) return false;
        putCount(values, key, -1);
        return true;
    }

    private void sync() {
        setChanged();
        if (level == null || level.isClientSide) return;
        int fill = Math.min(4, (int) Math.ceil(4.0D * liquidUnits() / capacity()));
        boolean raw = pendingServings() > 0 || water > 0 || !ingredientsEmpty();
        boolean first = false;
        boolean tail = false;
        int tails = 0;
        PotluckRecipe recipe = getRecipe();
        if (recipe != null) {
            for (PotluckRecipe.SpecialServing special : recipe.specialServings()) {
                int stored = count(readySpecials, special.key()) + count(pendingSpecials, special.key())
                        + count(rawSpecials, special.key());
                if (special.order() == PotluckRecipe.ServingOrder.FIRST && stored > 0) first = true;
                if (special.order() == PotluckRecipe.ServingOrder.LAST && stored > 0) {
                    tail = true;
                    tails = Math.min(3, tails + stored);
                }
            }
        }
        BlockState controller = getBlockState();
        BlockState visualState = controller
                .setValue(PotluckSoupBlock.FILL, fill)
                .setValue(PotluckSoupBlock.RAW, raw)
                .setValue(PotluckSoupBlock.FIRST, first)
                .setValue(PotluckSoupBlock.TAIL, tail)
                .setValue(PotluckSoupBlock.TAILS, tails);
        if (!controller.equals(visualState)) {
            controller = visualState;
            level.setBlock(worldPosition, controller, 3);
        } else {
            level.sendBlockUpdated(worldPosition, controller, controller, 3);
        }
        if (isElder()) {
            Direction facing = controller.getValue(PotluckSoupBlock.FACING);
            for (PotluckPart part : PotluckPart.values()) {
                if (part == PotluckPart.ORIGIN) continue;
                BlockPos partPos = PotluckSoupBlock.getPartPos(worldPosition, facing, part);
                BlockState partState = level.getBlockState(partPos);
                if (!partState.is(controller.getBlock())) continue;
                BlockState updated = partState
                        .setValue(PotluckSoupBlock.FILL, fill)
                        .setValue(PotluckSoupBlock.RAW, raw)
                        .setValue(PotluckSoupBlock.FIRST, first)
                        .setValue(PotluckSoupBlock.TAIL, tail)
                        .setValue(PotluckSoupBlock.TAILS, tails);
                if (!partState.equals(updated)) level.setBlock(partPos, updated, 3);
            }
        }
        level.updateNeighbourForOutputSignal(worldPosition, controller.getBlock());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            if (pendingServings() > 0) scheduleCooking();
            else scheduleAssembly();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("PotluckDataVersion", DATA_VERSION);
        tag.putBoolean("Initialized", initialized);
        tag.putBoolean("InitialSpecialsClaimed", initialSpecialsClaimed);
        if (recipeId != null) tag.putString("Recipe", recipeId.toString());
        if (armedRecipeId != null) tag.putString("ArmedRecipe", armedRecipeId.toString());
        tag.putInt("SelectedRecipeIndex", selectedRecipeIndex);
        tag.putInt("ReadyRegular", readyRegular);
        tag.putInt("PendingRegular", pendingRegular);
        tag.putInt("Water", water);
        int remaining = remainingCookTicks;
        if (level != null && completionGameTime >= 0L) remaining = (int) Math.max(1L, completionGameTime - level.getGameTime());
        tag.putInt("CookTicks", remaining);
        tag.put("Ingredients", ingredients.serializeNBT(registries));
        tag.put("Remainders", remainderOutput.serializeNBT(registries));
        tag.put("ReadySpecials", saveIntMap(readySpecials));
        tag.put("PendingSpecials", saveIntMap(pendingSpecials));
        tag.put("RawSpecials", saveIntMap(rawSpecials));
        tag.put("Credits", saveDoubleMap(credits));
        tag.putString("AppearanceTexture", appearanceTexture.toString());
        tag.putInt("AppearanceColor", appearanceColor);
        tag.put("AppearanceParticles", saveParticles(appearanceParticles, registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        initialized = tag.getBoolean("Initialized");
        initialSpecialsClaimed = tag.getBoolean("InitialSpecialsClaimed");
        recipeId = tag.contains("Recipe") ? ResourceLocation.tryParse(tag.getString("Recipe")) : null;
        armedRecipeId = tag.contains("ArmedRecipe") ? ResourceLocation.tryParse(tag.getString("ArmedRecipe")) : null;
        selectedRecipeIndex = tag.getInt("SelectedRecipeIndex");
        readyRegular = tag.getInt("ReadyRegular");
        pendingRegular = tag.getInt("PendingRegular");
        water = tag.getInt("Water");
        remainingCookTicks = tag.contains("CookTicks") ? Math.max(1, tag.getInt("CookTicks")) : 200;
        completionGameTime = -1L;
        assemblyScheduled = false;
        if (tag.contains("Ingredients")) ingredients.deserializeNBT(registries, tag.getCompound("Ingredients"));
        if (tag.contains("Remainders")) remainderOutput.deserializeNBT(registries, tag.getCompound("Remainders"));
        loadIntMap(tag.getCompound("ReadySpecials"), readySpecials);
        loadIntMap(tag.getCompound("PendingSpecials"), pendingSpecials);
        loadIntMap(tag.getCompound("RawSpecials"), rawSpecials);
        loadDoubleMap(tag.getCompound("Credits"), credits);
        if (tag.contains("AppearanceTexture")) {
            ResourceLocation loadedTexture = ResourceLocation.tryParse(tag.getString("AppearanceTexture"));
            if (loadedTexture != null) appearanceTexture = loadedTexture;
        }
        if (tag.contains("AppearanceColor")) appearanceColor = tag.getInt("AppearanceColor");
        appearanceParticles = loadParticles(tag.getList("AppearanceParticles", Tag.TAG_COMPOUND), registries);

        if (tag.getInt("PotluckDataVersion") < DATA_VERSION) migrateLegacy(tag);
    }

    private void migrateLegacy(CompoundTag tag) {
        recipeId = defaultRecipeId();
        initialSpecialsClaimed = true;
        readySpecials.clear();
        if (tag.getBoolean("FirstServing")) readySpecials.put("first_plate", 1);
        int oldTails = tag.getInt("ReadyTails") + tag.getInt("PendingTails") + tag.getInt("StoredTails");
        if (oldTails > 0) readySpecials.put("tail", oldTails);
        PotluckRecipe recipe = getRecipe();
        if (recipe != null) selectRecipe(recipe);
    }

    private static CompoundTag saveIntMap(Map<String, Integer> map) {
        CompoundTag tag = new CompoundTag();
        map.forEach(tag::putInt);
        return tag;
    }

    private static void loadIntMap(CompoundTag tag, Map<String, Integer> map) {
        map.clear();
        for (String key : tag.getAllKeys()) {
            int value = tag.getInt(key);
            if (value > 0) map.put(key, value);
        }
    }

    private static CompoundTag saveDoubleMap(Map<String, Double> map) {
        CompoundTag tag = new CompoundTag();
        map.forEach(tag::putDouble);
        return tag;
    }

    private static void loadDoubleMap(CompoundTag tag, Map<String, Double> map) {
        map.clear();
        for (String key : tag.getAllKeys()) {
            double value = tag.getDouble(key);
            if (value > 1.0E-7D) map.put(key, value);
        }
    }

    private static ListTag saveParticles(List<PotluckRecipe.ParticleSetting> particles,
                                         HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (PotluckRecipe.ParticleSetting particle : particles) {
            CompoundTag tag = new CompoundTag();
            Tag options = ParticleTypes.CODEC
                    .encodeStart(RegistryOps.create(NbtOps.INSTANCE, registries), particle.options())
                    .result().orElse(null);
            if (options == null) continue;
            tag.put("Options", options);
            tag.putInt("Count", particle.count());
            tag.putFloat("Chance", particle.chance());
            tag.putDouble("SpreadX", particle.spread().x());
            tag.putDouble("SpreadY", particle.spread().y());
            tag.putDouble("SpreadZ", particle.spread().z());
            tag.putDouble("VelocityX", particle.velocity().x());
            tag.putDouble("VelocityY", particle.velocity().y());
            tag.putDouble("VelocityZ", particle.velocity().z());
            tag.putString("State", particle.state().name());
            list.add(tag);
        }
        return list;
    }

    private static List<PotluckRecipe.ParticleSetting> loadParticles(ListTag list,
                                                                     HolderLookup.Provider registries) {
        List<PotluckRecipe.ParticleSetting> particles = new ArrayList<>();
        for (int index = 0; index < list.size(); index++) {
            CompoundTag tag = list.getCompound(index);
            if (!tag.contains("Options")) continue;
            ParticleOptions options = ParticleTypes.CODEC
                    .parse(RegistryOps.create(NbtOps.INSTANCE, registries), tag.get("Options"))
                    .result().orElse(null);
            if (options == null) continue;
            PotluckRecipe.ParticleState state;
            try {
                state = PotluckRecipe.ParticleState.valueOf(tag.getString("State"));
            } catch (IllegalArgumentException exception) {
                state = PotluckRecipe.ParticleState.HEATED;
            }
            particles.add(new PotluckRecipe.ParticleSetting(options, tag.getInt("Count"), tag.getFloat("Chance"),
                    new PotluckRecipe.Vec3(tag.getDouble("SpreadX"), tag.getDouble("SpreadY"), tag.getDouble("SpreadZ")),
                    new PotluckRecipe.Vec3(tag.getDouble("VelocityX"), tag.getDouble("VelocityY"), tag.getDouble("VelocityZ")),
                    state));
        }
        return List.copyOf(particles);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private enum InsertionSource {
        HAND, THROWN, HOPPER
    }

    private class InputHandler implements IItemHandler {
        @Override
        public int getSlots() {
            return ingredients.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return slot >= 0 && slot < ingredients.getSlots() ? ingredients.getStackInSlot(slot) : ItemStack.EMPTY;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot < 0 || slot >= ingredients.getSlots() || stack.isEmpty() || !isAcceptedInput(stack)
                    || !canStoreRemainder(getRemainder(stack))) return stack;
            if (getWaterUnits(stack) > 0 && liquidUnits() + getWaterUnits(stack) > capacity()) return stack;
            if (getWaterUnits(stack) == 0 && !canInsertIngredient(stack)) return stack;
            ItemStack result = stack.copy();
            result.shrink(1);
            if (!simulate) insertOne(stack, InsertionSource.HOPPER, null);
            return result;
        }

        private boolean canInsertIngredient(ItemStack stack) {
            ItemStack one = stack.copyWithCount(1);
            for (int slot = 0; slot < ingredients.getSlots(); slot++) {
                one = ingredients.insertItem(slot, one, true);
                if (one.isEmpty()) return true;
            }
            return false;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return slot >= 0 && slot < ingredients.getSlots() && isAcceptedInput(stack);
        }
    }

    private class OutputHandler implements IItemHandler {
        @Override public int getSlots() { return remainderOutput.getSlots(); }
        @Override public ItemStack getStackInSlot(int slot) { return remainderOutput.getStackInSlot(slot); }
        @Override public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) { return stack; }
        @Override public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return remainderOutput.extractItem(slot, amount, simulate);
        }
        @Override public int getSlotLimit(int slot) { return remainderOutput.getSlotLimit(slot); }
        @Override public boolean isItemValid(int slot, ItemStack stack) { return false; }
    }
}
