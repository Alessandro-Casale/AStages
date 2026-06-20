package com.alessandro.astages.infrastructure.networking.packet.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.infrastructure.networking.configuration.ModelCheckConfigTask;
import net.minecraft.ChatFormatting;
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
public record SendServerModelsAckC2S(Set<ResourceLocation> missingModelsOnServer, Set<ResourceLocation> missingModelsOnClient) implements AStagesPacket {
    public static final Type<SendServerModelsAckC2S> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("send_server_models_ack_c2s"));

    public static final StreamCodec<FriendlyByteBuf, SendServerModelsAckC2S> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new)), SendServerModelsAckC2S::missingModelsOnServer,
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new)), SendServerModelsAckC2S::missingModelsOnClient,
        SendServerModelsAckC2S::new
    );

    @Override
    public void run(IPayloadContext context) {

        if (!missingModelsOnClient.isEmpty()) {
            var reason = Component.translatable("message.astages.missing_model.kick", missingModelsOnClient.toString()).withStyle(ChatFormatting.RED);

            if (!missingModelsOnServer.isEmpty()) {
                reason.append(Component.literal("\n\n"));
                reason.append(
                    Component.translatable("message.astages.missing_model.warning", missingModelsOnServer.toString()).withStyle(ChatFormatting.GOLD)
                );
            }

            context.disconnect(reason);
        } else {
            if (!missingModelsOnServer.isEmpty()) {
                AStages.LOGGER.warn(Component.translatable("message.astages.missing_model.warning", missingModelsOnServer.toString()).getString());
            }

            context.finishCurrentTask(ModelCheckConfigTask.TYPE);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
