package com.alessandro.astages.api.store.container;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.Attribute;

import java.util.HashSet;
import java.util.Set;

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
