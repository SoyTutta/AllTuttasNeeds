package com.alltuttasneeds.delights.client.renderer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.Reader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FaceNameRegistry {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();
    private static final FileToIdConverter FACE_LISTER =
            FileToIdConverter.json("snow_golem_feast_faces");
    private static final ResourceLocation DEFAULT_FACE =
            ResourceLocation.fromNamespaceAndPath("tuttasdelights", "default");
    private static final ResourceLocation DEFAULT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "tuttasdelights", "block/ice_cream_faces/ice_cream_default");

    private static Map<ResourceLocation, Appearance> appearances = Map.of();
    private static Map<ResourceLocation, BakedModel[]> bakedModels = Map.of();

    private FaceNameRegistry() {}

    public static void reload(ResourceManager manager) {
        Map<ResourceLocation, Appearance> loaded = new HashMap<>();
        for (Map.Entry<ResourceLocation, Resource> entry
                : FACE_LISTER.listMatchingResources(manager).entrySet()) {
            ResourceLocation id = FACE_LISTER.fileToId(entry.getKey());
            try (Reader reader = entry.getValue().openAsReader()) {
                loaded.put(id, parseAppearance(id, GSON.fromJson(reader, JsonObject.class)));
            } catch (Exception exception) {
                LOGGER.error("Could not load Snow Golem Feast appearance {}", id, exception);
            }
        }
        loaded.putIfAbsent(DEFAULT_FACE, new Appearance(DEFAULT_TEXTURE, defaultModels()));
        appearances = Map.copyOf(loaded);
        bakedModels = Map.of();
        LOGGER.info("Loaded {} Snow Golem Feast appearances", appearances.size());
    }

    private static Appearance parseAppearance(ResourceLocation id, JsonObject json) {
        if (json == null) {
            throw new IllegalArgumentException("Empty appearance definition");
        }

        ResourceLocation texture = json.has("texture")
                ? ResourceLocation.parse(json.get("texture").getAsString())
                : null;
        List<ResourceLocation> models = defaultModels();
        if (json.has("models")) {
            JsonArray array = json.getAsJsonArray("models");
            if (array.size() != 4) {
                throw new IllegalArgumentException("Appearance " + id + " must define exactly four models");
            }
            models = new ArrayList<>(4);
            for (JsonElement element : array) {
                models.add(ResourceLocation.parse(element.getAsString()));
            }
        }
        return new Appearance(texture, List.copyOf(models));
    }

    public static void registerModels(ModelEvent.RegisterAdditional event) {
        for (Appearance appearance : appearances.values()) {
            for (ResourceLocation model : appearance.models()) {
                event.register(ModelResourceLocation.standalone(model));
            }
        }
    }

    public static void bakeModels(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel[]> loaded = new HashMap<>();
        BakedModel[] defaults = bakeAppearance(
                appearances.get(DEFAULT_FACE), event, null);
        loaded.put(DEFAULT_FACE, defaults);

        for (Map.Entry<ResourceLocation, Appearance> entry : appearances.entrySet()) {
            if (entry.getKey().equals(DEFAULT_FACE)) continue;
            loaded.put(entry.getKey(), bakeAppearance(entry.getValue(), event, defaults));
        }
        bakedModels = Map.copyOf(loaded);
    }

    private static BakedModel[] bakeAppearance(Appearance appearance,
                                                ModelEvent.ModifyBakingResult event,
                                                @Nullable BakedModel[] fallback) {
        BakedModel[] models = new BakedModel[4];
        TextureAtlasSprite replacement = appearance.texture() == null
                ? null
                : event.getTextureGetter().apply(
                        new Material(TextureAtlas.LOCATION_BLOCKS, appearance.texture()));

        for (int bite = 0; bite < 4; bite++) {
            BakedModel model = event.getModels().get(
                    ModelResourceLocation.standalone(appearance.models().get(bite)));
            if (model == null && fallback != null) {
                model = fallback[bite];
            }
            if (model != null && replacement != null) {
                model = new RetexturedModel(model, replacement);
            }
            models[bite] = model;
        }
        return models;
    }

    @Nullable
    public static BakedModel getModel(ResourceLocation face, int bite) {
        BakedModel[] models = bakedModels.get(face);
        if (models == null) models = bakedModels.get(DEFAULT_FACE);
        return models == null ? null : models[bite];
    }

    private static List<ResourceLocation> defaultModels() {
        List<ResourceLocation> models = new ArrayList<>(4);
        for (int bite = 0; bite < 4; bite++) {
            models.add(ResourceLocation.fromNamespaceAndPath(
                    "tuttasdelights",
                    "block/ice_cream_in_a_pumpkin/ice_cream_faces/face_bite" + bite));
        }
        return List.copyOf(models);
    }

    private record Appearance(@Nullable ResourceLocation texture,
                              List<ResourceLocation> models) {}

    private static final class RetexturedModel extends BakedModelWrapper<BakedModel> {

        private final Map<Direction, List<BakedQuad>> sideQuads = new EnumMap<>(Direction.class);
        private final List<BakedQuad> unculledQuads;

        private RetexturedModel(BakedModel originalModel, TextureAtlasSprite replacement) {
            super(originalModel);
            RandomSource random = RandomSource.create(0L);
            for (Direction direction : Direction.values()) {
                sideQuads.put(direction, retexture(
                        originalModel.getQuads(null, direction, random), replacement));
            }
            unculledQuads = retexture(originalModel.getQuads(null, null, random), replacement);
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                        RandomSource random) {
            return side == null ? unculledQuads : sideQuads.get(side);
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                        RandomSource random, ModelData data,
                                        @Nullable RenderType renderType) {
            return getQuads(state, side, random);
        }

        private static List<BakedQuad> retexture(List<BakedQuad> quads,
                                                 TextureAtlasSprite replacement) {
            List<BakedQuad> result = new ArrayList<>(quads.size());
            for (BakedQuad quad : quads) {
                if (!quad.getSprite().contents().name().equals(DEFAULT_TEXTURE)) {
                    result.add(quad);
                    continue;
                }

                int[] vertices = quad.getVertices().clone();
                int stride = vertices.length / 4;
                for (int vertex = 0; vertex < 4; vertex++) {
                    int offset = vertex * stride;
                    float u = Float.intBitsToFloat(vertices[offset + 4]);
                    float v = Float.intBitsToFloat(vertices[offset + 5]);
                    float localU = quad.getSprite().getUOffset(u);
                    float localV = quad.getSprite().getVOffset(v);
                    vertices[offset + 4] = Float.floatToRawIntBits(replacement.getU(localU));
                    vertices[offset + 5] = Float.floatToRawIntBits(replacement.getV(localV));
                }
                result.add(new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(),
                        replacement, quad.isShade(), quad.hasAmbientOcclusion()));
            }
            return List.copyOf(result);
        }
    }
}
