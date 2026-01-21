package com.alessandro.astages.api.base;

@FunctionalInterface
public interface Elaborator<T, U> {
    void elaborate(T t, U u);
}