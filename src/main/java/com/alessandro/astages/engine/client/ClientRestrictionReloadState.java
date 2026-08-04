package com.alessandro.astages.engine.client;

public class ClientRestrictionReloadState {
    private static boolean areRestrictionsAvailable = true;

    public static boolean areRestrictionsAvailable() {
        return areRestrictionsAvailable;
    }

    public static void buildingRestrictions() {
        areRestrictionsAvailable = false;
    }

    public static void restrictionsAvailable() {
        areRestrictionsAvailable = true;
    }
}
