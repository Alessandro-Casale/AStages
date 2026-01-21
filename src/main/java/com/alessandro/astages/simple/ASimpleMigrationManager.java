package com.alessandro.astages.simple;

import com.alessandro.astages.api.AFileIOUtils;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.registry.AStagesRegistries;
import com.google.common.reflect.TypeToken;
import net.neoforged.fml.loading.FMLPaths;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ASimpleMigrationManager {
    private static final Path PATH = FMLPaths.CONFIGDIR.get().resolve("astages/simple_restrictions.json");
    public static final Type TYPE = new TypeToken<Map<String, List<ASimpleRestriction>>>(){}.getType();

    public static void startMigration() {
        if (!AFileIOUtils.fileExists(PATH)) { return; }

        Map<String, List<ASimpleRestriction>> rawRestrictions = AFileIOUtils.readFileContent(PATH, TYPE);
        ASimpleRestrictionManager.RESTRICTION_CACHE = new HashMap<>();
        ASimpleRestrictionManager.ID_TO_TYPE_CACHE = new HashMap<>();
        ASimpleRestrictionManager.HAS_RESTRICTION_BEEN_MODIFIED = new HashMap<>();
        if (rawRestrictions != null && !rawRestrictions.isEmpty()) {
            rawRestrictions.forEach((typeIdUppercased, restrictions) -> {
                var type = AStagesRegistries.SIMPLE_RESTRICTION_TYPES.get(AResourceLocation.fromNamespaceAndPath(typeIdUppercased.toLowerCase()));

                if (type != null) {
                    ASimpleRestrictionManager.RESTRICTION_CACHE.put(type, restrictions);

                    restrictions.forEach(restriction -> ASimpleRestrictionManager.ID_TO_TYPE_CACHE
                        .computeIfAbsent(restriction.id, key -> new HashSet<>())
                        .add(type)
                    );

                    ASimpleRestrictionManager.HAS_RESTRICTION_BEEN_MODIFIED.put(type, true);

                    ASimpleRestrictionManager.synchronizeWithServer(type, restrictions);
                }
            });
        }

        AFileIOUtils.deleteFile(PATH);
        ASimpleRestrictionManager.IS_MIGRATION_HAPPENED = true;
    }
}