package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import com.alessandro.astages.util.ARestrictionType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ARestrictionManager {
    public static final AItemManager ITEM_INSTANCE = new AItemManager();
    public static final ADimensionManager DIMENSION_INSTANCE = new ADimensionManager();
    public static final AMobManager MOB_INSTANCE = new AMobManager();
    public static final ATimeManager TIME_INSTANCE = new ATimeManager();

    public static final Set<String> ALL_STAGES = new HashSet<>();

    public static ARestriction getRestrictionById(@NotNull ARestrictionType type, String id) {
        return switch (type) {
            case ITEM -> ITEM_INSTANCE.getRestriction(id);
            case MOB -> MOB_INSTANCE.getRestriction(id);
            case DIMENSION -> DIMENSION_INSTANCE.getRestriction(id);
            case TIME -> TIME_INSTANCE.getRestriction(id);
        };
    }
}
