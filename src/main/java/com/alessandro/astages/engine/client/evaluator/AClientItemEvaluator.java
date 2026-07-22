package com.alessandro.astages.engine.client.evaluator;

import com.alessandro.astages.api.hash.CustomItemStackKey;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.engine.client.registry.AClientItemRegistry;
import com.alessandro.astages.engine.client.restriction.item.AClientBaseItemRestriction;
import com.alessandro.astages.engine.client.restriction.item.AClientItemPropertyRestriction;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.item.RequestItemPropertyC2S;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

@NotNullParams
public record AClientItemEvaluator(AClientItemRegistry registry) {
    public @Nullable AClientBaseItemRestriction<?, ?> evaluate(AClientHolder holder, ItemStack stack) {
        if (holder.isServerActive()) {
            var serverRestriction = registry.stream().filter(r ->
                !AStagesClientUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.isRestricted(stack)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return registry.stream().filter(r ->
                !AStagesClientUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.isRestricted(stack)
            ).findFirst().orElse(null);
        }

        return null;
    }

    @UnderDevelopment
    @Info("Create strong association between requested restriction and properties")
    public @Nullable AClientItemPropertyRestriction evaluateProperties(AClientHolder holder, ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        var key = CustomItemStackKey.build(stack);
        if (registry.isPropertyPresent(key)) {
            var restriction = registry.getProperty(key);
            if (restriction != null) {
                return AStagesClientUtils.hasStage(holder, AStageType.SERVER, restriction.stage()) ||
                    AStagesClientUtils.hasStage(holder, AStageType.PLAYER, restriction.stage()) ? null : restriction;
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

    public @Nullable AClientBaseItemRestriction<?, ?> evaluate(AClientHolder holder, ResourceLocation resourceLocation) {
        if (holder.isServerActive()) {
            var serverRestriction = registry.getModRestrictions().stream().filter(r ->
                !AStagesClientUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.getModIds().contains(resourceLocation.getNamespace())
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return registry.getModRestrictions().stream().filter(r ->
                !AStagesClientUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.getModIds().contains(resourceLocation.getNamespace())
            ).findFirst().orElse(null);
        }

        return null;
    }

    private @Nullable String evaluateId(ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        for (var restriction : registry) {
            if (restriction.isRestricted(stack)) {
                return restriction.getId();
            }
        }

        return null;
    }

    public @Nullable Set<String> evaluateStages(ItemStack stack) {
        Set<String> toReturn = new HashSet<>();

        registry.forEach(restriction -> {
            if (restriction.isRestricted(stack) && restriction.isEnabled(Attributes.HIDING_JEI)) { toReturn.add(restriction.getStage()); }
        });

        return toReturn;
    }

    public @Nullable Set<String> evaluateStages(ResourceLocation resourceLocation) {
        Set<String> toReturn = new HashSet<>();

        registry.getModRestrictions().forEach(restriction -> {
            if (restriction.getModIds().contains(resourceLocation.getNamespace()) && restriction.isEnabled(Attributes.HIDING_JEI)) {
                toReturn.add(restriction.getStage());
            }
        });

        return toReturn;
    }
}