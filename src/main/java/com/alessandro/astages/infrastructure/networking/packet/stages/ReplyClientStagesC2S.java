package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AStageSource;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NotNullParamsAndMethodsReturn
public record ReplyClientStagesC2S(AStageSource requester, AStageSource askedFor, @Nullable UUID requesterUUID, @Nullable UUID playerUUID, Set<String> stages) implements AStagesPacket {
    public static final Type<ReplyClientStagesC2S> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("reply_client_stages_c2s"));

    public static final StreamCodec<FriendlyByteBuf, ReplyClientStagesC2S> STREAM_CODEC = StreamCodec.of(
        ReplyClientStagesC2S::encode,
        ReplyClientStagesC2S::decode
    );

    private static void encode(FriendlyByteBuf buf, ReplyClientStagesC2S packet) {
        buf.writeEnum(packet.requester);
        buf.writeEnum(packet.askedFor);
        if (packet.requester == AStageSource.PLAYER) { buf.writeUUID(packet.requesterUUID); }
        if (packet.askedFor == AStageSource.PLAYER) { buf.writeUUID(packet.playerUUID); }
        buf.writeCollection(packet.stages, FriendlyByteBuf::writeUtf);
    }

    private static ReplyClientStagesC2S decode(FriendlyByteBuf buf) {
        var requester = buf.readEnum(AStageSource.class);
        var askedFor = buf.readEnum(AStageSource.class);
        var requesterUUID = requester == AStageSource.PLAYER ? buf.readUUID() : null;
        var playerUUID = askedFor == AStageSource.PLAYER ? buf.readUUID() : null;
        var stages = buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf);

        return new ReplyClientStagesC2S(requester, askedFor, requesterUUID, playerUUID, stages);
    }

    @Override
    public void run(IPayloadContext context) {
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) { return; }

        CommandSource executor = requester == AStageSource.SERVER ? server : server.getPlayerList().getPlayer(requesterUUID);
        var playerChecked = askedFor == AStageSource.PLAYER ? server.getPlayerList().getPlayer(playerUUID) : null;

        if (executor == null) { return; }

        if (askedFor == AStageSource.PLAYER) {
            if (playerChecked == null) { return; }

            if (stages.isEmpty()) {
                executor.sendSystemMessage(Component.translatable("chat.astages.info.no_stages", playerChecked.getName()).withStyle(ChatFormatting.RED));
            } else {
                executor.sendSystemMessage(Component.translatable("chat.astages.info.has_stages", playerChecked.getName()).withStyle(ChatFormatting.GREEN));
                for (var stage : stages) {
                    executor.sendSystemMessage(Component.translatable("chat.astages.info.list_item", stage));
                }
            }
        } else if (askedFor == AStageSource.SERVER) {
            if (stages.isEmpty()) {
                executor.sendSystemMessage(Component.translatable("chat.astages.info.server.no_stages").withStyle(ChatFormatting.RED));
            } else {
                executor.sendSystemMessage(Component.translatable("chat.astages.info.server.has_stages").withStyle(ChatFormatting.GREEN));
                for (var stage : stages) {
                    executor.sendSystemMessage(Component.translatable("chat.astages.info.server.list_item", stage));
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
