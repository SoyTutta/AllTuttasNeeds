package com.alltuttasneeds.core.condition;

import com.alltuttasneeds.doors.config.DoorSet;
import com.alltuttasneeds.doors.config.TDConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;

public record DoorSetEnabledCondition(String set) implements ICondition {
    public static final MapCodec<DoorSetEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("set").forGetter(DoorSetEnabledCondition::set)
    ).apply(instance, DoorSetEnabledCondition::new));

    public static final DoorSetEnabledCondition CONSISTENT = new DoorSetEnabledCondition("consistent");
    public static final DoorSetEnabledCondition TRANSIT = new DoorSetEnabledCondition("transit");
    public static final DoorSetEnabledCondition PET = new DoorSetEnabledCondition("pet");
    public static final DoorSetEnabledCondition LATERAL = new DoorSetEnabledCondition("lateral");
    public static final DoorSetEnabledCondition SECRET = new DoorSetEnabledCondition("secret");

    public static DoorSetEnabledCondition forSet(DoorSet set) {
        return switch (set) {
            case CONSISTENT -> CONSISTENT;
            case TRANSIT -> TRANSIT;
            case PET -> PET;
            case LATERAL -> LATERAL;
            case SECRET -> SECRET;
        };
    }

    @Override
    public boolean test(IContext context) {
        return switch (set) {
            case "consistent" -> TDConfig.isSetEnabled(DoorSet.CONSISTENT);
            case "transit" -> TDConfig.isSetEnabled(DoorSet.TRANSIT);
            case "pet" -> TDConfig.isSetEnabled(DoorSet.PET);
            case "lateral" -> TDConfig.isSetEnabled(DoorSet.LATERAL);
            case "secret" -> TDConfig.isSetEnabled(DoorSet.SECRET);
            default -> false;
        };
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
