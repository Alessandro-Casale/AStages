package com.alessandro.astages.networking.packet;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

@NotNullParamsAndMethodsReturn
@Info("TO BE REMOVED")
public abstract class StageSyncerPacket {
//    private final Set<String> stages;
//    private final AOperation operation;
//
//    public StageSyncerPacket(Set<String> stages, AOperation operation) {
//        this.stages = stages;
//        this.operation = operation;
//    }
//
//    public StageSyncerPacket(FriendlyByteBuf buf) {
//        this.stages = buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf);
//        this.operation = buf.readEnum(AOperation.class);
//    }
//
//    public void toBytes(FriendlyByteBuf buf) {
//        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
//        buf.writeEnum(operation);
//    }
//
//    public abstract void handle();
//
//    public void handle(Supplier<NetworkEvent.Context> ctx) {
//        ctx.get().enqueueWork(this::handle);
//        ctx.get().setPacketHandled(true);
//    }
//
//    public Set<String> getStages() {
//        return stages;
//    }
//
//    public AOperation getOperation() {
//        return operation;
//    }
}
