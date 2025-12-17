package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.item.AClientItemPropertyRestriction;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@MethodsReturnNonnullByDefault
public record ItemPropertySyncerS2CPacket(String id, String stage, ItemStack stack, Component hiddenName, Component jadeItemMessage, Component jadeBlockMessage) implements AStagesPacket {
    public static final Type<ItemPropertySyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("item_property_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemPropertySyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, ItemPropertySyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, ItemPropertySyncerS2CPacket::stage,
        ItemStack.STREAM_CODEC, ItemPropertySyncerS2CPacket::stack,
        ByteBufCodecs.fromCodec(ComponentSerialization.CODEC), ItemPropertySyncerS2CPacket::hiddenName,
        ByteBufCodecs.fromCodec(ComponentSerialization.CODEC), ItemPropertySyncerS2CPacket::jadeItemMessage,
        ByteBufCodecs.fromCodec(ComponentSerialization.CODEC), ItemPropertySyncerS2CPacket::jadeBlockMessage,
        ItemPropertySyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemPropertyRestriction(id, stage, stack, hiddenName, jadeItemMessage, jadeBlockMessage);
        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
