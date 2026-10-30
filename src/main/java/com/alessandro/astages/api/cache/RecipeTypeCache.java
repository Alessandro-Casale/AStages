package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.manager.ACommonManager;
import com.alessandro.astages.api.manager.registry.ACommonRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ACommonRestriction;
import net.minecraft.world.item.crafting.RecipeType;

@NotNullParams
public abstract class RecipeTypeCache<R extends ACommonRestriction<?> & Comparable<R>> extends AKeyedRestrictionCache<R, RecipeType<?>> {
    public RecipeTypeCache(ACommonRegistry<?> registry) {
        super(registry);
    }

    public RecipeTypeCache(ACommonManager<?, ?> manager) {
        super(manager);
    }
}