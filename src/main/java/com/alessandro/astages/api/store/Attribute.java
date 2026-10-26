package com.alessandro.astages.api.store;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unchecked")
@NotNullParamsAndMethodsReturn
public class Attribute<T> {
    private static DeferredRegister<Attribute<?>> deferredRegister;

    private final String id;
    private final AttributeType<T> type;
    private final T defaultValue;

    private Attribute(String id, AttributeType<T> attributeType, @Nullable T defaultValue) {
        this.id = id;
        this.type = attributeType;
        this.defaultValue = defaultValue;
    }

    public static DeferredRegister<Attribute<?>> setCurrentDeferredRegister(DeferredRegister<Attribute<?>> deferredRegister) {
        Attribute.deferredRegister = deferredRegister;
        return deferredRegister;
    }

    public static <T> Attribute<T> create(String id, AttributeType<T> attributeType, @Nullable T defaultValue) {
        var attribute = new Attribute<>(id, attributeType, defaultValue);
        deferredRegister.register(id, () -> attribute);
        return attribute;
    }

    public String getId() {
        return id;
    }

    public AttributeType<T> getType() {
        return type;
    }

    public @Nullable T getDefaultValue() {
        return defaultValue;
    }

    public void encodeValue(RegistryFriendlyByteBuf buffer, Object value) {
        type.getCodec().encode(buffer, (T) value);
    }

    public T decodeValue(RegistryFriendlyByteBuf buf) {
        return type.getCodec().decode(buf);
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "id='" + id + '\'' +
            ", type=" + type +
            ", defaultValue=" + defaultValue +
            '}';
    }
}
