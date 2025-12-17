package com.alessandro.astages.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AFileIOUtils;
import com.alessandro.astages.api.ASetUtils;
import com.alessandro.astages.api.AStagesFolderSystem;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.event.server.*;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.stages.ServerStagesSyncerS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NotNullMethodsReturn
@EventBusSubscriber(modid = AStages.MODID)
public class ServerStage {
    private static Set<String> CACHE = new HashSet<>();

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        CACHE = AFileIOUtils.readHashSetOrDefault(getPermanentStagesFile(), String.class);
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        markAsDirty();
        CACHE.clear();
    }

    @Info("Migration purpose only!")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStartingHighest(ServerStartingEvent event) {
        var file = getPermanentStagesFile();
        var stageList = AFileIOUtils.readList(file, String.class);

        if (stageList == null) {
            var oldList = getServerStagesFromData(event.getServer());
            AFileIOUtils.writeFileContent(file, oldList);
        }
    }

    private static Path getPermanentStagesFile() {
        var file = AStagesFolderSystem.getServerPermanentFolder().resolve("server.json");
        return AFileIOUtils.getOrCreateFile(file);
    }

    public static Path getTemporaryStagesFile() {
        var file = AStagesFolderSystem.getServerTemporaryFolder().resolve("server.json");
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
        return CACHE.remove(stage) ? AStatus.SUCCESS : AStatus.NOT_PRESENT;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static AStatus removeServerStages(Set<String> stages) {
        return CACHE.removeAll(stages) ? AStatus.SUCCESS : AStatus.NOT_PRESENT;
    }

    public static void synchronizeWithClient(@Nullable ServerPlayer player, AOperation operation, String stage) {
        synchronizeWithClient(player, operation, ASetUtils.singleton(stage));
    }

    public static void synchronizeWithClient(@Nullable ServerPlayer player, AOperation operation, Set<String> stages) {
        AStagesUtils.checkServerStages(operation, stages);

        var server = ServerLifecycleHooks.getCurrentServer();
        var event = new StageSyncedServerEvent(ServerLifecycleHooks.getCurrentServer(), operation, stages);
        NeoForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            ANetworking.sendTo(player, new ServerStagesSyncerS2CPacket(stages, operation));

            switch (operation) {
                case ADD -> NeoForge.EVENT_BUS.post(new StageAddedServerEvent(server, ASetUtils.getOnlyElement(stages)));
                case ADD_ALL -> NeoForge.EVENT_BUS.post(new AllStagesAddedServerEvent(server, stages));
                case REMOVE -> NeoForge.EVENT_BUS.post(new StageRemovedServerEvent(server, ASetUtils.getOnlyElement(stages)));
                case REMOVE_ALL -> NeoForge.EVENT_BUS.post(new AllStagesRemovedServerEvent(server, stages));
                case LOGIN -> NeoForge.EVENT_BUS.post(new StageLoginServerEvent(server, stages));
            }
        } else {
            switch (event.getOperation()) {
                case ADD -> removeServerStage(ASetUtils.getOnlyElement(stages));
                case ADD_ALL, LOGIN -> removeServerStages(stages);
                case REMOVE -> addServerStage(ASetUtils.getOnlyElement(stages));
                case REMOVE_ALL -> addServerStages(stages);
            }
        }
    }

    // When saving, call this
    public static void markAsDirty() {
        AFileIOUtils.writeFileContent(getPermanentStagesFile(), CACHE);
    }

    public static List<String> getServerStagesFromData(MinecraftServer server) {
        return ServerStageWrapper.getStages(server);
    }
}
