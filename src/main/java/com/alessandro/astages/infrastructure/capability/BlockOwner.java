package com.alessandro.astages.infrastructure.capability;

import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.UUID;

@AutoRegisterCapability
public class BlockOwner {
    public static String OWNER_KEY = "owner";
    private UUID owner;

    public @Nullable UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (owner != null) {
            nbt.putUUID("owner", owner);
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt != null) {
            owner = nbt.contains(OWNER_KEY) ? nbt.getUUID(OWNER_KEY) : null;
        }
    }
}
