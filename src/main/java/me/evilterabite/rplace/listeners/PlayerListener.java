package me.evilterabite.rplace.listeners;

import me.evilterabite.rplace.RPlace;
import me.evilterabite.rplace.commands.CanvasCommand;
import me.evilterabite.rplace.events.PlayerEnterCanvasEvent;
import me.evilterabite.rplace.events.PlayerLeaveCanvasEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.logging.Level;

public class PlayerListener implements Listener {

    @EventHandler
    void onPlayerMove(PlayerMoveEvent event) {
        if(RPlace.canvas == null) return;
        Player player = event.getPlayer();
        if(RPlace.canvasZone.getWorld() != player.getWorld()) return;
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
            Bukkit.getPluginManager().callEvent(new PlayerLeaveCanvasEvent(event.getPlayer()));
        }
    }

    @EventHandler
    void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        if(event.getFrom() == RPlace.canvas.getZone().getWorld()) {
            if (RPlace.playersInCanvas.contains(event.getPlayer().getUniqueId())) {
                RPlace.getInstance().getLogger().log(Level.SEVERE, "You should not let players change worlds while on the canvas! This can cause major issues.");
                Bukkit.getPluginManager().callEvent(new PlayerLeaveCanvasEvent(event.getPlayer()));
            }
        }
    }

    @EventHandler
    void onPlayerTeleport(PlayerTeleportEvent event) {
        if(RPlace.playersInCanvas.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage("Please leave the canvas before teleporting out!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onPlayerInteract(PlayerInteractEvent event) {
        if(!event.hasItem()) return;
        if(!event.getItem().hasItemMeta()) return;
        if(event.getItem().getItemMeta().getDisplayName().equals(CanvasCommand.modItem.getItemMeta().getDisplayName())) {
            if(RPlace.playersInCanvas.contains(event.getPlayer().getUniqueId()) && event.getPlayer().hasPermission("rplace.moderator")) {
                Block block = event.getClickedBlock();
                block.setType(Material.WHITE_CONCRETE);
            }
        }
        if(event.getItem().getItemMeta().getDisplayName().equals(CanvasListener.paletteItem.getItemMeta().getDisplayName())) {
            RPlace.paletteGUI.open(event.getPlayer());
        }
    }
}
