package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.holder.AHolder;

import java.util.Collection;

public interface ARestrictionCache<R, T> {
    void index(R restriction);
    void add(T target, R restriction);
    void remove(R restriction);
    Collection<R> get(T target);
    R find(AHolder holder, T target);
    void clear();
}