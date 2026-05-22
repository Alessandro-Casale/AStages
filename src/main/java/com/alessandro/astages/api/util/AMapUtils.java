package com.alessandro.astages.api.util;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

@NotNullMethodsReturn
public class AMapUtils {
    public static <T, S> SetMultimap<T, S> buildSetMultiMap() {
        return MultimapBuilder.SetMultimapBuilder.hashKeys().hashSetValues().build();
    }
}
