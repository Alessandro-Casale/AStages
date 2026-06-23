package com.alessandro.astages.infrastructure.command.argument;

import com.alessandro.astages.AStages;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACommandArguments {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, AStages.MODID);

    @SuppressWarnings("unused") public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<AStagesDimensionArgument>> DIMENSION_IDS_ARGUMENT = ARGUMENT_TYPES.register("dimension_ids", () -> ArgumentTypeInfos.registerByClass(AStagesDimensionArgument.class, SingletonArgumentInfo.contextFree(AStagesDimensionArgument::dimensionIds)));
    @SuppressWarnings("unused") public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<AStagesSimpleRestrictionsIdsArgument>> SIMPLE_RESTRICTION_IDS_ARGUMENT = ARGUMENT_TYPES.register("simple_restriction_ids", () -> ArgumentTypeInfos.registerByClass(AStagesSimpleRestrictionsIdsArgument.class, SingletonArgumentInfo.contextFree(AStagesSimpleRestrictionsIdsArgument::simpleRestrictionIds)));

    @SuppressWarnings("unused") public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<AStagesRestrictionTypeArgument>> RESTRICTION_TYPE_ARGUMENT = ARGUMENT_TYPES.register("restriction_type", () -> ArgumentTypeInfos.registerByClass(AStagesRestrictionTypeArgument.class, SingletonArgumentInfo.contextFree(AStagesRestrictionTypeArgument::types)));
    @SuppressWarnings("unused") public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<AStagesSimpleRestrictionTypeArgument>> SIMPLE_RESTRICTION_TYPE_ARGUMENT = ARGUMENT_TYPES.register("simple_restriction_type", () -> ArgumentTypeInfos.registerByClass(AStagesSimpleRestrictionTypeArgument.class, SingletonArgumentInfo.contextFree(AStagesSimpleRestrictionTypeArgument::types)));
    @SuppressWarnings("unused") public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<AStagesPlayerArgument>> PLAYER_ARGUMENT = ARGUMENT_TYPES.register("player", () -> ArgumentTypeInfos.registerByClass(AStagesPlayerArgument.class, SingletonArgumentInfo.contextFree(AStagesPlayerArgument::onlineAndOfflinePlayers)));
}
