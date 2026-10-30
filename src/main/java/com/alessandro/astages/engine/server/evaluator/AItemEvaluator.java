package com.alessandro.astages.engine.server.evaluator;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.api.util.AEvaluatorUtils;
import com.alessandro.astages.engine.server.registry.AItemRegistry;
import com.alessandro.astages.engine.server.restriction.item.ABaseItemRestriction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public record AItemEvaluator(AItemRegistry registry) {
    public @Nullable ABaseItemRestriction<?, ?> evaluate(AStageContext context, ItemStack stack) {
        if (stack.isEmpty()) { return null; }
        return AEvaluatorUtils.evaluateRestriction(registry.getRestrictions(), AEvaluatorUtils.calculateEvaluationMode(registry), context, stack);
    }

    public @Nullable ABaseItemRestriction<?, ?> evaluateCache(Set<ABaseItemRestriction<?, ?>> cache, AStageContext context, ItemStack stack) {
        if (stack.isEmpty()) { return null; }
        return AEvaluatorUtils.evaluateRestriction(cache, AEvaluatorUtils.calculateEvaluationMode(registry), context, stack);
    }

    public @Nullable ABaseItemRestriction<?, ?> evaluateContainer(Map<Class<?>, List<Integer>> containersWhitelist, Set<ABaseItemRestriction<?, ?>> cache,
                                                                  AStageContext context, ItemStack stack, Slot slot) {
        if (stack.isEmpty()) { return null; }

        return AEvaluatorUtils.evaluateRestrictionUsing(cache, AEvaluatorUtils.calculateEvaluationMode(registry), context, restriction -> {
            var container = slot.container;

            if (containersWhitelist.containsKey(container.getClass())) {
                var whitelistedIndexes = containersWhitelist.get(container.getClass());

                if (whitelistedIndexes == null || whitelistedIndexes.contains(slot.index)) {
                    return restriction.isRestricted(stack);
                }
            }

            return ARestrictionResult.NOT_RESTRICTED;
        });
    }

    public @Nullable @Unmodifiable Set<ABaseItemRestriction<?, ?>> evaluateAll(ItemStack stack) {
        if (stack.isEmpty()) { return null; }
        return AEvaluatorUtils.evaluateRestrictions(registry.getRestrictions(), stack);
    }
}