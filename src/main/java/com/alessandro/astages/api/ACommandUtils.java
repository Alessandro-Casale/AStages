package com.alessandro.astages.api;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.mojang.brigadier.StringReader;

@NotNullParamsAndMethodsReturn
public class ACommandUtils {
    public static String parseGenericString(StringReader reader) {
        int i = reader.getCursor();
        while (reader.canRead() && reader.peek() != ' ') { reader.skip(); }
        return reader.getString().substring(i, reader.getCursor());
    }
}
