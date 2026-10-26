package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.network.data.CustomRestrictionData;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemPredicateRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.ARestrictionPacket;
import com.alessandro.astages.api.network.data.RestrictionData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullParamsAndMethodsReturn
public record SyncItemPredicateS2C(RestrictionData data, ResourceLocation modelId) implements ARestrictionPacket {
    public static final Type<SyncItemPredicateS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("predicate_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemPredicateS2C> STREAM_CODEC = StreamCodec.composite(
        RestrictionData.STREAM_CODEC, SyncItemPredicateS2C::data,
        ResourceLocation.STREAM_CODEC, SyncItemPredicateS2C::modelId,
        SyncItemPredicateS2C::new
    );

    public SyncItemPredicateS2C(AItemPredicateRestriction restriction) {
        this(CustomRestrictionData.item(restriction), restriction.getModelId());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemPredicateRestriction(id(), stage(), priority())
            .restrict(modelId)
            .setFromData(data, Attributes.HIDING_RECIPE_VIEWER)
            .setFromData(data, Attributes.SHOW_ACTION_BAR_NAME)
            .setFromData(data, Attributes.SHOW_TOOLTIP_NAME)
            .setFromData(data, Attributes.SHOW_RECIPE_VIEWER_NAME)
            .setFromData(data, Attributes.SHOW_JADE_ITEM_NAME)
            .setFromData(data, Attributes.SHOW_JADE_BLOCK_NAME);

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
