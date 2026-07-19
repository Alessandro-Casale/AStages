package com.alessandro.astages;

import java.util.Collection;

public abstract class RecipeViewerWrapper<T> {
    public abstract Collection<T> getAllEntries();

    public abstract void showEntries(Collection<T> entries);
    public abstract void hideEntries(Collection<T> entries);

    public boolean isRuntimeAvailable() {
        return true;
    }
}
