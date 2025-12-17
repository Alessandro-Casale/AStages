package com.alessandro.astages.plugin.container;

import com.alessandro.astages.api.foldersystem.AFolder;

import java.util.ArrayList;
import java.util.List;

public class FolderContainer {
    private final List<AFolder> folders = new ArrayList<>();

    public void register(AFolder folder) {
        folders.add(folder);
    }

    public List<AFolder> getFolders() {
        return folders;
    }
}
