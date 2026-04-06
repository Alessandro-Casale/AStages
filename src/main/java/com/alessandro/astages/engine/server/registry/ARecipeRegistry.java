package com.alessandro.astages.engine.server.registry;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.engine.server.restriction.recipe.ABaseRecipeRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.api.manager.registry.ARegistry;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class ARecipeRegistry extends ARegistry<ABaseRecipeRestriction<?, ?, ?>> {
    private final Set<ARecipeRestriction> recipes = new HashSet<>();
    private final Set<ARecipeModRestriction> mods = new HashSet<>();

    public Set<ARecipeRestriction> getRecipeRestrictions() {
        return recipes;
    }
    public Set<ARecipeModRestriction> getModRestrictions() {
        return mods;
    }

    public boolean register(ARecipeRestriction restriction) {
        if (commonRegister(restriction)) {
            recipes.add(restriction);
            return true;
        }

        return false;
    }

    public void register(ARecipeModRestriction restriction) {
        if (commonRegister(restriction)) {
            mods.add(restriction);
        }
    }

    public boolean commonRegister(ABaseRecipeRestriction<?, ?, ?> restriction) {
        return super.register(restriction, true);
    }

    @Override
    public @Nullable ABaseRecipeRestriction<?, ?, ?> remove(String id) {
        var restriction = super.remove(id);

        if (restriction != null) {
            if (restriction instanceof ARecipeRestriction) { recipes.remove(restriction); }
            else if (restriction instanceof ARecipeModRestriction) { mods.remove(restriction); }
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