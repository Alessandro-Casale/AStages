package com.alessandro.astages.engine.collision;

public class StructureCollisionManager {
    public static final StructureCollision SERVER_INSTANCE = new StructureCollision();
    public static final ClientStructureCollision CLIENT_INSTANCE = new ClientStructureCollision();

    public static void reloadBeforeScripts() {
        SERVER_INSTANCE.clearCache();
        CLIENT_INSTANCE.clearCache();
    }
}
