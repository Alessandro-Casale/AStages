package com.alessandro.astages;

import com.alessandro.astages.command.argument.ModArguments;
import com.alessandro.astages.networking.ModNetworking;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AStages.MODID)
public class AStages {
    public static final String MODID = "astages";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AStages() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModArguments.ARGUMENT_TYPES.register(modEventBus);

        ModNetworking.register();
    }
}
