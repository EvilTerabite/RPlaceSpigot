package me.evilterabite.rplace.events.player;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ModeratorRemovePixelEvent extends Event {

    public static HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
