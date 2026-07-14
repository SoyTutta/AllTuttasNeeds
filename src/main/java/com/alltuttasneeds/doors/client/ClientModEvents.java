package com.alltuttasneeds.doors.client;

import com.alltuttasneeds.ATNTextUtils;
import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.doors.TDTags;
import com.alltuttasneeds.doors.config.TDConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.LinkedHashMap;
import java.util.Map;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID, value = Dist.CLIENT)
public final class ClientModEvents {

    private static final Map<TagKey<Item>, Component> TOOLTIP_MAP = new LinkedHashMap<>();

    static {
        TOOLTIP_MAP.put(TDTags.SECRET_DOORS_ITEMS,
                ATNTextUtils.getTranslation("tooltip.style.secret")
                        .withStyle(ChatFormatting.BLUE));

        TOOLTIP_MAP.put(TDTags.DISCRETE_DOORS_ITEMS,
                ATNTextUtils.getTranslation("tooltip.style.discreet")
                        .withStyle(ChatFormatting.BLUE));

        TOOLTIP_MAP.put(TDTags.CLASSIC_DOORS_ITEMS,
                ATNTextUtils.getTranslation("tooltip.style.normal")
                        .withStyle(ChatFormatting.AQUA));

        TOOLTIP_MAP.put(TDTags.INDISCRETE_DOORS_ITEMS,
                ATNTextUtils.getTranslation("tooltip.style.indiscreet")
                        .withStyle(ChatFormatting.GREEN));

        TOOLTIP_MAP.put(TDTags.TRANSIT_DOORS_ITEMS,
                ATNTextUtils.getTranslation("tooltip.style.transit")
                        .withStyle(ChatFormatting.AQUA));

        TOOLTIP_MAP.put(TDTags.PET_DOORS_ITEMS,
                ATNTextUtils.getTranslation("tooltip.style.pet")
                        .withStyle(ChatFormatting.BLUE));

        TOOLTIP_MAP.put(TDTags.SLIDING_DOORS_ITEMS,
                ATNTextUtils.getTranslation("tooltip.style.sliding")
                        .withStyle(ChatFormatting.GREEN));
    }

    private ClientModEvents() {}

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!TDConfig.isModuleEnabled() || !TDConfig.tooltipsEnabled.get()) return;

        var itemStack = event.getItemStack();
        if (itemStack.isEmpty()) {
            return;
        }

        for (Map.Entry<TagKey<Item>, Component> entry : TOOLTIP_MAP.entrySet()) {
            if (itemStack.is(entry.getKey())) {
                event.getToolTip().add(entry.getValue());
            }
        }
    }
}
