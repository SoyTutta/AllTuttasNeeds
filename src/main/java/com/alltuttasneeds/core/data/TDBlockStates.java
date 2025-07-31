package com.alltuttasneeds.core.data;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.blocks.PetDoorBlock;
import com.alltuttasneeds.blocks.SecretDoorBlock;
import com.alltuttasneeds.blocks.SlidingDoorBlock;
import com.alltuttasneeds.blocks.TransitDoorBlock;
import com.alltuttasneeds.registry.TDContent;
import com.alltuttasneeds.registry.compat.*;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Map;
import java.util.function.Supplier;

import static net.neoforged.neoforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public class TDBlockStates extends BlockStateProvider {

    public TDBlockStates(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AllTuttasNeeds.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerDoorsFromMod(TDContent.DOORS, "tuttasdoors");

        if (Mods.MYNETHERSDELIGHT.isLoaded()) {
            registerDoorsFromMod(MNDContent.DOORS, "mynethersdelight");
        }
        if (Mods.ENDERSCAPE.isLoaded()) {
            registerDoorsFromMod(ESContent.DOORS, "enderscape");
        }
        if (Mods.ARTS_AND_CRAFTS.isLoaded()) {
            registerDoorsFromMod(ACContent.DOORS, "arts_and_crafts");
        }
        if (Mods.BIOMESOPLENTY.isLoaded()) {
            registerDoorsFromMod(BoPContent.DOORS, "biomesoplenty");
        }
        if (Mods.NATURES_SPIRIT.isLoaded()) {
            registerDoorsFromMod(NSContent.DOORS, "natures_spirit");
        }
        if (Mods.CREATE.isLoaded()) {
            registerDoorsFromMod(CreateContent.DOORS, "create");
        }
        if (Mods.MALUM.isLoaded()) {
            registerDoorsFromMod(MalumContent.DOORS, "malum");
        }
    }

    private void registerDoorsFromMod(Map<String, ? extends Supplier<? extends Block>> doorMap, String modNamespace) {
        for (Map.Entry<String, ? extends Supplier<? extends Block>> entry : doorMap.entrySet()) {
            String name = entry.getKey();
            Supplier<? extends Block> supplier = entry.getValue();
            Block block = supplier.get();
            String texturePath = deriveTexturePath(name);

            if (block instanceof SlidingDoorBlock) {
                slidingDoorBlockWithRenderType((DoorBlock) block, resourceBlock(texturePath + "_bottom", modNamespace), resourceBlock(texturePath + "_top", modNamespace));
            } else if (block instanceof PetDoorBlock) {
                petDoorBlockWithRenderType((PetDoorBlock) block, resourceBlock(texturePath, modNamespace), resourceBlock(texturePath, modNamespace));
            } else if (block instanceof TransitDoorBlock) {
                transitDoorBlockWithRenderType((DoorBlock) block, resourceBlock(texturePath + "_bottom", modNamespace), resourceBlock(texturePath + "_top", modNamespace));
            } else if (block instanceof SecretDoorBlock) {
                continue;
            } else if (block instanceof DoorBlock) {
                registerDoor(supplier, texturePath, modNamespace);
            }
        }
    }

    private String deriveTexturePath(String name) {
        if (name.contains("pet")) return "pet_door/" + name.replace("_pet_door", "_door");
        if (name.contains("indiscrete")) return "indiscrete_door/" + name.replace("_indiscrete_door", "_door");
        if (name.contains("normal")) return "door/" + name.replace("_normal_door", "_door");
        if (name.contains("discrete")) return "discrete_door/" + name.replace("_discrete_door", "_door");
        if (name.contains("transit")) return "transit_door/" + name.replace("_transit_door", "_door");
        if (name.contains("sliding")) return "sliding_door/" + name.replace("_sliding_door", "_door");
        return "door/" + name.replace("_door", "_door");
    }

    private ResourceLocation tuttasdoorsLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("tuttasdoors", "block/" + path);
    }

    private String getUniqueModelPath(Block block) {
        ResourceLocation key = key(block);
        return key.getNamespace() + "/" + key.getPath();
    }

    private void registerDoor(Supplier<? extends Block> blockSupplier, String texturePath, String modNamespace) {
        DoorBlock block = (DoorBlock) blockSupplier.get();
        String baseName = getUniqueModelPath(block);
        ResourceLocation bottomTexture = resourceBlock(texturePath + "_bottom", modNamespace);
        ResourceLocation topTexture = resourceBlock(texturePath + "_top", modNamespace);

        ModelFile bl = models().withExistingParent(baseName + "_bottom_left", mcLoc("block/door_bottom_left")).texture("bottom", bottomTexture).texture("top", topTexture);
        ModelFile blo = models().withExistingParent(baseName + "_bottom_left_open", mcLoc("block/door_bottom_left_open")).texture("bottom", bottomTexture).texture("top", topTexture);
        ModelFile br = models().withExistingParent(baseName + "_bottom_right", mcLoc("block/door_bottom_right")).texture("bottom", bottomTexture).texture("top", topTexture);
        ModelFile bro = models().withExistingParent(baseName + "_bottom_right_open", mcLoc("block/door_bottom_right_open")).texture("bottom", bottomTexture).texture("top", topTexture);
        ModelFile tl = models().withExistingParent(baseName + "_top_left", mcLoc("block/door_top_left")).texture("bottom", bottomTexture).texture("top", topTexture);
        ModelFile tlo = models().withExistingParent(baseName + "_top_left_open", mcLoc("block/door_top_left_open")).texture("bottom", bottomTexture).texture("top", topTexture);
        ModelFile tr = models().withExistingParent(baseName + "_top_right", mcLoc("block/door_top_right")).texture("bottom", bottomTexture).texture("top", topTexture);
        ModelFile tro = models().withExistingParent(baseName + "_top_right_open", mcLoc("block/door_top_right_open")).texture("bottom", bottomTexture).texture("top", topTexture);

        doorBlock(block, bl, blo, br, bro, tl, tlo, tr, tro);
    }

    public void transitDoorBlockWithRenderType(DoorBlock block, ResourceLocation bottom, ResourceLocation top) {
        String baseName = getUniqueModelPath(block);
        ModelFile bl = models().withExistingParent(baseName + "_bottom_left", tuttasdoorsLoc("transit_door_bottom_left")).texture("door", bottom);
        ModelFile blo = models().withExistingParent(baseName + "_bottom_left_open", tuttasdoorsLoc("transit_door_bottom_left_open")).texture("door", bottom);
        ModelFile br = models().withExistingParent(baseName + "_bottom_right", tuttasdoorsLoc("transit_door_bottom_right")).texture("door", bottom);
        ModelFile bro = models().withExistingParent(baseName + "_bottom_right_open", tuttasdoorsLoc("transit_door_bottom_right_open")).texture("door", bottom);
        ModelFile tl = models().withExistingParent(baseName + "_top_left", tuttasdoorsLoc("transit_door_top_left")).texture("door", top);
        ModelFile tlo = models().withExistingParent(baseName + "_top_left_open", tuttasdoorsLoc("transit_door_top_left_open")).texture("door", top);
        ModelFile tr = models().withExistingParent(baseName + "_top_right", tuttasdoorsLoc("transit_door_top_right")).texture("door", top);
        ModelFile tro = models().withExistingParent(baseName + "_top_right_open", tuttasdoorsLoc("transit_door_top_right_open")).texture("door", top);

        doorBlock(block, bl, blo, br, bro, tl, tlo, tr, tro);
    }

    public void slidingDoorBlockWithRenderType(DoorBlock block, ResourceLocation bottom, ResourceLocation top) {
        String baseName = getUniqueModelPath(block);
        ModelFile bl = models().withExistingParent(baseName + "_bottom_left", tuttasdoorsLoc("sliding_door_bottom_left")).texture("door", bottom);
        ModelFile blo = models().withExistingParent(baseName + "_bottom_left_open", tuttasdoorsLoc("sliding_door_bottom_left_open")).texture("door", bottom);
        ModelFile blto = models().withExistingParent(baseName + "_bottom_left_true_open", tuttasdoorsLoc("sliding_door_bottom_left_true_open")).texture("door", bottom);
        ModelFile br = models().withExistingParent(baseName + "_bottom_right", tuttasdoorsLoc("sliding_door_bottom_right")).texture("door", bottom);
        ModelFile bro = models().withExistingParent(baseName + "_bottom_right_open", tuttasdoorsLoc("sliding_door_bottom_right_open")).texture("door", bottom);
        ModelFile brto = models().withExistingParent(baseName + "_bottom_right_true_open", tuttasdoorsLoc("sliding_door_bottom_right_true_open")).texture("door", bottom);
        ModelFile tl = models().withExistingParent(baseName + "_top_left", tuttasdoorsLoc("sliding_door_top_left")).texture("door", top);
        ModelFile tlo = models().withExistingParent(baseName + "_top_left_open", tuttasdoorsLoc("sliding_door_top_left_open")).texture("door", top);
        ModelFile tlto = models().withExistingParent(baseName + "_top_left_true_open", tuttasdoorsLoc("sliding_door_top_left_true_open")).texture("door", top);
        ModelFile tr = models().withExistingParent(baseName + "_top_right", tuttasdoorsLoc("sliding_door_top_right")).texture("door", top);
        ModelFile tro = models().withExistingParent(baseName + "_top_right_open", tuttasdoorsLoc("sliding_door_top_right_open")).texture("door", top);
        ModelFile trto = models().withExistingParent(baseName + "_top_right_true_open", tuttasdoorsLoc("sliding_door_top_right_true_open")).texture("door", top);

        slidingDoorBlock((SlidingDoorBlock) block, bl, blo, blto, br, bro, brto, tl, tlo, tlto, tr, tro, trto);
    }

    private void slidingDoorBlock(SlidingDoorBlock block, ModelFile bl, ModelFile blo, ModelFile blto, ModelFile br, ModelFile bro, ModelFile brto, ModelFile tl, ModelFile tlo, ModelFile tlto, ModelFile tr, ModelFile tro, ModelFile trto) {
        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean open = state.getValue(BlockStateProperties.OPEN);
            boolean inWall = state.getValue(SlidingDoorBlock.IN_WALL);
            DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
            DoorHingeSide hinge = state.getValue(BlockStateProperties.DOOR_HINGE);
            int yRot = (int) facing.toYRot();

            ModelFile model = half == DoubleBlockHalf.LOWER ? (hinge == DoorHingeSide.LEFT ? (open ? (inWall ? blo : blto) : bl) : (open ? (inWall ? bro : brto) : br)) : (hinge == DoorHingeSide.LEFT ? (open ? (inWall ? tlo : tlto) : tl) : (open ? (inWall ? tro : trto) : tr));

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(yRot)
                    .uvLock(true)
                    .build();
        });
    }

    public void petDoorBlockWithRenderType(PetDoorBlock block, ResourceLocation customTexture, ResourceLocation vanillaTexture) {
        String baseName = getUniqueModelPath(block);
        ModelFile bottom = models().withExistingParent(baseName + "_bottom", tuttasdoorsLoc("pet_door_bottom")).texture("door", customTexture).texture("extra", vanillaTexture);
        ModelFile bottomOpen = models().withExistingParent(baseName + "_bottom_open", tuttasdoorsLoc("pet_door_bottom_open")).texture("door", customTexture).texture("extra", vanillaTexture);
        ModelFile top = models().withExistingParent(baseName + "_top", tuttasdoorsLoc("pet_door_top")).texture("door", customTexture).texture("extra", vanillaTexture);
        ModelFile topOpen = models().withExistingParent(baseName + "_top_open", tuttasdoorsLoc("pet_door_top_open")).texture("door", customTexture).texture("extra", vanillaTexture);
        petDoorBlock(block, bottom, bottomOpen, top, topOpen);
    }

    private void petDoorBlock(PetDoorBlock block, ModelFile bottom, ModelFile bottomOpen, ModelFile top, ModelFile topOpen) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            int yRot = (int) state.getValue(PetDoorBlock.FACING).toYRot();
            boolean isOpen = state.getValue(PetDoorBlock.OPEN);
            Half half = state.getValue(PetDoorBlock.HALF);
            ModelFile model = half == Half.BOTTOM ? (isOpen ? bottomOpen : bottom) : (isOpen ? topOpen : top);
            return ConfiguredModel.builder().modelFile(model).rotationY(yRot).build();
        }, TrapDoorBlock.POWERED, TrapDoorBlock.WATERLOGGED);
    }

    private static ResourceLocation resourceBlock(String name, String modNamespace) {
        return ResourceLocation.fromNamespaceAndPath(modNamespace, BLOCK_FOLDER + "/" + name);
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
}