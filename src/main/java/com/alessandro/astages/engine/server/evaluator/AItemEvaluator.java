package com.alessandro.astages.engine.server.evaluator;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.server.registry.AItemRegistry;
import com.alessandro.astages.engine.server.restriction.item.ABaseItemRestriction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@NotNullParams
public record AItemEvaluator(AItemRegistry registry) {
    public @Nullable ABaseItemRestriction<?, ?> evaluate(AHolder holder, ItemStack stack) {
        if (holder.isServerActive()) {
            var serverRestriction = registry.stream().filter(r ->
                !AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.isRestricted(stack)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return registry.stream().filter(r ->
                !AStagesUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.isRestricted(stack)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public @Nullable ABaseItemRestriction<?, ?> evaluateCache(Set<ABaseItemRestriction<?, ?>> cache, AHolder holder, ItemStack stack) {
        if (holder.isServerActive()) {
            var serverRestriction = cache.stream().filter(r ->
                !AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.isRestricted(stack)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return cache.stream().filter(r ->
                !AStagesUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.isRestricted(stack)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public @Nullable ABaseItemRestriction<?, ?> evaluateContainer(Map<Class<?>, List<Integer>> containersWhitelist, Set<ABaseItemRestriction<?, ?>> cache,
                                                                  AHolder holder, ItemStack stack, Slot slot) {
        if (holder.isServerActive()) {
            var serverRestriction = evaluateContainer(containersWhitelist, cache, holder, AStageType.SERVER, stack, slot);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return evaluateContainer(containersWhitelist, cache, holder, AStageType.PLAYER, stack, slot);
        }

        return null;
    }

    private @Nullable ABaseItemRestriction<?, ?> evaluateContainer(Map<Class<?>, List<Integer>> containersWhitelist, Set<ABaseItemRestriction<?, ?>> cache,
                                                                   AHolder holder, AStageType type, ItemStack stack, Slot slot) {
        var container = slot.container;
        var index = slot.index;

        var isPresent = containersWhitelist.containsKey(container.getClass());
        if (isPresent) {
            var whitelistedIndexes = containersWhitelist.get(container.getClass());
            if (whitelistedIndexes == null) {
                return cache.stream().filter(r -> r.isRestricted(stack) && !AStagesUtils.hasStage(holder, type, r.getStage())).findFirst().orElse(null);
            } else if (whitelistedIndexes.contains(index)) {
                return cache.stream().filter(r -> r.isRestricted(stack) && !AStagesUtils.hasStage(holder, type, r.getStage())).findFirst().orElse(null);
            }
        }

        return null;
    }

    public @Nullable @Unmodifiable List<ABaseItemRestriction<?, ?>> evaluateAll(ItemStack stack) {
        return registry.stream().filter(r -> r.isRestricted(stack)).toList();
    }
}