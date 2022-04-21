package me.evilterabite.rplace.commands;

import me.evilterabite.rplace.RPlace;
import me.evilterabite.rplace.libraries.Canvas;
import me.evilterabite.rplace.libraries.gui.CanvasGUI;
import me.evilterabite.rplace.utils.C;
import me.evilterabite.rplace.utils.ItemCreator;
import me.evilterabite.rplace.utils.Zone;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CanvasCommand implements CommandExecutor, Listener {

    public static final ItemStack modItem = ItemCreator.create(Material.NETHERITE_AXE, ChatColor.RED + "Canvas Moderator Axe","Set any block back to white concrete!");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("rplace.moderator")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("mod")) {
                        if(RPlace.playersInCanvas.contains(player.getUniqueId())) {
                            player.getInventory().setItem(8, modItem);
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + "[RPlace Moderation] You must be on the canvas when running the command!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "[RPlace Moderation] Unknown command!");
                        return true;
                    }
                } else {
                    if (player.hasPermission("rplace.admin")) {
                        RPlace.canvasGUI.open(player);
                        return true;
                    }
                }
            }
        }
        return true;
    }

    @EventHandler
    void onInteract(PlayerInteractEvent event) {
        if(!event.hasItem()) return;
        if(!event.getItem().hasItemMeta()) return;
        if(event.getItem().getItemMeta().getDisplayName().equals(modItem.getItemMeta().getDisplayName())) {
            if(RPlace.playersInCanvas.contains(event.getPlayer().getUniqueId()) && event.getPlayer().hasPermission("rplace.moderator")) {
                Block block = event.getClickedBlock();
                block.setType(Material.WHITE_CONCRETE);
            }
        }
    }
}
