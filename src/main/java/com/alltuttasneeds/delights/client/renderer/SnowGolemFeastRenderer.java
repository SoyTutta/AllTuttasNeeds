package com.alltuttasneeds.delights.client.renderer;

import com.alltuttasneeds.delights.block.entity.SnowGolemFeastBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import vectorwing.farmersdelight.common.block.PieBlock;

@OnlyIn(Dist.CLIENT)
public class SnowGolemFeastRenderer implements BlockEntityRenderer<SnowGolemFeastBlockEntity> {

    private final BlockRenderDispatcher blockRenderer;
    private final EntityRenderDispatcher entityRenderer;
    private final Font font;

    public SnowGolemFeastRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
        this.entityRenderer = context.getEntityRenderer();
        this.font = context.getFont();
    }

    @Override
    public void render(SnowGolemFeastBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = be.getBlockState();

        renderFace(be, state, poseStack, bufferSource, packedLight, packedOverlay);

        Component customName = be.getCustomName();
        if (customName != null) {
            renderName(customName, poseStack, bufferSource, packedLight);
        }
    }

    private void renderFace(SnowGolemFeastBlockEntity be, BlockState state, PoseStack poseStack,
                            MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        int bite = state.getValue(PieBlock.BITES);
        BakedModel model = FaceNameRegistry.getModel(be.getFaceId(), bite);
        if (model == null) return;

        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rotation = switch (facing) {
            case EAST -> -90.0F;
            case SOUTH -> 180.0F;
            case WEST -> 90.0F;
            default -> 0.0F;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.translate(-0.5, -0.5, -0.5);
        blockRenderer.getModelRenderer().renderModel(
                poseStack.last(),
                bufferSource.getBuffer(RenderType.cutoutMipped()),
                state, model, 1.0F, 1.0F, 1.0F, packedLight, packedOverlay);
        poseStack.popPose();
    }

    private void renderName(Component name, PoseStack poseStack,
                            MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.45, 0.5);
        poseStack.mulPose(entityRenderer.cameraOrientation());
        poseStack.scale(0.025F, -0.025F, 0.025F);
        Matrix4f matrix = poseStack.last().pose();
        float opacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int background = (int) (opacity * 255.0F) << 24;
        float x = (float) (-font.width(name) / 2);
        font.drawInBatch(name, x, 0.0F, 553648127, false, matrix, bufferSource,
                Font.DisplayMode.SEE_THROUGH, background, packedLight);
        font.drawInBatch(name, x, 0.0F, -1, false, matrix, bufferSource,
                Font.DisplayMode.NORMAL, 0, packedLight);
        poseStack.popPose();
    }
}
