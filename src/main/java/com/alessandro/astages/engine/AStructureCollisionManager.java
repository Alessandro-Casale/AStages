package com.alessandro.astages.engine;

import com.alessandro.astages.engine.server.collision.StructureCollision;

public class AStructureCollisionManager {
    public static final StructureCollision INSTANCE = new StructureCollision();

    public static void onReloadStarted() {
        INSTANCE.clearCache();
    }
}