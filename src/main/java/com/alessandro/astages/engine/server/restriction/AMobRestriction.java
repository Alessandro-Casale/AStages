package com.alessandro.astages.engine.server.restriction;

import com.alessandro.astages.api.constant.AFilter;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.api.wrapper.EquipmentWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@NotNullParamsAndMethodsReturn
public class AMobRestriction extends ARestriction<AMobRestriction, EntityType<?>, EntityType<?>> {
    private final Set<EntityType<?>> mobs = new HashSet<>();

    private final Set<EquipmentWrapper> equipments = new HashSet<>();
    private final Set<MobSpawnType> disabledSpawnTypes = new HashSet<>();
    private final Set<ResourceLocation> restrictedBiomes = new HashSet<>();
    private final Set<ResourceLocation> restrictedDimensions = new HashSet<>();

    private final Set<ResourceLocation> ignoredBiomes = new HashSet<>();

    public AMobRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.OVERALL_MOB_SPAWNING)
            .addAttribute(Attributes.SPAWN_WITH_EQUIPMENT)
            .addAttribute(Attributes.ATTACKING).setAttribute(Attributes.ATTACKING, true) // Left click interactions
            .addAttribute(Attributes.RIGHT_CLICK_INTERACTIONS).setAttribute(Attributes.RIGHT_CLICK_INTERACTIONS, true)

            .addAttribute(Attributes.REPLACEMENT, true)
            .addAttribute(Attributes.MIN_LIGHT_LEVEL, true)
            .addAttribute(Attributes.MAX_LIGHT_LEVEL, true)

            .addAttribute(Attributes.Mob.JADE_MESSAGE)
            .addAttribute(Attributes.Mob.INTERACTION_MESSAGE)
            .addAttribute(Attributes.Mob.ATTACK_MESSAGE);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, AMobRestriction.class)
            .build();
    }

    @Override
    public AMobRestriction restrict(EntityType<?> mob) {
        mobs.add(mob);
        return this;
    }

    @Override
    public boolean isRestricted(EntityType<?> mob) {
        return mobs.contains(mob);
    }

    public Set<EntityType<?>> getMobs() {
        return mobs;
    }

    public AMobRestriction associateLootRestriction(String id) {
        var restriction = new ALootRestriction(id, getStage());
        for (var mob : mobs) { restriction.restrictForEntities(mob); }
        restriction.entityFilter(AFilter.ALL);
        ARestrictionManager.LOOT_INSTANCE.addRestriction(restriction);

        return this;
    }

    public AMobRestriction associateLootRestriction() {
        return associateLootRestriction(getId() + ALootRestriction.IDENTIFIER);
    }

    public Set<EquipmentWrapper> getEquipments() {
        return equipments;
    }

    public Set<MobSpawnType> getDisabledSpawnTypes() {
        return disabledSpawnTypes;
    }

    public Set<ResourceLocation> getRestrictedBiomes() {
        return restrictedBiomes;
    }

    public Set<ResourceLocation> getRestrictedDimensions() {
        return restrictedDimensions;
    }

    public Set<ResourceLocation> getIgnoredBiomes() {
        return ignoredBiomes;
    }

    public AMobRestriction spawnWithEquipment() {
        return set(Attributes.SPAWN_WITH_EQUIPMENT, true);
    }

    public AMobRestriction equipment(EquipmentSlot slot, ItemStack stack) {
        equipments.add(new EquipmentWrapper(slot, stack));
        return this;
    }

    public AMobRestriction restrictSpawnType(MobSpawnType... types) {
        disabledSpawnTypes.addAll(Set.of(types));
        return this;
    }

    public AMobRestriction minLightLevel(int value) {
        return set(Attributes.MIN_LIGHT_LEVEL, value);
    }

    public AMobRestriction maxLightLevel(int value) {
        return set(Attributes.MAX_LIGHT_LEVEL, value);
    }

    public AMobRestriction restrictBiomeSpawn(ResourceLocation... biomes) {
        restrictedBiomes.addAll(Set.of(biomes));
        return this;
    }

    public AMobRestriction restrictDimensionSpawn(ResourceLocation... dimensions) {
        restrictedDimensions.addAll(Set.of(dimensions));
        return this;
    }

    public AMobRestriction ignoredBiomes(ResourceLocation... biomes) {
        ignoredBiomes.addAll(Set.of(biomes));
        return this;
    }

    public AMobRestriction replaceWith(EntityType<?> value) {
        return set(Attributes.REPLACEMENT, value);
    }

    public AMobRestriction disableOverallSpawning() {
        return set(Attributes.OVERALL_MOB_SPAWNING, false);
    }

    public AMobRestriction disableAttack() {
        return set(Attributes.ATTACKING, false);
    }

    public AMobRestriction disableRightClick() {
        return set(Attributes.RIGHT_CLICK_INTERACTIONS, false);
    }

    public AMobRestriction jadeMobMessage(Supplier<Component> message) {
        return set(Attributes.Mob.JADE_MESSAGE, message);
    }

    public AMobRestriction attackMessage(Supplier<Component> message) {
        return set(Attributes.Mob.ATTACK_MESSAGE, message);
    }

    public AMobRestriction interactionMessage(Supplier<Component> message) {
        return set(Attributes.Mob.INTERACTION_MESSAGE, message);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction jadeMessage(Supplier<Component> message) {
        return set(Attributes.Mob.JADE_MESSAGE, message);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction spawnReplacementWithEquipment(boolean value) {
        set(Attributes.SPAWN_WITH_EQUIPMENT, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setEquipment(EquipmentSlot slot, ItemStack stack) {
        equipments.add(new EquipmentWrapper(slot, stack));
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setMinLightLevel(int value) {
        set(Attributes.MIN_LIGHT_LEVEL, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setMaxLightLevel(int value) {
        set(Attributes.MAX_LIGHT_LEVEL, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setDisableSpawner(boolean ignoredValue) {
//        set(Attributes.SPAWNER, !value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setDimension(ResourceLocation value) {
        set(Attributes.DIMENSION, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setReplacing(EntityType<?> value) {
        set(Attributes.REPLACEMENT, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setEnableMobSpawning(boolean value) {
        set(Attributes.OVERALL_MOB_SPAWNING, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setCanBeAttacked(boolean value) {
        set(Attributes.ATTACKING, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setCanBeRightClicked(boolean value) {
        set(Attributes.RIGHT_CLICK_INTERACTIONS, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setJadeMobMessage(Supplier<Component> message) {
        set(Attributes.Mob.JADE_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setAttackMessage(Supplier<Component> message) {
        set(Attributes.Mob.ATTACK_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setInteractionMessage(Supplier<Component> message) {
        set(Attributes.Mob.INTERACTION_MESSAGE, message);
        return this;
    }
}