package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class AStructureRestriction implements ARestriction {
    public final String id;

    public List<ResourceLocation> structures = new ArrayList<>();

    public AStructureRestriction(String id) {
        this.id = id;
    }

    public AStructureRestriction restrict(ResourceLocation dimension) {
        structures.add(dimension);

        return this;
    }

    public boolean isRestricted(ResourceLocation structure) {
        for (ResourceLocation str : structures) {
            if (str.equals(structure)) {
                return true;
            }
        }

        return false;
    }
}
