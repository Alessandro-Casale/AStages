package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.ATextUtils;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.event.GrantedEvent;
import com.alessandro.astages.api.stage.implementation.AGrantable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

@NotNullParams
@SuppressWarnings("unchecked")
public abstract class BaseStage<T extends BaseStage<T>> implements AGrantable {
    private final String stage;
    private final String description;

    private boolean serverOnly = false;
    private boolean playerOnly = false;

    private final StageDisplay displayConfig = new StageDisplay();

    private boolean hasCustomGrantedEvent = false;
    private Consumer<GrantedEvent> grantedEvent;

    public BaseStage(String stage) {
        this(stage, ATextUtils.capitalizeWords(stage.replace('_', ' ')));
    }

    public BaseStage(String stage, String description) {
        this.stage = stage;
        this.description = description;
    }

    public String getStage() {
        return stage;
    }

    public String getDescription() {
        return description;
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

    public boolean isServerOnly() {
        return serverOnly;
    }

    public T setServerOnly(boolean serverOnly) {
        this.serverOnly = serverOnly;
        return (T) this;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public T setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
        return (T) this;
    }

    @Override
    public boolean hasCustomGrantedEvent() {
        return hasCustomGrantedEvent;
    }

    public T whenGranted(Consumer<GrantedEvent> consumer) {
        grantedEvent = consumer;
        hasCustomGrantedEvent = true;
        return (T) this;
    }

    @Override
    public void postGrantedEvent(GrantedEvent event) {
        grantedEvent.accept(event);
    }

    public Component getAddTitle() {
        return displayConfig.addTitle;
    }

    public T setAddTitle(Component addTitle) {
        displayConfig.addTitle = addTitle;
        return (T) this;
    }

    public Component getRemoveTitle() {
        return displayConfig.removeTitle;
    }

    public T setRemoveTitle(Component removeTitle) {
        displayConfig.removeTitle = removeTitle;
        return (T) this;
    }

    public Component getAddSubTitle() {
        return displayConfig.addSubTitle;
    }

    public T setAddSubTitle(Component addSubTitle) {
        displayConfig.addSubTitle = addSubTitle;
        return (T) this;
    }

    public Component getRemoveSubTitle() {
        return displayConfig.removeSubTitle;
    }

    public T setRemoveSubTitle(Component removeSubTitle) {
        displayConfig.removeSubTitle = removeSubTitle;
        return (T) this;
    }

    public Component getAddChatMessage() {
        return displayConfig.addChatMessage;
    }

    public T setAddChatMessage(Component addChatMessage) {
        displayConfig.addChatMessage = addChatMessage;
        return (T) this;
    }

    public Component getRemoveChatMessage() {
        return displayConfig.removeChatMessage;
    }

    public T setRemoveChatMessage(Component removeChatMessage) {
        displayConfig.removeChatMessage = removeChatMessage;
        return (T) this;
    }

    public int getFadeIn() {
        return displayConfig.fadeIn;
    }

    public T setFadeIn(int fadeIn) {
        displayConfig.fadeIn = fadeIn;
        return (T) this;
    }

    public int getFadeOut() {
        return displayConfig.fadeOut;
    }

    public T setFadeOut(int fadeOut) {
        displayConfig.fadeOut = fadeOut;
        return (T) this;
    }

    public int getStay() {
        return displayConfig.stay;
    }

    public T setStay(int stay) {
        displayConfig.stay = stay;
        return (T) this;
    }

    public ItemStack getStack() {
        return displayConfig.stack;
    }

    public T setStack(ItemStack stack) {
        displayConfig.stack = stack;
        return (T) this;
    }
}
