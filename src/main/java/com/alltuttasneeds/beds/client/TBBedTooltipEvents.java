package com.alltuttasneeds.beds.client;

import com.alltuttasneeds.ATNTextUtils;
import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.beds.BedTier;
import com.alltuttasneeds.beds.BedTierResolver;
import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.EnumMap;
import java.util.Map;

@EventBusSubscriber(modid = AllTuttasNeeds.MODID, value = Dist.CLIENT)
public final class TBBedTooltipEvents {
    private static final Map<BedTier, Component> TOOLTIP_MAP = new EnumMap<>(BedTier.class);

    static {
        TOOLTIP_MAP.put(BedTier.BASIC, tooltip("basic", ChatFormatting.GRAY));
        TOOLTIP_MAP.put(BedTier.LOW, tooltip("low", ChatFormatting.YELLOW));
        TOOLTIP_MAP.put(BedTier.NORMAL, tooltip("normal", ChatFormatting.GREEN));
        TOOLTIP_MAP.put(BedTier.DELUXE, tooltip("deluxe", ChatFormatting.LIGHT_PURPLE));
    }

    private TBBedTooltipEvents() {}

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!TBConfig.tooltipsEnabled.get() || event.getItemStack().isEmpty()) return;

        if (!(event.getItemStack().getItem() instanceof BlockItem blockItem)) return;
        BedTier tier = BedTierResolver.resolve(blockItem.getBlock());
        if (tier != null) event.getToolTip().add(TOOLTIP_MAP.get(tier));
    }

    private static Component tooltip(String type, ChatFormatting color) {
        return ATNTextUtils.getTranslation("tooltip.beds." + type).withStyle(color);
    }
}
