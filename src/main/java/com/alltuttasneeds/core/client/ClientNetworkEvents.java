package com.alltuttasneeds.core.client;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.beds.BedBlanketIngredients;
import com.alltuttasneeds.beds.BedCoverIngredients;
import com.alltuttasneeds.beds.BedIngredientSyncState;
import com.alltuttasneeds.beds.BedTierSyncState;
import com.alltuttasneeds.delights.crafting.DelightsRecipeSyncState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID, value = Dist.CLIENT)
public final class ClientNetworkEvents {
    private ClientNetworkEvents() {}

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        BedCoverIngredients.clearSynced();
        BedBlanketIngredients.clearSynced();
        BedIngredientSyncState.clear();
        BedTierSyncState.clear();
        DelightsRecipeSyncState.clear();
    }
}
