package com.alessandro.astages.engine.client;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.event.update.ClientItemUpdateEvent;
import com.alessandro.astages.api.event.update.ClientRecipeUpdateEvent;

public class ClientRestrictionEventService {
    public static void fireUpdates() {
        ALoader.EVENT_BUS.post(new ClientItemUpdateEvent());
        ALoader.EVENT_BUS.post(new ClientRecipeUpdateEvent());
    }
}