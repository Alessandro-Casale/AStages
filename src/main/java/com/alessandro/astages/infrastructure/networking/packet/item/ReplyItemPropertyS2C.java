package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemPropertyRestriction;
import com.alessandro.astages.engine.server.restriction.item.ABaseItemRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullParamsAndMethodsReturn
public record ReplyItemPropertyS2C(String id, String stage, ItemStack stack, Component hiddenName, Component jadeItemMessage, Component jadeBlockMessage) implements AStagesPacket {
    public static final Type<ReplyItemPropertyS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("reply_item_property_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ReplyItemPropertyS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, ReplyItemPropertyS2C::id,
        ByteBufCodecs.STRING_UTF8, ReplyItemPropertyS2C::stage,
        ItemStack.STREAM_CODEC, ReplyItemPropertyS2C::stack,
        ByteBufCodecs.fromCodec(ComponentSerialization.CODEC), ReplyItemPropertyS2C::hiddenName,
        ByteBufCodecs.fromCodec(ComponentSerialization.CODEC), ReplyItemPropertyS2C::jadeItemMessage,
        ByteBufCodecs.fromCodec(ComponentSerialization.CODEC), ReplyItemPropertyS2C::jadeBlockMessage,
        ReplyItemPropertyS2C::new
    );

    public ReplyItemPropertyS2C(ABaseItemRestriction<?, ?> restriction, ItemStack stack) {
        this(restriction.getId(), restriction.getStage(), stack,
            restriction.get(Attributes.Item.HIDDEN_NAME).apply(stack),
            restriction.get(Attributes.Item.JADE_ITEM_MESSAGE).apply(stack),
            restriction.get(Attributes.Item.JADE_BLOCK_MESSAGE).apply(stack));
    }

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
