package com.alltuttasneeds.delights.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FaceVariantProvider implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String MODID = "tuttasdelights";

    static final List<FaceEntry> FACES = List.of(
            new FaceEntry("default", MODID + ":block/ice_cream_faces/ice_cream_default", List.of(
                    "normal", "smile", "smiley", "happy", "feliz", "alegre", "sonriente",
                    "sonrisa", "clásico", "clasico", "por defecto", "predeterminado")),
            new FaceEntry("mini", MODID + ":block/ice_cream_faces/ice_cream_mini", List.of(
                    "small", "tiny", "little", "pequeño", "pequeno", "pequeña", "pequena",
                    "chiquito", "chiquita", "peque")),
            new FaceEntry("kitty", MODID + ":block/ice_cream_faces/ice_cream_kitty", List.of(
                    "cat", "kitten", "gato", "gata", "gatito", "gatita", "michi", "neko")),
            new FaceEntry("troll", MODID + ":block/ice_cream_faces/ice_cream_troll", List.of(
                    "trollface", "troll face", "trol", "cara troll", "troleador", "bromista")),
            new FaceEntry("sad", MODID + ":block/ice_cream_faces/ice_cream_sad", List.of(
                    "unhappy", "frown", "triste", "melancólico", "melancolico", "desanimado",
                    "llorón", "lloron")),
            new FaceEntry("cool", MODID + ":block/ice_cream_faces/ice_cream_cool", List.of(
                    "sunglasses", "shades", "genial", "guay", "fachero", "canchero", "copado",
                    "gafas", "lentes")),
            new FaceEntry("creeper", MODID + ":block/ice_cream_faces/ice_cream_creeper", List.of(
                    "creeper face", "cara creeper", "explosive", "explosivo", "dinamita", "boom")),
            new FaceEntry("toby", MODID + ":block/ice_cream_faces/ice_cream_toby", List.of(
                    "tobi", "toby fox")),
            new FaceEntry("surprised", MODID + ":block/ice_cream_faces/ice_cream_surprised", List.of(
                    "surprise", "shocked", "sorpresa", "sorprendido", "sorprendida", "asombrado",
                    "asombrada", "impactado", "impactada")),
            new FaceEntry("serious", MODID + ":block/ice_cream_faces/ice_cream_serious", List.of(
                    "straight face", "neutral", "serio", "seria", "inexpresivo", "inexpresiva",
                    "neutro", "neutra")),
            new FaceEntry("chad", MODID + ":block/ice_cream_faces/ice_cream_chad", List.of(
                    "based", "basado", "basada", "mandíbula")),
            new FaceEntry("farcr", MODID + ":block/ice_cream_faces/ice_cream_farcr", List.of())
    );

    private final PackOutput output;

    public FaceVariantProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (FaceEntry face : FACES) {
            JsonObject json = new JsonObject();
            json.addProperty("texture", face.texture());
            Path path = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve(MODID)
                    .resolve("snow_golem_feast_faces")
                    .resolve(face.name() + ".json");
            futures.add(DataProvider.saveStable(cache, GSON.toJsonTree(json), path));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "Tuttas Delights Snow Golem Feast Appearances";
    }

    record FaceEntry(String name, String texture, List<String> aliases) {}
}
