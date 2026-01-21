package com.alessandro.astages.simple;

import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@NotNullParamsAndMethodsReturn
public enum ASimpleRestrictionTypeDeprecated {
    ITEM,
    MOD,
    DIMENSION,
    GUI,
    ORE,
    STRUCTURE,
    BIOME,
    TAME,
    MOUNT,
    RECIPE,
    ARMOR;

    public String getId() {
        return toString().toLowerCase(Locale.ROOT);
    }

    public static List<String> types() {
        var toReturn = new ArrayList<String>();

        for (var type : values()) {
            toReturn.add(type.name().toLowerCase(Locale.ROOT));
        }

        return toReturn;
    }

    public static ASimpleRestrictionTypeDeprecated getType(String name) {
        return ASimpleRestrictionTypeDeprecated.valueOf(name.toUpperCase(Locale.ROOT));
    }

    public ARestrictionType convert() {
        return switch (this) {
            case ITEM, MOD, ARMOR -> ARestrictionTypes.ITEM;
            case DIMENSION -> ARestrictionTypes.DIMENSION;
            case GUI -> ARestrictionTypes.SCREEN;
            case ORE -> ARestrictionTypes.ORE;
            case STRUCTURE -> ARestrictionTypes.STRUCTURE;
            case BIOME -> throw new IllegalArgumentException("Biome not yet implemented!");
            case TAME, MOUNT -> ARestrictionTypes.PET;
            case RECIPE -> ARestrictionTypes.RECIPE;
        };
    }
}
