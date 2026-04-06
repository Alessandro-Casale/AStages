package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.holder.AClientHolder;

import java.util.Collection;

public interface AClientRestrictionCache<R, T> {
    void index(R restriction);
    void add(T target, R restriction);
    void remove(R restriction);
    Collection<R> get(T target);
    R find(AClientHolder holder, T target);
    void clear();
}