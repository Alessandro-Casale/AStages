package com.alessandro.astages.core.server.restriction.recipe;

import com.alessandro.astages.api.feature.AMarkable;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.util.ReloadType;
import org.jetbrains.annotations.NotNull;

public class ABaseRecipeRestriction<R extends ARestriction<R, U, V>, U, V> extends ARestriction<R, U, V> implements AMarkable {
    public ABaseRecipeRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static @NotNull ABaseRecipeRestriction<?, ?, ?> newBuilder() {
        return new ABaseRecipeRestriction<>("null", "null");
    }

    @Override
    public boolean isCorrectClassForConfigs(@NotNull ARestriction<?, ?, ?> config) {
        return config instanceof ABaseRecipeRestriction<?, ?, ?>;
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, ABaseRecipeRestriction.class)
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

    @Override
    public void markAsDirty() {
        ANetworking.sendToAllPlayers(new RequestReloadS2CPacket(ReloadType.JEI_RECIPE));
    }
}
