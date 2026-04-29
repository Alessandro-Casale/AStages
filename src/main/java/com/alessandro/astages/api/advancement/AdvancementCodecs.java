package com.alessandro.astages.api.advancement;

import com.alessandro.astages.infrastructure.advancement.StageEarnTrigger;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntityPredicate;

public class AdvancementCodecs {
    public static final Codec<StageEarnTrigger.TriggerInstance> STAGES_CODEC = RecordCodecBuilder.create(inst -> inst.group(
        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(StageEarnTrigger.TriggerInstance::player),
        StageMatcher.CODEC.optionalFieldOf("stage").forGetter(StageEarnTrigger.TriggerInstance::stage),
        StageMatcher.CODEC.listOf().optionalFieldOf("stages").forGetter(StageEarnTrigger.TriggerInstance::stages),
        Codec.BOOL.optionalFieldOf("server_scope", false).forGetter(StageEarnTrigger.TriggerInstance::checkServerStages)
    ).apply(inst, StageEarnTrigger.TriggerInstance::new));
}
