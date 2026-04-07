package com.alessandro.astages.api.store.config;

import com.alessandro.astages.api.nullability.NotNullParams;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@NotNullParams
public class AConfigPreset<T> {
    private final Class<T> targetClass;
    private final List<Consumer<T>> operations = new ArrayList<>();

    public AConfigPreset(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public AConfigPreset<T> apply(Consumer<T> consumer) {
        operations.add(consumer);
        return this;
    }

    public boolean isCorrectType(T object) {
        return targetClass.isAssignableFrom(object.getClass());
    }

    public void applyTo(T object) {
        if (!isCorrectType(object)) return;

        for (var op : operations) {
            op.accept(object);
        }
    }

}