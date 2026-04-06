package com.alessandro.astages.engine.client;

public class ClientRestrictionReloadState {
    private static boolean areScriptsAvailable = false;
    private static boolean didJeiFinishReloading = false;
    private static boolean isReloading = false;

    public static void reloadStarted() {
        isReloading = true;
        areScriptsAvailable = false;
        didJeiFinishReloading = false;
    }

    public static void areScriptsAvailable(boolean areScriptsAvailable) {
        ClientRestrictionReloadState.areScriptsAvailable = areScriptsAvailable;

        if (ClientRestrictionReloadState.areScriptsAvailable) {
            if (didJeiFinishReloading) {
                isReloading = false;
            }

            ClientRestrictionEventService.fireUpdates();
        }
    }

    public static boolean areScriptsAvailable() {
        return areScriptsAvailable;
    }

    public static void jeiStartedReload() {
        didJeiFinishReloading = false;
    }

    public static void jeiFinishedReload() {
        didJeiFinishReloading = true;

        if (areScriptsAvailable) {
            isReloading = false;
        }

        ClientRestrictionEventService.fireUpdates();
    }

    public static boolean didJeiFinishReloading() {
        return didJeiFinishReloading;
    }

    public static boolean isReloadFinished() {
        return !isReloading;
    }

    public static boolean ableToUpdateJeiUI() {
        return areScriptsAvailable() && didJeiFinishReloading() && isReloadFinished();
    }
}