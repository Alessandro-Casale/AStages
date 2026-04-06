package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.engine.AStageManager;
import com.alessandro.astages.api.store.container.AttributeStore;

@NotNullMethodsReturn
public class ClientStage extends ClientBaseStage<ClientStage> {
    public ClientStage(String stage) {
        super(stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(AStageManager.ATTACHED_ATTRIBUTES, ClientStage.class)
            .build();
    }
}