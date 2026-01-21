package com.alessandro.astages.store;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import net.neoforged.neoforge.registries.DeferredRegister;

@NotNullMethodsReturn
public class ASimpleRestrictionType {
    private static DeferredRegister<ASimpleRestrictionType> deferredRegister;

    private final String type;

    private ASimpleRestrictionType(String type) {
        this.type = type;
    }

    public static DeferredRegister<ASimpleRestrictionType> setCurrentDeferredRegister(DeferredRegister<ASimpleRestrictionType> deferredRegister) {
        ASimpleRestrictionType.deferredRegister = deferredRegister;
        return deferredRegister;
    }

    public static ASimpleRestrictionType create(String type) {
        var toReturn = new ASimpleRestrictionType(type);
        deferredRegister.register(type, () -> toReturn);
        return toReturn;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}