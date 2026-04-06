package com.alessandro.astages.api.misc;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T, U, S> {
    void accept(T t, U u, S s);

    default TriConsumer<T, U, S> andThen(TriConsumer<? super T, ? super U, ? super S> after) {
        Objects.requireNonNull(after);

        return (l, r, s) -> {
            accept(l, r, s);
            after.accept(l, r, s);
        };
    }
}