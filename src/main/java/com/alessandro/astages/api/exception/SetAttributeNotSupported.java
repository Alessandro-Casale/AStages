package com.alessandro.astages.api.exception;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.store.Attribute;

@NotNullParams
public class SetAttributeNotSupported extends RuntimeException {
    public SetAttributeNotSupported(Attribute<?> attribute) {
        super("Set method for restriction " + attribute.getId() + " is not supported for this object!");
    }
}
