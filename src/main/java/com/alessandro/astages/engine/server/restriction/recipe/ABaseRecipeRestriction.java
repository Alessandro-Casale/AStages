package com.alessandro.astages.engine.server.restriction.recipe;

import com.alessandro.astages.api.feature.AMarkable;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.reload.ClientReloadPhase;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.store.APriority;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestReloadS2C;

@NotNullParamsAndMethodsReturn
public class ABaseRecipeRestriction<R extends ARestriction<R, U, V>, U, V> extends ARestriction<R, U, V> implements AMarkable {
    public ABaseRecipeRestriction(String id, String stage, APriority defaultPriority) {
        super(id, stage, defaultPriority);
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, ABaseRecipeRestriction.class)
            .build();
    }

    @Override
    public @Nullable R restrict(U object) {
        return null;
    }

    @Override
    public ARestrictionResult isRestricted(V object) {
        return ARestrictionResult.IGNORED;
    }

    @Override
    public void markAsDirty() {
        Networking.sendToAllPlayers(new RequestReloadS2C(ClientReloadPhase.RECIPE_RESTRICTION_MARKED_AS_DIRTY));
    }
}
