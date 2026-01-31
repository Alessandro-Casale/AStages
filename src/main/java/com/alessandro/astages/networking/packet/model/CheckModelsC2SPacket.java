package com.alessandro.astages.networking.packet.model;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.AModelManager;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@NotNullParams
public class CheckModelsC2SPacket implements AStagesPacket {
    private final Set<ResourceLocation> modelIds;

    public CheckModelsC2SPacket(Set<ResourceLocation> modelIds) {
        this.modelIds = modelIds;
    }

    public CheckModelsC2SPacket(FriendlyByteBuf buf) {
        this.modelIds = buf.readCollection(HashSet::new, FriendlyByteBuf::readResourceLocation);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(modelIds, FriendlyByteBuf::writeResourceLocation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var twin = AModelManager.missingAndAdditionalModels(modelIds);
            var player = ctx.get().getSender();
            if (player == null) { return; }

            if (!twin.a().isEmpty()) {
                player.connection.disconnect(Component.translatable("message.astages.missing_model.kick", twin.a()).withStyle(ChatFormatting.RED));
            } else if (!twin.b().isEmpty()) {
                player.sendSystemMessage(Component.translatable("message.astages.missing_model.warning", twin.b()).withStyle(ChatFormatting.RED));
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
