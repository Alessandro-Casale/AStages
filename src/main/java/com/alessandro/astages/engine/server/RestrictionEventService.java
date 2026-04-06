package com.alessandro.astages.engine.server;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.constant.AEventPhase;
import com.alessandro.astages.api.event.AddRestrictionEvent;

public class RestrictionEventService {
    public static void addRestrictionsViaJavaCode(AEventPhase stage) {
        ALoader.EVENT_BUS.post(new AddRestrictionEvent(stage));
    }
}