package com.alltuttasneeds.beds;

import com.alltuttasneeds.ATNTextUtils;
import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.beds.config.TBConfig;
import com.alltuttasneeds.beds.compat.BedCompatRegistry;
import com.alltuttasneeds.doors.config.TDConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TBCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AllTuttasNeeds.MODID);

    private static final List<DyeColor> CREATIVE_COLOR_ORDER = List.of(
            DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.GRAY, DyeColor.BLACK,
            DyeColor.BROWN, DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW,
            DyeColor.LIME, DyeColor.GREEN, DyeColor.CYAN, DyeColor.LIGHT_BLUE,
            DyeColor.BLUE, DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK
    );

    @Nullable
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TUTTAS_BEDS = TBConfig.moduleEnabled.get()
            ? TABS.register("beds", () -> {
                CreativeModeTab.Builder builder = CreativeModeTab.builder()
                        .title(ATNTextUtils.getTranslation("itemGroup.tuttasbeds"))
                        .icon(() -> TBContent.BED_FRAME != null ? new ItemStack(TBContent.BED_FRAME.get()) : new ItemStack(Items.RED_BED))
                        .displayItems((parameters, output) -> {
                            if (TBContent.BED_FRAME != null) accept(output, TBContent.BED_FRAME);

                            BedCompatRegistry.loaded().flatMap(compat -> compat.families().stream()).forEach(family -> {
                                accept(output, family.looseMattress());
                                for (CoverMaterial cover : family.looseMattressCovers().keySet()) {
                                    accept(output, family.looseMattressCovers().get(cover));
                                }

                                accept(output, family.bedBare());
                                for (CoverMaterial cover : family.bedBasicCovers().keySet()) {
                                    accept(output, family.bedBasicCovers().get(cover));
                                }

                                for (Map.Entry<BlanketMaterial, Map<DyeColor, Supplier<Block>>> entry : family.bedBlankets().entrySet()) {
                                    for (DyeColor color : CREATIVE_COLOR_ORDER) {
                                        accept(output, entry.getValue().get(color));
                                    }
                                }

                                for (DyeColor color : CREATIVE_COLOR_ORDER) {
                                    accept(output, family.bedDeluxe().get(color));
                                }
                            });
                        });

                if (TDConfig.anySetEnabled()) {
                    builder.withTabsBefore(ResourceLocation.fromNamespaceAndPath(AllTuttasNeeds.MODID, "tuttasdoors"));
                }
                return builder.build();
            })
            : null;

    private static void accept(CreativeModeTab.Output output, @Nullable Supplier<? extends ItemLike> item) {
        if (item != null && item.get() != null) {
            output.accept(item.get());
        }
    }
}
