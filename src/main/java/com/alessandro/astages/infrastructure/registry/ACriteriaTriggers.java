package com.alessandro.astages.infrastructure.registry;

import com.alessandro.astages.AStages;
import com.alessandro.astages.infrastructure.advancement.StageEarnTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACriteriaTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> CRITERIA_TRIGGERS = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, AStages.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, StageEarnTrigger> STAGE_EARN = CRITERIA_TRIGGERS.register("stage_earn", StageEarnTrigger::new);
}
