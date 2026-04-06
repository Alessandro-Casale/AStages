package com.alessandro.astages.api.holder;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.restriction.AClientRestriction;
import org.jetbrains.annotations.Contract;

import java.util.function.Consumer;
import java.util.function.Predicate;

@NotNullParamsAndMethodsReturn
public class AClientRestrictionHolder<T extends AClientRestriction<?, ?, ?>> {
    private final T restriction;
    private boolean lastCondition;

    private AClientRestrictionHolder(T restriction) {
        this.restriction = restriction;
        this.lastCondition = true;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T extends AClientRestriction<?, ?, ?>> AClientRestrictionHolder<T> hold(T restriction) {
        return new AClientRestrictionHolder<>(restriction);
    }

    public AClientRestrictionHolder<T> when(Predicate<T> predicate) {
        lastCondition = predicate.test(restriction);
        return this;
    }

    public AClientRestrictionHolder<T> then(Consumer<T> consumer) {
        if (lastCondition) {
            consumer.accept(restriction);
        }

        return this;
    }

    public AClientRestrictionHolder<T> otherwise(Consumer<T> consumer) {
        if (!lastCondition) {
            consumer.accept(restriction);
        }

        return this;
    }

    public AClientRestrictionHolder<T> always(Consumer<T> consumer) {
        consumer.accept(restriction);
        return this;
    }

    public AClientRestrictionHolder<T> whenDisabled(Attribute<Boolean> attribute) {
        lastCondition = restriction.isDisabled(attribute);
        return this;
    }

    public AClientRestrictionHolder<T> whenEnabled(Attribute<Boolean> attribute) {
        lastCondition = restriction.isEnabled(attribute);
        return this;
    }
}