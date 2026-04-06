package com.alessandro.astages.infrastructure.networking;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNull;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface AStagesPacket extends CustomPacketPayload {
    void run(IPayloadContext context);

    default void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> run(context)).exceptionally(e -> {
            AStages.LOGGER.info(e.getLocalizedMessage());
            return null;
        });
    }
}
