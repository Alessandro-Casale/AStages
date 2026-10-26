package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.network.data.CustomRestrictionData;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.ARestrictionPacket;
import com.alessandro.astages.api.network.data.RestrictionData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@NotNullParamsAndMethodsReturn
public record SyncItemS2C(RestrictionData data, List<Item> items) implements ARestrictionPacket {
    public static final Type<SyncItemS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("item_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemS2C> STREAM_CODEC = StreamCodec.composite(
        RestrictionData.STREAM_CODEC, SyncItemS2C::data,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()), SyncItemS2C::items,
        SyncItemS2C::new
    );

    public SyncItemS2C(AItemRestriction restriction) {
        this(CustomRestrictionData.item(restriction), restriction.getItems());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemRestriction(id(), stage(), priority())
            .setFromData(data, Attributes.HIDING_RECIPE_VIEWER)
            .setFromData(data, Attributes.SHOW_ACTION_BAR_NAME)
            .setFromData(data, Attributes.SHOW_TOOLTIP_NAME)
            .setFromData(data, Attributes.SHOW_RECIPE_VIEWER_NAME)
            .setFromData(data, Attributes.SHOW_JADE_ITEM_NAME)
            .setFromData(data, Attributes.SHOW_JADE_BLOCK_NAME);

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
