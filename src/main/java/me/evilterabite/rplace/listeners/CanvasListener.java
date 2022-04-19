package me.evilterabite.rplace.listeners;

import me.evilterabite.rplace.RPlace;
import me.evilterabite.rplace.events.PlayerEnterCanvasEvent;
import me.evilterabite.rplace.events.PlayerLeaveCanvasEvent;
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

    @EventHandler
    void onEnterCanvas(PlayerEnterCanvasEvent event) {
        RPlace.playersInCanvas.add(event.getPlayer().getUniqueId());

        storePlayerContents(event.getPlayer());
        event.getPlayer().sendMessage("You entered the canvas! We stored your items for now!");
        ItemStack[] hotbar = {
                new ItemStack(Material.BLACK_WOOL),
                new ItemStack(Material.WHITE_WOOL),
                new ItemStack(Material.ORANGE_WOOL),
                new ItemStack(Material.MAGENTA_WOOL),
                new ItemStack(Material.LIGHT_BLUE_WOOL),
                new ItemStack(Material.YELLOW_WOOL),
                new ItemStack(Material.GREEN_WOOL),
                new ItemStack(Material.PINK_WOOL),
                new ItemStack(Material.BROWN_WOOL)
        };
        event.getPlayer().getInventory().setContents(hotbar);

    }

    @EventHandler
    void onLeaveCanvas(PlayerLeaveCanvasEvent event) {
        RPlace.playersInCanvas.remove(event.getPlayer().getUniqueId());

        restorePlayerContents(event.getPlayer());
        event.getPlayer().sendMessage("You left the canvas... here's your items!");
    }

    public static void storePlayerContents(Player player) {
        playerInventoryMap.put(player.getUniqueId(), player.getInventory().getContents());
        if(player.getInventory().getArmorContents().length != 0) {
            playerArmourMap.put(player.getUniqueId(), player.getInventory().getArmorContents());
        }
        player.getInventory().clear();
    }

    public static void restorePlayerContents(Player player) {
        player.getInventory().clear();
        if(playerInventoryMap.containsKey(player.getUniqueId())) {
            player.getInventory().setContents(playerInventoryMap.get(player.getUniqueId()));
        }
        if(playerArmourMap.containsKey(player.getUniqueId())) {
            player.getInventory().setArmorContents(playerArmourMap.get(player.getUniqueId()));
        }
    }
}
