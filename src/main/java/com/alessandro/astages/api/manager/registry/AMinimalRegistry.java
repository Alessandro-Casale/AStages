package com.alessandro.astages.api.manager.registry;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.ARestriction;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

@NotNullMethodsReturn
public interface AMinimalRegistry<R extends ARestriction<?, ?, ?>> extends Iterable<R> {
    Set<R> getRestrictions();

    @Nullable R getById(String id);
    @Nullable R remove(String id);

    void clear();

    Set<String> getIds();
    Set<String> getStages();

    @Override
    default Iterator<R> iterator() {
        return getRestrictions().iterator();
    }

    default Stream<R> stream() {
        return getRestrictions().stream();
    }
}