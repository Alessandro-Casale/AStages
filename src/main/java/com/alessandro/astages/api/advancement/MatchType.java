package com.alessandro.astages.api.advancement;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

@NotNullMethodsReturn
public enum MatchType implements StringRepresentable {
    LITERAL, WILDCARD, REGEX;

    public static final Codec<MatchType> CODEC = StringRepresentable.fromEnum(MatchType::values);

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}