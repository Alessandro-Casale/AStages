package com.alessandro.astages.core.client.restriction.recipe;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.client.AClientRestriction;
import org.jetbrains.annotations.NotNull;

public class AClientBaseRecipeRestriction<R extends AClientRestriction<R, U, V>, U, V> extends AClientRestriction<R, U, V> {
    public AClientBaseRecipeRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientBaseRecipeRestriction.class)
            .build();
    }

    @Override
    public R restrict(U object) {
        return null;
    }

    @Override
    public boolean isRestricted(V object) {
        return false;
    }
}
