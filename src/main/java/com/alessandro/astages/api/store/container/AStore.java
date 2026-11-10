package com.alessandro.astages.api.store.container;

import com.alessandro.astages.api.exception.SetAttributeNotSupported;
import com.alessandro.astages.api.network.data.RestrictionData;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.store.Attribute;

@NotNullParamsAndMethodsReturn
public interface AStore<V> {
    <T> @Nullable T get(Attribute<T> attribute);
    <T> V set(Attribute<T> attribute, T value);
    AttributeStore allowedAttributes();

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

    default <T> V setFromData(RestrictionData data, Attribute<T> attribute) {
        return set(attribute, data.attributes().getAttribute(attribute));
    }
}