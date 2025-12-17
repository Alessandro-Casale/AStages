package com.alessandro.astages.api;

import com.alessandro.astages.api.develop.NotYetImplemented;
import com.alessandro.astages.api.foldersystem.ADirectoryResource;
import com.alessandro.astages.api.foldersystem.AFolder;
import com.alessandro.astages.api.misc.Ref;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.plugin.APluginManager;
import com.alessandro.astages.plugin.AStagesPlugin;
import com.alessandro.astages.plugin.ForPlugins;
import com.alessandro.astages.plugin.container.FolderContainer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@NotNullParamsAndMethodsReturn
public class AStagesFolderSystem {
    @NotYetImplemented @SuppressWarnings("unused") @ForPlugins public static Map<String, File> FOLDER_FILES = new HashMap<>();
    @NotYetImplemented @SuppressWarnings("unused") @ForPlugins public static Map<String, LevelResource> FOLDER_RESOURCES = new HashMap<>();

    public static final ADirectoryResource ASTAGES_DATA_DIR = new ADirectoryResource("astagesdata");
    public static final ADirectoryResource SERVER_DATA_DIR = new ADirectoryResource("server");
    public static final ADirectoryResource PLAYER_DATA_DIR = new ADirectoryResource("player");
    public static final ADirectoryResource PERMANENT_STAGES_DIR = new ADirectoryResource("permanent");
    public static final ADirectoryResource TEMPORARY_STAGES_DIR = new ADirectoryResource("temporary");

    private static final Ref<Path> astagesDataFolder = new Ref<>();
    private static final Ref<Path> serverPermanentFolder = new Ref<>();
    private static final Ref<Path> playerPermanentFolder = new Ref<>();
    private static final Ref<Path> serverTemporaryFolder = new Ref<>();
    private static final Ref<Path> playerTemporaryFolder = new Ref<>();

    public static void buildPaths(MinecraftServer server) {
        var root = new AFolder(ASTAGES_DATA_DIR, true, astagesDataFolder);

        root
            .subFolder(SERVER_DATA_DIR, false, null, serverDir -> {
                serverDir.subFolder(PERMANENT_STAGES_DIR, false, serverPermanentFolder);
                serverDir.subFolder(TEMPORARY_STAGES_DIR, false, serverTemporaryFolder);
            })
            .subFolder(PLAYER_DATA_DIR, false, null, playerDir -> {
                playerDir.subFolder(PERMANENT_STAGES_DIR, false, playerPermanentFolder);
                playerDir.subFolder(TEMPORARY_STAGES_DIR, false, playerTemporaryFolder);
            });

        root.buildAndPopulateMaps(server).forEach(AFileIOUtils::createDirectory);

        var folderContainer = new FolderContainer();
        APluginManager.callMethod(folderContainer, AStagesPlugin::registerFolders);
        for (var folder : folderContainer.getFolders()) {
            folder.buildAndPopulateMaps(server).forEach(AFileIOUtils::createDirectory);
        }
    }

    public static Path getAStagesDataFolder() {
        return astagesDataFolder.getValue();
    }

    public static Path getServerPermanentFolder() {
        return serverPermanentFolder.getValue();
    }

    public static Path getPlayerPermanentFolder() {
        return playerPermanentFolder.getValue();
    }

    public static Path getServerTemporaryFolder() {
        return serverTemporaryFolder.getValue();
    }

    public static Path getPlayerTemporaryFolder() {
        return playerTemporaryFolder.getValue();
    }
}
