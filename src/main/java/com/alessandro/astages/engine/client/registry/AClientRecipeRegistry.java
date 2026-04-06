package com.alessandro.astages.engine.client.registry;

import com.alessandro.astages.api.manager.registry.AClientRegistry;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.engine.client.restriction.recipe.AClientBaseRecipeRestriction;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeModRestriction;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeRestriction;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AClientRecipeRegistry extends AClientRegistry<AClientBaseRecipeRestriction<?, ?, ?>> {
    private final Set<AClientRecipeRestriction> recipes = new HashSet<>();
    private final Set<AClientRecipeModRestriction> mods = new HashSet<>();

    public Set<AClientRecipeRestriction> getRecipeRestrictions() {
        return recipes;
    }
    public Set<AClientRecipeModRestriction> getModRestrictions() {
        return mods;
    }

    public void register(AClientRecipeRestriction restriction) {
        super.register(restriction);
        recipes.add(restriction);
    }

    public void register(AClientRecipeModRestriction restriction) {
        super.register(restriction);
        mods.add(restriction);
    }

    @Override
    public @Nullable AClientBaseRecipeRestriction<?, ?, ?> remove(String id) {
        var restriction = super.remove(id);

        if (restriction != null) {
            if (restriction instanceof AClientRecipeRestriction) { recipes.remove(restriction); }
            else if (restriction instanceof AClientRecipeModRestriction) { mods.remove(restriction); }
        }

        return restriction;
    }

    @Override
    public void clear() {
        super.clear();
        recipes.clear();
        mods.clear();
    }
}