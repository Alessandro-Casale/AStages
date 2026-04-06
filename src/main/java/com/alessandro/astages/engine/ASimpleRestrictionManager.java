package com.alessandro.astages.engine;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.base.Elaborator;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.constant.ReloadType;
import com.alessandro.astages.api.exception.SimpleRestrictionsException;
import com.alessandro.astages.api.foldersystem.AFolderPaths;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.plugin.container.SimpleRestrictionsContainer;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.store.ASimpleRestrictionType;
import com.alessandro.astages.api.util.AFileIOUtils;
import com.alessandro.astages.engine.server.MiscStorage;
import com.alessandro.astages.engine.server.RestrictionSyncService;
import com.alessandro.astages.engine.simple.ASimpleCommands;
import com.alessandro.astages.engine.simple.ASimpleElaborator;
import com.alessandro.astages.engine.simple.ASimpleRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.engine.store.ASimpleRestrictionTypes;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestReloadS2C;
import com.alessandro.astages.infrastructure.registry.AStagesRegistries;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.*;
import java.util.function.BiConsumer;

@NotNullParamsAndMethodsReturn
public class ASimpleRestrictionManager {
    public static Map<ASimpleRestrictionType, List<ASimpleRestriction>> RESTRICTION_CACHE = null;
    public static Map<String, Set<ASimpleRestrictionType>> ID_TO_TYPE_CACHE = null;
    public static Map<ASimpleRestrictionType, Boolean> HAS_RESTRICTION_BEEN_MODIFIED = null;
    public static boolean IS_MIGRATION_HAPPENED = false;
    private static int canBeReloadable = 0;

    public static Map<ASimpleRestrictionType, ARestrictionType> CONVERSION_MAP = new HashMap<>();
    public static Map<ASimpleRestrictionType, Elaborator<ASimpleRestriction, Boolean>> ELABORATION_MAP = new HashMap<>();
    public static Map<ASimpleRestrictionType, Elaborator<String, ASimpleRestrictionType>> AFTER_REMOVE_ELABORATION_MAP = new HashMap<>();
    public static Map<ASimpleRestrictionType, BiConsumer<CommandBuildContext, ArgumentBuilder<CommandSourceStack, ?>>> COMMAND_MAP = new HashMap<>();

    static {
        CONVERSION_MAP.put(ASimpleRestrictionTypes.ITEM, ARestrictionTypes.ITEM);
        CONVERSION_MAP.put(ASimpleRestrictionTypes.MOD, ARestrictionTypes.ITEM);
        CONVERSION_MAP.put(ASimpleRestrictionTypes.ARMOR, ARestrictionTypes.ITEM);
        CONVERSION_MAP.put(ASimpleRestrictionTypes.DIMENSION, ARestrictionTypes.DIMENSION);
        CONVERSION_MAP.put(ASimpleRestrictionTypes.GUI, ARestrictionTypes.SCREEN);
        CONVERSION_MAP.put(ASimpleRestrictionTypes.ORE, ARestrictionTypes.ORE);
        CONVERSION_MAP.put(ASimpleRestrictionTypes.STRUCTURE, ARestrictionTypes.STRUCTURE);
        CONVERSION_MAP.put(ASimpleRestrictionTypes.TAME, ARestrictionTypes.PET);
        CONVERSION_MAP.put(ASimpleRestrictionTypes.MOUNT, ARestrictionTypes.PET);
        CONVERSION_MAP.put(ASimpleRestrictionTypes.RECIPE, ARestrictionTypes.RECIPE);

        ELABORATION_MAP.put(ASimpleRestrictionTypes.ITEM, ASimpleElaborator::elaborateItem);
        ELABORATION_MAP.put(ASimpleRestrictionTypes.MOD, ASimpleElaborator::elaborateMod);
        ELABORATION_MAP.put(ASimpleRestrictionTypes.DIMENSION, ASimpleElaborator::elaborateDimension);
        ELABORATION_MAP.put(ASimpleRestrictionTypes.GUI, ASimpleElaborator::elaborateGui);
        ELABORATION_MAP.put(ASimpleRestrictionTypes.ORE, ASimpleElaborator::elaborateOre);
        ELABORATION_MAP.put(ASimpleRestrictionTypes.STRUCTURE, ASimpleElaborator::elaborateStructure);
        ELABORATION_MAP.put(ASimpleRestrictionTypes.TAME, ASimpleElaborator::elaborateTame);
        ELABORATION_MAP.put(ASimpleRestrictionTypes.MOUNT, ASimpleElaborator::elaborateMount);
        ELABORATION_MAP.put(ASimpleRestrictionTypes.RECIPE, ASimpleElaborator::elaborateRecipe);
        ELABORATION_MAP.put(ASimpleRestrictionTypes.ARMOR, ASimpleElaborator::elaborateArmor);

        AFTER_REMOVE_ELABORATION_MAP.put(ASimpleRestrictionTypes.ORE,
            (restrictionId, type) -> Networking.sendToAllPlayers(new RequestReloadS2C(ReloadType.ORE)));

        COMMAND_MAP.put(ASimpleRestrictionTypes.ITEM, ASimpleCommands::item);
        COMMAND_MAP.put(ASimpleRestrictionTypes.MOD, ASimpleCommands::mod);
        COMMAND_MAP.put(ASimpleRestrictionTypes.DIMENSION, ASimpleCommands::dimension);
        COMMAND_MAP.put(ASimpleRestrictionTypes.GUI, ASimpleCommands::gui);
        COMMAND_MAP.put(ASimpleRestrictionTypes.ORE, ASimpleCommands::ore);
        COMMAND_MAP.put(ASimpleRestrictionTypes.STRUCTURE, ASimpleCommands::structure);
        COMMAND_MAP.put(ASimpleRestrictionTypes.TAME, ASimpleCommands::tame);
        COMMAND_MAP.put(ASimpleRestrictionTypes.MOUNT, ASimpleCommands::mount);
        COMMAND_MAP.put(ASimpleRestrictionTypes.RECIPE, ASimpleCommands::recipe);
        COMMAND_MAP.put(ASimpleRestrictionTypes.ARMOR, ASimpleCommands::armor);

        var simpleRestrictionsContainer = SimpleRestrictionsContainer.initialize();
        PluginManager.callMethod(simpleRestrictionsContainer, AStagesPlugin::registerSimpleRestriction);
        CONVERSION_MAP.putAll(simpleRestrictionsContainer.getTemporaryConversionMap());
        ELABORATION_MAP.putAll(simpleRestrictionsContainer.getTemporaryElaborationMap());
        AFTER_REMOVE_ELABORATION_MAP.putAll(simpleRestrictionsContainer.getTemporaryAfterRemoveElaborationMap());
        COMMAND_MAP.putAll(simpleRestrictionsContainer.getTemporaryCommandMap());
    }

    public static void addRestriction(ASimpleRestrictionType type, String id, String stage, String object) {
        var restriction = new ASimpleRestriction(id, stage, object);

        RESTRICTION_CACHE.computeIfAbsent(type, key -> new ArrayList<>()).add(restriction);
        ID_TO_TYPE_CACHE.computeIfAbsent(id, key -> new HashSet<>()).add(type);
        HAS_RESTRICTION_BEEN_MODIFIED.put(type, true);

        synchronizeWithServer(type, restriction);

        canBeReloadable++;
        if (canBeReloadable == AStagesCommon.SIMPLE_RESTRICTIONS_RELOADABLE.get()) {
            writeToFile(false);
            canBeReloadable = 0;
        }
    }

    public static void synchronizeWithServer(ASimpleRestrictionType type, Collection<ASimpleRestriction> restrictions) {
        restrictions.forEach(restriction -> synchronizeWithServer(type, restriction));
    }

    public static void synchronizeWithServerNoUpdate(ASimpleRestrictionType type, Collection<ASimpleRestriction> restrictions) {
        restrictions.forEach(restriction -> synchronizeWithServerNoUpdate(type, restriction));
    }

    public static void synchronizeWithServer(ASimpleRestrictionType type, ASimpleRestriction restriction) {
        synchronizeWithServer(type, restriction, true);
    }

    public static void synchronizeWithServerNoUpdate(ASimpleRestrictionType type, ASimpleRestriction restriction) {
        synchronizeWithServer(type, restriction, false);
    }

    private static void synchronizeWithServer(ASimpleRestrictionType type, ASimpleRestriction restriction, boolean markAsDirty) {
        ELABORATION_MAP.get(type).elaborate(restriction, markAsDirty);
    }

    public static void readFromFile() {
        if (RESTRICTION_CACHE != null && IS_MIGRATION_HAPPENED) {
            AStages.LOGGER.info("Simple Restrictions cache already built! Skipped file reading.");
            return;
        }

        RESTRICTION_CACHE = new HashMap<>();
        ID_TO_TYPE_CACHE = new HashMap<>();
        HAS_RESTRICTION_BEEN_MODIFIED = new HashMap<>();

        // Issue: doesn't check if the folder has a registry associated.
        // The implementation below this block of code checks only for actual existing registries.
//        var root = AStagesFolderSystem.getSimpleRestrictionsFolder().getValue();
//        Files.list(root).forEach(directory -> {
//            var modId = directory.getFileName().toString();
//
//            Files.list(directory).forEach(file -> {
//                var restrictionTypeIdentifier = file.getFileName().toString();
//
//                List<ASimpleRestriction> restrictions = AFileIOUtils.readList(file, ASimpleRestriction.class);
//
//                var restrictionType = AStagesRegistries.SIMPLE_RESTRICTION_TYPES.getValue(AResourceLocation.fromNamespaceAndPath(modId, restrictionTypeIdentifier));
//                RESTRICTION_CACHE.put(restrictionType, restrictions);
//                HAS_RESTRICTION_BEEN_MODIFIED.put(restrictionType, false);
//
//                synchronizeWithServer(restrictionType, restrictions);
//            });
//        });

        AStagesRegistries.SIMPLE_RESTRICTION_TYPES.entrySet()
            .forEach(entry -> {
                var restrictionIdentifier = entry.getKey().location();
                var modId = restrictionIdentifier.getNamespace();
                var restrictionTypeIdentifier = restrictionIdentifier.getPath();

                var file = AFolderPaths.getSimpleRestrictionsFolderForMod(modId)
                    .resolve(restrictionTypeIdentifier + AFileIOUtils.JSON_EXTENSION);

                if (AFileIOUtils.fileExists(file)) {
                    List<ASimpleRestriction> restrictions = AFileIOUtils.readList(file, ASimpleRestriction.class);

                    var restrictionType = entry.getValue();
                    RESTRICTION_CACHE.put(restrictionType, restrictions);
                    HAS_RESTRICTION_BEEN_MODIFIED.put(restrictionType, false);

                    for (var restriction : restrictions) {
                        ID_TO_TYPE_CACHE.computeIfAbsent(restriction.id, key -> new HashSet<>()).add(restrictionType);
                    }

                    synchronizeWithServer(restrictionType, restrictions);
                }
            });
    }

    public static void writeToFile(boolean clearCaches) {
        HAS_RESTRICTION_BEEN_MODIFIED.forEach((type, hasBeenModified) -> {
            if (hasBeenModified) {
                var restrictionsToStore = RESTRICTION_CACHE.getOrDefault(type, null);

                var identifier = AStagesRegistries.SIMPLE_RESTRICTION_TYPES.getKey(type);
                if (identifier == null) {
                    throw SimpleRestrictionsException.onWrite();
                }

                var modId = identifier.getNamespace();
                var restrictionTypeIdentifier = identifier.getPath();

                var file = AFolderPaths.getSimpleRestrictionsFolderForMod(modId)
                    .resolve(restrictionTypeIdentifier + AFileIOUtils.JSON_EXTENSION);

                if (restrictionsToStore == null) {
                    AFileIOUtils.deleteFile(file);

                    var parentDirectory = file.getParent();
                    if (AFileIOUtils.directoryHasNoFilesWithExtension(parentDirectory, AFileIOUtils.JSON_EXTENSION)) {
                        AFileIOUtils.deleteDirectory(parentDirectory);
                    }
                } else {
                    AFileIOUtils.writeFileContent(file, restrictionsToStore);
                }
            }
        });

        if (clearCaches) {
            RESTRICTION_CACHE = null;
            ID_TO_TYPE_CACHE = null;
            HAS_RESTRICTION_BEEN_MODIFIED = null;
        }
    }

    public static void removeRestriction(String id, ASimpleRestrictionType type) {
        ARestrictionManager.removeRestriction(id, CONVERSION_MAP.get(type));
        RESTRICTION_CACHE.get(type).removeIf(restriction -> restriction.id.equals(id));
        ID_TO_TYPE_CACHE.get(id).remove(type);
        MiscStorage.SIMPLE_IDS.remove(id);
        HAS_RESTRICTION_BEEN_MODIFIED.put(type, true);

        RestrictionSyncService.reflectSimpleIdsChangesToClients(null, List.of(id.substring(7)), ASyncOperation.REMOVE);

        if (RESTRICTION_CACHE.get(type).isEmpty()) {
            RESTRICTION_CACHE.remove(type);
        }

        if (AFTER_REMOVE_ELABORATION_MAP.containsKey(type)) {
            AFTER_REMOVE_ELABORATION_MAP.get(type).elaborate(id, type);
        }
    }

    public static void reloadBeforeScripts() { // BETTER BEFORE!
        if (ServerLifecycleHooks.getCurrentServer() == null) { return; }

        for (var type : RESTRICTION_CACHE.keySet()) {
            synchronizeWithServerNoUpdate(type, RESTRICTION_CACHE.get(type));
        }
    }

    public static Set<ASimpleRestrictionType> getAssociatedTypes(String id) {
        AStages.LOGGER.debug(ID_TO_TYPE_CACHE.toString());
        return ID_TO_TYPE_CACHE.getOrDefault(id, new HashSet<>());
    }
}