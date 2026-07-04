package com.alessandro.astages.api.util;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.network.chat.Component;

@NotNullMethodsReturn
public class AComponentUtils {
    public static Component nullToEmpty(@Nullable Component component) {
        return component != null ? component : Component.empty();
    }
}