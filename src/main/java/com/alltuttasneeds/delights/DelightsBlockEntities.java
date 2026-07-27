package com.alltuttasneeds.delights;

import com.alltuttasneeds.delights.DelightsModule;
import com.alltuttasneeds.delights.block.entity.SnowGolemFeastBlockEntity;
import com.alltuttasneeds.delights.block.entity.PotluckSoupBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class DelightsBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> FROZEN_BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DelightsModule.MODID);
    public static final DeferredRegister<BlockEntityType<?>> GUARDIAN_BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DelightsModule.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SnowGolemFeastBlockEntity>> SNOW_GOLEM_FEAST_BE =
            FROZEN_BLOCK_ENTITIES.register("snow_golem_feast_be",
                    () -> BlockEntityType.Builder.of(SnowGolemFeastBlockEntity::new,
                                    DelightsBlocks.ICE_CREAM_IN_A_PUMPKIN_HEAD_BLOCK.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PotluckSoupBlockEntity>> POTLUCK_SOUP_BE =
            GUARDIAN_BLOCK_ENTITIES.register("potluck_soup",
                    () -> BlockEntityType.Builder.of(PotluckSoupBlockEntity::new,
                                    DelightsBlocks.POTLUCK_SOUP_BLOCK.get(),
                                    DelightsBlocks.ELDER_POTLUCK_SOUP_BLOCK.get())
                            .build(null));
}
