package com.alessandro.astages.api.util;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.store.config.AConfigPreset;

@NotNullMethodsReturn
public class APresetUtils {
    public static <T> AConfigPreset<T> createPresetFor(Class<T> clazz) {
        return new AConfigPreset<>(clazz);
    }
}