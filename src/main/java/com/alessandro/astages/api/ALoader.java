package com.alessandro.astages.api;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.Consumer;

public class ALoader {
    public static final EventBus EVENT_BUS = new EventBus();

    public static class EventBus {
        public void post(Event event) {
            NeoForge.EVENT_BUS.post(event);
        }

        public <T extends Event> void addListener(EventPriority priority, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer) {
            NeoForge.EVENT_BUS.addListener(priority, receiveCancelled, eventType, consumer);
        }

        public <T extends Event> void addListener(Consumer<T> consumer) {
            NeoForge.EVENT_BUS.addListener(consumer);
        }
    }
}
