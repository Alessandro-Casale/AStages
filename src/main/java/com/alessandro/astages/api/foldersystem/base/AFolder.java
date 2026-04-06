package com.alessandro.astages.api.foldersystem.base;

import com.alessandro.astages.api.misc.Ref;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@NotNullParamsAndMethodsReturn
public class AFolder {
    private final ADirectoryResource resource;
    private final boolean isRoot;
    private final Ref<Path> associatedVar;

    private final Map<ADirectoryResource, AFolder> subFolders = new HashMap<>();

    private AFolder(ADirectoryResource resource, boolean isRoot, @Nullable Ref<Path> associatedVar) {
        this.resource = resource;
        this.isRoot = isRoot;
        this.associatedVar = associatedVar;
    }

    public static AFolder root(ADirectoryResource resource) {
        return new AFolder(resource, true, null);
    }

    public static AFolder root(ADirectoryResource resource, Ref<Path> associatedVar) {
        return new AFolder(resource, true, associatedVar);
    }

    public void subFolder(ADirectoryResource resource, @Nullable Ref<Path> associatedVar) {
        subFolders.computeIfAbsent(resource, key -> new AFolder(key, false, associatedVar));
    }

    public AFolder subFolder(ADirectoryResource resource, @Nullable Ref<Path> associatedVar, Consumer<AFolder> builder) {
        var folder = subFolders.computeIfAbsent(resource, key -> new AFolder(key, false, associatedVar));
        builder.accept(folder);
        return this;
    }

    public String getFolderRepresentation(boolean includeRoot) {
        return !includeRoot && isRoot ? "" : resource.getDir();
    }

    public LevelResource asLevelResource() {
        return new LevelResource(resource.getDir());
    }

    public String getFolderName() {
        return resource.getDir();
    }

    public AFolderContainer build() {
        var toReturn = new AFolderContainer();
        buildSubFolders(this, toReturn, "", "");
        return toReturn;
    }

    public static void buildSubFolders(AFolder current, AFolderContainer container, String prefixWithRoot, String prefixWithoutRoot) {
        if (current.isRoot) { container.setRoot(current); }

        var pathWithRoot = prefixWithRoot.isEmpty() ? current.getFolderRepresentation(true) : prefixWithRoot + File.separatorChar + current.getFolderRepresentation(true);
        var pathWithoutRoot = prefixWithoutRoot.isEmpty() ? current.getFolderRepresentation(false) : prefixWithoutRoot + File.separatorChar + current.getFolderRepresentation(false);

        container.addFolderWithRoot(pathWithRoot, current);
        container.addFolderWithoutRoot(pathWithoutRoot, current);

        for (var folder : current.subFolders.values()) {
            buildSubFolders(folder, container, pathWithRoot, pathWithoutRoot);
        }
    }

    public List<Path> buildAndPopulateMaps(Function<AFolderContainer, Path> parentFunction) {
        var container = build();

        var toReturn = new ArrayList<Path>();
        var parent = parentFunction.apply(container);

        container.foldersWithoutRoot.forEach((incompletePath, folder) -> {
            var completePath = parent.resolve(incompletePath);
            toReturn.add(completePath);

            if (folder.associatedVar != null) {
                folder.associatedVar.setValue(completePath);
            }
        });
        return toReturn;
    }

    public static Path getConfigRootGenerator(AFolderContainer container) {
        return FMLPaths.CONFIGDIR.get().resolve(container.getRoot().getFolderName());
    }

    public static Path getServerRootGenerator(AFolderContainer container, MinecraftServer server) {
        return server.getWorldPath(container.getRoot().asLevelResource());
    }

    @Override
    public final boolean equals(Object obj) {
        if (!(obj instanceof AFolder that)) return false;

        return isRoot == that.isRoot && resource.equals(that.resource) /*&& Objects.equals(associatedVar, that.associatedVar)*/ && subFolders.equals(that.subFolders);
    }

    @Override
    public int hashCode() {
        int result = resource.hashCode();
        result = 31 * result + Boolean.hashCode(isRoot);
        // result = 31 * result + Objects.hashCode(associatedVar);
        result = 31 * result + subFolders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AFolder{" +
            "resource=" + resource +
            ", isRoot=" + isRoot +
            ", associatedVar=" + associatedVar +
            ", subFolders=" + subFolders +
            '}';
    }
}