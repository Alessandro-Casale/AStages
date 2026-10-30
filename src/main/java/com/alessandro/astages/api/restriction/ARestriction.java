package com.alessandro.astages.api.restriction;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.store.APriority;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.store.config.AConfigurablePreset;
import com.alessandro.astages.api.store.container.AStore;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.ARestrictionManager;
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
@SuppressWarnings("unchecked")
@NotNullParamsAndMethodsReturn
public abstract class ARestriction<R extends ARestriction<R, U, V>, U, V> implements ACommonRestriction<V>, AStore<R>, AConfigurablePreset<R>, Comparable<R> {
    private final String id;
    private final String stage;
    private int priority = 0;

    private final AttributeStore attributes;

    public ARestriction(String id, String stage) {
        this.id = id;
        this.stage = stage;
        this.attributes = allowedAttributes();
    }

    public ARestriction(String id, String stage, APriority defaultPriority) {
        this.id = id;
        this.stage = stage;
        this.attributes = allowedAttributes();
        this.priority = defaultPriority.getPriority();
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

    @Override
    public <T> R set(Attribute<T> attribute, T value) {
        checkAttribute(attribute);
        attributes.setAttribute(attribute, value);

        return (R) this;
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, ARestriction.class)
            .build();
    }

    public abstract @Nullable R restrict(U object);

    @Override
    public abstract ARestrictionResult isRestricted(V object);

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ARestriction<?, ?, ?>)) { return false; }

        return Objects.equals(((ARestriction<?, ?, ?>) obj).id, this.id) &&
            Objects.equals(((ARestriction<?, ?, ?>) obj).stage, this.stage);
            // && Objects.equals(((ARestriction<?, ?, ?>) obj).attributes, this.attributes);
    }

    @Override
    public int compareTo(R that) {
        if (this.priority == that.getPriority()) {
            return this.id.compareTo(that.getId());
        }

        return -Integer.compare(this.priority, that.getPriority()); // Ascending order!
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getStage() {
        return stage;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public R setPriority(APriority priority) {
        this.priority = priority.getPriority();

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