package com.alessandro.astages.store;

import com.alessandro.astages.api.exception.SetAttributeNotSupported;
import com.alessandro.astages.api.nullability.NotNull;

public interface AStore<V> {
    <T> T get(Attribute<T> attribute);
    <T> V set(Attribute<T> attribute, T value);
    @NotNull AttributeStore allowedAttributes();

    default boolean isDisabled(Attribute<Boolean> attribute) throws SetAttributeNotSupported {
        return !get(attribute);
    }

    default boolean isEnabled(Attribute<Boolean> attribute) throws SetAttributeNotSupported {
        return get(attribute);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    default boolean isValueNull(Attribute<?> attribute) {
        return get(attribute) == null;
    }

    default void checkAttribute(Attribute<?> attribute) throws SetAttributeNotSupported {
        if (!allowedAttributes().containsKey(attribute)) {
            throw new SetAttributeNotSupported(attribute);
        }
    }
}