package com.alessandro.astages.networking;

import com.alessandro.astages.AStages;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public interface AStagesPacket extends CustomPacketPayload {
    void run(IPayloadContext context);

    default void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> run(context)).exceptionally(e -> {
            AStages.LOGGER.info(e.getLocalizedMessage());
            return null;
        });
    }
}
