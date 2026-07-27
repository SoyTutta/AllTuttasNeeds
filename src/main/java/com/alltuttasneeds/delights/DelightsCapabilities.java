package com.alltuttasneeds.delights;

import com.alltuttasneeds.delights.block.PotluckSoupBlock;
import com.alltuttasneeds.delights.block.entity.PotluckSoupBlockEntity;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class DelightsCapabilities {
    private DelightsCapabilities() {}

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, pos, state, blockEntity, side) -> {
            if (!(state.getBlock() instanceof PotluckSoupBlock potluckBlock)) return null;
            PotluckSoupBlockEntity potluck = potluckBlock.getController(level, pos, state);
            if (potluck == null) return null;
            return side == Direction.DOWN ? potluck.getRemainderOutput() : potluck.getInputHandler();
        }, DelightsBlocks.POTLUCK_SOUP_BLOCK.get(), DelightsBlocks.ELDER_POTLUCK_SOUP_BLOCK.get());
    }
}
