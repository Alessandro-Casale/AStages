package com.alessandro.astages.engine.client.restriction;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class AClientStructureRestriction extends AClientRestriction<AClientStructureRestriction, ResourceLocation, ResourceLocation> {
    private final Set<ResourceLocation> structures = new HashSet<>();

    public AClientStructureRestriction(String id, String stage) {
        super(id, stage);
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
    public AClientStructureRestriction restrict(ResourceLocation structure) {
        structures.add(structure);

        return this;
    }

    @Override
    public boolean isRestricted(ResourceLocation structure) {
        for (ResourceLocation str : structures) {
            if (str.equals(structure)) {
                return true;
            }
        }

        return false;
    }

    public Set<ResourceLocation> getStructures() {
        return structures;
    }
}