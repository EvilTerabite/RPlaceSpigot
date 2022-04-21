package me.evilterabite.rplace.libraries.gui;

import me.evilterabite.rplace.RPlace;
import me.evilterabite.rplace.libraries.Canvas;
import me.evilterabite.rplace.utils.ItemCreator;
import me.evilterabite.rplace.utils.Zone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class CanvasGUI implements Listener {

    public static Inventory inv;
    public Location posOne;
    public Location posTwo;
    private final ArrayList<UUID> waitingForChat;

    public CanvasGUI() {
        waitingForChat = new ArrayList<>();
        inv = Bukkit.createInventory(null, 9, ChatColor.YELLOW + "Canvas GUI");
        register();
    }

    public void open(Player player) {
        player.openInventory(inv);
        createItems();
    }

    public void createItems() {
        if(RPlace.canvas != null && RPlace.canvasZone != null) {
            ItemStack timer = ItemCreator.create(Material.CLOCK, ChatColor.YELLOW + "Set the timer", "Click here to set the timer");
            ItemStack reset = ItemCreator.create(Material.PAPER, ChatColor.YELLOW + "RESET THE CANVAS", "Click here to reset the canvas");
            ItemStack remove = ItemCreator.create(Material.REDSTONE_BLOCK, ChatColor.RED + "DELETE THE CANVAS", "Click here to delete the canvas FOREVER!");
            inv.setItem(0, new ItemStack(Material.AIR));
            inv.setItem(1, new ItemStack(Material.AIR));
            inv.setItem(2, timer);
            inv.setItem(4, reset);
            inv.setItem(6, remove);
        } else {
            ItemStack setPos1;
            ItemStack setPos2;
            if (posOne == null) {
                setPos1 = ItemCreator.create(Material.RED_CONCRETE, ChatColor.RED + "Position 1 not set", "Click here to set position 1 to where you are standing!");
            } else {
                setPos1 = ItemCreator.create(Material.GREEN_CONCRETE, ChatColor.RED + "Position 1 is set!", "Click here to set position 1 to where you are standing!");
            }
            if (posTwo == null) {
                setPos2 = ItemCreator.create(Material.RED_CONCRETE, ChatColor.GREEN + "Position 2 not set", "Click here to set position 2 to where you are standing!");
            } else {
                setPos2 = ItemCreator.create(Material.GREEN_CONCRETE, ChatColor.GREEN + "Position 2 is set!", "Click here to set position 2 to where you are standing!");
            }

            ItemStack createCanvas;

            if (posOne != null && posTwo != null) {
                createCanvas = ItemCreator.create(Material.EMERALD_BLOCK, ChatColor.GREEN + "Create the Canvas", "Click here to create the canvas!");
            } else {
                createCanvas = ItemCreator.create(Material.REDSTONE_BLOCK, ChatColor.RED + "Cannot Create Canvas", "The 2 positions have not been set!");
            }

            inv.setItem(0, setPos1);
            inv.setItem(1, setPos2);
            inv.setItem(6, createCanvas);
            inv.setItem(2, new ItemStack(Material.AIR));
            inv.setItem(4, new ItemStack(Material.AIR));
        }

    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if(event.getInventory() != inv) return;
        event.setCancelled(true);
        int slot = event.getSlot();
        Player player = (Player) event.getWhoClicked();
        if(RPlace.canvas == null && RPlace.canvasZone == null) {
            if (slot == 0) {
                posOne = player.getLocation().subtract(0, 1, 0);
                update();
            }
            if (slot == 1) {
                posTwo = player.getLocation().subtract(0, 1, 0);
                update();
            }

            if (slot == 6) {
                if (posOne != null && posTwo != null) {
                    Canvas canvas = new Canvas("test", new Zone(posOne, posTwo));
                    canvas.create();
                    player.closeInventory();
                }
            }
        } else {
            assert RPlace.canvas != null;
            if(slot == 2) {
                waitingForChat.add(player.getUniqueId());
                player.sendMessage("How many seconds would you like to set: (Type it in chat)");
                player.closeInventory();
            }
            if(slot == 4) {
                RPlace.canvas.reset();
                player.closeInventory();
            }
            if(slot == 6) {
                RPlace.canvas.remove();
                player.closeInventory();
            }
        }

    }

    @EventHandler
    void onChatEvent(AsyncPlayerChatEvent event) {
        if(waitingForChat.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            waitingForChat.remove(event.getPlayer().getUniqueId());
            try {
                RPlace.canvas.setPlaceBlockTimer(Integer.parseInt(event.getMessage()));
            } catch (NumberFormatException e) {
                event.getPlayer().sendMessage("That's not a number :(");
            }
            event.getPlayer().sendMessage("Set the timer to " + event.getMessage());
        }
    }

    private void update() {
        createItems();
    }

    private void register() {
        RPlace.getInstance().getServer().getPluginManager().registerEvents(this, RPlace.getInstance());
    }

}
