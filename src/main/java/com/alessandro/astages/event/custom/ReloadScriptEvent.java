package com.alessandro.astages.event.custom;

import com.alessandro.astages.api.develop.Info;
import net.neoforged.bus.api.Event;

@SuppressWarnings("unused")
@Info("Not required in 1.21.X, used only in 1.20.X versions!")
public class ReloadScriptEvent extends Event {
    private final EventScriptType scriptType;

    public ReloadScriptEvent(EventScriptType scriptType) {
        this.scriptType = scriptType;
    }

    public static class BeforeScriptsLoaded extends ReloadScriptEvent {
        public BeforeScriptsLoaded(EventScriptType scriptType) {
            super(scriptType);
        }
    }

    public static class AfterScriptsLoaded extends ReloadScriptEvent {
        public AfterScriptsLoaded(EventScriptType scriptType) {
            super(scriptType);
        }
    }

    public EventScriptType getScriptType() {
        return scriptType;
    }

    public enum EventScriptType {
        CLIENT,
        SERVER,
        STARTUP
    }
}
