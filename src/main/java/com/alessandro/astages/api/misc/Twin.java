package com.alessandro.astages.api.misc;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

@NotNullParamsAndMethodsReturn
public record Twin<A, B>(@Nullable A id, @Nullable B value) {
    public Twin() {
        this(null, null);
    }

    public boolean isValid() {
        return id != null && value != null;
    }

    public static <BUF extends ByteBuf, A, B> StreamCodec<BUF, Twin<A, B>> codec(StreamCodec<BUF, A> firstCodec, StreamCodec<BUF, B> secondCodec) {
        return StreamCodec.composite(
            firstCodec, Twin::id,
            secondCodec, Twin::value,
            Twin::new
        );
    }
}
