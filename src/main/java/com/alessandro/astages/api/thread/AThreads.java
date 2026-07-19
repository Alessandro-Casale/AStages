package com.alessandro.astages.api.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AThreads {
    private static final ExecutorService CACHE_EXECUTOR = Executors.newFixedThreadPool(
        Math.max(1, Runtime.getRuntime().availableProcessors() - 1),
        r -> {
            Thread t = new Thread(r, "AStages-RecipeViewer-Cache");
            t.setDaemon(true);
            return t;
        }
    );
}
