package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class AMobRestriction implements ARestriction {
    public final String id;

    public boolean disableSpawner = true;
    public boolean isDimensionSet = false;
    public ResourceLocation dimension = null;
    public boolean shouldReplace = false;
    public EntityType<?> replacing = null;

    public List<EntityType<?>> mobs = new ArrayList<>();

    public AMobRestriction(String id) {
        this.id = id;
    }

    public AMobRestriction restrict(EntityType<?> mob) {
        mobs.add(mob);

        return this;
    }

    public boolean isRestricted(EntityType<?> mob) {
//        for (ResourceLocation m : mobs) {
//            if (m.equals(mob)) {
//                return true;
//            }
//        }

        return mobs.contains(mob);
    }

    public boolean isDisableSpawner() {
        return disableSpawner;
    }

    public AMobRestriction setDisableSpawner(boolean disableSpawner) {
        this.disableSpawner = disableSpawner;

        return this;
    }

    public boolean isDimensionSet() {
        return isDimensionSet;
    }

//    public AMobRestriction setDimensionSet(boolean dimensionSet) {
//        isDimensionSet = dimensionSet;
//
//        return this;
//    }

    public ResourceLocation getDimension() {
        return dimension;
    }

    public AMobRestriction setDimension(ResourceLocation dimension) {
        this.isDimensionSet = true;
        this.dimension = dimension;

        return this;
    }

    public boolean isShouldReplace() {
        return shouldReplace;
    }

//    public AMobRestriction setShouldReplace(boolean shouldReplace) {
//        this.shouldReplace = shouldReplace;
//
//        return this;
//    }

    public EntityType<?> getReplacing() {
        return replacing;
    }

    public AMobRestriction setReplacing(EntityType<?> replacing) {
        this.shouldReplace = true;
        this.replacing = replacing;

        return this;
    }
}
