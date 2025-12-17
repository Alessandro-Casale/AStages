package com.alessandro.astages.event.custom;

import net.neoforged.bus.api.Event;

public class ClientReloadResourcePacksEvent extends Event {
    public static class Pre extends ClientReloadResourcePacksEvent { }
    public static class Post extends ClientReloadResourcePacksEvent { }
}
