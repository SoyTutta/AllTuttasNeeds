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
            new FaceEntry("default", MODID + ":alltuttasneeds/block/ice_cream_faces/ice_cream_default", List.of(
                    "normal", "smile", "smiley", "happy", "feliz", "alegre", "sonriente",
                    "sonrisa", "clásico", "clasico", "por defecto", "predeterminado"), 80),
            new FaceEntry("mini", MODID + ":alltuttasneeds/block/ice_cream_faces/ice_cream_mini", List.of(
                    "small", "tiny", "little", "pequeño", "pequeno", "pequeña", "pequena",
                    "chiquito", "chiquita", "peque"), 80),
            new FaceEntry("kitty", MODID + ":alltuttasneeds/block/ice_cream_faces/ice_cream_kitty", List.of(
                    "cat", "kitten", "gato", "gata", "gatito", "gatita", "michi", "neko"), 80),
            new FaceEntry("sad", MODID + ":alltuttasneeds/block/ice_cream_faces/ice_cream_sad", List.of(
                    "unhappy", "frown", "triste", "melancólico", "melancolico", "desanimado",
                    "llorón", "lloron"), 80),
            new FaceEntry("cool", MODID + ":alltuttasneeds/block/ice_cream_faces/ice_cream_cool", List.of(
                    "sunglasses", "shades", "genial", "guay", "fachero", "canchero", "copado",
                    "gafas", "lentes"), 80),
            new FaceEntry("surprised", MODID + ":alltuttasneeds/block/ice_cream_faces/ice_cream_surprised", List.of(
                    "surprise", "shocked", "sorpresa", "sorprendido", "sorprendida", "asombrado",
                    "asombrada", "impactado", "impactada"), 80),
            new FaceEntry("serious", MODID + ":alltuttasneeds/block/ice_cream_faces/ice_cream_serious", List.of(
                    "straight face", "neutral", "serio", "seria", "inexpresivo", "inexpresiva",
                    "neutro", "neutra", "poker face"), 80),
            new FaceEntry("chad", MODID + ":alltuttasneeds/block/ice_cream_faces/ice_cream_chad", List.of(
                    "based", "basado", "basada", "gigachad", "giga chad", "sigma", "mandíbula", "mandibula"), 80),
            new FaceEntry("pocket", MODID + ":alltuttasneeds/block/ice_cream_faces/ice_cream_pocket", List.of(
                    "bolsillo", "de bolsillo"), 80),

            new FaceEntry("alex", MODID + ":alltuttasneeds/block/ice_cream_faces/mobs/ice_cream_alex", List.of(), 36),
            new FaceEntry("creeper", MODID + ":alltuttasneeds/block/ice_cream_faces/mobs/ice_cream_creeper", List.of(
                    "creeper face", "cara creeper", "explosive", "explosivo", "dinamita", "boom"), 36),
            new FaceEntry("enderman", MODID + ":alltuttasneeds/block/ice_cream_faces/mobs/ice_cream_enderman", List.of(
                    "ender", "ender man"), 36),
            new FaceEntry("ghast", MODID + ":alltuttasneeds/block/ice_cream_faces/mobs/ice_cream_ghast", List.of(), 36),
            new FaceEntry("skeleton", MODID + ":alltuttasneeds/block/ice_cream_faces/mobs/ice_cream_skeleton", List.of(
                    "esqueleto"), 36),
            new FaceEntry("slime", MODID + ":alltuttasneeds/block/ice_cream_faces/mobs/ice_cream_slime", List.of(), 36),
            new FaceEntry("spider", MODID + ":alltuttasneeds/block/ice_cream_faces/mobs/ice_cream_spider", List.of(
                    "araña", "arana"), 36),
            new FaceEntry("steve", MODID + ":alltuttasneeds/block/ice_cream_faces/mobs/ice_cream_steve", List.of(), 36),
            new FaceEntry("strider", MODID + ":alltuttasneeds/block/ice_cream_faces/mobs/ice_cream_strider", List.of(
                    "lavagante"), 36),
            new FaceEntry("zombie", MODID + ":alltuttasneeds/block/ice_cream_faces/mobs/ice_cream_zombie", List.of(
                    "zombi"), 36),

            new FaceEntry("papyrus", MODID + ":alltuttasneeds/block/ice_cream_faces/undertale/ice_cream_papyrus", List.of(
                    "paps", "nyeh"), 15),
            new FaceEntry("sans", MODID + ":alltuttasneeds/block/ice_cream_faces/undertale/ice_cream_sans", List.of(), 15),
            new FaceEntry("temmie", MODID + ":alltuttasneeds/block/ice_cream_faces/undertale/ice_cream_temmie", List.of(
                    "tem", "hoi", "bob"), 15),
            new FaceEntry("toby", MODID + ":alltuttasneeds/block/ice_cream_faces/undertale/ice_cream_toby", List.of(
                    "tobi", "toby fox", "annoying dog"), 15),

            new FaceEntry("anime", MODID + ":alltuttasneeds/block/ice_cream_faces/special/ice_cream_anime", List.of(
                    "kawaii", "uwu", "owo"), 15),
            new FaceEntry("eye", MODID + ":alltuttasneeds/block/ice_cream_faces/special/ice_cream_eye", List.of(
                    "ojo", "cyclops", "cíclope", "ciclope"), 15),
            new FaceEntry("real_sans", MODID + ":alltuttasneeds/block/ice_cream_faces/special/ice_cream_real_sans", List.of(
                    "real sans", "sans real", "true sans", "sans verdadero"), 5),
            new FaceEntry("troll", MODID + ":alltuttasneeds/block/ice_cream_faces/special/ice_cream_troll", List.of(
                    "trollface", "troll face", "trol", "cara troll", "troleador", "bromista"), 15),

            new FaceEntry("farcr", MODID + ":alltuttasneeds/block/ice_cream_faces/player/ice_cream_farcr", List.of(), 0),
            new FaceEntry("soytutta", MODID + ":alltuttasneeds/block/ice_cream_faces/player/ice_cream_soytutta", List.of(), 0)
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

    record FaceEntry(String name, String texture, List<String> aliases, int randomWeight) {}
}
