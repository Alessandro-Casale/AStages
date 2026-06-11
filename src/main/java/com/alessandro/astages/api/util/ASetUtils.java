package com.alessandro.astages.api.util;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NotNullMethodsReturn
public class ASetUtils {
    public static <T> T getOnlyElement(Set<T> set) {
        return Iterables.getOnlyElement(set);
    }

    public static <T> @Unmodifiable Set<T> singleton(T element) {
        return Collections.singleton(element);
    }

    public static <T> Set<T> newSynchronizedSet() {
        return Collections.synchronizedSet(new HashSet<>());
    }

    public static <T> Set<T> synchronizedSet(Set<T> set) {
        return Collections.synchronizedSet(set);
    }
}
