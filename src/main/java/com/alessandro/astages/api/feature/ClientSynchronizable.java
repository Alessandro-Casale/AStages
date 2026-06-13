package com.alessandro.astages.api.feature;

import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.develop.Info;
import net.minecraft.server.level.ServerPlayer;

@Info("Implement this interface to add client synchronization to a manager!")
public interface ClientSynchronizable {
    void synchronizeWithClient(@Nullable ServerPlayer player);
}
