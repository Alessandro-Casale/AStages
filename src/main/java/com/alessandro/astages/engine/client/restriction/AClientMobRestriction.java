package com.alessandro.astages.engine.client.restriction;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class AClientMobRestriction extends AClientRestriction<AClientMobRestriction, EntityType<?>, EntityType<?>> {
    private final List<EntityType<?>> mobs = new ArrayList<>();

    public AClientMobRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.Mob.JADE_MOB_MESSAGE);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientMobRestriction.class)
            .build();
    }

    @Override
    public AClientMobRestriction restrict(EntityType<?> mob) {
        mobs.add(mob);
        return this;
    }

    @Override
    public boolean isRestricted(EntityType<?> mob) {
        return mobs.contains(mob);
    }

    public List<EntityType<?>> getMobs() {
        return mobs;
    }
}
