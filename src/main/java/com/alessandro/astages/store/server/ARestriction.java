package com.alessandro.astages.store.server;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.AStore;
import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.ConfigurableAttributeStore;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.function.Function;

/**
 * Base class for all Restrictions related to AStages!
 *
 * @param <R> The restriction itself
 * @param <U> For restrict method object type
 * @param <V> For isRestricted method object type
 */
public abstract class ARestriction<R extends ARestriction<R, U, V>, U, V> implements AStore<R>, Comparable<R> {
    private final String id;
    private final String stage;
    private int priority = 0;
    private final boolean markForConfig;

    private final AttributeStore attributes;

    public ARestriction(@NotNull String id, @NotNull String stage) {
        if (id.equals("null") && stage.equals("null")) {
            this.id = id;
            this.stage = stage;
            this.attributes = new ConfigurableAttributeStore();
            this.markForConfig = true;
        } else {
            this.id = id;
            this.stage = stage;
            this.attributes = allowedAttributes();
            this.markForConfig = false;
        }
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

    public <T> void displayMessage(Attribute<Function<T, Component>> attribute, T value, Player player) {
        if (!isValueNull(attribute)) {
            player.displayClientMessage(getMessage(attribute, value), true);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> R set(Attribute<T> attribute, T value) {
        if (!markForConfig) {
            checkAttribute(attribute);
            attributes.setAttribute(attribute, value);
        } else {
            if (attributes instanceof ConfigurableAttributeStore config) {
                config.setAttribute(attribute, value);
            }
        }

        return (R) this;
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, ARestriction.class)
            .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ARestriction<?, ?, ?>)) { return false; }

        return Objects.equals(((ARestriction<?, ?, ?>) obj).id, this.id) &&
            Objects.equals(((ARestriction<?, ?, ?>) obj).stage, this.stage);
            // && Objects.equals(((ARestriction<?, ?, ?>) obj).attributes, this.attributes);
    }

    @Override
    public int compareTo(@NotNull R that) {
        if (this.priority == that.getPriority()) {
            return this.id.compareTo(that.getId());
        }

        return -Integer.compare(this.priority, that.getPriority()); // Ascending order!
    }

    public abstract R restrict(U object);

    public abstract boolean isRestricted(V object);

    @NotNullParams
    @SuppressWarnings({"unchecked", "unused"})
    public R withAttributes(ARestriction<?, ?, ?>... configs) {
        for (var config : configs) {
            if (isCorrectClassForConfigs(config)) {
                attributes.overwrite(config.getConfigurableAttributeStore());
            } else {
                AStages.LOGGER.debug("SKIPPED CONFIG");
            }
        }

        return (R) this;
    }

    public ConfigurableAttributeStore getConfigurableAttributeStore() {
        if (markForConfig && attributes instanceof ConfigurableAttributeStore config) {
            return config;
        }

        throw new RuntimeException("You cannot use restriction with id " + getId() + " for configuration!");
    }

    public boolean isCorrectClassForConfigs(@NotNull ARestriction<?, ?, ?> config) {
        Class<?> expectedClass = this.getClass();
        return config.getClass().equals(expectedClass);
    }

    public boolean isConfig() {
        return markForConfig && attributes instanceof ConfigurableAttributeStore;
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
        return "ARestriction{" +
            "id='" + id + '\'' +
            ", stage='" + stage + '\'' +
            ", priority=" + priority +
            '}';
    }
}