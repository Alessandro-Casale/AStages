package com.alessandro.astages.engine.simple;

public class ASimpleRestriction {
    public String id;
    public String stage;
    public String object; // Must be string to be stored in file!

    public ASimpleRestriction(String id, String stage, String object) {
        this.id = id;
        this.stage = stage;
        this.object = object;
    }

    @Override
    public String toString() {
        return "ASimpleRestriction{" +
            "id='" + id + '\'' +
            ", stage='" + stage + '\'' +
            ", object='" + object + '\'' +
            '}';
    }
}
