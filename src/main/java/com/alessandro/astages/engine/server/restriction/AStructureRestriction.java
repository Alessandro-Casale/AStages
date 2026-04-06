package com.alessandro.astages.engine.server.restriction;

import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NotNullParamsAndMethodsReturn
public class AStructureRestriction extends ARestriction<AStructureRestriction, ResourceLocation, ResourceLocation> {
    private final List<ResourceLocation> structures = new ArrayList<>();
    private List<Block> allowedBreakableBlocks = null;
    private List<Block> allowedPlaceableBlocks = null;
    private List<Block> allowedInteractableBlocks = null;
    private List<BlockState> allowedBreakableStates = null;
    private List<BlockState> allowedPlaceableStates = null;
    private List<BlockState> allowedInteractableStates = null;
    private List<EntityType<?>> allowedTargetableEntities = null;

    public AStructureRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static AStructureRestriction newBuilder() {
        return new AStructureRestriction("null", "null");
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.ATTACKING)
            .addAttribute(Attributes.GENERIC_INTERACTIONS)
            .addAttribute(Attributes.ENTERING)
            .addAttribute(Attributes.BLOCK_PLACING)
            .addAttribute(Attributes.BLOCK_BREAKING)
            .addAttribute(Attributes.EXPLOSIONS_AFFECT_BLOCKS)
            .addAttribute(Attributes.EXPLOSIONS_AFFECT_ENTITIES)

            .addAttribute(Attributes.Structure.ATTACK_MESSAGE)
            .addAttribute(Attributes.Structure.INTERACT_MESSAGE)
            .addAttribute(Attributes.Structure.ENTER_MESSAGE)
            .addAttribute(Attributes.Structure.PLACING_MESSAGE)
            .addAttribute(Attributes.Structure.MINING_MESSAGE);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, AStructureRestriction.class)
            .build();
    }

    @Override
    public AStructureRestriction restrict(ResourceLocation structure) {
        structures.add(structure);

        return this;
    }

    @Override
    public boolean isRestricted(ResourceLocation structure) {
        for (ResourceLocation str : structures) {
            if (str.equals(structure)) {
                return true;
            }
        }

        return false;
    }

    public List<ResourceLocation> getStructures() {
        return structures;
    }

    public boolean isBlockBreakable(BlockState state) {
        if (allowedBreakableBlocks != null) {
            if (allowedBreakableBlocks.contains(state.getBlock())) {
                return true;
            }
        }

        if (allowedBreakableStates != null) {
            return allowedBreakableStates.contains(state);
        }

        return false;
    }

    public boolean isBlockPlaceable(BlockState state) {
        if (allowedPlaceableBlocks != null) {
            if (allowedPlaceableBlocks.contains(state.getBlock())) {
                return true;
            }
        }

        if (allowedPlaceableStates != null) {
            return allowedPlaceableStates.contains(state);
        }

        return false;
    }

    public boolean isBlockInteractable(BlockState state) {
        if (allowedInteractableBlocks != null) {
            if (allowedInteractableBlocks.contains(state.getBlock())) {
                return true;
            }
        }

        if (allowedInteractableStates != null) {
            return allowedInteractableStates.contains(state);
        }

        return false;
    }

    public boolean isEntityTargetable(EntityType<?> entityType) {
        if (allowedTargetableEntities == null) { return false; }
        return allowedTargetableEntities.contains(entityType);
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setCanAttackEntities(boolean value) {
        set(Attributes.ATTACKING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setCanEnter(boolean value) {
        set(Attributes.ENTERING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setCanInteract(boolean value) {
        set(Attributes.GENERIC_INTERACTIONS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setCanBlockBePlaced(boolean value) {
        set(Attributes.BLOCK_PLACING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setCanBlockBeBroken(boolean value) {
        set(Attributes.BLOCK_BREAKING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setMakeExplosionsAffectBlocks(boolean value) {
        set(Attributes.EXPLOSIONS_AFFECT_BLOCKS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setMakeExplosionsAffectEntities(boolean value) {
        set(Attributes.EXPLOSIONS_AFFECT_ENTITIES, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction addAllowedBreakableStates(BlockState... states) {
        if (allowedBreakableStates == null) { allowedBreakableStates = new ArrayList<>(); }
        allowedBreakableStates.addAll(List.of(states));
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction addAllowedPlaceableStates(BlockState... states) {
        if (allowedPlaceableStates == null) { allowedPlaceableStates = new ArrayList<>(); }
        allowedPlaceableStates.addAll(List.of(states));
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction addAllowedInteractableStates(BlockState... states) {
        if (allowedInteractableStates == null) { allowedInteractableStates = new ArrayList<>(); }
        allowedInteractableStates.addAll(List.of(states));
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction addAllowedBreakableBlocks(Block... blocks) {
        if (allowedBreakableBlocks == null) { allowedBreakableBlocks = new ArrayList<>(); }
        allowedBreakableBlocks.addAll(List.of(blocks));
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction addAllowedPlaceableBlocks(Block... blocks) {
        if (allowedPlaceableBlocks == null) { allowedPlaceableBlocks = new ArrayList<>(); }
        allowedPlaceableBlocks.addAll(List.of(blocks));
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction addAllowedInteractableBlocks(Block... blocks) {
        if (allowedInteractableBlocks == null) { allowedInteractableBlocks = new ArrayList<>(); }
        allowedInteractableBlocks.addAll(List.of(blocks));
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction addAllowedTargetableEntities(EntityType<?>... entities) {
        if (allowedTargetableEntities == null) { allowedTargetableEntities = new ArrayList<>(); }
        allowedTargetableEntities.addAll(List.of(entities));
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setAttackMessage(Function<ResourceLocation, Component> message) {
        set(Attributes.Structure.ATTACK_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setInteractMessage(Function<ResourceLocation, Component> message) {
        set(Attributes.Structure.INTERACT_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setEnterMessage(Function<ResourceLocation, Component> message) {
        set(Attributes.Structure.ENTER_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setPlaceMessage(Function<ResourceLocation, Component> message) {
        set(Attributes.Structure.PLACING_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setBreakMessage(Function<ResourceLocation, Component> message) {
        set(Attributes.Structure.MINING_MESSAGE, message);
        return this;
    }
}
