package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.engine.server.restriction.AEffectRestriction;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.api.manager.AManager;
import net.minecraft.world.effect.MobEffect;

public class AEffectManager extends AManager<AEffectRestriction, MobEffect, MobEffect> {
    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.EFFECT;
    }
}