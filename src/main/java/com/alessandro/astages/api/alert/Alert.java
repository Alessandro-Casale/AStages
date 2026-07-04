package com.alessandro.astages.api.alert;

@FunctionalInterface
public interface Alert<T, U, V, W, X, Y>  {
    void alert(T t, U u, V v, W w, X x, Y y);
}