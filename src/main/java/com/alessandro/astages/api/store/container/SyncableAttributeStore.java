package com.alessandro.astages.api.store.container;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.infrastructure.registry.AStagesRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

@NotNullParamsAndMethodsReturn
public class SyncableAttributeStore extends AttributeStore {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncableAttributeStore> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SyncableAttributeStore decode(RegistryFriendlyByteBuf buffer) {
            return SyncableAttributeStore.decodeAndBuild(buffer);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, SyncableAttributeStore store) {
           store.encode(buffer);
        }
    };

    public static SyncableAttributeStore decodeAndBuild(RegistryFriendlyByteBuf buffer) {
        return new SyncableAttributeStore().decode(buffer);
    }

    private SyncableAttributeStore decode(RegistryFriendlyByteBuf buffer) {
        int size = ByteBufCodecs.VAR_INT.decode(buffer);

        for (int i = 0; i < size; i++) {
            var attribute = ByteBufCodecs.registry(AStagesRegistries.Keys.ATTRIBUTES).decode(buffer);
            put(attribute, attribute.decodeValue(buffer));
        }

        return this;
    }

    private void encode(RegistryFriendlyByteBuf buffer) {
        buffer.writeVarInt(size());

        forEach((attribute, value) -> {
            ByteBufCodecs.registry(AStagesRegistries.Keys.ATTRIBUTES).encode(buffer, attribute);
            attribute.encodeValue(buffer, value);
        });
    }

    public SyncableAttributeStore syncAttribute(Attribute<?> attribute, Object value) {
        put(attribute, value);
        return this;
    }
}
