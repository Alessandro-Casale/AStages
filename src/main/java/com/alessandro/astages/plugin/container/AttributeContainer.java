package com.alessandro.astages.plugin.container;

import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

import java.util.HashMap;

@NotNullParamsAndMethodsReturn
public class AttributeContainer {
    private final HashMap<Class<?>, AttributeStore> STORE = new HashMap<>();

    public static AttributeContainer initialize() {
        return new AttributeContainer();
    }

    public void addAttribute(Class<?> restriction, Attribute<?> attribute) {
        STORE.computeIfAbsent(restriction, key -> AttributeStore.builder()).addAttribute(attribute);
    }

    public <T> void addAttribute(Class<?> restriction, Attribute<T> attribute, T initialValue) {
        STORE.computeIfAbsent(restriction, key -> AttributeStore.builder()).addAttribute(attribute).setAttribute(attribute, initialValue);
    }

    public void addAttributes(Class<?> restriction, Attribute<?>... attributes) {
        STORE.computeIfAbsent(restriction, key -> AttributeStore.builder());

        for (var attribute : attributes) {
            STORE.get(restriction).addAttribute(attribute);
        }
    }

    public HashMap<Class<?>, AttributeStore> get() {
        return STORE;
    }
}
