package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.doors.compat.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ShapeMapProvider implements DataProvider {

    private final PackOutput output;

    public ShapeMapProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        CompatRegistry.loaded().forEach(compat -> {
            List<WoodFamily> families = compat.woodFamilies();
            if (families.isEmpty()) return;

            JsonObject add = new JsonObject();
            for (WoodFamily family : families) {
                buildFamily(family, compat.namespace(), add);
            }
            if (add.size() == 0) return;

            JsonObject root = new JsonObject();
            root.add("add", add);

            var path = output.getOutputFolder(PackOutput.Target.DATA_PACK)
                    .resolve("tuttasdoors/shape_map/" + compat.namespace() + "doors.json");

            futures.add(DataProvider.saveStable(cache, root, path));
        });

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Tuttas Doors – Shape Map";
    }

    private static void buildFamily(WoodFamily family, String registryNs, JsonObject add) {
        List<DoorVariant> order    = family.displayOrder();
        String            wood     = family.registryName();
        String            familyNs = family.familyNamespace();

        boolean hasDiscrete = order.contains(DoorVariant.DISCRETE);
        boolean hasPet      = order.contains(DoorVariant.PET);
        boolean hasTrapdoor = order.contains(DoorVariant.TRAPDOOR);

        String anchor = hasDiscrete
                ? id(registryNs, wood + "_discrete_door")
                : id(familyNs,   wood + "_door");

        JsonArray primaryItems = new JsonArray();
        for (DoorVariant v : order) {
            if (v == DoorVariant.DISCRETE  && hasDiscrete)  continue;
            if (v == DoorVariant.ORIGINAL  && !hasDiscrete) continue;
            if (v == DoorVariant.PET)                        continue;
            if (v == DoorVariant.TRAPDOOR)                   continue;
            primaryItems.add(resolve(v, wood, familyNs, registryNs));
        }

        if (primaryItems.size() > 0) {
            add.add(anchor, primaryItems);
        }

        if (hasPet && hasTrapdoor) {
            JsonArray petItems = new JsonArray();
            petItems.add(id(registryNs, wood + "_pet_door"));
            add.add(id(familyNs, wood + "_trapdoor"), petItems);
        }
    }

    private static String resolve(DoorVariant v, String wood, String familyNs, String registryNs) {
        return switch (v) {
            case ORIGINAL   -> id(familyNs,    wood + "_door");
            case TRAPDOOR   -> id(familyNs,    wood + "_trapdoor");
            case DISCRETE   -> id(registryNs,  wood + "_discrete_door");
            case NORMAL     -> id(registryNs,  wood + "_normal_door");
            case INDISCRETE -> id(registryNs,  wood + "_indiscrete_door");
            case TRANSIT    -> id(registryNs,  wood + "_transit_door");
            case PET        -> id(registryNs,  wood + "_pet_door");
        };
    }

    private static String id(String namespace, String path) {
        return namespace + ":" + path;
    }
}