package com.alessandro.astages.internal.experimental.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.manager.AManagerContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReloadableInstanceScanner {
    public static void getAllReloadableInstances() {
        AStages.LOGGER.info("[ReloadableInstanceScanner] Search started!");
        var type = Type.getType(AManagerContainer.class);

        for (ModFileScanData scanData : ModList.get().getAllScanData()) {
            for (ModFileScanData.AnnotationData annotationData : scanData.getAnnotations()) {
                if (!annotationData.annotationType().equals(type)) {
                    continue;
                }

                try {
                    // Get class full name
                    String className = Type.getObjectType(annotationData.clazz().getInternalName()).getClassName();

                    // Load class
                    Class<?> clazz = Class.forName(className);
                    for (Field field : clazz.getDeclaredFields()) {
                        if (!field.isAnnotationPresent(ReloadableInstance.class)) continue;

                        if (!Modifier.isStatic(field.getModifiers())) {
                            throw new IllegalStateException("@ReloadableInstance must be static: " + field);
                        }

                        try {
                            Object instance = field.get(null);
                            if (instance instanceof AReloadable reloadableInstance) {
                                ReloadRegistry.register(reloadableInstance);
                                AStages.LOGGER.info("[ReloadableInstanceScanner] Registered reloadable instance: {}", reloadableInstance);
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access field: " + field, e);
                        }
                    }
                } catch (Exception e) {
                    AStages.LOGGER.warn(e.getLocalizedMessage());
                }
            }
        }

        AStages.LOGGER.info("[ReloadableInstanceScanner] Search end! Found {} reloadable instances!", ReloadRegistry.getRegisteredReloadableInstances().size());
    }
}
