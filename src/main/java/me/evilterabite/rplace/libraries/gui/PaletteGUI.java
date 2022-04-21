package me.evilterabite.rplace.libraries.gui;

import me.evilterabite.rplace.RPlace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;


public class PaletteGUI implements Listener {
    public static Inventory inv;
    private final HashMap<Integer, ItemStack> slotMap;

    public PaletteGUI() {
        slotMap = new HashMap<>();
        inv = Bukkit.createInventory(null, 54, ChatColor.LIGHT_PURPLE + "Block Palette");
        register();
    }

    public void open(Player player) {
        player.openInventory(inv);
        createItems();
    }

    public void createItems() {
        int slot = 0;
        for(Material material : RPlace.whitelistedBlocks) {
            ItemStack item = new ItemStack(material);
            slotMap.put(slot, item);
            inv.setItem(slot, item);
            slot++;
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if(event.getClickedInventory() != inv) return;
        if(slotMap.containsKey(event.getSlot())) {
            Player player = (Player) event.getWhoClicked();
            ItemStack item = slotMap.get(event.getSlot());
            player.getInventory().setItem(1, item);
            player.closeInventory();
        }
        event.setCancelled(true);

    }


    private void update() {
        createItems();
    }

    private void register() {
        RPlace.getInstance().getServer().getPluginManager().registerEvents(this, RPlace.getInstance());
    }
}
