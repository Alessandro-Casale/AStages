package com.alessandro.astages.capability;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import net.minecraft.server.MinecraftServer;

import java.util.List;

@SuppressWarnings("removal")
@NotNullMethodsReturn
public class ServerStageWrapper {
    public static List<String> getStages(MinecraftServer server) {
        return ServerStageData.getData(server).getServerStages();
    }
}
