package com.alessandro.astages.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerStageProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerStage> PLAYER_STAGE = CapabilityManager.get(new CapabilityToken<>() { });

    private PlayerStage playerStage = null;
    private final LazyOptional<PlayerStage> optional = LazyOptional.of(this::getOrCreatePlayerStage);

    private PlayerStage getOrCreatePlayerStage() {
        if (this.playerStage == null) {
            this.playerStage = new PlayerStage();
        }

        return this.playerStage;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_STAGE) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        getOrCreatePlayerStage().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getOrCreatePlayerStage().loadNBTData(nbt);
    }
}
