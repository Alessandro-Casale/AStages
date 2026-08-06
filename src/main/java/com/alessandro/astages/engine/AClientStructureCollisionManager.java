package com.alessandro.astages.engine;

import com.alessandro.astages.engine.client.collision.ClientStructureCollision;

public class AClientStructureCollisionManager {
    public static final ClientStructureCollision INSTANCE = new ClientStructureCollision();

    public static void onReloadStarted() {
        INSTANCE.clearCache();
    }
}