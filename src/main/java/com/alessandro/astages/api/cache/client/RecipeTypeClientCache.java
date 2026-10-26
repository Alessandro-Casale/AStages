package com.alessandro.astages.api.cache.client;

import com.alessandro.astages.api.cache.AKeyedClientRestrictionCache;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.manager.registry.AClientMinimalRegistry;
import com.alessandro.astages.api.restriction.AClientRestriction;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class RecipeTypeClientCache<R extends AClientRestriction<R, ?, ?>> extends AKeyedClientRestrictionCache<R, RecipeType<?>> {
    public RecipeTypeClientCache(AClientMinimalRegistry<?> registry) {
        super(registry);
    }

    public RecipeTypeClientCache(AClientMinimalManager<?, ?> manager) {
        super(manager);
    }
}