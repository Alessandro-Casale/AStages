package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.network.data.CustomRestrictionData;
import com.alessandro.astages.api.network.data.RestrictionData;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemTagRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemTagRestriction;
import com.alessandro.astages.infrastructure.networking.ASyncRestrictionPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public record SyncItemTagS2C(RestrictionData data, TagKey<Item> tag, Set<Item> ignoredItems) implements ASyncRestrictionPacket<AClientItemTagRestriction> {
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
    public AClientMinimalManager<?, ?> manager() {
        return AClientRestrictionManager.ITEM_INSTANCE;
    }

    @Override
    public Class<AClientItemTagRestriction> restrictionClass() {
        return AClientItemTagRestriction.class;
    }

    @Override
    public AClientItemTagRestriction instantiateRestriction() {
        return new AClientItemTagRestriction(id(), stage(), priority());
    }

    @Override
    public void fillContents(AClientItemTagRestriction restriction) {
        restriction
            .restrict(tag)
            .ignoreItems(ignoredItems);
    }

    @Override
    public void register(AClientItemTagRestriction restriction) {
        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
