package com.alessandro.astages.api.plugin.container;

import com.alessandro.astages.api.base.Elaborator;
import com.alessandro.astages.api.exception.SimpleRestrictionsException;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.store.ASimpleRestrictionType;
import com.alessandro.astages.engine.simple.ASimpleRestriction;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@NotNullMethodsReturn
public class SimpleRestrictionsContainer {
    private final Map<ASimpleRestrictionType, ARestrictionType> TEMPORARY_CONVERSION_MAP = new HashMap<>();
    private final Map<ASimpleRestrictionType, Elaborator<ASimpleRestriction, Boolean>> TEMPORARY_ELABORATION_MAP = new HashMap<>();
    private final Map<ASimpleRestrictionType, Elaborator<String, ASimpleRestrictionType>> TEMPORARY_AFTER_REMOVE_ELABORATION_MAP = new HashMap<>(); // NOT mandatory
    private final Map<ASimpleRestrictionType, BiConsumer<CommandBuildContext, ArgumentBuilder<CommandSourceStack, ?>>> TEMPORARY_COMMAND_MAP = new HashMap<>();

    private ASimpleRestrictionType latestModifiedType;

    public static SimpleRestrictionsContainer initialize() {
        return new SimpleRestrictionsContainer();
    }

    public SimpleRestrictionsContainer registerFor(ASimpleRestrictionType newType) {
        latestModifiedType = newType;
        return this;
    }

    public SimpleRestrictionsContainer convertTo(ARestrictionType associatedType) {
        if (latestModifiedType == null) {
            throw SimpleRestrictionsException.onRegisterConversionMethod();
        }

        TEMPORARY_CONVERSION_MAP.put(latestModifiedType, associatedType);
        return this;
    }

    public SimpleRestrictionsContainer elaborateUsing(Elaborator<ASimpleRestriction, Boolean> elaborator) {
        if (latestModifiedType == null) {
            throw SimpleRestrictionsException.onRegisterElaborationMethod();
        }

        TEMPORARY_ELABORATION_MAP.put(latestModifiedType, elaborator);
        return this;
    }

    public SimpleRestrictionsContainer afterRemoveRun(Elaborator<String, ASimpleRestrictionType> elaborator) {
        if (latestModifiedType == null) {
            throw SimpleRestrictionsException.onRegisterAfterRemoveMethod();
        }

        TEMPORARY_AFTER_REMOVE_ELABORATION_MAP.put(latestModifiedType, elaborator);
        return this;
    }

    public SimpleRestrictionsContainer addCommand(BiConsumer<CommandBuildContext, ArgumentBuilder<CommandSourceStack, ?>> command) {
        if (latestModifiedType == null) {
            throw SimpleRestrictionsException.onCommandAddedMethod();
        }

        TEMPORARY_COMMAND_MAP.put(latestModifiedType, command);
        return this;
    }

    public Map<ASimpleRestrictionType, ARestrictionType> getTemporaryConversionMap() {
        return TEMPORARY_CONVERSION_MAP;
    }

    public Map<ASimpleRestrictionType, Elaborator<ASimpleRestriction, Boolean>> getTemporaryElaborationMap() {
        return TEMPORARY_ELABORATION_MAP;
    }

    public Map<ASimpleRestrictionType, Elaborator<String, ASimpleRestrictionType>> getTemporaryAfterRemoveElaborationMap() {
        return TEMPORARY_AFTER_REMOVE_ELABORATION_MAP;
    }

    public Map<ASimpleRestrictionType, BiConsumer<CommandBuildContext, ArgumentBuilder<CommandSourceStack, ?>>> getTemporaryCommandMap() {
        return TEMPORARY_COMMAND_MAP;
    }
}