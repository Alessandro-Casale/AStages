package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.ATextUtils;
import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.event.GrantedEvent;
import com.alessandro.astages.api.stage.implementation.AGrantable;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.store.AStore;
import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.StageAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

@NotNullParams
@SuppressWarnings("unchecked")
public abstract class BaseStage<S extends BaseStage<S>> implements AStore<S>, AGrantable {
    private final String stage;
    private final String description;

    private final AttributeStore attributes;

    public BaseStage(String stage) {
        this(stage, ATextUtils.capitalizeWords(stage.replace('_', ' ')));
    }

    public BaseStage(String stage, String description) {
        this.stage = stage;
        this.description = description;
        this.attributes = allowedAttributes();
    }

    public String getStage() {
        return stage;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public <T> T get(Attribute<T> attribute) {
        checkAttribute(attribute);

        return attributes.getAttribute(attribute);
    }

    public <T> Component getMessageOrNull(Attribute<Function<T, Component>> attribute, T value) {
        if (isValueNull(attribute)) { return null; }
        var message = attributes.getAttribute(attribute);

        return message.apply(value);
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
            .addAttribute(StageAttributes.PLAYER_ONLY)
            .addAttribute(StageAttributes.SERVER_ONLY)

            .addAttribute(StageAttributes.TITLE_ADD)
            .addAttribute(StageAttributes.TITLE_REMOVE, true)
            .addAttribute(StageAttributes.SUBTITLE_ADD, true)
            .addAttribute(StageAttributes.SUBTITLE_REMOVE, true)
            .addAttribute(StageAttributes.CHAT_MESSAGE_ADD, true)
            .addAttribute(StageAttributes.CHAT_MESSAGE_REMOVE, true)

            .addAttribute(StageAttributes.FADE_IN)
            .addAttribute(StageAttributes.FADE_OUT)
            .addAttribute(StageAttributes.STAY)

            .addAttribute(StageAttributes.ICON, true)

            .addAttribute(StageAttributes.GRANTED_EVENT, true);

        return AttributeStore.compose()
            .withSelf(defaultAttributes)
            .withPlugin(AStageManager.ATTACHED_ATTRIBUTES, BaseStage.class)
            .build();
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof BaseStage<?> other)) return false;

        return this.stage.equals(other.stage);
    }

    @Override
    public int hashCode() {
        return stage.hashCode();
    }

    public S setServerOnly(boolean serverOnly) {
        set(StageAttributes.SERVER_ONLY, serverOnly);
        return (S) this;
    }

    public S setPlayerOnly(boolean playerOnly) {
        set(StageAttributes.PLAYER_ONLY, playerOnly);
        return (S) this;
    }

    public S whenGranted(Consumer<GrantedEvent> consumer) {
        set(StageAttributes.GRANTED_EVENT, consumer);
        return (S) this;
    }

    @Override
    public void postGrantedEvent(GrantedEvent event) {
        get(StageAttributes.GRANTED_EVENT).accept(event);
    }

    public S setAddTitle(Function<String, Component> addTitle) {
        set(StageAttributes.TITLE_ADD, addTitle);
        return (S) this;
    }

    public S setRemoveTitle(Function<String, Component> removeTitle) {
        set(StageAttributes.TITLE_REMOVE, removeTitle);
        return (S) this;
    }

    public S setAddSubTitle(Function<String, Component> addSubTitle) {
        set(StageAttributes.SUBTITLE_ADD, addSubTitle);
        return (S) this;
    }

    public S setRemoveSubTitle(Function<String, Component> removeSubTitle) {
        set(StageAttributes.SUBTITLE_REMOVE, removeSubTitle);
        return (S) this;
    }

    public S setAddChatMessage(Function<String, Component> addChatMessage) {
        set(StageAttributes.CHAT_MESSAGE_ADD, addChatMessage);
        return (S) this;
    }

    public S setRemoveChatMessage(Function<String, Component> removeChatMessage) {
        set(StageAttributes.CHAT_MESSAGE_REMOVE, removeChatMessage);
        return (S) this;
    }

    public S setFadeIn(int fadeIn) {
        set(StageAttributes.FADE_IN, fadeIn);
        return (S) this;
    }

    public S setFadeOut(int fadeOut) {
        set(StageAttributes.FADE_OUT, fadeOut);
        return (S) this;
    }

    public S setStay(int stay) {
        set(StageAttributes.STAY, stay);
        return (S) this;
    }

    public S setIcon(ItemStack stack) {
        set(StageAttributes.ICON, stack);
        return (S) this;
    }
}