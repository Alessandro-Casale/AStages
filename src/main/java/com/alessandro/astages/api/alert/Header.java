package com.alessandro.astages.api.alert;

@FunctionalInterface
public interface Header<T, U, V> {
    void header(T t, U u, V v);
}