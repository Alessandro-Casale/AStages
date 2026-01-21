package com.alessandro.astages.api;

import com.alessandro.astages.api.foldersystem.ADirectoryResource;
import com.alessandro.astages.api.foldersystem.AFolder;
import com.alessandro.astages.api.misc.Ref;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.plugin.APluginManager;
import com.alessandro.astages.plugin.AStagesPlugin;
import com.alessandro.astages.plugin.container.FolderContainer;
import com.alessandro.astages.registry.AStagesRegistries;
import com.alessandro.astages.simple.ASimpleLocation;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@NotNullParamsAndMethodsReturn
public class AStagesFolderSystem {
    public static final ADirectoryResource ASTAGES_DATA_DIR = new ADirectoryResource("astages");
    public static final ADirectoryResource SIMPLE_RESTRICTIONS_DIR = new ADirectoryResource("simple");
    public static final ADirectoryResource SERVER_DATA_DIR = new ADirectoryResource("server");
    public static final ADirectoryResource PLAYER_DATA_DIR = new ADirectoryResource("player");
    public static final ADirectoryResource PERMANENT_STAGES_DIR = new ADirectoryResource("permanent");
    public static final ADirectoryResource TEMPORARY_STAGES_DIR = new ADirectoryResource("temporary");

    private static final Ref<Path> astagesDataFolder = new Ref<>();
    private static final Ref<Path> simpleRestrictionsFolder = new Ref<>();
    private static final Ref<Path> serverPermanentFolder = new Ref<>();
    private static final Ref<Path> playerPermanentFolder = new Ref<>();
    private static final Ref<Path> serverTemporaryFolder = new Ref<>();
    private static final Ref<Path> playerTemporaryFolder = new Ref<>();

    private static final Map<String, Ref<Path>> simpleFolders = new HashMap<>();

    public static void buildServerPaths(MinecraftServer server) {
        var root = AFolder.root(ASTAGES_DATA_DIR, astagesDataFolder);

        root
            .subFolder(SERVER_DATA_DIR, null, serverDir -> {
                serverDir.subFolder(PERMANENT_STAGES_DIR, serverPermanentFolder);
                serverDir.subFolder(TEMPORARY_STAGES_DIR, serverTemporaryFolder);
            })
            .subFolder(PLAYER_DATA_DIR, null, playerDir -> {
                playerDir.subFolder(PERMANENT_STAGES_DIR, playerPermanentFolder);
                playerDir.subFolder(TEMPORARY_STAGES_DIR, playerTemporaryFolder);
            });

        if (AStagesCommon.SIMPLE_RESTRICTIONS_FOLDER.get() == ASimpleLocation.SERVER_FOLDER) {
            attachSimpleRestrictionFolders(root);
        }

        root
            .buildAndPopulateMaps(container -> AFolder.getServerRootGenerator(container, server))
            .forEach(AFileIOUtils::createDirectory);

        var folderContainer = new FolderContainer();
        APluginManager.callMethod(folderContainer, AStagesPlugin::registerServerFolders);
        for (var folder : folderContainer.getFolders()) {
            folder
                .buildAndPopulateMaps(container -> AFolder.getServerRootGenerator(container, server))
                .forEach(AFileIOUtils::createDirectory);
        }
    }

    public static void buildConfigPaths() {
        var root = AFolder.root(ASTAGES_DATA_DIR);

        if (AStagesCommon.SIMPLE_RESTRICTIONS_FOLDER.get() == ASimpleLocation.CONFIG_FOLDER) {
            attachSimpleRestrictionFolders(root);
        }

        root
            .buildAndPopulateMaps(AFolder::getConfigRootGenerator)
            .forEach(AFileIOUtils::createDirectory);

        var folderContainer = new FolderContainer();
        APluginManager.callMethod(folderContainer, AStagesPlugin::registerConfigFolders);
        for (var folder : folderContainer.getFolders()) {
            folder
                .buildAndPopulateMaps(AFolder::getConfigRootGenerator)
                .forEach(AFileIOUtils::createDirectory);
        }
    }

    private static void attachSimpleRestrictionFolders(AFolder root) {
        root.subFolder(SIMPLE_RESTRICTIONS_DIR, simpleRestrictionsFolder, simpleRestrictionsDir -> {
            for (var modId : ARegistryUtils.getAllUniqueKeys(AStagesRegistries.SIMPLE_RESTRICTION_TYPES)) {
                simpleFolders.put(modId, new Ref<>());
                simpleRestrictionsDir.subFolder(new ADirectoryResource(modId), simpleFolders.get(modId));
            }
        });
    }

    public static Path getAStagesDataFolder() {
        return astagesDataFolder.getValue();
    }

    public static Ref<Path> getSimpleRestrictionsFolder() {
        return simpleRestrictionsFolder;
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

    public static @Nullable Path getSimpleRestrictionsFolderForMod(String modId) {
        return simpleFolders.getOrDefault(modId, null).getValue();
    }
}