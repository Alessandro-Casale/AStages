package com.alessandro.astages.api.holder;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.store.server.ARestriction;
import org.jetbrains.annotations.Contract;

import java.util.function.Consumer;
import java.util.function.Predicate;

@NotNullParamsAndMethodsReturn
public class ARestrictionHolder<T extends ARestriction<?, ?, ?>> {
    private final T restriction;
    private boolean lastCondition;

    private ARestrictionHolder(T restriction) {
        this.restriction = restriction;
        this.lastCondition = true;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T extends ARestriction<?, ?, ?>> ARestrictionHolder<T> hold(T restriction) {
        return new ARestrictionHolder<>(restriction);
    }

    public ARestrictionHolder<T> when(Predicate<T> predicate) {
        lastCondition = predicate.test(restriction);
        return this;
    }

    public ARestrictionHolder<T> then(Consumer<T> consumer) {
        if (lastCondition) {
            consumer.accept(restriction);
        }

        return this;
    }

    public ARestrictionHolder<T> otherwise(Consumer<T> consumer) {
        if (!lastCondition) {
            consumer.accept(restriction);
        }

        return this;
    }

    public ARestrictionHolder<T> always(Consumer<T> consumer) {
        consumer.accept(restriction);
        return this;
    }

    public ARestrictionHolder<T> whenDisabled(Attribute<Boolean> attribute) {
        lastCondition = restriction.isDisabled(attribute);
        return this;
    }

    public ARestrictionHolder<T> whenEnabled(Attribute<Boolean> attribute) {
        lastCondition = restriction.isEnabled(attribute);
        return this;
    }
}
