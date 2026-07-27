package com.alltuttasneeds.delights.client.renderer;

import com.alltuttasneeds.delights.block.PotluckPart;
import com.alltuttasneeds.delights.block.PotluckSoupBlock;
import com.alltuttasneeds.delights.block.entity.PotluckSoupBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class PotluckSoupRenderer implements BlockEntityRenderer<PotluckSoupBlockEntity> {
    public PotluckSoupRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(PotluckSoupBlockEntity potluck, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (potluck.getLiquidUnits() <= 0) return;
        Bounds bounds = getBounds(potluck);
        float y = (float) potluck.getLiquidHeight();
        int color = potluck.getAppearanceColor();
        float alpha = ((color >>> 24) & 255) / 255.0F;
        float red = ((color >>> 16) & 255) / 255.0F;
        float green = ((color >>> 8) & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(textureFile(potluck.getAppearanceTexture())));
        Matrix4f matrix = poseStack.last().pose();

        vertex(consumer, matrix, bounds.minX, y, bounds.minZ, 0.0F, 0.0F,
                red, green, blue, alpha, packedLight, packedOverlay);
        vertex(consumer, matrix, bounds.minX, y, bounds.maxZ, 0.0F, bounds.v,
                red, green, blue, alpha, packedLight, packedOverlay);
        vertex(consumer, matrix, bounds.maxX, y, bounds.maxZ, bounds.u, bounds.v,
                red, green, blue, alpha, packedLight, packedOverlay);
        vertex(consumer, matrix, bounds.maxX, y, bounds.minZ, bounds.u, 0.0F,
                red, green, blue, alpha, packedLight, packedOverlay);
    }

    private static ResourceLocation textureFile(ResourceLocation texture) {
        String path = texture.getPath();
        if (!path.startsWith("textures/")) path = "textures/" + path;
        if (!path.endsWith(".png")) path += ".png";
        return ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), path);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z,
                               float u, float v, float red, float green, float blue, float alpha,
                               int packedLight, int packedOverlay) {
        consumer.addVertex(matrix, x, y, z)
                .setColor(red, green, blue, alpha)
                .setUv(u, v)
                .setOverlay(packedOverlay)
                .setLight(packedLight)
                .setNormal(0.0F, 1.0F, 0.0F);
    }

    private static Bounds getBounds(PotluckSoupBlockEntity potluck) {
        BlockState state = potluck.getBlockState();
        if (!(state.getBlock() instanceof PotluckSoupBlock block) || !block.isElder()) {
            return new Bounds(0.1875F, 0.8125F, 0.1875F, 0.8125F, 1.0F, 1.0F);
        }
        Direction facing = state.getValue(PotluckSoupBlock.FACING);
        BlockPos origin = potluck.getBlockPos();
        int minX = 0;
        int maxX = 0;
        int minZ = 0;
        int maxZ = 0;
        for (PotluckPart part : new PotluckPart[]{PotluckPart.ORIGIN, PotluckPart.FRONT,
                PotluckPart.RIGHT, PotluckPart.DIAGONAL}) {
            BlockPos partPos = PotluckSoupBlock.getPartPos(origin, facing, part);
            int x = partPos.getX() - origin.getX();
            int z = partPos.getZ() - origin.getZ();
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minZ = Math.min(minZ, z);
            maxZ = Math.max(maxZ, z);
        }
        return new Bounds(minX + 0.125F, maxX + 0.875F,
                minZ + 0.125F, maxZ + 0.875F, 2.0F, 2.0F);
    }

    @Override
    public boolean shouldRenderOffScreen(PotluckSoupBlockEntity blockEntity) {
        return blockEntity.getBlockState().getBlock() instanceof PotluckSoupBlock block && block.isElder();
    }

    @Override
    public AABB getRenderBoundingBox(PotluckSoupBlockEntity blockEntity) {
        Bounds bounds = getBounds(blockEntity);
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX() + bounds.minX, pos.getY(), pos.getZ() + bounds.minZ,
                pos.getX() + bounds.maxX, pos.getY() + 2.0D, pos.getZ() + bounds.maxZ);
    }

    private record Bounds(float minX, float maxX, float minZ, float maxZ, float u, float v) {}
}
