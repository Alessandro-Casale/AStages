package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.core.AClientStageManager;
import com.alessandro.astages.store.AttributeStore;

@NotNullMethodsReturn
public class ClientTemporaryStage extends ClientBaseStage<ClientTemporaryStage> {
    public ClientTemporaryStage(String stage) {
        super(stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(AClientStageManager.ATTACHED_ATTRIBUTES, ClientTemporaryStage.class)
            .build();
    }
}