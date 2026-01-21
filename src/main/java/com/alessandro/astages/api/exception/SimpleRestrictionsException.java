package com.alessandro.astages.api.exception;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

@NotNullParamsAndMethodsReturn
public class SimpleRestrictionsException extends RuntimeException {
    private SimpleRestrictionsException(String message) {
        super(message);
    }

    public static SimpleRestrictionsException onWrite() {
        return new SimpleRestrictionsException("Trying to write a restriction type not previously registered!");
    }

    public static SimpleRestrictionsException onRegisterConversionMethod() {
        return new SimpleRestrictionsException("Trying to associate a restriction without an associated simple restriction!");
    }

    public static SimpleRestrictionsException onRegisterElaborationMethod() {
        return new SimpleRestrictionsException("Trying to register an elaboration method without an associated simple restriction!");
    }

    public static SimpleRestrictionsException onRegisterAfterRemoveMethod() {
        return new SimpleRestrictionsException("Trying to register a after-remove method without an associated simple restriction!");
    }

    public static SimpleRestrictionsException onCommandAddedMethod() {
        return new SimpleRestrictionsException("Trying to register a command without an associated simple restriction!");
    }
}