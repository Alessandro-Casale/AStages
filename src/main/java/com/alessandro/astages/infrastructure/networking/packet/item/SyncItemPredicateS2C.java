package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.network.data.CustomRestrictionData;
import com.alessandro.astages.api.network.data.RestrictionData;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemPredicateRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.infrastructure.networking.ASyncRestrictionPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

@NotNullParamsAndMethodsReturn
public record SyncItemPredicateS2C(RestrictionData data, ResourceLocation modelId) implements ASyncRestrictionPacket<AClientItemPredicateRestriction> {
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
    public AClientMinimalManager<?, ?> manager() {
        return AClientRestrictionManager.ITEM_INSTANCE;
    }

    @Override
    public Class<AClientItemPredicateRestriction> restrictionClass() {
        return AClientItemPredicateRestriction.class;
    }

    @Override
    public AClientItemPredicateRestriction instantiateRestriction() {
        return new AClientItemPredicateRestriction(id(), stage(), priority());
    }

    @Override
    public void fillContents(AClientItemPredicateRestriction restriction) {
        restriction.restrict(modelId);
    }

    @Override
    public void register(AClientItemPredicateRestriction restriction) {
        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
