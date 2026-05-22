package com.alessandro.astages.api.misc;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

@NotNullMethodsReturn
public record Twin<A, B>(A id, B value) {
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
