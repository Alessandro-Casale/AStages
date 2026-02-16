package com.alessandro.astages.networking.packet;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParams;

@NotNullParams
@Info("TO BE REMOVED")
public abstract class RestrictionSyncerPacket {
    // implements AStagesPacket {
//    private final String id;
//    private final String stage;
//
//    public RestrictionSyncerPacket(String id, String stage) {
//        this.id = id;
//        this.stage = stage;
//    }
//
//    public RestrictionSyncerPacket(FriendlyByteBuf buf) {
//        id = buf.readUtf();
//        stage = buf.readUtf();
//    }
//
//    public void toBytes(FriendlyByteBuf buf) {
//        buf.writeUtf(id);
//        buf.writeUtf(stage);
//    }
//
//    public abstract void handle();
//
//    public void handle(IPayloadContext context) {
//        context.enqueueWork(() -> handle()).exceptionally(e -> {
//            AStages.LOGGER.debug(e.getLocalizedMessage());
//            return null;
//        });
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public String getStage() {
//        return stage;
//    }
}
