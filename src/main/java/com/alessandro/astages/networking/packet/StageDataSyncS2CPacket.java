package com.alessandro.astages.networking.packet;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class StageDataSyncS2CPacket {
    private final List<String> stages;

    public StageDataSyncS2CPacket(List<String> stages) {
        this.stages = stages;
    }

    public StageDataSyncS2CPacket(@NotNull FriendlyByteBuf buf) {
        var readList = buf.readList(FriendlyByteBuf::readByteArray);
        var newStageList = new ArrayList<String>();

        for (var byteList : readList) {
            newStageList.add(new String(byteList));
        }

        stages = newStageList;
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        var stagesAsByte = new ArrayList<byte[]>();

        stages.forEach(stage -> {
//            ACollectionLibrary.LOGGER.debug(stage);
            stagesAsByte.add(stage.getBytes());
        });

        buf.writeCollection(stagesAsByte, FriendlyByteBuf::writeByteArray);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            ClientPlayerStage.set(stages);
            MinecraftForge.EVENT_BUS.post(new StageSyncedPlayerEvent());
        });
        ctx.get().setPacketHandled(true);
    }
}
