package com.alessandro.astages.api.foldersystem;

import java.util.Objects;

public class ADirectoryResource {
    private final String dirResource;

    public ADirectoryResource(String dirResource) {
        this.dirResource = dirResource;
    }

    public String getDir() {
        return dirResource;
    }

    @Override
    public String toString() {
        return dirResource;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ADirectoryResource that)) return false;

        return Objects.equals(dirResource, that.dirResource);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dirResource);
    }
}
