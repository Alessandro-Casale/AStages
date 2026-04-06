package com.alessandro.astages.engine;

import com.alessandro.astages.api.misc.TriConsumer;
import com.alessandro.astages.api.plugin.AStagesPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PluginManager {
    public static final List<AStagesPlugin> PLUGINS = new ArrayList<>();

    public static void callMethod(Consumer<AStagesPlugin> method) {
        PLUGINS.forEach(method);
    }

    public static <T> void callMethod(T parameter, BiConsumer<AStagesPlugin, T> method) {
        for (var plugin : PLUGINS) {
            method.accept(plugin, parameter);
        }
    }

    public static <T, S> void callMethod(T parameter1, S parameter2, TriConsumer<AStagesPlugin, T, S> method) {
        for (var plugin : PLUGINS) {
            method.accept(plugin, parameter1, parameter2);
        }
    }
}
