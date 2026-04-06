package com.alessandro.astages.infrastructure.plugin;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.engine.PluginManager;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.Modifier;

@NotNullMethodsReturn
public class PluginScanner {
    public static void getAllPlugins() {
        AStages.LOGGER.info("AStages Plugin search started!");
        var type = Type.getType(AStagesPlugin.class);

        for (ModFileScanData scanData : ModList.get().getAllScanData()) {
            for (ModFileScanData.ClassData classData : scanData.getClasses()) {
                if (classData.interfaces().contains(type)) {
                    try {
                        // Get class full name
                        String className = Type.getObjectType(classData.clazz().getInternalName()).getClassName();

                        // Load class
                        Class<?> clazz = Class.forName(className);

                        // Check if implement the class/interface (exclude the class/interface itself)
                        if (AStagesPlugin.class.isAssignableFrom(clazz) && clazz != AStagesPlugin.class) {
                            if (!Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
                                AStagesPlugin plugin = (AStagesPlugin) clazz.getDeclaredConstructor().newInstance();
                                AStages.LOGGER.info("[PluginScanner] Registered plugin: {}", plugin.id());
                                PluginManager.PLUGINS.add(plugin);
                            }
                        }
                    } catch (Exception e) {
                        AStages.LOGGER.warn(e.getLocalizedMessage());
                    }
                }
            }
        }

        AStages.LOGGER.info("[PluginScanner] Search end! Found {} plugins!", PluginManager.PLUGINS.size());
    }
}
