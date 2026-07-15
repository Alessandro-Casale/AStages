package com.alessandro.astages.engine.server;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStageSource;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.PluginManager;
import com.alessandro.astages.infrastructure.capability.ServerStage;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.dimension.SyncDimensionIdsS2C;
import com.alessandro.astages.infrastructure.networking.packet.simple.SyncSimpleIdsS2C;
import com.alessandro.astages.infrastructure.networking.packet.stages.SyncKnownStagesS2C;
import com.alessandro.astages.infrastructure.networking.packet.stages.SyncServerStagesS2C;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class RestrictionSyncService {
    public static void clearClientOnLogin(ServerPlayer player) {
        PluginManager.callMethod(player, AStagesPlugin::clearClientOnLogin);
    }

    public static void clientSynchronization(@Nullable ServerPlayer player) {
        AStages.TIMER.start();

        RestrictionRegistry.getRegisteredManagers()
            .forEach(manager -> {
                if (manager instanceof ClientSynchronizable syncManager) {
                    syncManager.synchronizeWithClient(player);
                }
            });

        Networking.sendTo(player, new SyncDimensionIdsS2C(ARestrictionManager.DIMENSION_INSTANCE.getRegistry().getIds()));

        AStages.TIMER.stop();
        AStages.LOGGER.info("AStages synchronization took {}!", AStages.TIMER);

        PluginManager.callMethod(player, AStagesPlugin::clientSynchronization);
    }

    public static void reflectServerStagesChangesToClients(@Nullable ServerPlayer player) {
        Networking.sendTo(player, new SyncServerStagesS2C(ServerStage.getServerStages(), AOperation.LOGIN));
    }

    public static void reflectSimpleIdsChangesToClients(@Nullable ServerPlayer player, Collection<String> ids, ASyncOperation operation) {
        Networking.sendTo(player, new SyncSimpleIdsS2C(ids, operation));
    }

    public static void reflectAllStagesChangesToClients(@Nullable ServerPlayer player) {
        Networking.sendTo(player, new SyncKnownStagesS2C(MiscStorage.STAGES_ONLY_FOR_PLAYER, ASyncOperation.ADD, AStageSource.PLAYER));
        Networking.sendTo(player, new SyncKnownStagesS2C(MiscStorage.STAGES_ONLY_FOR_SERVER, ASyncOperation.ADD, AStageSource.SERVER));
        Networking.sendTo(player, new SyncKnownStagesS2C(MiscStorage.ALL_STAGES, ASyncOperation.ADD, AStageSource.BOTH));
    }
}