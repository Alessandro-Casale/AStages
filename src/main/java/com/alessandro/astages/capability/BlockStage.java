package com.alessandro.astages.capability;

import com.alessandro.astages.AStages;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

@AutoRegisterCapability
public class BlockStage {
    private UUID owner;

    public UUID getOwner() {
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
//            try {
                owner = nbt.getUUID("owner");
//            } catch (NullPointerException exception) {
//                AStages.LOGGER.debug(exception.getMessage());
//            }
        }
    }
}
