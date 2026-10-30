package com.alessandro.astages.engine.client.evaluator;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.hash.CustomItemStackKey;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.api.util.AEvaluatorUtils;
import com.alessandro.astages.engine.client.registry.AClientItemRegistry;
import com.alessandro.astages.engine.client.restriction.item.AClientBaseItemRestriction;
import com.alessandro.astages.engine.client.restriction.item.AClientItemPropertyRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.item.RequestItemPropertyC2S;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

@NotNullParams
public record AClientItemEvaluator(AClientItemRegistry registry) {
    public @Nullable AClientBaseItemRestriction<?, ?> evaluate(AStageContext context, ItemStack stack) {
        return AEvaluatorUtils.evaluateRestriction(registry.getRestrictions(), AEvaluatorUtils.calculateEvaluationMode(registry), context, stack);
    }

    @UnderDevelopment
    @Info("Create strong association between requested restriction and properties")
    public @Nullable AClientItemPropertyRestriction evaluateProperties(AStageContext context, ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        var key = CustomItemStackKey.build(stack);
        if (registry.isPropertyPresent(key)) {
            var restriction = registry.getProperty(key);
            if (restriction != null) {
                return context.isUnlocked(restriction.getStage()) ? null : restriction;
            } else {
                return null;
            }
        }

        var id = evaluateId(stack);
        if (id != null) {
//            AStages.LOGGER.debug("Requested for stack: {}, id: {}", stack, id);
            Networking.sendToServer(new RequestItemPropertyC2S(id, registry.getById(id).getStage(), stack));
        } else {
            registry.setNullProperty(key);
        }

        return null;
    }

    public @Nullable AClientBaseItemRestriction<?, ?> evaluate(AStageContext context, ResourceLocation resourceLocation) {
        return AEvaluatorUtils.evaluateRestrictionUsing(registry.getModRestrictions(), AEvaluatorUtils.calculateEvaluationMode(registry), context, r -> r.getModIds().contains(resourceLocation.getNamespace()) ? ARestrictionResult.RESTRICTED : ARestrictionResult.NOT_RESTRICTED);
    }

    private @Nullable String evaluateId(ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        for (var restriction : registry) {
            if (restriction.isRestricted(stack) == ARestrictionResult.RESTRICTED) {
                return restriction.getId();
            }
        }

        return null;
    }

    public @Nullable Set<String> evaluateStages(ItemStack stack) {
        Set<String> toReturn = new HashSet<>();

        registry.forEach(restriction -> {
            if (restriction.isRestricted(stack) == ARestrictionResult.RESTRICTED && restriction.isEnabled(Attributes.HIDING_RECIPE_VIEWER)) { toReturn.add(restriction.getStage()); }
        });

        return toReturn;
    }

    public @Nullable Set<String> evaluateStages(ResourceLocation resourceLocation) {
        Set<String> toReturn = new HashSet<>();

        registry.getModRestrictions().forEach(restriction -> {
            if (restriction.getModIds().contains(resourceLocation.getNamespace()) && restriction.isEnabled(Attributes.HIDING_RECIPE_VIEWER)) {
                toReturn.add(restriction.getStage());
            }
        });

        return toReturn;
    }
}