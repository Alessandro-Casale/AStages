package com.alessandro.astages.infrastructure.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.event.player.*;
import com.alessandro.astages.api.foldersystem.AFolderPaths;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.util.AFileIOUtils;
import com.alessandro.astages.api.util.ASetUtils;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.api.util.ATitleUtils;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.stages.SyncPlayerStagesS2C;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.nio.file.Path;
import java.util.*;

@NotNullParamsAndMethodsReturn
@EventBusSubscriber(modid = AStages.MODID)
public class OfflinePlayerStage {
    public static final Map<UUID, Set<String>> CACHE = new HashMap<>();

    // Try using a Google BiMap
    public static Map<UUID, String> UUID_USERNAME;
    public static Map<String, UUID> USERNAME_UUID;
    // public static BiMap<String, UUID> USERNAME_UUID = HashBiMap.create();

    @SubscribeEvent
    public static void serverStarted(ServerStartingEvent event) {
        UUID_USERNAME = AFileIOUtils.readMapOrDefault(getConfigFile("uuid_to_username"), UUID.class, String.class);
        USERNAME_UUID = AFileIOUtils.readMapOrDefault(getConfigFile("username_to_uuid"), String.class, UUID.class);
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppingEvent event) {
        AFileIOUtils.writeFileContent(getConfigFile("uuid_to_username"), UUID_USERNAME);
        AFileIOUtils.writeFileContent(getConfigFile("username_to_uuid"), USERNAME_UUID);
    }

    @Info("Migration purpose only!")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var playerUUID = event.getEntity().getUUID();
        var playerName = event.getEntity().getGameProfile().getName();
        UUID_USERNAME.put(playerUUID, playerName);
        USERNAME_UUID.put(playerName, playerUUID);

        var file = getPermanentStagesFile(event.getEntity());
        var stageList = AFileIOUtils.readList(file, String.class);

        if (stageList == null) {
            var oldList = getPlayerStagesFromCapability(event.getEntity());
            AFileIOUtils.writeFileContent(file, oldList);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        var player = event.getEntity();
        markAsDirty(player);
        CACHE.remove(player.getUUID()); // Clear CACHE
    }

    private static Path getConfigFile(String fileName) {
        var file = AFolderPaths.getAStagesDataFolder().resolve(fileName + ".json");
        return AFileIOUtils.getOrCreateFile(file);
    }

    private static Path getPermanentStagesFile(Player player) {
        return getPermanentStagesFile(player.getUUID());
    }

    private static Path getPermanentStagesFile(UUID uuid) {
        var file = AFolderPaths.getPlayerPermanentFolder().resolve(uuid + ".json");
        return AFileIOUtils.getOrCreateFile(file);
    }

    public static Path getTemporaryStagesFile(Player player) {
        return getTemporaryStagesFile(player.getUUID());
    }

    public static Path getTemporaryStagesFile(UUID uuid) {
        var file = AFolderPaths.getPlayerTemporaryFolder().resolve(uuid + ".json");
        return AFileIOUtils.getOrCreateFile(file);
    }

    public static Set<String> getPlayerStagesFromCache(Player player) {
        return getPlayerStagesFromCache(player.getUUID());
    }

    public static Set<String> getPlayerStagesFromCache(UUID uuid) {
        if (!CACHE.containsKey(uuid)) {
            var stages = AFileIOUtils.readHashSetOrDefault(getPermanentStagesFile(uuid), String.class);
            CACHE.put(uuid, stages);
        }

        return CACHE.get(uuid);
    }

    public static void addPlayerStage(Player player, String stage) {
        addPlayerStage(player.getUUID(), stage);
    }

    public static void addPlayerStage(UUID uuid, String stage) {
        CACHE.computeIfAbsent(uuid, k -> new HashSet<>()).add(stage);
    }

    public static void addPlayerStages(Player player, Set<String> stages) {
        addPlayerStages(player.getUUID(), stages);
    }

    public static void addPlayerStages(UUID uuid, Set<String> stages) {
        CACHE.computeIfAbsent(uuid, k -> new HashSet<>()).addAll(stages);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static AStatus removePlayerStage(Player player, String stage) {
        return removePlayerStage(player.getUUID(), stage);
    }

    public static AStatus removePlayerStage(UUID uuid, String stage) {
        return CACHE.computeIfAbsent(uuid, k -> new HashSet<>()).remove(stage) ? AStatus.SUCCESS : AStatus.NOT_PRESENT;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static AStatus removePlayerStages(Player player, Set<String> stages) {
        return removePlayerStages(player.getUUID(), stages);
    }

    public static AStatus removePlayerStages(UUID uuid, Set<String> stages) {
        return CACHE.computeIfAbsent(uuid, k -> new HashSet<>()).removeAll(stages) ? AStatus.SUCCESS : AStatus.NOT_PRESENT;
    }

    @Info("Synchronization is required only if the player is 'physically' in the server!")
    public static void synchronizeWithClient(UUID uuid, AOperation operation, String stage, boolean silentTitle) {
        var player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        if (player != null) { synchronizeWithClient(player, operation, stage, silentTitle); }
    }

    public static void synchronizeWithClient(Player player, AOperation operation, String stage, boolean silentTitle) {
        synchronizeWithClient(player, operation, ASetUtils.singleton(stage), silentTitle);
    }

    @Info("Synchronization is required only if the player is 'physically' in the server!")
    public static void synchronizeWithClient(UUID uuid, AOperation operation, Set<String> stages, boolean silentTitle) {
        var player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        if (player != null) { synchronizeWithClient(player, operation, stages, silentTitle); }
    }

    public static void synchronizeWithClient(Player player, AOperation operation, Set<String> stages, boolean silentTitle) {
        AStagesUtils.checkPlayerStages(player, operation, stages);

        var event = new StageSyncedPlayerEvent(player, operation, stages);
        ALoader.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            Networking.sendToPlayer((ServerPlayer) player, new SyncPlayerStagesS2C(stages, operation));

            if (!silentTitle) {
                if (player instanceof ServerPlayer serverPlayer) {
                    stages.forEach(stage -> ATitleUtils.showTitles(serverPlayer, operation, stage));
                }
            }

            switch (operation) {
                case ADD -> ALoader.EVENT_BUS.post(new StageAddedPlayerEvent(player, ASetUtils.getOnlyElement(stages)));
                case ADD_ALL -> ALoader.EVENT_BUS.post(new AllStagesAddedPlayerEvent(player, stages));
                case REMOVE -> ALoader.EVENT_BUS.post(new StageRemovedPlayerEvent(player, ASetUtils.getOnlyElement(stages)));
                case REMOVE_ALL -> ALoader.EVENT_BUS.post(new AllStagesRemovedPlayerEvent(player, stages));
                case LOGIN -> ALoader.EVENT_BUS.post(new StageLoginPlayerEvent(player, stages));
            }
        } else {
            switch (event.getOperation()) {
                case ADD -> removePlayerStage(player, ASetUtils.getOnlyElement(stages));
                case ADD_ALL, LOGIN -> removePlayerStages(player, stages);
                case REMOVE -> addPlayerStage(player, ASetUtils.getOnlyElement(stages));
                case REMOVE_ALL -> addPlayerStages(player, stages);
            }
        }
    }

    // When saving, call this
    public static void markAsDirty(Player player) {
        var stages = CACHE.get(player.getUUID());
        AFileIOUtils.writeFileContent(getPermanentStagesFile(player), stages);
    }

    public static List<String> getPlayerStagesFromCapability(Player player) {
        return PlayerStageWrapper.getStages(player);
    }
}
