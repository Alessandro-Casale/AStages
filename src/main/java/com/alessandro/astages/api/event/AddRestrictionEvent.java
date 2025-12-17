package com.alessandro.astages.api.event;

import com.alessandro.astages.api.constant.ARestrictionStage;
import net.neoforged.bus.api.Event;

public class AddRestrictionEvent extends Event {
    private final ARestrictionStage stage;

    public AddRestrictionEvent(ARestrictionStage stage) {
        this.stage = stage;
    }

    public ARestrictionStage getStage() {
        return stage;
    }
}
