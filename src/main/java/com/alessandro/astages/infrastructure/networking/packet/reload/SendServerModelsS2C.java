package com.alessandro.astages.infrastructure.networking.packet.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientModelManager;
import com.alessandro.astages.engine.AModelManager;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public record SendServerModelsS2C(Set<ResourceLocation> serverModels) implements AStagesPacket {
    public static final Type<SendServerModelsS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("send_server_models_s2c"));

    public static final StreamCodec<FriendlyByteBuf, SendServerModelsS2C> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new)),
        SendServerModelsS2C::serverModels,
        SendServerModelsS2C::new
    );

    public SendServerModelsS2C() {
        this(AModelManager.MODELS.getModels());
    }

    @Override
    public void run(IPayloadContext context) {
        var clientModels = AClientModelManager.MODELS.getModels();

        var missingModels = new HashSet<>(serverModels);
        missingModels.removeAll(clientModels);

        var unknownModels = new HashSet<>(clientModels);
        unknownModels.removeAll(serverModels);

        if (!unknownModels.isEmpty()) {
            AStages.LOGGER.warn(Component.translatable("message.astages.missing_model.warning", unknownModels.toString()).getString());
        }

        context.reply(new SendServerModelsAckC2S(unknownModels, missingModels));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
