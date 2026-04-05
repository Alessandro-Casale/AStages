package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.store.AttributeStore;

public class ClientTemporaryStage extends ClientBaseStage<ClientTemporaryStage> {
    public ClientTemporaryStage(String stage) {
        super(stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withPlugin(AStageManager.ATTACHED_ATTRIBUTES, ClientTemporaryStage.class)
            .build();
    }
}