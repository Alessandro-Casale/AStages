package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.network.data.CustomRestrictionData;
import com.alessandro.astages.api.network.data.RestrictionData;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemModRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemModRestriction;
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
public record SyncItemModS2C(RestrictionData data, Set<String> modIds, Set<Item> ignoredItems, Set<TagKey<Item>> ignoredTags) implements ASyncRestrictionPacket<AClientItemModRestriction> {
    public static final Type<SyncItemModS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_item_mod_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemModS2C> STREAM_CODEC = StreamCodec.composite(
        RestrictionData.STREAM_CODEC, SyncItemModS2C::data,
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), SyncItemModS2C::modIds,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.collection(HashSet::new)), SyncItemModS2C::ignoredItems,
        ACodecs.tagKey(Registries.ITEM).apply(ByteBufCodecs.collection(HashSet::new)), SyncItemModS2C::ignoredTags,
        SyncItemModS2C::new
    );

    public SyncItemModS2C(AItemModRestriction restriction) {
        this(CustomRestrictionData.item(restriction), restriction.getModIds(), restriction.getIgnoredItems(), restriction.getIgnoredTags());
    }

    @Override
    public AClientMinimalManager<?, ?> manager() {
        return AClientRestrictionManager.ITEM_INSTANCE;
    }

    @Override
    public Class<AClientItemModRestriction> restrictionClass() {
        return AClientItemModRestriction.class;
    }

    @Override
    public AClientItemModRestriction instantiateRestriction() {
        return new AClientItemModRestriction(id(), stage(), priority());
    }

    @Override
    public void fillContents(AClientItemModRestriction restriction) {
        for (var modId : modIds) {
            restriction.restrict(modId);
        }

        restriction
            .ignoreItems(ignoredItems)
            .ignoreTags(ignoredTags);
    }

    @Override
    public void register(AClientItemModRestriction restriction) {
        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
