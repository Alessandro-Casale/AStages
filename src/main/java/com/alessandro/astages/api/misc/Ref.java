package com.alessandro.astages.api.misc;

import java.util.Objects;

public class Ref<T> {
    T value;

    public Ref() {
        this(null);
    }

    public Ref(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Ref<?> ref)) return false;

        return Objects.equals(value, ref.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}