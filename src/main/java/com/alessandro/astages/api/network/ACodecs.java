package com.alessandro.astages.api.network;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Function8;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Contract;

import java.util.function.Function;

@NotNullParamsAndMethodsReturn
public class ACodecs {
    public static final StreamCodec<ByteBuf, ResourceLocation> RESOURCE_LOCATION = ByteBufCodecs.STRING_UTF8.map(
        // String -> ResourceLocation
        ResourceLocation::parse,
        // ResourceLocation -> String
        ResourceLocation::toString
    );

    public static final StreamCodec<ByteBuf, AABB> AABB_CODEC = StreamCodec.composite(
        ByteBufCodecs.DOUBLE, aabb -> aabb.minX,
        ByteBufCodecs.DOUBLE, aabb -> aabb.minY,
        ByteBufCodecs.DOUBLE, aabb -> aabb.minZ,
        ByteBufCodecs.DOUBLE, aabb -> aabb.maxX,
        ByteBufCodecs.DOUBLE, aabb -> aabb.maxY,
        ByteBufCodecs.DOUBLE, aabb -> aabb.maxZ,
        AABB::new
    );

    public static final StreamCodec<ByteBuf, BoundingBox> BOUNDING_BOX_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, BoundingBox::minX,
        ByteBufCodecs.INT, BoundingBox::minY,
        ByteBufCodecs.INT, BoundingBox::minZ,
        ByteBufCodecs.INT, BoundingBox::maxX,
        ByteBufCodecs.INT, BoundingBox::maxY,
        ByteBufCodecs.INT, BoundingBox::maxZ,
        BoundingBox::new
    );

    public static final StreamCodec<ByteBuf, ChunkPos> CHUNK_POS_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, chunkPos -> chunkPos.x,
        ByteBufCodecs.INT, chunkPos -> chunkPos.z,
        ChunkPos::new
    );

    @NotNullParams
    public static <T> StreamCodec<RegistryFriendlyByteBuf, T> nullableOr(StreamCodec<RegistryFriendlyByteBuf, T> inner) {
        return new StreamCodec<>() {
            @Override
            public @Nullable T decode(RegistryFriendlyByteBuf buf) {
                boolean present = buf.readBoolean();
                return present ? inner.decode(buf) : null;
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, @Nullable T value) {
                buf.writeBoolean(value != null);
                if (value != null) {
                    inner.encode(buf, value);
                }
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final Function7<T1, T2, T3, T4, T5, T6, T7, C> factory) {
        return new StreamCodec<>() {
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                T4 t4 = codec4.decode(buffer);
                T5 t5 = codec5.decode(buffer);
                T6 t6 = codec6.decode(buffer);
                T7 t7 = codec7.decode(buffer);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            public void encode(B buffer, C object) {
                codec1.encode(buffer, getter1.apply(object));
                codec2.encode(buffer, getter2.apply(object));
                codec3.encode(buffer, getter3.apply(object));
                codec4.encode(buffer, getter4.apply(object));
                codec5.encode(buffer, getter5.apply(object));
                codec6.encode(buffer, getter6.apply(object));
                codec7.encode(buffer, getter7.apply(object));
            }
        };
    }

    @Contract(value = "_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ -> new", pure = true)
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> factory) {
        return new StreamCodec<>() {
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                T4 t4 = codec4.decode(buffer);
                T5 t5 = codec5.decode(buffer);
                T6 t6 = codec6.decode(buffer);
                T7 t7 = codec7.decode(buffer);
                T8 t8 = codec8.decode(buffer);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            public void encode(B buffer, C object) {
                codec1.encode(buffer, getter1.apply(object));
                codec2.encode(buffer, getter2.apply(object));
                codec3.encode(buffer, getter3.apply(object));
                codec4.encode(buffer, getter4.apply(object));
                codec5.encode(buffer, getter5.apply(object));
                codec6.encode(buffer, getter6.apply(object));
                codec7.encode(buffer, getter7.apply(object));
                codec8.encode(buffer, getter8.apply(object));
            }
        };
    }
}
