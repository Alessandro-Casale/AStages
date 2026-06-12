package com.alessandro.astages.api.exception;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

@NotNullParamsAndMethodsReturn
public class SimpleRestrictionsException extends RuntimeException {
    private SimpleRestrictionsException(String message) {
        super(message);
    }

    public static SimpleRestrictionsException onWrite() {
        return new SimpleRestrictionsException("Trying to write id restriction type not previously registered!");
    }

    public static SimpleRestrictionsException onRegisterConversionMethod() {
        return new SimpleRestrictionsException("Trying to associate id restriction without an associated simple restriction!");
    }

    public static SimpleRestrictionsException onRegisterElaborationMethod() {
        return new SimpleRestrictionsException("Trying to register an elaboration method without an associated simple restriction!");
    }

    public static SimpleRestrictionsException onRegisterAfterRemoveMethod() {
        return new SimpleRestrictionsException("Trying to register id after-remove method without an associated simple restriction!");
    }

    public static SimpleRestrictionsException onCommandAddedMethod() {
        return new SimpleRestrictionsException("Trying to register id command without an associated simple restriction!");
    }
}