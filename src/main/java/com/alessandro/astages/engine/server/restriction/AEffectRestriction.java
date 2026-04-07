package com.alessandro.astages.engine.server.restriction;

import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.effect.MobEffect;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class AEffectRestriction extends ARestriction<AEffectRestriction, MobEffect, MobEffect> {
    private final List<MobEffect> effects = new ArrayList<>();

    public AEffectRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, AEffectRestriction.class)
            .build();
    }

    @Override
    public AEffectRestriction restrict(MobEffect effect) {
        effects.add(effect);

        return this;
    }

    @Override
    public boolean isRestricted(MobEffect effect) {
        return effects.contains(effect);
    }
}
