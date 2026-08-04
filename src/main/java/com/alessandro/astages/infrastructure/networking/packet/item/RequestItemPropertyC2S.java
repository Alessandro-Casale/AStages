package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.Contract;

import java.util.Objects;
import java.util.function.Function;

@NotNullMethodsReturn
public record RequestItemPropertyC2S(String id, String stage, ItemStack stack) implements AStagesPacket {
    private static final Function<String, RuntimeException> EXCEPTION = id -> new RuntimeException("Illegal identifier synchronization: " + id + " de-synchronized between server and client!");
    private static final Function<String, RuntimeException> NULL_EXCEPTION = id -> new NullPointerException("Illegal null synchronization: " + id + " not found on server!");

    public static final Type<RequestItemPropertyC2S> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("request_item_property_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestItemPropertyC2S> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, RequestItemPropertyC2S::id,
        ByteBufCodecs.STRING_UTF8, RequestItemPropertyC2S::stage,
        ItemStack.STREAM_CODEC, RequestItemPropertyC2S::stack,
        RequestItemPropertyC2S::new
    );

    @Override
    public void run(IPayloadContext context) {
        // HERE WE ARE ON SERVER!
        var serverRestriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(id);

        if (serverRestriction != null) {
            if (!Objects.equals(serverRestriction.getId(), id)) { throw EXCEPTION.apply(id); }
            if (!Objects.equals(serverRestriction.getStage(), stage)) { throw EXCEPTION.apply(id); }

            Networking.sendToPlayer((ServerPlayer) context.player(), new ReplyItemPropertyS2C(id, stage, stack,
                serverRestriction.get(Attributes.Item.ACTION_BAR_MESSAGE).apply(stack),
                serverRestriction.get(Attributes.Item.TOOLTIP_MESSAGE).apply(stack),
                serverRestriction.get(Attributes.Item.RECIPE_VIEWER_MESSAGE).apply(stack),
                serverRestriction.get(Attributes.Item.JADE_ITEM_MESSAGE).apply(stack),
                serverRestriction.get(Attributes.Item.JADE_BLOCK_MESSAGE).apply(stack)
            ));
        } else {
            throw NULL_EXCEPTION.apply(id);
        }
    }

    @Contract(pure = true)
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
