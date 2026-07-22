package com.alessandro.astages.infrastructure.integration.rei.predicate;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import dev.architectury.event.EventResult;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.visibility.DisplayVisibilityPredicate;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;

@NotNullParams
public class StageRecipeVisibilityHandler implements DisplayVisibilityPredicate {
    @Override
    public EventResult handleDisplay(DisplayCategory<?> displayCategory, Display display) {
        var rs = display.getDisplayLocation();
        if (rs.isEmpty()) { return EventResult.pass(); }

        var recipe = RecipeManagerContext.getInstance().byId(rs.get());
        if (recipe == null) { return EventResult.pass(); }

        var wrapper = new RecipeWrapper(recipe.value().getType(), rs.get());
        var restriction = AClientRestrictionManager.RECIPE_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), wrapper);

        return restriction != null ? EventResult.interruptFalse() : EventResult.pass();
    }

    @Override
    public double getPriority() {
        return 10;
    }
}
