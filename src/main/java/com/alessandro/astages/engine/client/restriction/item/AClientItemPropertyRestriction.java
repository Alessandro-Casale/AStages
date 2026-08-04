package com.alessandro.astages.engine.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.store.container.AStore;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class AClientItemPropertyRestriction implements AStore<AClientItemPropertyRestriction> {
    private final String id;
    private final String stage;
    private final ItemStack stack;

    private final AttributeStore attributes;

    public AClientItemPropertyRestriction(String id, String stage, ItemStack stack) {
        this.id = id;
        this.stage = stage;
        this.stack = stack;

        this.attributes = allowedAttributes();
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.Item.ACTION_BAR_MESSAGE)
            .addAttribute(Attributes.Item.TOOLTIP_MESSAGE)
            .addAttribute(Attributes.Item.RECIPE_VIEWER_MESSAGE)
            .addAttribute(Attributes.Item.JADE_ITEM_MESSAGE)
            .addAttribute(Attributes.Item.JADE_BLOCK_MESSAGE);

        return AttributeStore.compose()
            .withSelf(defaultAttributes)
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientItemPropertyRestriction.class)
            .build();
    }

    @Override
    public <T> T get(Attribute<T> attribute) {
        checkAttribute(attribute);

        return attributes.getAttribute(attribute);
    }

    public <T> Component getMessage(Attribute<Function<T, Component>> attribute, T value) {
        var message = attributes.getAttribute(attribute);

        return message.apply(value);
    }

    @Override
    public <T> AClientItemPropertyRestriction set(Attribute<T> attribute, T value) {
        checkAttribute(attribute);
        attributes.setAttribute(attribute, value);

        return this;
    }

    public String getId() {
        return id;
    }

    public String getStage() {
        return stage;
    }

    public ItemStack getStack() {
        return stack;
    }
}
