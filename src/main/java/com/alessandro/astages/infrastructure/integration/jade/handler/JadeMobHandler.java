package com.alessandro.astages.infrastructure.integration.jade.handler;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IWailaClientRegistration;

@NotNullParams
public class JadeMobHandler {
    public static void registerClient(IWailaClientRegistration registration) {
        registration.addTooltipCollectedCallback((tooltip, accessor) -> {
            if (accessor instanceof EntityAccessor entityAccessor) {
                var entity = entityAccessor.getEntity();
                var type = entity.getType();

                var restriction = AClientRestrictionManager.MOB_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), type);

                if (restriction != null) {
                    tooltip.getTooltip().clear();

                    if (!restriction.isValueNull(Attributes.Mob.JADE_MOB_MESSAGE)) {
                        tooltip.getTooltip().add(restriction.get(Attributes.Mob.JADE_MOB_MESSAGE).get());
                    }
                }
            }
        });
    }
}