package com.alessandro.astages.api.util;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.constant.AEvaluationMode;
import com.alessandro.astages.api.manager.registry.ACommonRegistry;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.ACommonRestriction;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@NotNullParamsAndMethodsReturn
public class AEvaluatorUtils {
    public static <R extends ACommonRestriction<V>, V> @Nullable R evaluateRestriction(Collection<R> restrictions, AEvaluationMode mode, AStageContext context, V object) {
        return evaluateRestrictionUsing(restrictions, mode, context, r -> r.isRestricted(object));
    }

    public static <R extends ACommonRestriction<?>> @Nullable R evaluateRestrictionUsing(Collection<R> restrictions, AEvaluationMode mode, AStageContext context, Function<R, ARestrictionResult> isRestrictedMethod) {
        return switch (mode) {
            case LEGACY -> getLegacyRestrictionWithCondition(restrictions, context, r -> isRestrictedMethod.apply(r) == ARestrictionResult.RESTRICTED);
            case PRIORITIZED -> getPrioritizedRestrictionWithCondition(restrictions, context, r -> isRestrictedMethod.apply(r) == ARestrictionResult.RESTRICTED);
        };
    }

    public static  <R extends ACommonRestriction<?> & Comparable<R>, W> @Nullable R evaluateRestrictionFromCache(IndexedOrderedMultiMap<W, R> cache, AEvaluationMode mode, AStageContext context, W value) {
        var restrictions = cache.get(value);

        return switch (mode) {
            case LEGACY -> getLegacyRestrictionWithCondition(restrictions, context, r -> true);
            case PRIORITIZED -> getPrioritizedRestrictionWithCondition(restrictions, context, r -> true);
        };
    }

    @SafeVarargs
    public static <R extends ACommonRestriction<?>> @Nullable R evaluateRestrictionFromMultipleSources(AEvaluationMode mode, AStageContext context, Function<R, ARestrictionResult> isRestrictedMethod, Supplier<? extends Collection<? extends R>>... sources) {
        return switch (mode) {
            case LEGACY -> {
                for (var source : sources) {
                    var result = getLegacyRestrictionWithCondition(source.get(), context, r -> isRestrictedMethod.apply(r) == ARestrictionResult.RESTRICTED);

                    if (result != null) { yield result; }
                }

                yield null;
            }
            case PRIORITIZED -> {
                var merged = new HashSet<R>();
                for (var source : sources) { merged.addAll(source.get()); }

                yield getPrioritizedRestrictionWithCondition(merged, context, r -> isRestrictedMethod.apply(r) == ARestrictionResult.RESTRICTED);
            }
        };
    }

    public static <R extends ACommonRestriction<?>> @Nullable R getPrioritizedRestrictionWithCondition(Collection<R> restrictions, AStageContext context, Predicate<R> condition) {
        R latestResult = null;

        for (var restriction : restrictions) {
            if (context.isUnlocked(restriction.getStage())) {
                continue;
            }

            if (latestResult != null) {
                if (restriction.getPriority() < latestResult.getPriority() && condition.test(restriction)) {
                    latestResult = restriction;
                }
            } else {
                if (condition.test(restriction)) {
                    latestResult = restriction;
                }
            }
        }

        return latestResult;
    }

    public static <R extends ACommonRestriction<?>> @Nullable R getLegacyRestrictionWithCondition(Collection<R> restrictions, AStageContext context, Predicate<R> condition) {
        R latestResult = null;

        for (var type : context.types()) {
            latestResult = null;

            for (var restriction : restrictions) {
                if (!type.isUnlocked(restriction.getStage()) && condition.test(restriction)) {
                    latestResult = restriction;
                    break;
                }
            }

            if (latestResult == null) { return null; }
        }

        return latestResult;
    }

    public static <R extends ACommonRestriction<V>, V> @UnmodifiableView Set<R> evaluateRestrictions(Collection<R> restrictions, V object) {
        return evaluateRestrictionsUsing(restrictions, r -> r.isRestricted(object));
    }

    public static <R extends ACommonRestriction<?>> @UnmodifiableView Set<R> evaluateRestrictionsUsing(Collection<R> restrictions, Function<R, ARestrictionResult> isRestrictedMethod) {
        var toReturn = new HashSet<R>();

        for (var restriction : restrictions) {
            if (isRestrictedMethod.apply(restriction) == ARestrictionResult.RESTRICTED) {
                toReturn.add(restriction);
            }
        }

        return Collections.unmodifiableSet(toReturn);
    }

    public static <R extends ACommonRestriction<?>> @UnmodifiableView Set<R> getRestrictionsForStages(Collection<R> restrictions, Set<String> stages) {
        var result = new HashSet<R>();

        for (var restriction : restrictions) {
            if (stages.contains(restriction.getStage())) {
                result.add(restriction);
            }
        }

        return Collections.unmodifiableSet(result);
    }

    public static <R extends ACommonRestriction<?>> boolean hasAtLeastOneRestrictionForStages(Collection<R> restrictions, Set<String> stages) {
        for (var restriction : restrictions) {
            if (stages.contains(restriction.getStage())) {
                return true;
            }
        }

        return false;
    }

    @SafeVarargs
    public static <R> @Nullable R firstNonNull(Supplier<? extends R>... sources) {
        for (var source : sources) {
            var result = source.get();

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    public static AEvaluationMode calculateEvaluationMode(ACommonRegistry<?> registry) {
        return calculateEvaluationMode(registry.getForcedEvaluationMode());
    }

    public static AEvaluationMode calculateEvaluationMode(@Nullable AEvaluationMode forcedMode) {
        return forcedMode != null ? forcedMode : AStagesCommon.EVALUATION_MODE.get();
    }
}
