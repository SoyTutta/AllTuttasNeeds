package com.alltuttasneeds.core.network;

import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.beds.BedBlanketIngredients;
import com.alltuttasneeds.beds.BedCoverIngredients;
import com.alltuttasneeds.beds.BedIngredientSyncState;
import com.alltuttasneeds.beds.BedTier;
import com.alltuttasneeds.beds.BedTierResolver;
import com.alltuttasneeds.beds.BedTierSyncState;
import com.alltuttasneeds.beds.config.TBConfig;
import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.delights.config.DelightsConfig;
import com.alltuttasneeds.delights.crafting.DelightsRecipeSyncState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public final class ATNNetwork {
    private static final String PROTOCOL_VERSION = "4";

    private ATNNetwork() {}

    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(PROTOCOL_VERSION);
        registrar.configurationBidirectional(
                PresencePayload.TYPE,
                PresencePayload.STREAM_CODEC,
                (payload, context) -> {});
        registrar.playToClient(
                ServerRulesSyncPayload.TYPE,
                ServerRulesSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    BedTierSyncState.apply(
                            payload.tiers(),
                            payload.bedsModuleEnabled(),
                            payload.tieredSleepDurationEnabled(),
                            payload.vanillaBedsUseTieredSleepDuration(),
                            payload.sleepDurationMultipliers());
                    DelightsRecipeSyncState.apply(payload.useCheeseWedges());
                });
        registrar.playToClient(
                BedIngredientsSyncPayload.TYPE,
                BedIngredientsSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    BedCoverIngredients.applySynced(payload.covers());
                    BedBlanketIngredients.applySynced(payload.blankets(), payload.recipeItems());
                    BedIngredientSyncState.apply(payload.directApplyDisabled());
                });
    }

    public static void syncServerRules(Stream<ServerPlayer> players) {
        ServerRulesSyncPayload rules = ServerRulesSyncPayload.create();
        BedIngredientsSyncPayload ingredients = BedIngredientsSyncPayload.create();
        players.forEach(player -> {
            PacketDistributor.sendToPlayer(player, rules);
            PacketDistributor.sendToPlayer(player, ingredients);
        });
    }

    private static final class PresencePayload implements CustomPacketPayload {
        private static final Type<PresencePayload> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(AllTuttasNeeds.MODID, "presence"));
        private static final PresencePayload INSTANCE = new PresencePayload();
        private static final StreamCodec<FriendlyByteBuf, PresencePayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

        private PresencePayload() {}

        @Override
        public Type<PresencePayload> type() {
            return TYPE;
        }
    }

    private record ServerRulesSyncPayload(Map<ResourceLocation, BedTier> tiers,
                                          boolean bedsModuleEnabled,
                                          boolean tieredSleepDurationEnabled,
                                          boolean vanillaBedsUseTieredSleepDuration,
                                          Map<BedTier, Double> sleepDurationMultipliers,
                                          boolean useCheeseWedges) implements CustomPacketPayload {
        private static final int MAX_ENTRIES = 32768;
        private static final Type<ServerRulesSyncPayload> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(AllTuttasNeeds.MODID, "server_rules"));
        private static final StreamCodec<RegistryFriendlyByteBuf, ServerRulesSyncPayload> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public ServerRulesSyncPayload decode(RegistryFriendlyByteBuf buffer) {
                int size = buffer.readVarInt();
                if (size < 0 || size > MAX_ENTRIES) {
                    throw new IllegalArgumentException("Invalid synced bed tier count: " + size);
                }

                Map<ResourceLocation, BedTier> tiers = new HashMap<>(size);
                for (int index = 0; index < size; index++) {
                    tiers.put(buffer.readResourceLocation(), buffer.readEnum(BedTier.class));
                }

                boolean bedsModuleEnabled = buffer.readBoolean();
                boolean tieredSleepDurationEnabled = buffer.readBoolean();
                boolean vanillaBedsUseTieredSleepDuration = buffer.readBoolean();
                EnumMap<BedTier, Double> sleepDurationMultipliers = new EnumMap<>(BedTier.class);
                for (BedTier tier : BedTier.values()) {
                    double multiplier = buffer.readDouble();
                    if (!Double.isFinite(multiplier) || multiplier < 0.1D || multiplier > 10.0D) {
                        throw new IllegalArgumentException("Invalid synced sleep multiplier for " + tier + ": " + multiplier);
                    }
                    sleepDurationMultipliers.put(tier, multiplier);
                }
                boolean useCheeseWedges = buffer.readBoolean();
                return new ServerRulesSyncPayload(
                        tiers,
                        bedsModuleEnabled,
                        tieredSleepDurationEnabled,
                        vanillaBedsUseTieredSleepDuration,
                        sleepDurationMultipliers,
                        useCheeseWedges);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, ServerRulesSyncPayload payload) {
                buffer.writeVarInt(payload.tiers().size());
                payload.tiers().forEach((id, tier) -> {
                    buffer.writeResourceLocation(id);
                    buffer.writeEnum(tier);
                });
                buffer.writeBoolean(payload.bedsModuleEnabled());
                buffer.writeBoolean(payload.tieredSleepDurationEnabled());
                buffer.writeBoolean(payload.vanillaBedsUseTieredSleepDuration());
                for (BedTier tier : BedTier.values()) {
                    buffer.writeDouble(payload.sleepDurationMultipliers().get(tier));
                }
                buffer.writeBoolean(payload.useCheeseWedges());
            }
        };

        private ServerRulesSyncPayload {
            tiers = Map.copyOf(tiers);
            sleepDurationMultipliers = Map.copyOf(sleepDurationMultipliers);
        }

        private static ServerRulesSyncPayload create() {
            Map<ResourceLocation, BedTier> tiers = new LinkedHashMap<>();
            for (Block block : BuiltInRegistries.BLOCK) {
                BedTier tier = BedTierResolver.resolve(block);
                if (tier != null) tiers.put(BuiltInRegistries.BLOCK.getKey(block), tier);
            }

            EnumMap<BedTier, Double> sleepDurationMultipliers = new EnumMap<>(BedTier.class);
            for (BedTier tier : BedTier.values()) {
                sleepDurationMultipliers.put(tier, TBConfig.sleepDurationMultiplier(tier));
            }
            boolean useCheeseWedges = DelightsConfig.isModuleEnabled()
                    && DelightsConfig.useCheeseWedges()
                    && Mods.BREWIN_AND_CHEWIN.isLoaded();
            return new ServerRulesSyncPayload(
                    tiers,
                    TBConfig.isModuleEnabled(),
                    TBConfig.tieredSleepDurationEnabled.get(),
                    TBConfig.vanillaBedsUseTieredSleepDuration.get(),
                    sleepDurationMultipliers,
                    useCheeseWedges);
        }

        @Override
        public Type<ServerRulesSyncPayload> type() {
            return TYPE;
        }
    }

    private record BedIngredientsSyncPayload(List<BedCoverIngredients.SyncedEntry> covers,
                                             List<BedBlanketIngredients.SyncedIngredient> blankets,
                                             List<BedBlanketIngredients.SyncedRecipeItem> recipeItems,
                                             Set<String> directApplyDisabled) implements CustomPacketPayload {
        private static final int MAX_ENTRIES = 32768;
        private static final Type<BedIngredientsSyncPayload> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(AllTuttasNeeds.MODID, "bed_ingredients"));
        private static final StreamCodec<RegistryFriendlyByteBuf, BedIngredientsSyncPayload> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public BedIngredientsSyncPayload decode(RegistryFriendlyByteBuf buffer) {
                List<BedCoverIngredients.SyncedEntry> covers = new ArrayList<>();
                for (int index = 0, size = readSize(buffer, "cover"); index < size; index++) {
                    covers.add(new BedCoverIngredients.SyncedEntry(
                            buffer.readUtf(256), buffer.readResourceLocation()));
                }

                List<BedBlanketIngredients.SyncedIngredient> blankets = new ArrayList<>();
                for (int index = 0, size = readSize(buffer, "blanket"); index < size; index++) {
                    blankets.add(new BedBlanketIngredients.SyncedIngredient(
                            buffer.readUtf(256), buffer.readEnum(net.minecraft.world.item.DyeColor.class),
                            buffer.readResourceLocation()));
                }

                List<BedBlanketIngredients.SyncedRecipeItem> recipeItems = new ArrayList<>();
                for (int index = 0, size = readSize(buffer, "blanket recipe item"); index < size; index++) {
                    recipeItems.add(new BedBlanketIngredients.SyncedRecipeItem(
                            buffer.readUtf(256), buffer.readEnum(net.minecraft.world.item.DyeColor.class),
                            buffer.readEnum(BedTier.class), buffer.readResourceLocation()));
                }

                Set<String> disabled = new HashSet<>();
                for (int index = 0, size = readSize(buffer, "disabled direct application"); index < size; index++) {
                    disabled.add(buffer.readUtf(256));
                }
                return new BedIngredientsSyncPayload(covers, blankets, recipeItems, disabled);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, BedIngredientsSyncPayload payload) {
                buffer.writeVarInt(payload.covers().size());
                payload.covers().forEach(entry -> {
                    buffer.writeUtf(entry.suffix(), 256);
                    buffer.writeResourceLocation(entry.item());
                });

                buffer.writeVarInt(payload.blankets().size());
                payload.blankets().forEach(entry -> {
                    buffer.writeUtf(entry.suffix(), 256);
                    buffer.writeEnum(entry.color());
                    buffer.writeResourceLocation(entry.item());
                });

                buffer.writeVarInt(payload.recipeItems().size());
                payload.recipeItems().forEach(entry -> {
                    buffer.writeUtf(entry.suffix(), 256);
                    buffer.writeEnum(entry.color());
                    buffer.writeEnum(entry.tier());
                    buffer.writeResourceLocation(entry.item());
                });

                buffer.writeVarInt(payload.directApplyDisabled().size());
                payload.directApplyDisabled().forEach(suffix -> buffer.writeUtf(suffix, 256));
            }

            private int readSize(RegistryFriendlyByteBuf buffer, String type) {
                int size = buffer.readVarInt();
                if (size < 0 || size > MAX_ENTRIES) {
                    throw new IllegalArgumentException("Invalid synced " + type + " count: " + size);
                }
                return size;
            }
        };

        private BedIngredientsSyncPayload {
            covers = List.copyOf(covers);
            blankets = List.copyOf(blankets);
            recipeItems = List.copyOf(recipeItems);
            directApplyDisabled = Set.copyOf(directApplyDisabled);
        }

        private static BedIngredientsSyncPayload create() {
            return new BedIngredientsSyncPayload(
                    BedCoverIngredients.syncedEntries(),
                    BedBlanketIngredients.syncedIngredients(),
                    BedBlanketIngredients.syncedRecipeItems(),
                    Set.copyOf(TBConfig.directApplyDisabled.get()));
        }

        @Override
        public Type<BedIngredientsSyncPayload> type() {
            return TYPE;
        }
    }
}
