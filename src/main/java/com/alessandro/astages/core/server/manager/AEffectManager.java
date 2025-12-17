package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.AEffectRestriction;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.server.AManager;
import net.minecraft.world.effect.MobEffect;

public class AEffectManager extends AManager<AEffectRestriction, MobEffect, MobEffect> {
    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.EFFECT;
    }
}
