package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemRestriction;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@NotNullParamsAndMethodsReturn
public record SyncItemS2C(String id, String stage, List<Item> items, boolean renderItemName, boolean hideTooltip, boolean hideInJei) implements AStagesPacket {
    public static final Type<SyncItemS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("item_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncItemS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncItemS2C::stage,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()), SyncItemS2C::items,
        ByteBufCodecs.BOOL, SyncItemS2C::renderItemName,
        ByteBufCodecs.BOOL, SyncItemS2C::hideTooltip,
        ByteBufCodecs.BOOL, SyncItemS2C::hideInJei,
        SyncItemS2C::new
    );

    public SyncItemS2C(AItemRestriction restriction) {
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
