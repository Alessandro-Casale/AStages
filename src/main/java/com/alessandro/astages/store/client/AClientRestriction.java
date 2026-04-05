package com.alessandro.astages.store.client;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.item.ABaseItemRestriction;
import com.alessandro.astages.store.AStore;
import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.store.AttributeStore;
import net.minecraft.network.chat.Component;

import java.util.Objects;
import java.util.function.Function;

public abstract class AClientRestriction<R extends AClientRestriction<R, U, V>, U, V> implements AStore<R>, Comparable<R> {
    private final String id;
    private final String stage;
    private int priority = 0;

    private final AttributeStore attributes;

    protected AClientRestriction(String id, String stage) {
        this.id = id;
        this.stage = stage;
        this.attributes = allowedAttributes();
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> R set(Attribute<T> attribute, T value) {
        checkAttribute(attribute);
        attributes.setAttribute(attribute, value);

        return (R) this;
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientRestriction.class)
            .build();
    }

    public abstract R restrict(U object);

    public abstract boolean isRestricted(V object);

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AClientRestriction<?, ?, ?>)) { return false; }

        return Objects.equals(((AClientRestriction<?, ?, ?>) obj).id, this.id) &&
            Objects.equals(((AClientRestriction<?, ?, ?>) obj).stage, this.stage);
    }

    @Override
    public int compareTo(@NotNull R that) {
        if (this.priority == that.getPriority()) {
            return this.id.compareTo(that.getId());
        }

        return -Integer.compare(this.priority, that.getPriority()); // Ascending order!
    }

    public String getId() {
        return id;
    }

    public String getStage() {
        return stage;
    }

    public int getPriority() {
        return priority;
    }

    @SuppressWarnings({ "unchecked", "unused" })
    public R setPriority(int priority) {
        this.priority = priority;

        return (R) this;
    }

    @Override
    public String toString() {
        return "AClientRestriction{" +
            "id='" + id + '\'' +
            ", stage='" + stage + '\'' +
            ", priority=" + priority +
            '}';
    }
}