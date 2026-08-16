package com.alessandro.astages.api.viewer;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.engine.client.restriction.item.AClientBaseItemRestriction;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public abstract class EntryViewerWrapper<T> {
    public abstract @Unmodifiable Collection<T> getAllEntries();

    public abstract void showEntries(Collection<T> entries);
    public abstract void hideEntries(Collection<T> entries);

    public abstract Set<String> evaluateStages(T entry);
    public abstract @Nullable AClientBaseItemRestriction<?, ?> evaluateRestriction(AClientHolder holder, T entry);

    public boolean isRuntimeAvailable() {
        return true;
    }

    public static boolean checkRuntime(boolean isRuntimeUnavailable, Class<?> clazz) {
        if (isRuntimeUnavailable) {
            AStages.LOGGER.error("[{}] Instance is reloading!", clazz.getSimpleName());
            return false;
        }

        return true;
    }
}
