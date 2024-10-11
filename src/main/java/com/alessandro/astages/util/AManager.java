package com.alessandro.astages.util;

import com.alessandro.astages.core.ADimensionRestriction;

public interface AManager<T extends ARestriction, U> {
    void addRestriction(String stage, T restriction);

    T getRestriction(String id);
    T getRestriction(U object);
}
