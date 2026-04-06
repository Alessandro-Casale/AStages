package com.alessandro.astages.infrastructure.networking.packet.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.ReloadType;
import com.alessandro.astages.api.event.update.ClientItemUpdateEvent;
import com.alessandro.astages.api.event.update.ClientOreUpdateEvent;
import com.alessandro.astages.api.event.update.ClientRecipeUpdateEvent;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.ClientRestrictionReloadState;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullMethodsReturn
public record RequestReloadS2C(ReloadType reloadType) implements AStagesPacket {
    public static final Type<RequestReloadS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("request_reload_s2c"));

    public static final StreamCodec<FriendlyByteBuf, RequestReloadS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.idMapper(ReloadType.BY_ID, ReloadType::getId),
        RequestReloadS2C::reloadType,
        RequestReloadS2C::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (reloadType) {
            case CLIENT_BEFORE -> AClientRestrictionManager.reloadBeforeScripts();
            case CLIENT_SYNC -> AClientRestrictionManager.reloadAfterScripts();
            case RELOAD_BEFORE -> ClientRestrictionReloadState.reloadStarted();
            case JEI_ITEM -> ALoader.EVENT_BUS.post(new ClientItemUpdateEvent());
            case JEI_RECIPE -> ALoader.EVENT_BUS.post(new ClientRecipeUpdateEvent());
            case ORE -> ALoader.EVENT_BUS.post(new ClientOreUpdateEvent());
            case ITEM -> AClientRestrictionManager.ITEM_INSTANCE.getRegistry().clearProperties();
            case RECIPE -> AStages.LOGGER.debug("No other operations required for MarkAsDirty method for recipe restrictions!");
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
