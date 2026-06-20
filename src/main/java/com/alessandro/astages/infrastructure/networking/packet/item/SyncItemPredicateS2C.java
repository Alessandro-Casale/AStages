package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemPredicateRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullParamsAndMethodsReturn
public record SyncItemPredicateS2C(String id, String stage, ResourceLocation modelId, boolean renderItemName, boolean hideTooltip, boolean hideInJei) implements AStagesPacket {
    public static final Type<SyncItemPredicateS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("predicate_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemPredicateS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncItemPredicateS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncItemPredicateS2C::stage,
        ResourceLocation.STREAM_CODEC, SyncItemPredicateS2C::modelId,
        ByteBufCodecs.BOOL, SyncItemPredicateS2C::renderItemName,
        ByteBufCodecs.BOOL, SyncItemPredicateS2C::hideTooltip,
        ByteBufCodecs.BOOL, SyncItemPredicateS2C::hideInJei,
        SyncItemPredicateS2C::new
    );

    public SyncItemPredicateS2C(AItemPredicateRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModelId(), restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemPredicateRestriction(id, stage)
                .set(Attributes.RENDERING_NAME, renderItemName)
                .set(Attributes.HIDING_TOOLTIP, hideTooltip)
                .set(Attributes.HIDING_JEI, hideInJei)
                .restrict(modelId);

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
