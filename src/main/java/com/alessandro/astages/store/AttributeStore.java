package com.alessandro.astages.store;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.develop.Info;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AttributeStore extends HashMap<Attribute<?>, Object> {
    @Contract(value = " -> new", pure = true)
    public static AttributeStore builder() {
        return new AttributeStore();
    }

    public <T> AttributeStore addAttribute(Attribute<T> attribute) {
        return addAttribute(attribute, false);
    }

    @Info("If attribute is optional, allow NULL default value!")
    public <T> AttributeStore addAttribute(Attribute<T> attribute, boolean isOptional) {
        if (isOptional) {
            put(attribute, attribute.getDefaultValue());
        } else if (attribute.getDefaultValue() == null) {
            throw new NullPointerException(attribute.getId() + " has default value null!");
        } else {
            put(attribute, attribute.getDefaultValue());
        }

        return this;
    }

    @SuppressWarnings("unused")
    @Info("Check if optional attribute has value or is null!")
    public <T> boolean isPresent(Attribute<T> attribute) {
        return get(attribute) == null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(Attribute<T> attribute) {
        return (T) get(attribute); // getOrDefault(attribute, attribute.getDefaultValue());
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> AttributeStore setAttribute(Attribute<T> attribute, T value) {
        put(attribute, value);
        return this;
    }

    public AttributeStore combineWith(AttributeStore store) {
        putAll(store);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public AttributeStore overwrite(AttributeStore other) {
        Set<Attribute<?>> attributesToOverwrite;

        if (other instanceof ConfigurableAttributeStore config) {
            attributesToOverwrite = config.getModifiedAttributes();
        } else {
            attributesToOverwrite = other.allAttributes();
        }

        for (var attribute : attributesToOverwrite) {
            this.put(attribute, other.getAttribute(attribute));
        }

        return this;
    }

    public Set<Attribute<?>> allAttributes() {
        return keySet();
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();

        for (var attribute : keySet()) {
            builder.append(attribute.getId()).append(" ");
        }

        return builder.toString();
    }
}
