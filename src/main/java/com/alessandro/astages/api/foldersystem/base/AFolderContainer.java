package com.alessandro.astages.api.foldersystem.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AFolderContainer {
    private AFolder root;
    public final Map<String, AFolder> foldersWithRoot = new HashMap<>();
    public final Map<String, AFolder> foldersWithoutRoot = new HashMap<>();
    public final Map<AFolder, String> folderToStringPathWithRoot = new HashMap<>();
    public final Map<AFolder, String> folderToStringPathWithoutRoot = new HashMap<>();

    public void setRoot(AFolder root) {
        this.root = root;
    }

    public AFolder getRoot() {
        return root;
    }

    public void addFolderWithRoot(String path, AFolder folder) {
        foldersWithRoot.put(path, folder);
        folderToStringPathWithRoot.put(folder, path);
    }

    public void addFolderWithoutRoot(String path, AFolder folder) {
        foldersWithoutRoot.put(path, folder);
        folderToStringPathWithoutRoot.put(folder, path);
    }

    public Collection<AFolder> getFoldersWithRoot() {
        return foldersWithRoot.values();
    }

    public Collection<AFolder> getFoldersWithoutRoot() {
        return foldersWithoutRoot.values();
    }

    public String getStringPathWithRoot(AFolder folder) {
        return folderToStringPathWithRoot.get(folder);
    }

    public String getStringPathWithoutRoot(AFolder folder) {
        return folderToStringPathWithoutRoot.get(folder);
    }
}
