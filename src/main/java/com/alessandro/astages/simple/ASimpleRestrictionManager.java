package com.alessandro.astages.simple;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.util.ReloadType;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NotNullParamsAndMethodsReturn
public class ASimpleRestrictionManager {
    public static Map<ASimpleRestrictionType, List<ASimpleRestriction>> RESTRICTIONS = null;
    private static int canBeReloadable = 0;

    //.setLenient()
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    public static final Type TYPE = new TypeToken<Map<ASimpleRestrictionType, List<ASimpleRestriction>>>(){}.getType();

    public static void addRestriction(ASimpleRestrictionType type, String id, String stage, String object) {
        var newList = RESTRICTIONS.getOrDefault(type, new ArrayList<>());
        var restriction = new ASimpleRestriction(id, stage, object);
        newList.add(restriction);
        RESTRICTIONS.put(type, newList);

        synchronizeWithServer(type, restriction);

        canBeReloadable++;

        if (canBeReloadable == 5) {
            writeToFile();
            canBeReloadable = 0;
        }
    }

    public static void synchronizeWithServer(ASimpleRestrictionType type, ASimpleRestriction restriction) {
        synchronizeWithServer(type, restriction, true);
    }
    public static void synchronizeWithServerNoUpdate(ASimpleRestrictionType type, ASimpleRestriction restriction) {
        synchronizeWithServer(type, restriction, false);
    }

    private static void synchronizeWithServer(ASimpleRestrictionType type, ASimpleRestriction restriction, boolean markAsDirty) {
        switch (type) {
            case ITEM -> ASimpleElaborator.elaborateItem(restriction, markAsDirty);
            case MOD -> ASimpleElaborator.elaborateMod(restriction, markAsDirty);
            case DIMENSION -> ASimpleElaborator.elaborateDimension(restriction);
            case GUI -> ASimpleElaborator.elaborateGui(restriction);
            case ORE -> ASimpleElaborator.elaborateOre(restriction, markAsDirty);
            case STRUCTURE -> ASimpleElaborator.elaborateStructure(restriction);
            case BIOME -> AStages.LOGGER.debug("NOT YET IMPLEMENTED!");
            case TAME -> ASimpleElaborator.elaborateTame(restriction);
            case MOUNT -> ASimpleElaborator.elaborateMount(restriction);
            case RECIPE -> ASimpleElaborator.elaborateRecipe(restriction);
            case ARMOR -> ASimpleElaborator.elaborateArmor(restriction, markAsDirty);
        }
    }

    public static void readFromFile() {
        if (RESTRICTIONS != null) {
            AStages.LOGGER.error("SKIPPED READING!");
            return;
        }

        AStages.LOGGER.debug("READING IN PROGRESS...");

        try (var fileReader = new FileReader(getConfigFileWithRestrictions())) {
            RESTRICTIONS = GSON.fromJson(fileReader, TYPE);

            if (RESTRICTIONS == null) {
                RESTRICTIONS = new HashMap<>();
            } else {
                for (var type : RESTRICTIONS.keySet()) {
                    for (var restriction : RESTRICTIONS.get(type)) {
                        synchronizeWithServer(type, restriction);
                    }
                }
            }
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }
    }

    public static void writeToFile() {
        AStages.LOGGER.debug("WRITING IN PROGRESS...");

        try (var fileWriter = new FileWriter(getConfigFileWithRestrictions())) {
            GSON.toJson(RESTRICTIONS, fileWriter);
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File getConfigFileWithRestrictions() {
        File saveDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "astages");

        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        File file = new File(saveDir, "simple_restrictions.json");
        try {
            file.createNewFile();
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }

        return file;
    }

    public static void removeRestriction(String id, ASimpleRestrictionType type) {
        ARestrictionManager.removeRestriction(id, type.convert());
        RESTRICTIONS.get(type).removeIf(restriction -> restriction.id.equals(id));
        ARestrictionManager.SIMPLE_IDS.remove(id);
        ARestrictionManager.reflectSimpleIdsChangesToClients(null, List.of(id.substring(7)), ASyncOperation.REMOVE);

        if (RESTRICTIONS.get(type).isEmpty()) {
            RESTRICTIONS.remove(type);
        }

        if (type == ASimpleRestrictionType.ORE) {
            ANetworking.sendToAllPlayers(new RequestReloadS2CPacket(ReloadType.ORE));
        }
    }

    public static void reloadBeforeScripts() { // BETTER BEFORE!
        for (var type : RESTRICTIONS.keySet()) {
            for (var restriction : RESTRICTIONS.get(type)) {
                synchronizeWithServerNoUpdate(type, restriction);
            }
        }
    }
}
