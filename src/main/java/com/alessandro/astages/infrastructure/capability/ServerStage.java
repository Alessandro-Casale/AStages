package com.alessandro.astages.infrastructure.capability;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.event.server.*;
import com.alessandro.astages.api.foldersystem.AFolderPaths;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.util.AFileIOUtils;
import com.alessandro.astages.api.util.ASetUtils;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.api.util.ATitleUtils;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.stages.SyncServerStagesS2C;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NotNullMethodsReturn
public class ServerStage {
    public static final String SERVER_STAGES_FILE = "server.json";

    private static Set<String> CACHE = new HashSet<>();

    public static Path getPermanentStagesFile() {
        var file = AFolderPaths.getServerPermanentFolder().resolve(SERVER_STAGES_FILE);
        return AFileIOUtils.getOrCreateFile(file);
    }

    public static Path getTemporaryStagesFile() {
        var file = AFolderPaths.getServerTemporaryFolder().resolve(SERVER_STAGES_FILE);
        return AFileIOUtils.getOrCreateFile(file);
    }

    public static Set<String> getServerStages() {
        return CACHE;
    }

    public static void addServerStage(String stage) {
        CACHE.add(stage);
    }

    public static void addServerStages(Set<String> stages) {
        CACHE.addAll(stages);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static AStatus removeServerStage(String stage) {
        return CACHE.remove(stage) ? AStatus.SUCCESSFUL : AStatus.NOT_PRESENT;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static AStatus removeServerStages(Set<String> stages) {
        return CACHE.removeAll(stages) ? AStatus.SUCCESSFUL : AStatus.NOT_PRESENT;
    }

    public static boolean synchronizeWithClient(@Nullable ServerPlayer player, AOperation operation, String stage) {
        return synchronizeWithClient(player, operation, ASetUtils.singleton(stage));
    }

    public static boolean synchronizeWithClient(@Nullable ServerPlayer player, AOperation operation, Set<String> stages) {
        AStagesUtils.checkServerStages(operation, stages);

        var server = ServerLifecycleHooks.getCurrentServer();
        var event = new StageSyncedServerEvent(ServerLifecycleHooks.getCurrentServer(), operation, stages);
        ALoader.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            Networking.sendTo(player, new SyncServerStagesS2C(stages, operation));

            switch (operation) {
                case ADD -> ALoader.EVENT_BUS.post(new StageAddedServerEvent(server, ASetUtils.getOnlyElement(stages)));
                case ADD_ALL -> ALoader.EVENT_BUS.post(new AllStagesAddedServerEvent(server, stages));
                case REMOVE -> ALoader.EVENT_BUS.post(new StageRemovedServerEvent(server, ASetUtils.getOnlyElement(stages)));
                case REMOVE_ALL -> ALoader.EVENT_BUS.post(new AllStagesRemovedServerEvent(server, stages));
                case LOGIN -> ALoader.EVENT_BUS.post(new StageLoginServerEvent(server, stages));
            }
        } else {
            switch (event.getOperation()) {
                case ADD -> removeServerStage(ASetUtils.getOnlyElement(stages));
                case ADD_ALL, LOGIN -> removeServerStages(stages);
                case REMOVE -> addServerStage(ASetUtils.getOnlyElement(stages));
                case REMOVE_ALL -> addServerStages(stages);
            }
        }

        return !event.isCanceled();
    }

    public static void displayStageAlert(MinecraftServer server, AOperation operation, String stage, AStatus status, boolean eventCancelled, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        displayStageAlert(server, operation, ASetUtils.singleton(stage), status, eventCancelled, showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static void displayStageAlert(MinecraftServer ignoredServer, AOperation operation, Set<String> stages, AStatus status, boolean eventCancelled, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        if (eventCancelled) { return; }

        ATitleUtils.displayStageAlert(AHolder.server(), operation, stages, status, showTitle, displayChatMessage, displayActionBarMessage);
    }

    // When saving, call this
    public static void markAsDirty() {
        AFileIOUtils.writeFileContent(getPermanentStagesFile(), CACHE);
    }

    public static void clearCache() {
        CACHE.clear();
    }

    public static List<String> getServerStagesFromData(MinecraftServer server) {
        return ServerStageWrapper.getStages(server);
    }

    public static void setCache(Set<String> cache) {
        CACHE = cache;
    }
}