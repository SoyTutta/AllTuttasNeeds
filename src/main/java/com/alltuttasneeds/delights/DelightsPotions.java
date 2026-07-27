package com.alltuttasneeds.delights;

import com.alltuttasneeds.delights.DelightsModule;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;


public class DelightsPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, DelightsModule.MODID);

    public static final Holder<Potion> FROGGY_POTION = POTIONS.register("froggy_potion",
            () -> new Potion(new MobEffectInstance(MobEffects.JUMP, 400, 2),
                    new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 3))
    );
    public static final Holder<Potion> LONG_FROGGY_POTION = POTIONS.register("long_froggy_potion",
            () -> new Potion(new MobEffectInstance(MobEffects.JUMP, 800, 2),
                    new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 800, 3))
    );
    public static final Holder<Potion> STRONG_FROGGY_POTION = POTIONS.register("strong_froggy_potion",
            () -> new Potion(new MobEffectInstance(MobEffects.JUMP, 400, 4),
                    new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 3))
    );

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
