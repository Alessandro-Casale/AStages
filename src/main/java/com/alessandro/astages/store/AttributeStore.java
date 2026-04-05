package com.alessandro.astages.store;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

@NotNullParamsAndMethodsReturn
public class AttributeStore extends HashMap<Attribute<?>, Object> {
    @Contract(value = " -> new", pure = true)
    public static AttributeStore builder() {
        return new AttributeStore();
    }

    public static Builder compose() {
        return new Builder();
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

    public AttributeStore combineWith(AttributeStore other) {
//        putAll(store);
//        return this;
        AttributeStore result = new AttributeStore();
        result.putAll(this);
        result.putAll(other);
        return result;
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
        var joiner = new StringJoiner(", ");

        for (var attribute : keySet()) {
            joiner.add(attribute.getId());
        }


        return "AttributeStore " + getClass().getName() + "@"
            + Integer.toHexString(hashCode())
            + " [" + joiner + "]";
    }

    public static class Builder {
        private @Nullable AttributeStore selfAttributes;
        private @Nullable AttributeStore superAttributes;
        private @Nullable AttributeStore pluginAttributes;

        public Builder withSelf(AttributeStore selfAttributes) {
            this.selfAttributes = selfAttributes;
            return this;
        }

        public Builder withSuper(AttributeStore superAttributes) {
            this.superAttributes = superAttributes;
            return this;
        }

        public Builder withPlugin(AttributeStore pluginAttributes) {
            this.pluginAttributes = pluginAttributes;
            return this;
        }

        public Builder withPlugin(Map<Class<?>, AttributeStore> map, Class<?> clazz) {
            this.pluginAttributes = map.getOrDefault(clazz, null);
            return this;
        }

        public AttributeStore build() {
            AttributeStore result = new AttributeStore();

            if (superAttributes != null) {
                result = result.combineWith(superAttributes);
            }

            if (selfAttributes != null) {
                result = result.combineWith(selfAttributes);
            }

            if (pluginAttributes != null) {
                result = result.combineWith(pluginAttributes);
            }

            return result;
        }
    }
}
