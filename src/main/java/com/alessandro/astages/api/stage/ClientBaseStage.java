package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.store.container.AStore;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientStageManager;
import com.alessandro.astages.engine.store.StageAttributes;

public abstract class ClientBaseStage<S extends ClientBaseStage<S>> implements AStore<S> {
    private final String stage;

    private final AttributeStore attributes;

    public ClientBaseStage(String stage) {
        this.stage = stage;
        this.attributes = allowedAttributes();
    }

    @Override
    public <T> T get(Attribute<T> attribute) {
        checkAttribute(attribute);

        return attributes.getAttribute(attribute);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> S set(Attribute<T> attribute, T value) {
        checkAttribute(attribute);
        attributes.setAttribute(attribute, value);

        return (S) this;
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(StageAttributes.ICON, true);

        return AttributeStore.compose()
            .withSelf(defaultAttributes)
            .withPlugin(AClientStageManager.ATTACHED_ATTRIBUTES, ClientBaseStage.class)
            .build();
    }

    public String getStage() {
        return stage;
    }
}