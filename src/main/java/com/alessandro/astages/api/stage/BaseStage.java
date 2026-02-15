package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.ATextUtils;
import com.alessandro.astages.api.exception.SetAttributeNotSupported;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.event.GrantedEvent;
import com.alessandro.astages.api.stage.implementation.AGrantable;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.StageAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

@NotNullParams
@SuppressWarnings("unchecked")
public abstract class BaseStage<S extends BaseStage<S>> implements AGrantable {
    private final String stage;
    private final String description;

    private final AttributeStore attributes;

    public BaseStage(String stage) {
        this(stage, ATextUtils.capitalizeWords(stage.replace('_', ' ')));
    }

    public BaseStage(String stage, String description) {
        this.stage = stage;
        this.description = description;
        attributes = allowedAttributes();
    }

    public String getStage() {
        return stage;
    }

    public String getDescription() {
        return description;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isValueNull(Attribute<?> attribute) {
        return get(attribute) == null;
    }

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
    public <T> S set(Attribute<T> attribute, T value) {
        checkAttribute(attribute);
        attributes.setAttribute(attribute, value);

        return (S) this;
    }

    public boolean isDisabled(Attribute<Boolean> attribute) throws SetAttributeNotSupported {
        return !get(attribute);
    }

    public boolean isEnabled(Attribute<Boolean> attribute) throws SetAttributeNotSupported {
        return get(attribute);
    }

    public void checkAttribute(Attribute<?> attribute) throws SetAttributeNotSupported {
        if (!allowedAttributes().containsKey(attribute)) {
            throw new SetAttributeNotSupported(attribute);
        }
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

    public @NotNull AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(StageAttributes.PLAYER_ONLY)
            .addAttribute(StageAttributes.SERVER_ONLY)

            .addAttribute(StageAttributes.TITLE_ADD)
            .addAttribute(StageAttributes.TITLE_REMOVE)
            .addAttribute(StageAttributes.SUBTITLE_ADD)
            .addAttribute(StageAttributes.SUBTITLE_REMOVE)
            .addAttribute(StageAttributes.CHAT_MESSAGE_ADD)
            .addAttribute(StageAttributes.CHAT_MESSAGE_REMOVE)

            .addAttribute(StageAttributes.FADE_IN)
            .addAttribute(StageAttributes.FADE_OUT)
            .addAttribute(StageAttributes.STAY)

            .addAttribute(StageAttributes.ICON)

            .addAttribute(StageAttributes.GRANTED_EVENT);

        var pluginAttributes = AStageManager.ATTACHED_ATTRIBUTES.getOrDefault(BaseStage.class, null);

        if (pluginAttributes != null) {
            return defaultAttributes.combineWith(pluginAttributes);
        } else {
            return defaultAttributes;
        }
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