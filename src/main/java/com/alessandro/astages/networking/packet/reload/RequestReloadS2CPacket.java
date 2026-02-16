package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.actions.ClientItemUpdateEvent;
import com.alessandro.astages.event.custom.actions.ClientOreUpdateEvent;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.util.ReloadType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullMethodsReturn
public record RequestReloadS2CPacket(ReloadType reloadType) implements AStagesPacket {
    public static final Type<RequestReloadS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("request_reload_s2c_packet"));

    public static final StreamCodec<FriendlyByteBuf, RequestReloadS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.idMapper(ReloadType.BY_ID, ReloadType::getId),
        RequestReloadS2CPacket::reloadType,
        RequestReloadS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (reloadType) {
            case CLIENT_BEFORE -> AClientRestrictionManager.reloadBeforeScripts();
            case CLIENT_SYNC -> AClientRestrictionManager.reloadAfterScripts();
            case RELOAD_BEFORE -> AClientRestrictionManager.reloadStarted();
            case JEI_ITEM -> ALoader.EVENT_BUS.post(new ClientItemUpdateEvent());
            case JEI_RECIPE -> ALoader.EVENT_BUS.post(new ClientRecipeUpdateEvent());
            case ORE -> ALoader.EVENT_BUS.post(new ClientOreUpdateEvent());
            case ITEM -> AClientRestrictionManager.ITEM_INSTANCE.clearProperties();
            case RECIPE -> AStages.LOGGER.debug("No other operations required for MarkAsDirty method for recipe restrictions!");
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
