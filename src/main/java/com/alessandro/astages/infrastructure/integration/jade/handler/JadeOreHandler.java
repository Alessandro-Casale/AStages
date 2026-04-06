package com.alessandro.astages.infrastructure.integration.jade.handler;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;

@NotNullParams
public class JadeOreHandler {
    public static void registerClient(IWailaClientRegistration registration) {
        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                var original = blockAccessor.getBlockState();
                var restriction = AClientRestrictionManager.ORE_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), original);

                if (restriction != null) {
                    return registration.blockAccessor()
                        .from(blockAccessor)
                        .blockState(restriction.getReplacement())
                        .build();
                }
            }

            return accessor;
        });
    }
}