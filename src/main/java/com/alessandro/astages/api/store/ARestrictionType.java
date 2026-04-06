package com.alessandro.astages.api.store;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import net.neoforged.neoforge.registries.DeferredRegister;

@NotNullMethodsReturn
public class ARestrictionType {
    private static DeferredRegister<ARestrictionType> deferredRegister;

    private final String type;

    private ARestrictionType(String type) {
        this.type = type;
    }

    public static DeferredRegister<ARestrictionType> setCurrentDeferredRegister(DeferredRegister<ARestrictionType> deferredRegister) {
        ARestrictionType.deferredRegister = deferredRegister;
        return deferredRegister;
    }

    public static ARestrictionType create(String type) {
        var toReturn = new ARestrictionType(type);
        deferredRegister.register(type, () -> toReturn);
        return toReturn;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ARestrictionType{" +
            "type='" + type + '\'' +
            '}';
    }
}
