package com.alessandro.astages.plugin;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.Modifier;

@NotNullMethodsReturn
public class APluginFinder {
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
                                AStages.LOGGER.info("AStages Plugin found: {}", plugin.id());
                                APluginManager.PLUGINS.add(plugin);
                            }
                        }
                    } catch (Exception e) {
                        AStages.LOGGER.warn(e.getLocalizedMessage());
                    }
                }
            }
        }

        AStages.LOGGER.info("AStages Plugin search end! Found {} plugins!", APluginManager.PLUGINS.size());
    }
}
