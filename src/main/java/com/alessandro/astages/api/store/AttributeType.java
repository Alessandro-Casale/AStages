package com.alessandro.astages.api.store;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.google.common.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

@SuppressWarnings("unchecked")
@NotNullParamsAndMethodsReturn
public class AttributeType<T> {
    private final Class<T> type;
    private final StreamCodec<? extends ByteBuf, T> codec;

    private AttributeType(Class<T> type, @Nullable StreamCodec<? extends ByteBuf, T> codec) {
        this.type = type;
        this.codec = codec;
    }

    public static <T> AttributeType<T> create(Class<T> type) {
        return new AttributeType<>(type, null);
    }

    public static <T, B extends ByteBuf> AttributeType<T> create(Class<T> type, StreamCodec<B, T> codec) {
        return new AttributeType<>(type, codec);
    }

    public static <T> AttributeType<T> create(TypeToken<T> token) {
        return new AttributeType<>((Class<T>) token.getRawType(), null);
    }

    public static <T, B extends ByteBuf> AttributeType<T> create(TypeToken<T> token, StreamCodec<B, T> codec) {
        return new AttributeType<>((Class<T>) token.getRawType(), codec);
    }

    public Class<T> getType() {
        return type;
    }

    public <B extends ByteBuf> StreamCodec<B, T> getCodec() {
        return (StreamCodec<B, T>) codec;
    }

    public boolean isSyncable() {
        return codec != null;
    }
}
