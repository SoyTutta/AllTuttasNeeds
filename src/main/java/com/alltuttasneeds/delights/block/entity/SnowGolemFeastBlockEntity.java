package com.alltuttasneeds.delights.block.entity;

import com.alltuttasneeds.delights.DelightsBlockEntities;
import com.alltuttasneeds.delights.face.SnowGolemFaceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SnowGolemFeastBlockEntity extends BlockEntity {

    @Nullable
    private Component customName;
    @Nullable
    private ResourceLocation faceId;

    public SnowGolemFeastBlockEntity(BlockPos pos, BlockState state) {
        super(DelightsBlockEntities.SNOW_GOLEM_FEAST_BE.get(), pos, state);
    }

    @Nullable
    public Component getCustomName() {
        return customName;
    }

    public void setCustomName(@Nullable Component name) {
        this.customName = name;
        ResourceLocation namedFace = SnowGolemFaceRegistry.getFaceForName(
                name == null ? null : name.getString());
        if (namedFace != null) {
            this.faceId = namedFace;
        }
        sync();
    }

    public ResourceLocation getFaceId() {
        return faceId == null ? SnowGolemFaceRegistry.DEFAULT_FACE : faceId;
    }

    public void ensureFaceAssigned() {
        if (faceId != null || !(level instanceof ServerLevel serverLevel)) return;

        ResourceLocation namedFace = SnowGolemFaceRegistry.getFaceForName(
                customName == null ? null : customName.getString());
        faceId = namedFace != null
                ? namedFace
                : SnowGolemFaceRegistry.getRandomFace(serverLevel.random);
        sync();
    }

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(customName, registries));
        }
        if (faceId != null) {
            tag.putString("Face", faceId.toString());
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("CustomName", CompoundTag.TAG_STRING)) {
            customName = parseCustomNameSafe(tag.getString("CustomName"), registries);
        } else if (tag.contains("custom_name", CompoundTag.TAG_STRING)) {
            String legacyName = tag.getString("custom_name");
            customName = legacyName.isEmpty() ? null : Component.literal(legacyName);
        } else {
            customName = null;
        }
        faceId = tag.contains("Face", CompoundTag.TAG_STRING)
                ? ResourceLocation.tryParse(tag.getString("Face"))
                : null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        ensureFaceAssigned();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
