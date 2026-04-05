package com.alessandro.astages.core.server.restriction;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.server.ARestriction;
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

    @SuppressWarnings("unused")
    public static APetRestriction newBuilder() {
        return new APetRestriction("null", "null");
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.TAMABLE)
            .addAttribute(Attributes.BREEDABLE)
            .addAttribute(Attributes.MOUNTABLE)

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

    @SuppressWarnings("unused")
    public APetRestriction setTamable(boolean value) {
        set(Attributes.TAMABLE, value);
        return this;
    }

    @SuppressWarnings("unused")
    public APetRestriction setBreedable(boolean value) {
        set(Attributes.BREEDABLE, value);
        return this;
    }

    @SuppressWarnings("unused")
    public APetRestriction setMountable(boolean value) {
        set(Attributes.MOUNTABLE, value);
        return this;
    }

    @SuppressWarnings("unused")
    public APetRestriction setTameMessage(Function<Entity, Component> message) {
        set(Attributes.Pet.TAME_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public APetRestriction setBreedMessage(Function<Entity, Component> message) {
        set(Attributes.Pet.BREED_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public APetRestriction setMountMessage(Function<Entity, Component> message) {
        set(Attributes.Pet.MOUNT_MESSAGE, message);
        return this;
    }
}
