package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.item.AClientItemRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemRestriction;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.store.Attributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@NotNullParamsAndMethodsReturn
public record ItemSyncerS2CPacket(String id, String stage, List<Item> items, boolean renderItemName, boolean hideTooltip, boolean hideInJei) implements AStagesPacket {
    public static final Type<ItemSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("item_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, ItemSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, ItemSyncerS2CPacket::stage,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()), ItemSyncerS2CPacket::items,
        ByteBufCodecs.BOOL, ItemSyncerS2CPacket::renderItemName,
        ByteBufCodecs.BOOL, ItemSyncerS2CPacket::hideTooltip,
        ByteBufCodecs.BOOL, ItemSyncerS2CPacket::hideInJei,
        ItemSyncerS2CPacket::new
    );

    public ItemSyncerS2CPacket(AItemRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getItems(), restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemRestriction(id, stage)
                .set(Attributes.RENDERING_NAME, renderItemName)
                .set(Attributes.HIDING_TOOLTIP, hideTooltip)
                .set(Attributes.HIDING_JEI, hideInJei);

        for (var item : items) {
            restriction.restrict(item);
        }

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
