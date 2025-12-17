package com.alessandro.astages.api;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.resources.ResourceLocation;

@NotNullParamsAndMethodsReturn
public class ATextUtils {
    public static String stageToDescription(String input) {
        return capitalizeWords(input.replace('_', ' '));
    }

    public static String structureToDescription(ResourceLocation input) {
        return capitalizeWords(input.getPath().replace('_', ' '));
    }

    public static String dimensionToDescription(ResourceLocation input) {
        return capitalizeWords(input.getPath().replace('_', ' '));
    }

    public static String capitalizeWords(String input) {
        // split the input string into an array of words
        String[] words = input.split("\\s");

        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(Character.toTitleCase(word.charAt(0)))
                .append(word.substring(1))
                .append(" ");
        }

        return result.toString().trim();
    }
}
