package com.alessandro.astages.api.util;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

@NotNullParamsAndMethodsReturn
public class AClientRestrictionUtils {
//    public static  <W, R extends AClientRestriction<R, ?, ?>> @Nullable R getRestrictionFromCache(AClientHolder holder, OrderedMultiMap<W, R> cache, W value) {
//        if (holder.isServerActive()) {
//            var serverRestriction = getRestrictionFromCache(holder, AStageType.SERVER, cache, value);
//            if (serverRestriction == null) { return null; }
//        }
//
//        if (holder.isPlayerActive()) {
//            return getRestrictionFromCache(holder, AStageType.PLAYER, cache, value);
//        }
//
//        return null;
//    }
//
//    public static  <W, R extends AClientRestriction<R, ?, ?>> @Nullable R getRestrictionFromCache(AClientHolder holder, AStageType type, OrderedMultiMap<W, R> cache, W value) {
//        var restrictions = cache.get(value);
//
//        if (!restrictions.isEmpty()) {
//            for (var restriction : restrictions) {
//                if (!AStagesClientUtils.hasStage(holder, type, restriction.getStage())) {
//                    return restriction;
//                }
//            }
//        }
//
//        return null;
//    }
//
//    public static  <W, R extends AClientRestriction<R, ?, ?>> @Nullable R getRestrictionFromCache(AClientHolder holder, IndexedOrderedMultiMap<W, R> cache, W value) {
//        if (holder.isServerActive()) {
//            var serverRestriction = getRestrictionFromCache(holder, AStageType.SERVER, cache, value);
//            if (serverRestriction == null) { return null; }
//        }
//
//        if (holder.isPlayerActive()) {
//            return getRestrictionFromCache(holder, AStageType.PLAYER, cache, value);
//        }
//
//        return null;
//    }
//
//    public static  <W, R extends AClientRestriction<R, ?, ?>> @Nullable R getRestrictionFromCache(AClientHolder holder, AStageType type, IndexedOrderedMultiMap<W, R> cache, W value) {
//        var restrictions = cache.get(value);
//
//        if (!restrictions.isEmpty()) {
//            for (var restriction : restrictions) {
//                if (!AStagesClientUtils.hasStage(holder, type, restriction.getStage())) {
//                    return restriction;
//                }
//            }
//        }
//
//        return null;
//    }
}
