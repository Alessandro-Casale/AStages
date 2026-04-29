package com.alessandro.astages.api.advancement;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.regex.Pattern;

@NotNullParamsAndMethodsReturn
public record StageMatcher(String pattern, MatchType type, Pattern compiled) {
    public static final Codec<StageMatcher> CODEC = Codec.either(
        Codec.STRING,
        RecordCodecBuilder.<StageMatcher>create(inst -> inst.group(
            Codec.STRING.fieldOf("pattern").forGetter(StageMatcher::pattern),
            MatchType.CODEC.optionalFieldOf("type", MatchType.LITERAL).forGetter(StageMatcher::type)
        ).apply(inst, StageMatcher::new))
    ).xmap(
        either -> either.map(StageMatcher::literal, matcher -> matcher),
        matcher -> matcher.type() == MatchType.LITERAL ? Either.left(matcher.pattern()) : Either.right(matcher)
    );

    public static StageMatcher literal(String stage) {
        return new StageMatcher(stage, MatchType.LITERAL);
    }

    public StageMatcher(String pattern, MatchType type) {
        this(pattern, type, compile(pattern, type));
    }

    private static @Nullable Pattern compile(String pattern, MatchType type) {
        return switch (type) {
            case LITERAL -> null;
            case WILDCARD -> Pattern.compile("^" + Pattern.quote(pattern).replace("*", ".*") + "$");
            case REGEX -> Pattern.compile(pattern);
        };
    }

    public boolean match(String stage) {
        if (type == MatchType.LITERAL) {
            return pattern.equals(stage);
        }

        return compiled != null && compiled.matcher(stage).matches();
    }
}