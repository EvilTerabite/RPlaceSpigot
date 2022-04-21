package me.evilterabite.rplace.listeners;

import me.evilterabite.rplace.RPlace;
import me.evilterabite.rplace.events.PlayerEnterCanvasEvent;
import me.evilterabite.rplace.events.PlayerLeaveCanvasEvent;
import me.evilterabite.rplace.utils.C;
import me.evilterabite.rplace.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class CanvasListener implements Listener {

    public static HashMap<UUID, ItemStack[]> playerInventoryMap = new HashMap<>();
    public static HashMap<UUID, ItemStack[]> playerArmourMap = new HashMap<>();
    public static ItemStack paletteItem = ItemCreator.create(Material.CHEST, ChatColor.LIGHT_PURPLE + "Open the Block Palette", "");

    @EventHandler
    void onEnterCanvas(PlayerEnterCanvasEvent event) {
        if(RPlace.canvas == null) return;
        RPlace.playersInCanvas.add(event.getPlayer().getUniqueId());
        storePlayerContents(event.getPlayer());
        event.getPlayer().sendMessage(C.canvasEnter());
        if(C.invisPlayer()) {
            event.getPlayer().setInvisible(true);
        }
        event.getPlayer().getInventory().setItem(0, paletteItem);
    }

    @EventHandler
    void onLeaveCanvas(PlayerLeaveCanvasEvent event) {
        if(RPlace.canvas == null) return;
        RPlace.playersInCanvas.remove(event.getPlayer().getUniqueId());

        restorePlayerContents(event.getPlayer());
        event.getPlayer().sendMessage(C.canvasLeave());
        if(C.invisPlayer()) {
            event.getPlayer().setInvisible(false);
        }
    }

    public static void storePlayerContents(Player player) {
        playerInventoryMap.put(player.getUniqueId(), player.getInventory().getContents());
        if(player.getInventory().getArmorContents().length != 0) {
            playerArmourMap.put(player.getUniqueId(), player.getInventory().getArmorContents());
        }
        player.getInventory().clear();
    }

    public static void restorePlayerContents(Player player) {
        if(RPlace.canvas == null) return;
        player.getInventory().clear();
        if(playerInventoryMap.containsKey(player.getUniqueId())) {
            player.getInventory().setContents(playerInventoryMap.get(player.getUniqueId()));
        }
        if(playerArmourMap.containsKey(player.getUniqueId())) {
            player.getInventory().setArmorContents(playerArmourMap.get(player.getUniqueId()));
        }
    }
}
