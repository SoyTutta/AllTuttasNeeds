package com.alltuttasneeds.beds.datagen;

import com.alltuttasneeds.beds.BedModelNaming;
import com.alltuttasneeds.beds.block.BedPosition;
import com.alltuttasneeds.beds.block.BedStateProperties;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import com.alltuttasneeds.beds.TBContent;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TBBlockStates extends BlockStateProvider {

    public TBBlockStates(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "tuttasbeds", existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        if (TBContent.BED_FRAME != null) {
            registerFramedFamily(TBContent.BED_FRAME.get(), "frame");
        }

        BedCompatRegistry.loaded().flatMap(compat -> compat.families().stream()).forEach(family -> {
            registerMattress(family.looseMattress().get());
            family.looseMattressCovers().values().forEach(block -> registerMattress(block.get()));

            registerFramedFamily(family.bedBare().get(), "bed");
            family.bedBasicCovers().values().forEach(block -> registerFramedFamily(block.get(), "bed"));

            family.bedBlankets().values().forEach(colors -> colors.values().forEach(block -> registerFramedFamily(block.get(), "bed")));
            family.bedDeluxe().values().forEach(block -> registerFramedFamily(block.get(), "bed"));
        });
    }

    private void registerMattress(Block block) {
        ResourceLocation texture = blockTexture(block);
        String name = uniqueName(block);

        ModelFile head = models().withExistingParent(name + "_head", modLoc("block/template/mattress/head")).texture("0", texture);
        ModelFile foot = models().withExistingParent(name + "_foot", modLoc("block/template/mattress/foot")).texture("0", texture);

        VariantBlockStateBuilder builder = getVariantBuilder(block);
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            int yRot = yRotation(facing);
            builder.partialState().with(BedBlock.FACING, facing).with(BedBlock.PART, BedPart.HEAD)
                    .modelForState().modelFile(head).rotationY(yRot).addModel();
            builder.partialState().with(BedBlock.FACING, facing).with(BedBlock.PART, BedPart.FOOT)
                    .modelForState().modelFile(foot).rotationY(yRot).addModel();
        }
    }

    private void registerFramedFamily(Block block, String templateGroup) {
        ResourceLocation texture = blockTexture(block);
        String name = uniqueName(block);

        VariantBlockStateBuilder builder = getVariantBuilder(block);

        for (BedPosition position : BedPosition.values()) {
            String posName = position.getSerializedName();

            for (boolean bunk : new boolean[]{false, true}) {
                String group = bunk ? templateGroup + "_bunk" : templateGroup;
                String suffix = posName + (bunk ? "_bunk" : "");

                ModelFile head = models().withExistingParent(name + "_" + suffix + "_head",
                        modLoc("block/template/" + group + "/" + posName + "_head")).texture("0", texture);
                ModelFile foot = models().withExistingParent(name + "_" + suffix + "_foot",
                        modLoc("block/template/" + group + "/" + posName + "_foot")).texture("0", texture);

                for (Direction facing : Direction.Plane.HORIZONTAL) {
                    int yRot = yRotation(facing);
                    builder.partialState()
                            .with(BedBlock.FACING, facing).with(BedBlock.PART, BedPart.HEAD)
                            .with(BedStateProperties.BED_POSITION, position).with(BedStateProperties.BUNK, bunk)
                            .modelForState().modelFile(head).rotationY(yRot).addModel();
                    builder.partialState()
                            .with(BedBlock.FACING, facing).with(BedBlock.PART, BedPart.FOOT)
                            .with(BedStateProperties.BED_POSITION, position).with(BedStateProperties.BUNK, bunk)
                            .modelForState().modelFile(foot).rotationY(yRot).addModel();
                }
            }
        }
    }

    public ResourceLocation blockTexture(Block block) {
        return BedModelNaming.blockTexture(block);
    }

    private String uniqueName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    private static int yRotation(Direction facing) {
        return (int) facing.toYRot();
    }
}
