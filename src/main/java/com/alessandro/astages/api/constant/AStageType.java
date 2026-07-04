package com.alessandro.astages.api.constant;

import com.alessandro.astages.api.nullability.NotNullParams;

@NotNullParams
public enum AStageType {
    PLAYER("player"),
    SERVER("server");

    private final String descriptionId;

    AStageType(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    public String getDescriptionId() {
        return descriptionId;
    }

    public static String getDescriptionIdFor(AStageType type) {
        return type.getDescriptionId();
    }
}