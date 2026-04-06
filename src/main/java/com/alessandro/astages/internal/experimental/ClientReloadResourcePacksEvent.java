package com.alessandro.astages.internal.experimental;

import net.neoforged.bus.api.Event;

public class ClientReloadResourcePacksEvent extends Event {
    public static class Pre extends ClientReloadResourcePacksEvent { }
    public static class Post extends ClientReloadResourcePacksEvent { }
}
