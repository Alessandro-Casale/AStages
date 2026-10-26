package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.network.data.CustomRestrictionData;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemTagRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemTagRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.ARestrictionPacket;
import com.alessandro.astages.api.network.data.RestrictionData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public record SyncItemTagS2C(RestrictionData data, TagKey<Item> tag, Set<Item> ignoredItems) implements ARestrictionPacket {
    public static final Type<SyncItemTagS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("tag_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemTagS2C> STREAM_CODEC = StreamCodec.composite(
        RestrictionData.STREAM_CODEC, SyncItemTagS2C::data,
        ACodecs.tagKey(Registries.ITEM), SyncItemTagS2C::tag,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.collection(HashSet::new)), SyncItemTagS2C::ignoredItems,
        SyncItemTagS2C::new
    );

    public SyncItemTagS2C(AItemTagRestriction restriction) {
        this(CustomRestrictionData.item(restriction), restriction.getTag(), restriction.getIgnoredItems());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemTagRestriction(id(), stage(), priority())
            .restrict(tag)
            .ignoreItems(ignoredItems)
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
