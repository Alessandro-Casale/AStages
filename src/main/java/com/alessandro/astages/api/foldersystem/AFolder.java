package com.alessandro.astages.api.foldersystem;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.misc.Ref;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@NotNullParamsAndMethodsReturn
public class AFolder {
    private final ADirectoryResource resource;
    private final boolean isRoot;
    private final Ref<Path> associatedVar;

    private final Map<ADirectoryResource, AFolder> subFolders = new HashMap<>();

    public AFolder(ADirectoryResource resource, boolean isRoot, @Nullable Ref<Path> associatedVar) {
        this.resource = resource;
        this.isRoot = isRoot;
        this.associatedVar = associatedVar;
    }

    public void subFolder(ADirectoryResource resource, boolean isRoot, @Nullable Ref<Path> associatedVar) {
        subFolders.computeIfAbsent(resource, key -> new AFolder(key, isRoot, associatedVar));
    }

    public AFolder subFolder(ADirectoryResource resource, boolean isRoot, @Nullable Ref<Path> associatedVar, Consumer<AFolder> builder) {
        var folder = subFolders.computeIfAbsent(resource, key -> new AFolder(key, isRoot, associatedVar));
        builder.accept(folder);
        return this;
    }

    public String getFolderRepresentation(boolean includeRoot) {
        return !includeRoot && isRoot ? "" : resource.getDir();
    }

    public LevelResource asLevelResource() {
        return new LevelResource(resource.getDir());
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

    @Info("Must be checked!")
    public List<Path> buildAndPopulateMaps(MinecraftServer server) {
        var container = build();

        var toReturn = new ArrayList<Path>();
        var parent = server.getWorldPath(container.getRoot().asLevelResource());

        container.foldersWithoutRoot.forEach((incompletePath, folder) -> {
            var completePath = parent.resolve(incompletePath);
            toReturn.add(completePath);

            if (folder.associatedVar != null) {
                folder.associatedVar.setValue(completePath);
            }
        });
        return toReturn;
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
