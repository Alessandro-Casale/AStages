package com.alessandro.astages.core.client.restriction;

import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.client.AClientRestriction;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
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
        return AttributeStore.builder()
                .addAttribute(Attributes.Mob.JADE_MOB_MESSAGE);
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
