package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.restriction.ACommonRestriction;
import com.alessandro.astages.api.stage.AStageContext;

import java.util.Collection;

public interface ARestrictionCache<R extends ACommonRestriction<?>, T> {
    void index(R restriction);
    void add(T target, R restriction);
    void remove(R restriction);
    Collection<R> get(T target);
    R find(AStageContext context, T target);
    void clear();
}