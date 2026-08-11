package com.alltuttasneeds.beds.event;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TuttaBedBlock;
import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.world.poi.ExtendPoiTypesEvent;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID)
public final class TBVillagerBedEvents {
    private TBVillagerBedEvents() {}

    @SubscribeEvent
    public static void onExtendPoiTypes(ExtendPoiTypesEvent event) {
        if (!TBConfig.isModuleEnabled() || !TBConfig.villagersCanUseTuttaBeds.get()) return;

        Set<BlockState> bedStates = new HashSet<>();
        BuiltInRegistries.BLOCK.forEach(block -> addBedStates(block, bedStates));
        if (!bedStates.isEmpty()) event.addStatesToPoi(PoiTypes.HOME, Set.copyOf(bedStates));
    }

    private static void addBedStates(Block block, Set<BlockState> bedStates) {
        if (!(block instanceof TuttaBedBlock) && !(block instanceof LooseMattressBlock)) return;
        block.getStateDefinition().getPossibleStates().stream()
                .filter(state -> state.getValue(BedBlock.PART) == BedPart.HEAD)
                .forEach(bedStates::add);
    }
}
