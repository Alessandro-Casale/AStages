package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ADimensionRestriction implements ARestriction {
    public final String id;

    public boolean bidirectional = false; // NOT YET IMPLEMENTED!

    public List<ResourceLocation> dimensions = new ArrayList<>();

    public ADimensionRestriction(String id) {
        this.id = id;
    }

    public ADimensionRestriction restrict(ResourceLocation dimension) {
        dimensions.add(dimension);

        return this;
    }



    public boolean isRestricted(ResourceLocation dimension) {
        for (ResourceLocation dim : dimensions) {
            if (dim.equals(dimension)) {
                return true;
            }
        }

        return false;
    }

    public boolean isBidirectional() {
        return bidirectional;
    }

    public ADimensionRestriction setBidirectional(boolean bidirectional) {
        this.bidirectional = bidirectional;

        return this;
    }
}
