package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.event.GrantedEvent;
import com.alessandro.astages.api.stage.implementation.AGrantable;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.store.config.AConfigurablePreset;
import com.alessandro.astages.api.store.container.AStore;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.api.util.ATextUtils;
import com.alessandro.astages.engine.AStageManager;
import com.alessandro.astages.engine.server.MiscStorage;
import com.alessandro.astages.engine.store.StageAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

@NotNullParams
@SuppressWarnings("unchecked")
public abstract class BaseStage<S extends BaseStage<S>> implements AStore<S>, AConfigurablePreset<S>, AGrantable {
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

        if (attribute == StageAttributes.PLAYER_ONLY) {
            MiscStorage.STAGES_ONLY_FOR_PLAYER.add(getStage());
            MiscStorage.STAGES_ONLY_FOR_SERVER.remove(getStage());
        } else if (attribute == StageAttributes.SERVER_ONLY) {
            MiscStorage.STAGES_ONLY_FOR_PLAYER.remove(getStage());
            MiscStorage.STAGES_ONLY_FOR_SERVER.add(getStage());
        }

        return (S) this;
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(StageAttributes.PLAYER_ONLY)
            .addAttribute(StageAttributes.SERVER_ONLY)

            .addAttribute(StageAttributes.SHOW_DEFAULT)
            .addAttribute(StageAttributes.TITLE_ADD, true)
            .addAttribute(StageAttributes.TITLE_REMOVE, true)
            .addAttribute(StageAttributes.SUBTITLE_ADD, true)
            .addAttribute(StageAttributes.SUBTITLE_REMOVE, true)
            .addAttribute(StageAttributes.CHAT_MESSAGE_ADD, true)
            .addAttribute(StageAttributes.CHAT_MESSAGE_REMOVE, true)
            .addAttribute(StageAttributes.ACTION_BAR_MESSAGE_ADD, true)
            .addAttribute(StageAttributes.ACTION_BAR_MESSAGE_REMOVE, true)

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

    public S serverOnly() {
        return set(StageAttributes.SERVER_ONLY, true);
    }

    public S playerOnly() {
        return set(StageAttributes.PLAYER_ONLY, true);
    }

    public S titleOnAdd(Function<String, Component> title) {
        set(StageAttributes.SHOW_DEFAULT, false);
        return set(StageAttributes.TITLE_ADD, title);
    }

    public S subTitleOnAdd(Function<String, Component> subTitle) {
        set(StageAttributes.SHOW_DEFAULT, false);
        return set(StageAttributes.SUBTITLE_ADD, subTitle);
    }

    public S chatMessageOnAdd(Function<String, Component> chatMessage) {
        set(StageAttributes.SHOW_DEFAULT, false);
        return set(StageAttributes.CHAT_MESSAGE_ADD, chatMessage);
    }

    public S actionBarMessageOnAdd(Function<String, Component> actionBarMessage) {
        set(StageAttributes.SHOW_DEFAULT, false);
        return set(StageAttributes.ACTION_BAR_MESSAGE_ADD, actionBarMessage);
    }

    public S titleOnRemove(Function<String, Component> title) {
        set(StageAttributes.SHOW_DEFAULT, false);
        return set(StageAttributes.TITLE_REMOVE, title);
    }

    public S subTitleOnRemove(Function<String, Component> subTitle) {
        set(StageAttributes.SHOW_DEFAULT, false);
        return set(StageAttributes.SUBTITLE_REMOVE, subTitle);
    }

    public S chatMessageOnRemove(Function<String, Component> chatMessage) {
        set(StageAttributes.SHOW_DEFAULT, false);
        return set(StageAttributes.CHAT_MESSAGE_REMOVE, chatMessage);
    }

    public S actionBarMessageOnRemove(Function<String, Component> actionBarMessage) {
        set(StageAttributes.SHOW_DEFAULT, false);
        return set(StageAttributes.ACTION_BAR_MESSAGE_REMOVE, actionBarMessage);
    }

    public S fadeIn(int fadeIn) {
        return set(StageAttributes.FADE_IN, fadeIn);
    }

    public S fadeOut(int fadeOut) {
        return set(StageAttributes.FADE_OUT, fadeOut);
    }

    public S stay(int stay) {
        return set(StageAttributes.STAY, stay);
    }

    public S icon(ItemStack stack) {
        return set(StageAttributes.ICON, stack);
    }

    public S whenGranted(Consumer<GrantedEvent> consumer) {
        set(StageAttributes.GRANTED_EVENT, consumer);
        return (S) this;
    }

    @Override
    public void postGrantedEvent(GrantedEvent event) {
        get(StageAttributes.GRANTED_EVENT).accept(event);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setServerOnly(boolean serverOnly) {
        set(StageAttributes.SERVER_ONLY, serverOnly);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setPlayerOnly(boolean playerOnly) {
        set(StageAttributes.PLAYER_ONLY, playerOnly);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setAddTitle(Function<String, Component> addTitle) {
        set(StageAttributes.TITLE_ADD, addTitle);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setRemoveTitle(Function<String, Component> removeTitle) {
        set(StageAttributes.TITLE_REMOVE, removeTitle);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setAddSubTitle(Function<String, Component> addSubTitle) {
        set(StageAttributes.SUBTITLE_ADD, addSubTitle);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setRemoveSubTitle(Function<String, Component> removeSubTitle) {
        set(StageAttributes.SUBTITLE_REMOVE, removeSubTitle);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setAddChatMessage(Function<String, Component> addChatMessage) {
        set(StageAttributes.CHAT_MESSAGE_ADD, addChatMessage);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setRemoveChatMessage(Function<String, Component> removeChatMessage) {
        set(StageAttributes.CHAT_MESSAGE_REMOVE, removeChatMessage);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setAddTitle(Component addTitle) {
        set(StageAttributes.TITLE_ADD, stage -> addTitle);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setRemoveTitle(Component removeTitle) {
        set(StageAttributes.TITLE_REMOVE, stage -> removeTitle);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setAddSubTitle(Component addSubTitle) {
        set(StageAttributes.SUBTITLE_ADD, stage -> addSubTitle);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setRemoveSubTitle(Component removeSubTitle) {
        set(StageAttributes.SUBTITLE_REMOVE, stage -> removeSubTitle);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setAddChatMessage(Component addChatMessage) {
        set(StageAttributes.CHAT_MESSAGE_ADD, stage -> addChatMessage);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setRemoveChatMessage(Component removeChatMessage) {
        set(StageAttributes.CHAT_MESSAGE_REMOVE, stage -> removeChatMessage);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setFadeIn(int fadeIn) {
        set(StageAttributes.FADE_IN, fadeIn);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setFadeOut(int fadeOut) {
        set(StageAttributes.FADE_OUT, fadeOut);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setStay(int stay) {
        set(StageAttributes.STAY, stay);
        return (S) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public S setIcon(ItemStack stack) {
        set(StageAttributes.ICON, stack);
        return (S) this;
    }
}