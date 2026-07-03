package com.alltuttasneeds.registry.compat.framework;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Declarative definition of a single wood (or wood-like material) door family.
 *
 * @param familyId       Unique key used to group/dedupe in the creative tab, e.g. {@code "minecraft:oak"}
 *                        or {@code "biomesoplenty:fir"}.
 * @param registryName   Base name: doors are registered as {@code "<registryName>_<variant>_door"}.
 * @param setType        BlockSetType for sounds / open-close behaviour.
 * @param baseDoor       Block whose {@code Properties} are copied as a template.
 * @param displayOrder   Ordered list of variants. Drives registration, creative tab AND tag membership.
 * @param nonFlammable   Adds every registered variant to {@code NON_FLAMMABLE_WOOD} when true.
 * @param propertiesCustomizer Extra tweak applied after {@code ofFullCopy(baseDoor)}.
 * @param plankId        Plank ingredient used in crafting recipes. May be a tag key prefixed with
 *                        {@code "#"} (e.g. {@code "#malum:runewood_planks"}) or a plain item ID
 *                        (e.g. {@code "biomesoplenty:fir_planks"}). When {@code null}, auto-derived
 *                        as {@code familyNamespace() + ":" + registryName() + "_planks"}.
 *                        Cutting-board output is the same ID with the {@code #} stripped when present.
 * @param logKind        Which log/stripped-log naming convention this family's raw material follows
 *                        (regular log, nether stem, or bamboo block). Defaults to {@link LogKind#LOG}.
 */
public record WoodFamily(
        String familyId,
        String registryName,
        Supplier<BlockSetType> setType,
        Supplier<? extends Block> baseDoor,
        List<DoorVariant> displayOrder,
        boolean nonFlammable,
        UnaryOperator<BlockBehaviour.Properties> propertiesCustomizer,
        @Nullable String plankId,
        LogKind logKind
) {
    // ── Convenience constructors (preserve backward-compat with every existing call site) ──

    public WoodFamily(String familyId, String registryName, Supplier<BlockSetType> setType,
                      Supplier<? extends Block> baseDoor, List<DoorVariant> displayOrder) {
        this(familyId, registryName, setType, baseDoor, displayOrder, false, UnaryOperator.identity(), null, LogKind.LOG);
    }

    public WoodFamily(String familyId, String registryName, Supplier<BlockSetType> setType,
                      Supplier<? extends Block> baseDoor, List<DoorVariant> displayOrder, LogKind logKind) {
        this(familyId, registryName, setType, baseDoor, displayOrder, false, UnaryOperator.identity(), null, logKind);
    }

    public WoodFamily(String familyId, String registryName, Supplier<BlockSetType> setType,
                      Supplier<? extends Block> baseDoor, List<DoorVariant> displayOrder,
                      boolean nonFlammable) {
        this(familyId, registryName, setType, baseDoor, displayOrder, nonFlammable, UnaryOperator.identity(), null, LogKind.LOG);
    }

    public WoodFamily(String familyId, String registryName, Supplier<BlockSetType> setType,
                      Supplier<? extends Block> baseDoor, List<DoorVariant> displayOrder,
                      boolean nonFlammable, UnaryOperator<BlockBehaviour.Properties> propertiesCustomizer) {
        this(familyId, registryName, setType, baseDoor, displayOrder, nonFlammable, propertiesCustomizer, null, LogKind.LOG);
    }

    public WoodFamily(String familyId, String registryName, Supplier<BlockSetType> setType,
                      Supplier<? extends Block> baseDoor, List<DoorVariant> displayOrder,
                      boolean nonFlammable, UnaryOperator<BlockBehaviour.Properties> propertiesCustomizer,
                      @Nullable String plankId) {
        this(familyId, registryName, setType, baseDoor, displayOrder, nonFlammable, propertiesCustomizer, plankId, LogKind.LOG);
    }

    // ── Derived accessors ──────────────────────────────────────────────────────────────────

    /** Namespace this family conceptually belongs to (left side of {@link #familyId()}). */
    public String familyNamespace() {
        return familyId.substring(0, familyId.indexOf(':'));
    }

    /** Location of the mod's own pre-existing door (the {@link DoorVariant#ORIGINAL} slot). */
    public ResourceLocation originalLocation() {
        return ResourceLocation.fromNamespaceAndPath(familyNamespace(), registryName + "_door");
    }

    /** Location of the mod's own pre-existing trapdoor (the {@link DoorVariant#TRAPDOOR} slot). */
    public ResourceLocation trapdoorLocation() {
        return ResourceLocation.fromNamespaceAndPath(familyNamespace(), registryName + "_trapdoor");
    }

    /**
     * Resolved plank ingredient ID for shaped/conversion recipes. When {@link #plankId()} is
     * {@code null}, auto-derived as {@code familyNamespace():registryName_planks}.
     * May start with {@code "#"} to indicate a tag.
     */
    public String resolvedPlankId() {
        return plankId != null ? plankId : familyNamespace() + ":" + registryName + "_planks";
    }

    public String cuttingOutputId() {
        String id = resolvedPlankId();
        return id.startsWith("#") ? id.substring(1) : id;
    }

    /** Location of this family's raw log-like block, per {@link #logKind()}'s naming convention. */
    public ResourceLocation logId() {
        return ResourceLocation.fromNamespaceAndPath(familyNamespace(), registryName + logKind.suffix());
    }

    /** Location of this family's stripped log-like block, per {@link #logKind()}'s naming convention. */
    public ResourceLocation strippedLogId() {
        return ResourceLocation.fromNamespaceAndPath(familyNamespace(), "stripped_" + registryName + logKind.suffix());
    }

    /** The subset of {@link #displayOrder()} that actually gets registered (excludes the ORIGINAL/TRAPDOOR sentinels). */
    public EnumSet<DoorVariant> registeredVariants() {
        EnumSet<DoorVariant> registered = EnumSet.noneOf(DoorVariant.class);
        for (DoorVariant variant : displayOrder) {
            if (variant.isRegistrable()) registered.add(variant);
        }
        return registered;
    }

    /**
     * Which conceptual tier (DISCRETE/NORMAL/INDISCRETE) the mod's own {@link DoorVariant#ORIGINAL}
     * door represents, inferred from whichever one of those three tiers this family did
     * <i>not</i> register itself. {@code null} when zero or 2+ tiers are missing, since the
     * tier can't be inferred unambiguously in that case.
     */
    @Nullable
    public DoorVariant impliedOriginalTier() {
        EnumSet<DoorVariant> registered = registeredVariants();
        DoorVariant missing = null;
        int missingCount = 0;
        for (DoorVariant tier : new DoorVariant[]{DoorVariant.DISCRETE, DoorVariant.NORMAL, DoorVariant.INDISCRETE}) {
            if (!registered.contains(tier)) {
                missing = tier;
                missingCount++;
            }
        }
        return missingCount == 1 ? missing : null;
    }
}
