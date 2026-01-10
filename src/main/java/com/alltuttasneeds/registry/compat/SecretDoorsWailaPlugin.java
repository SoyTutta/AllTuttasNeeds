package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.blocks.SecretDoorBlock;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class SecretDoorsWailaPlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                if (blockAccessor.getBlock() instanceof SecretDoorBlock secretDoor) {

                    Block disguise = secretDoor.getDisguiseBlock();

                    return registration.blockAccessor()
                            .from(blockAccessor)
                            .blockState(disguise.defaultBlockState())
                            .build();
                }
            }
            return accessor;
        });
    }
}