package com.alltuttasneeds.doors.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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

    public String familyNamespace() {
        return familyId.substring(0, familyId.indexOf(':'));
    }

    public ResourceLocation originalLocation() {
        return ResourceLocation.fromNamespaceAndPath(familyNamespace(), registryName + "_door");
    }

    public ResourceLocation trapdoorLocation() {
        return ResourceLocation.fromNamespaceAndPath(familyNamespace(), registryName + "_trapdoor");
    }

    public String resolvedPlankId() {
        return plankId != null ? plankId : familyNamespace() + ":" + registryName + "_planks";
    }

    public String cuttingOutputId() {
        String id = resolvedPlankId();
        return id.startsWith("#") ? id.substring(1) : id;
    }

    public ResourceLocation logId() {
        return ResourceLocation.fromNamespaceAndPath(familyNamespace(), registryName + logKind.suffix());
    }

    public ResourceLocation strippedLogId() {
        return ResourceLocation.fromNamespaceAndPath(familyNamespace(), "stripped_" + registryName + logKind.suffix());
    }

    public EnumSet<DoorVariant> registeredVariants() {
        EnumSet<DoorVariant> registered = EnumSet.noneOf(DoorVariant.class);
        for (DoorVariant variant : displayOrder) {
            if (variant.isRegistrable()) registered.add(variant);
        }
        return registered;
    }

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
