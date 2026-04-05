package com.alessandro.astages.core.server.restriction;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.develop.Info;
import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@NotNullParamsAndMethodsReturn
public class ARegionRestriction extends ARestriction<ARegionRestriction, Void, BlockPos> {
    private Type type = Type.CUBE;

    private BlockPos center;
    private int deltaX;
    private int deltaY;
    private int deltaZ;

    private final List<String> disabledCommands = new ArrayList<>();

    public ARegionRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static ARegionRestriction newBuilder() {
        return new ARegionRestriction("null", "null");
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.GENERIC_INTERACTIONS)
            .addAttribute(Attributes.EXPLOSIONS_AFFECT_BLOCKS)
            .addAttribute(Attributes.EXPLOSIONS_AFFECT_ENTITIES)
            .addAttribute(Attributes.MOB_SPAWNING)
            .addAttribute(Attributes.PERFORM_COMMANDS)

            .addAttribute(Attributes.DIMENSION, true)

            .addAttribute(Attributes.Region.INTERACT_MESSAGE)
            .addAttribute(Attributes.Region.COMMAND_MESSAGE);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, ARegionRestriction.class)
            .build();
    }

    @Override
    @DoNotCall
    @Info("Prefer using methods below!")
    public ARegionRestriction restrict(Void unused) { return null; }

    public ARegionRestriction setDimension(ResourceLocation dimension) {
        set(Attributes.DIMENSION, dimension);
        return this;
    }

    @Override
    public boolean isRestricted(BlockPos blockPos) {
        if (type == Type.CUBE) {
            int dX = Math.abs(blockPos.getX() - center.getX());
            int dY = Math.abs(blockPos.getY() - center.getY());
            int dZ = Math.abs(blockPos.getZ() - center.getZ());
            return dX <= deltaX && dY <= deltaY && dZ <= deltaZ;
        }

        if (type == Type.SPHERE) {
            int dX = Math.abs(blockPos.getX() - center.getX());
            int dY = Math.abs(blockPos.getY() - center.getY());
            int dZ = Math.abs(blockPos.getZ() - center.getZ());
            return (dX * dX) / (deltaX * deltaX) + (dY * dY) / (deltaY * deltaY) + (dZ * dZ) / (deltaZ * deltaZ) <= 1;
        }

        return false;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ARegionRestriction setArea(Type type, BlockPos center, int deltaX, int deltaY, int deltaZ) {
        this.type = type;
        this.center = center;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;

        return this;
    }

    public ARegionRestriction setArea(Type type, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.type = type;
        this.deltaX = (maxX - minX) / 2;
        this.deltaY = (maxY - minY) / 2;
        this.deltaZ = (maxZ - minZ) / 2;
        this.center = new BlockPos((maxX + minX) / 2, (maxY + minY) / 2, (maxZ + minZ) / 2);

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ARegionRestriction setArea(Type type, BlockPos center, int radius) {
        this.type = type;
        this.center = center;
        this.deltaX = radius;
        this.deltaY = radius;
        this.deltaZ = radius;

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ARegionRestriction setArea(Type type, BlockPos from, BlockPos to) {
        return setArea(type, from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ());
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setMakeExplosionsAffectBlocks(boolean value) {
        set(Attributes.EXPLOSIONS_AFFECT_BLOCKS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setMakeExplosionsAffectEntities(boolean value) {
        set(Attributes.EXPLOSIONS_AFFECT_ENTITIES, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setCanInteract(boolean value) {
        set(Attributes.GENERIC_INTERACTIONS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setEnableMobSpawning(boolean value) {
        set(Attributes.MOB_SPAWNING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setPerformCommands(boolean value) {
        set(Attributes.PERFORM_COMMANDS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ARegionRestriction addDisabledCommands(String... commands) {
        Arrays.stream(commands).forEach(command -> {
            var formatted = command.startsWith("/") ? command.replace("/", "") : command;
            disabledCommands.add(command);
        });

        return this;
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setInteractMessage(Supplier<Component> message) {
        set(Attributes.Region.INTERACT_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setCommandMessage(Function<String, Component> message) {
        set(Attributes.Region.COMMAND_MESSAGE, message);
        return this;
    }

    public List<String> getDisabledCommands() {
        return disabledCommands;
    }

    @Override
    public String toString() {
        return "ARegionRestriction{" +
                "type=" + type +
                ", center=" + center +
                ", deltaX=" + deltaX +
                ", deltaY=" + deltaY +
                ", deltaZ=" + deltaZ +
                ", disabledCommands=" + disabledCommands +
                '}';
    }

    public enum Type {
        SPHERE, CUBE
    }
}
