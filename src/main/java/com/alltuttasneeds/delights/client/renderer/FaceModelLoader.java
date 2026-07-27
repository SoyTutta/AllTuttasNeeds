package com.alltuttasneeds.delights.client.renderer;

import com.google.gson.*;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.*;

import java.util.function.Function;

public class FaceModelLoader implements IGeometryLoader<FaceModelLoader.FaceGeometry> {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("tuttasdelights", "face_with_texture");

    // Textura placeholder que usan tus face_biteX.json en "#10"
    public static final ResourceLocation PLACEHOLDER =
            ResourceLocation.fromNamespaceAndPath(
                    "tuttasdelights", "block/ice_cream_faces/ice_cream_default");

    @Override
    public FaceGeometry read(JsonObject json, JsonDeserializationContext ctx) {
        int bite = json.get("base_bite").getAsInt();
        String texture = json.get("face_texture").getAsString();
        return new FaceGeometry(bite, ResourceLocation.parse(texture));
    }

    public static class FaceGeometry implements IUnbakedGeometry<FaceGeometry> {

        private final int bite;
        private final ResourceLocation texture;

        public FaceGeometry(int bite, ResourceLocation texture) {
            this.bite = bite;
            this.texture = texture;
        }

        private ResourceLocation baseLoc() {
            return ResourceLocation.fromNamespaceAndPath(
                    "tuttasdelights", "block/ice_cream_in_a_pumpkin/ice_cream_faces/face_bite" + bite);
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver,
                                   IGeometryBakingContext context) {
            // Importante: resolver el modelo base para que sus padres también se resuelvan
            UnbakedModel base = resolver.apply(baseLoc());
            base.resolveParents(resolver);
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context,
                               ModelBaker baker,
                               Function<Material, TextureAtlasSprite> spriteGetter,
                               ModelState modelState,
                               ItemOverrides overrides) {

            Material overrideMaterial = new Material(
                    TextureAtlas.LOCATION_BLOCKS, texture);

            // Interceptamos cualquier pedido de la textura placeholder
            Function<Material, TextureAtlasSprite> patchedGetter = mat -> {
                if (mat.texture().equals(PLACEHOLDER)) {
                    return spriteGetter.apply(overrideMaterial);
                }
                return spriteGetter.apply(mat);
            };

            UnbakedModel baseUnbaked = baker.getModel(baseLoc());
            return baseUnbaked.bake(baker, patchedGetter, modelState);
        }
    }
}
