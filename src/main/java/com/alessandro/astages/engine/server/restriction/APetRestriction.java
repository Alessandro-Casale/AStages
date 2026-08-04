package com.alessandro.astages.engine.server.restriction;

import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NotNullParamsAndMethodsReturn
public class APetRestriction extends ARestriction<APetRestriction, EntityType<?>, EntityType<?>> {
    private final List<EntityType<?>> pets = new ArrayList<>();

    public List<EntityType<?>> getPets() {
        return pets;
    }

    public APetRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.TAMING)
            .addAttribute(Attributes.BREEDING)
            .addAttribute(Attributes.MOUNTING)

            .addAttribute(Attributes.Pet.TAME_MESSAGE)
            .addAttribute(Attributes.Pet.BREED_MESSAGE)
            .addAttribute(Attributes.Pet.MOUNT_MESSAGE);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, APetRestriction.class)
            .build();
    }

    @Override
    public APetRestriction restrict(EntityType<?> pet) {
        pets.add(pet);

        return this;
    }

    @Override
    public boolean isRestricted(EntityType<?> pet) {
        return pets.contains(pet);
    }

    public APetRestriction allowTaming() {
        return set(Attributes.TAMING, true);
    }

    public APetRestriction allowBreeding() {
        return set(Attributes.BREEDING, true);
    }

    public APetRestriction allowMounting() {
        return set(Attributes.MOUNTING, true);
    }

    public APetRestriction tameMessage(Function<Entity, Component> message) {
        return set(Attributes.Pet.TAME_MESSAGE, message);
    }

    public APetRestriction breedMessage(Function<Entity, Component> message) {
        return set(Attributes.Pet.BREED_MESSAGE, message);
    }

    public APetRestriction mountMessage(Function<Entity, Component> message) {
        return set(Attributes.Pet.MOUNT_MESSAGE, message);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public APetRestriction setTamable(boolean value) {
        set(Attributes.TAMING, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public APetRestriction setBreedable(boolean value) {
        set(Attributes.BREEDING, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public APetRestriction setMountable(boolean value) {
        set(Attributes.MOUNTING, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public APetRestriction setTameMessage(Function<Entity, Component> message) {
        set(Attributes.Pet.TAME_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public APetRestriction setBreedMessage(Function<Entity, Component> message) {
        set(Attributes.Pet.BREED_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public APetRestriction setMountMessage(Function<Entity, Component> message) {
        set(Attributes.Pet.MOUNT_MESSAGE, message);
        return this;
    }
}