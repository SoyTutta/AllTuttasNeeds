package com.alltuttasneeds.beds.event;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.beds.block.LooseMattressBlock;
import com.alltuttasneeds.beds.block.TuttaBedBlock;
import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.GameData;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID)
public final class TBVillagerBedEvents {
    private TBVillagerBedEvents() {}

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            if (!TBConfig.villagersCanUseTuttaBeds.get()) return;
            Holder<PoiType> home = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolder(PoiTypes.HOME).orElseThrow();
            BuiltInRegistries.BLOCK.forEach(block -> registerBedStates(block, home));
        });
    }

    private static void registerBedStates(Block block, Holder<PoiType> home) {
        if (!(block instanceof TuttaBedBlock) && !(block instanceof LooseMattressBlock)) return;
        block.getStateDefinition().getPossibleStates().stream()
                .filter(state -> state.getValue(BedBlock.PART) == BedPart.HEAD)
                .forEach(state -> GameData.getBlockStatePointOfInterestTypeMap().put(state, home));
    }
}
