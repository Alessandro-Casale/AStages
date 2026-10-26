package com.alessandro.astages.api.util;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.constant.AEvaluationMode;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.manager.registry.AClientMinimalRegistry;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.AClientRestriction;
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
public class AClientEvaluatorUtils {
    public static <R extends AClientRestriction<?, ?, V>, V> @Nullable R evaluateRestriction(Collection<R> restrictions, AEvaluationMode mode, AClientHolder holder, V object) {
        return evaluateRestrictionUsing(restrictions, mode, holder, r -> r.isRestricted(object));
    }

    public static <R extends AClientRestriction<?, ?, V>, V> @Nullable R evaluateRestrictionUsing(Collection<R> restrictions, AEvaluationMode mode, AClientHolder holder, Function<R, ARestrictionResult> isRestrictedMethod) {
        return switch (mode) {
            case LEGACY -> getLegacyRestrictionWithCondition(restrictions, holder, r -> isRestrictedMethod.apply(r) == ARestrictionResult.RESTRICTED);
            case PRIORITIZED -> getPrioritizedRestrictionWithCondition(restrictions, holder, r -> isRestrictedMethod.apply(r) == ARestrictionResult.RESTRICTED);
        };
    }

    public static  <R extends AClientRestriction<R, ?, ?>, W> @Nullable R evaluateRestrictionFromCache(IndexedOrderedMultiMap<W, R> cache, AEvaluationMode mode, AClientHolder holder, W value) {
        var restrictions = cache.get(value);

        return switch (mode) {
            case LEGACY -> getLegacyRestrictionWithCondition(restrictions, holder, r -> true);
            case PRIORITIZED -> getPrioritizedRestrictionWithCondition(restrictions, holder, r -> true);
        };
    }

    public static <R extends AClientRestriction<?, ?, ?>> @Nullable R getPrioritizedRestrictionWithCondition(Collection<R> restrictions, AClientHolder holder, Predicate<R> condition) {
        R latestResult = null;

        for (var restriction : restrictions) {
            if (AStagesClientUtils.hasStage(holder, restriction.getStage())) {
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

    public static <R extends AClientRestriction<?, ?, ?>> @Nullable R getLegacyRestrictionWithCondition(Collection<R> restrictions, AClientHolder holder, Predicate<R> condition) {
        if (holder.isServerActive()) {
            var serverRestriction = restrictions.stream().filter(r ->
                !AStagesClientUtils.hasStage(holder, AStageType.SERVER, r.getStage()) && condition.test(r)
            ).findFirst().orElse(null);

            if (serverRestriction == null) {
                return null;
            } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return restrictions.stream().filter(r ->
                !AStagesClientUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) && condition.test(r)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public static <R extends AClientRestriction<?, ?, V>, V> @UnmodifiableView @Nullable Set<R> evaluateRestrictions(Collection<R> restrictions, V object) {
        return evaluateRestrictionsUsing(restrictions, r -> r.isRestricted(object));
    }

    public static <R extends AClientRestriction<?, ?, V>, V> @UnmodifiableView @Nullable Set<R> evaluateRestrictionsUsing(Collection<R> restrictions, Function<R, ARestrictionResult> isRestrictedMethod) {
        var toReturn = new HashSet<R>();

        for (var restriction : restrictions) {
            if (isRestrictedMethod.apply(restriction) == ARestrictionResult.RESTRICTED) {
                toReturn.add(restriction);
            }
        }

        return Collections.unmodifiableSet(toReturn);
    }

    public static <R extends AClientRestriction<?, ?, ?>> @UnmodifiableView Set<R> getRestrictionsForStages(Collection<R> restrictions, Set<String> stages) {
        var result = new HashSet<R>();

        for (var restriction : restrictions) {
            if (stages.contains(restriction.getStage())) {
                result.add(restriction);
            }
        }

        return Collections.unmodifiableSet(result);
    }

    public static <R extends AClientRestriction<?, ?, ?>> @UnmodifiableView boolean hasAtLeastOneRestrictionForStages(Collection<R> restrictions, Set<String> stages) {
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

    public static AEvaluationMode calculateEvaluationMode(AClientMinimalRegistry<?> registry) {
        return calculateEvaluationMode(registry.getForcedEvaluationMode());
    }

    public static AEvaluationMode calculateEvaluationMode(@Nullable AEvaluationMode forcedMode) {
        return forcedMode != null ? forcedMode : AStagesCommon.EVALUATION_MODE.get();
    }
}
