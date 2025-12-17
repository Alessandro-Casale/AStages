package com.alessandro.astages.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class APluginManager {
    public static final List<AStagesPlugin> PLUGINS = new ArrayList<>();

    public static void callMethod(Consumer<AStagesPlugin> method) {
        PLUGINS.forEach(method);
    }

    public static <T> void callMethod(T parameter, BiConsumer<AStagesPlugin, T> method) {
        for (var plugin : PLUGINS) {
            method.accept(plugin, parameter);
        }
    }
}
