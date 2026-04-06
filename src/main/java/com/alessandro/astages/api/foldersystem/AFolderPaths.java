package com.alessandro.astages.api.foldersystem;

import com.alessandro.astages.api.misc.Ref;
import com.alessandro.astages.api.nullability.Nullable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AFolderPaths {
    private static final Ref<Path> astagesDataFolder = new Ref<>();
    private static final Ref<Path> simpleRestrictionsFolder = new Ref<>();
    private static final Ref<Path> serverPermanentFolder = new Ref<>();
    private static final Ref<Path> playerPermanentFolder = new Ref<>();
    private static final Ref<Path> serverTemporaryFolder = new Ref<>();
    private static final Ref<Path> playerTemporaryFolder = new Ref<>();

    private static final Map<String, Ref<Path>> simpleFolders = new HashMap<>();

    public static Ref<Path> referenceForAStagesDataFolder() {
        return astagesDataFolder;
    }

    public static Ref<Path> referenceForSimpleRestrictionsFolder() {
        return simpleRestrictionsFolder;
    }

    public static Ref<Path> referenceForServerPermanentFolder() {
        return serverPermanentFolder;
    }

    public static Ref<Path> referenceForPlayerPermanentFolder() {
        return playerPermanentFolder;
    }

    public static Ref<Path> referenceForServerTemporaryFolder() {
        return serverTemporaryFolder;
    }

    public static Ref<Path> referenceForPlayerTemporaryFolder() {
        return playerTemporaryFolder;
    }

    public static Path getAStagesDataFolder() {
        return astagesDataFolder.getValue();
    }

    public static Path getSimpleRestrictionsFolder() {
        return simpleRestrictionsFolder.getValue();
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
        var ref = simpleFolders.get(modId);
        return ref != null ? ref.getValue() : null;
    }

    public static Ref<Path> referenceForSimpleRestrictionsFolderForMod(String modId) {
        return simpleFolders.get(modId);
    }

    public static void initializeReferenceForSimpleRestrictionsFolder(String modId) {
        simpleFolders.put(modId, new Ref<>());
    }
}