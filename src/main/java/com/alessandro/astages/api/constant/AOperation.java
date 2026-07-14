package com.alessandro.astages.api.constant;

public enum AOperation {
    ADD(true, true, true),
    ADD_ALL(true, false, true),
    REMOVE(false, true, false),
    REMOVE_ALL(false, false, false),
    LOGIN(false, false, false);

    private final boolean needToBeChecked;
    private final boolean supportOnlyOneStage;
    private final boolean handleStageRecognization;

    AOperation(boolean needToBeChecked, boolean supportOnlyOneStage, boolean handleStageRecognization) {
        this.needToBeChecked = needToBeChecked;
        this.supportOnlyOneStage = supportOnlyOneStage;
        this.handleStageRecognization = handleStageRecognization;
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
}
