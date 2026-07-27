package com.alltuttasneeds.delights.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FaceRuleProvider implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String MODID = "tuttasdelights";

    private final PackOutput output;

    public FaceRuleProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (FaceVariantProvider.FaceEntry face : FaceVariantProvider.FACES) {
            JsonObject json = new JsonObject();
            JsonArray names = new JsonArray();
            names.add(face.name());
            face.aliases().forEach(names::add);
            json.add("names", names);
            json.addProperty("random_weight", 1);

            Path path = output.getOutputFolder(PackOutput.Target.DATA_PACK)
                    .resolve(MODID)
                    .resolve("snow_golem_feast_faces")
                    .resolve(face.name() + ".json");
            futures.add(DataProvider.saveStable(cache, GSON.toJsonTree(json), path));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "Tuttas Delights Snow Golem Feast Face Rules";
    }
}
