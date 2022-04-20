package me.evilterabite.rplace.listeners;

import me.evilterabite.rplace.RPlace;
import me.evilterabite.rplace.events.PlayerEnterCanvasEvent;
import me.evilterabite.rplace.events.PlayerLeaveCanvasEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    void onPlayerMove(PlayerMoveEvent event) {
        if(RPlace.canvas == null) return;
        Player player = event.getPlayer();
        if(!RPlace.playersInCanvas.contains(player.getUniqueId())) {
            if(RPlace.canvasZone.contains(player.getLocation())) {
                Bukkit.getPluginManager().callEvent(new PlayerEnterCanvasEvent(player));
            }
        } else {
            if(!RPlace.canvasZone.contains(player.getLocation())) {
                Bukkit.getPluginManager().callEvent(new PlayerLeaveCanvasEvent(player));
            }
        }
    }

    @EventHandler
    void onPlayerDisconnect(PlayerQuitEvent event) {
        if(RPlace.playersInCanvas.contains(event.getPlayer().getUniqueId())) {
            CanvasListener.restorePlayerContents(event.getPlayer());
        }
    }
}
