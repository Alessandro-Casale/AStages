package com.alessandro.astages.api.misc;

public record Twin<A, B>(A a, B b) {
    public Twin() {
        this(null, null);
    }

    public boolean isValid() {
        return a != null && b != null;
    }
}
