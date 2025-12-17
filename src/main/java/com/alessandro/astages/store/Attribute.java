package com.alessandro.astages.store;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import net.neoforged.neoforge.registries.DeferredRegister;

@NotNullParamsAndMethodsReturn
public class Attribute<T> {
    private static DeferredRegister<Attribute<?>> deferredRegister;

    private final String id;
    private final Class<T> type;
    private final T defaultValue;

    private Attribute(String id, Class<T> type, @Nullable T defaultValue) {
        this.id = id;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    private Attribute(String id, AttributeType<T> attributeType, @Nullable T defaultValue) {
        this(id, attributeType.getType(), defaultValue);
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

    public Class<T> getType() {
        return type;
    }

    public @Nullable T getDefaultValue() {
        return defaultValue;
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
