package com.alessandro.astages.infrastructure.manager;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.manager.AManagerContainer;
import com.alessandro.astages.api.manager.ClientManagerInstance;
import com.alessandro.astages.engine.client.ClientRestrictionRegistry;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ClientManagerScanner {
    public static void getAllClientManagers() {
        AStages.LOGGER.info("[ManagerScanner] Search started!");
        var type = Type.getType(AManagerContainer.class);

        for (ModFileScanData scanData : ModList.get().getAllScanData()) {
            for (ModFileScanData.ClassData classData : scanData.getClasses()) {
                if (classData.interfaces().contains(type)) {
                    try {
                        // Get class full name
                        String className = Type.getObjectType(classData.clazz().getInternalName()).getClassName();

                        // Load class
                        Class<?> clazz = Class.forName(className);
                        for (Field field : clazz.getDeclaredFields()) {
                            if (!field.isAnnotationPresent(ClientManagerInstance.class)) continue;

                            if (!Modifier.isStatic(field.getModifiers())) {
                                throw new IllegalStateException("@ClientManagerInstance must be static: " + field);
                            }

                            try {
                                Object instance = field.get(null);
                                if (instance instanceof AClientMinimalManager<?, ?> manager) {
                                    ClientRestrictionRegistry.register(manager.associatedType(), manager);
                                    AStages.LOGGER.info("[ManagerScanner] Registered manager: {}", manager.associatedType());
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
        }

        AStages.LOGGER.info("[ManagerScanner] Search end! Found {} managers!", ClientRestrictionRegistry.getRegisteredManagers().size());
    }
}