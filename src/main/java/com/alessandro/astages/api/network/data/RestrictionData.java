package com.alessandro.astages.api.network.data;

import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.store.container.SyncableAttributeStore;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

@NotNullParamsAndMethodsReturn
public record RestrictionData(String id, String stage, int priority, @Nullable SyncableAttributeStore attributes) {
    public static final StreamCodec<RegistryFriendlyByteBuf, RestrictionData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, RestrictionData::id,
        ByteBufCodecs.STRING_UTF8, RestrictionData::stage,
        ByteBufCodecs.VAR_INT, RestrictionData::priority,
        ACodecs.nullableOr(SyncableAttributeStore.STREAM_CODEC), RestrictionData::attributes,
        RestrictionData::new
    );

    public static RestrictionData of(ARestriction<?, ?, ?> restriction) {
        return of(restriction.getId(), restriction.getStage(), restriction.getPriority());
    }

    public static RestrictionData of(String id, String stage, int priority) {
        return new RestrictionData(id, stage, priority, null);
    }

    public static RestrictionData of(ARestriction<?, ?, ?> restriction, Attribute<?>... attributes) {
        var store = new SyncableAttributeStore();

        for (var attribute : attributes) {
            store.syncAttribute(attribute, restriction.get(attribute));
        }

        return of(restriction.getId(), restriction.getStage(), restriction.getPriority(), store);
    }

    public static RestrictionData of(String id, String stage, int priority, SyncableAttributeStore attributes) {
        return new RestrictionData(id, stage, priority, attributes);
    }
}
