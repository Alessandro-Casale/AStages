package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.item.AClientItemPredicateRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.networking.ACodes;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.store.Attributes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ItemPredicateSyncerS2CPacket(String id, String stage, ResourceLocation modelId, boolean renderItemName, boolean hideTooltip, boolean hideInJei) implements AStagesPacket {
    public static final Type<ItemPredicateSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("predicate_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemPredicateSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, ItemPredicateSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, ItemPredicateSyncerS2CPacket::stage,
        ACodes.RESOURCE_LOCATION, ItemPredicateSyncerS2CPacket::modelId,
        ByteBufCodecs.BOOL, ItemPredicateSyncerS2CPacket::renderItemName,
        ByteBufCodecs.BOOL, ItemPredicateSyncerS2CPacket::hideTooltip,
        ByteBufCodecs.BOOL, ItemPredicateSyncerS2CPacket::hideInJei,
        ItemPredicateSyncerS2CPacket::new
    );

    public ItemPredicateSyncerS2CPacket(AItemPredicateRestriction restriction) {
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
