package com.alessandro.astages.infrastructure.advancement;

import com.alessandro.astages.api.advancement.AdvancementCodecs;
import com.alessandro.astages.api.advancement.StageMatcher;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.util.AStagesUtils;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Optional;

@NotNullMethodsReturn
public class StageEarnTrigger extends SimpleCriterionTrigger<StageEarnTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return AdvancementCodecs.STAGES_CODEC;
    }

    public void trigger(ServerPlayer player) {
        trigger(player, instance -> instance.matches(player));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<StageMatcher> stage, Optional<List<StageMatcher>> stages, boolean checkServerStages) implements SimpleCriterionTrigger.SimpleInstance {
        public boolean matches(ServerPlayer player) {
           var activeStages = checkServerStages ? AStagesUtils.getStages(AHolder.server()) : AStagesUtils.getStages(AHolder.player(player));

            if (stage.isPresent() && activeStages.stream().noneMatch(s -> stage.get().match(s))) {
                return false;
            }

            if (stages.isPresent()) {
                for (StageMatcher matcher : stages.get()) {
                    if (activeStages.stream().noneMatch(matcher::match)) return false;
                }
            }

            return true;
        }
    }
}
