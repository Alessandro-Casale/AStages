package com.alessandro.astages.engine.client.restriction;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AClientStructureRestriction extends AClientRestriction<AClientStructureRestriction, ResourceKey<Structure>, ResourceKey<Structure>> {
    private final Set<ResourceKey<Structure>> structures = new HashSet<>();

    public AClientStructureRestriction(String id, String stage, int priority) {
        super(id, stage, priority);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.ENTERING);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientStructureRestriction.class)
            .build();
    }

    @Override
    public AClientStructureRestriction restrict(ResourceKey<Structure> structure) {
        structures.add(structure);

        return this;
    }

    @Override
    public ARestrictionResult isRestricted(ResourceKey<Structure> structure) {
        for (var str : structures) {
            if (str.equals(structure)) {
                return ARestrictionResult.RESTRICTED;
            }
        }

        return ARestrictionResult.NOT_RESTRICTED;
    }

    public Set<ResourceKey<Structure>> getStructures() {
        return structures;
    }
}