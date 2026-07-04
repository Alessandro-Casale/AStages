package com.alessandro.astages.infrastructure.folder;

import com.alessandro.astages.api.config.SimpleLocation;
import com.alessandro.astages.api.util.AFileIOUtils;
import com.alessandro.astages.api.util.ARegistryUtils;
import com.alessandro.astages.api.foldersystem.AFolderPaths;
import com.alessandro.astages.api.foldersystem.AFolderResources;
import com.alessandro.astages.api.foldersystem.base.ADirectoryResource;
import com.alessandro.astages.api.foldersystem.base.AFolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.plugin.container.FolderContainer;
import com.alessandro.astages.engine.PluginManager;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import com.alessandro.astages.infrastructure.registry.AStagesRegistries;
import net.minecraft.server.MinecraftServer;

@NotNullParamsAndMethodsReturn
public class AStagesFolderSystem {
    public static void buildServerPaths(MinecraftServer server) {
        var root = AFolder.root(AFolderResources.ASTAGES_DATA_DIR, AFolderPaths.referenceForAStagesDataFolder());

        root
            .subFolder(AFolderResources.SERVER_DATA_DIR, null, serverDir -> {
                serverDir.subFolder(AFolderResources.PERMANENT_STAGES_DIR, AFolderPaths.referenceForServerPermanentFolder());
                serverDir.subFolder(AFolderResources.TEMPORARY_STAGES_DIR, AFolderPaths.referenceForServerTemporaryFolder());
            })
            .subFolder(AFolderResources.PLAYER_DATA_DIR, null, playerDir -> {
                playerDir.subFolder(AFolderResources.PERMANENT_STAGES_DIR, AFolderPaths.referenceForPlayerPermanentFolder());
                playerDir.subFolder(AFolderResources.TEMPORARY_STAGES_DIR, AFolderPaths.referenceForPlayerTemporaryFolder());
            });

        if (AStagesCommon.SIMPLE_RESTRICTIONS_FOLDER.get() == SimpleLocation.SERVER_FOLDER) {
            attachSimpleRestrictionFolders(root);
        }

        root
            .buildAndPopulateMaps(container -> AFolder.getServerRootGenerator(container, server))
            .forEach(AFileIOUtils::createDirectory);

        var folderContainer = new FolderContainer();
        PluginManager.callMethod(folderContainer, AStagesPlugin::registerServerFolders);
        for (var folder : folderContainer.getFolders()) {
            folder
                .buildAndPopulateMaps(container -> AFolder.getServerRootGenerator(container, server))
                .forEach(AFileIOUtils::createDirectory);
        }
    }

    public static void buildConfigPaths() {
        var root = AFolder.root(AFolderResources.ASTAGES_DATA_DIR);

        if (AStagesCommon.SIMPLE_RESTRICTIONS_FOLDER.get() == SimpleLocation.CONFIG_FOLDER) {
            attachSimpleRestrictionFolders(root);
        }

        root
            .buildAndPopulateMaps(AFolder::getConfigRootGenerator)
            .forEach(AFileIOUtils::createDirectory);

        var folderContainer = new FolderContainer();
        PluginManager.callMethod(folderContainer, AStagesPlugin::registerConfigFolders);
        for (var folder : folderContainer.getFolders()) {
            folder
                .buildAndPopulateMaps(AFolder::getConfigRootGenerator)
                .forEach(AFileIOUtils::createDirectory);
        }
    }

    private static void attachSimpleRestrictionFolders(AFolder root) {
        root.subFolder(AFolderResources.SIMPLE_RESTRICTIONS_DIR, AFolderPaths.referenceForSimpleRestrictionsFolder(), simpleRestrictionsDir -> {
            for (var modId : ARegistryUtils.getAllUniqueKeys(AStagesRegistries.SIMPLE_RESTRICTION_TYPES)) {
                AFolderPaths.initializeReferenceForSimpleRestrictionsFolder(modId);
                simpleRestrictionsDir.subFolder(new ADirectoryResource(modId), AFolderPaths.referenceForSimpleRestrictionsFolderForMod(modId));
            }
        });
    }
}