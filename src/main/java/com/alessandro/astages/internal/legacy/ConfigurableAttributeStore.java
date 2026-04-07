package com.alessandro.astages.internal.legacy;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.store.container.AttributeStore;

import java.util.HashSet;
import java.util.Set;

@Deprecated(forRemoval = true)
@NotNullParamsAndMethodsReturn
public class ConfigurableAttributeStore extends AttributeStore {
    private final Set<Attribute<?>> modifiedAttributes = new HashSet<>();

    @Override
    public <T> AttributeStore setAttribute(Attribute<T> attribute, T value) {
        modifiedAttributes.add(attribute);
        return super.setAttribute(attribute, value);
    }

    public Set<Attribute<?>> getModifiedAttributes() {
        return modifiedAttributes;
    }
}
