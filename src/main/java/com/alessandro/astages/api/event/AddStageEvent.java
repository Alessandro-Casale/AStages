package com.alessandro.astages.api.event;

import com.alessandro.astages.api.constant.AEventPhase;
import net.neoforged.bus.api.Event;

public class AddStageEvent extends Event {
    private final AEventPhase phase;

    public AddStageEvent(AEventPhase phase) {
        this.phase = phase;
    }

    public AEventPhase getEventPhase() {
        return phase;
    }
}
