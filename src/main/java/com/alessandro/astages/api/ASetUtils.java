package com.alessandro.astages.api;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.google.common.collect.Iterables;

import java.util.Collections;
import java.util.Set;

@NotNullMethodsReturn
public class ASetUtils {
    public static <T> T getOnlyElement(Set<T> set) {
        return Iterables.getOnlyElement(set);
    }

    public static <T> Set<T> singleton(T element) {
        return Collections.singleton(element);
    }
}
