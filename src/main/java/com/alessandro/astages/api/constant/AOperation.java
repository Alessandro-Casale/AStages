package com.alessandro.astages.api.constant;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum AOperation {
    ADD(true, true, true, 0),
    ADD_ALL(true, false, true, 1),
    REMOVE(false, true, false, 2),
    REMOVE_ALL(false, false, false, 3),
    LOGIN(false, false, false, 4);

    private final boolean needToBeChecked;
    private final boolean supportOnlyOneStage;
    private final boolean handleStageRecognization;

    AOperation(boolean needToBeChecked, boolean supportOnlyOneStage, boolean handleStageRecognization, int id) {
        this.needToBeChecked = needToBeChecked;
        this.supportOnlyOneStage = supportOnlyOneStage;
        this.handleStageRecognization = handleStageRecognization;
        this.id = id;
    }

    public boolean needToBeChecked() {
        return needToBeChecked;
    }

    public boolean supportOnlyOneStage() {
        return supportOnlyOneStage;
    }

    public boolean handleStageRecognization() {
        return handleStageRecognization;
    }

    // NeoForge Part
    public static final IntFunction<AOperation> BY_ID =
        ByIdMap.continuous(
            AOperation::getId,
            AOperation.values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
        );

    private final int id;

    public int getId() {
        return id;
    }
}
