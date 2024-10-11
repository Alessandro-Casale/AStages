package com.alessandro.astages.command.argument;

import com.alessandro.astages.AStages;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModArguments {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, AStages.MODID);

    public static final RegistryObject<ArgumentTypeInfo<?, ?>> ADD_STAGES_ARGUMENT = ARGUMENT_TYPES.register("add_stages", () -> ArgumentTypeInfos.registerByClass(AStagesAddArgument.class, SingletonArgumentInfo.contextFree(AStagesAddArgument::stages)));
    public static final RegistryObject<ArgumentTypeInfo<?, ?>> REMOVE_STAGES_ARGUMENT = ARGUMENT_TYPES.register("remove_stages", () -> ArgumentTypeInfos.registerByClass(AStagesRemoveArgument.class, SingletonArgumentInfo.contextFree(AStagesRemoveArgument::stages)));
}
