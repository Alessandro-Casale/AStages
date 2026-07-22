package com.alessandro.astages.infrastructure.networking.packet.reload;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.ClientReloadContext;
import com.alessandro.astages.api.reload.ClientReloadPhase;
import com.alessandro.astages.engine.PluginManager;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullMethodsReturn
public record RequestReloadS2C(ClientReloadPhase reloadType) implements AStagesPacket {
    public static final Type<RequestReloadS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("request_reload_s2c"));

    public static final StreamCodec<FriendlyByteBuf, RequestReloadS2C> STREAM_CODEC = StreamCodec.composite(
        ACodecs.enumByName(ClientReloadPhase.class), RequestReloadS2C::reloadType,
        RequestReloadS2C::new
    );

    @Override
    public void run(IPayloadContext context) {
        PluginManager.callMethod(reloadType, new ClientReloadContext(), AStagesPlugin::onClientReload, AStagesPlugin::getDescriptionForClientReload);

//        switch (reloadType) {
//            case CLIENT_BEFORE -> AClientRestrictionManager.onReloadStarted();
//            case CLIENT_SYNC -> AClientRestrictionManager.onReloadFinished();
//            case RELOAD_BEFORE -> ClientRestrictionReloadState.reloadStarted();
//            case JEI_ITEM -> ALoader.EVENT_BUS.post(new ClientItemUpdateEvent());
//            case JEI_RECIPE -> ALoader.EVENT_BUS.post(new ClientRecipeUpdateEvent());
//            case ORE -> ALoader.EVENT_BUS.post(new ClientOreUpdateEvent());
//            case ITEM -> AClientRestrictionManager.ITEM_INSTANCE.getRegistry().clearProperties();
//            case RECIPE -> AStages.LOGGER.debug("No other operations required for MarkAsDirty method for recipe restrictions!");
//        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
