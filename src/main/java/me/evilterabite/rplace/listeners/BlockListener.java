package me.evilterabite.rplace.listeners;

import me.evilterabite.rplace.RPlace;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.Objects;

public class BlockListener implements Listener {

    @EventHandler
    void onBlockBreak(BlockBreakEvent event) {
        if (RPlace.canvas == null) return;
        Block block = event.getBlock();
        if(RPlace.canvas.getZone().getWorld() != block.getLocation().getWorld()) return;
        if (RPlace.canvas.getZone().contains(block.getLocation())) {
            event.getPlayer().sendMessage("This block is part of the canvas and cannot be broken!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onBlockPlace(BlockPlaceEvent event) {
        if (RPlace.canvas == null) return;
        Block block = event.getBlock();
        if(RPlace.canvas.getZone().getWorld() != block.getLocation().getWorld()) return;
        Player player = event.getPlayer();
        if (RPlace.playersInCanvas.contains(player.getUniqueId())) {
            if (RPlace.canvasZone.contains(block.getLocation())) {
                if (RPlace.whitelistedBlocks.contains(block.getType())) {
                    if (!RPlace.timedPlayers.contains(player.getUniqueId())) {
                        Objects.requireNonNull(RPlace.canvas.getZone().getWorld()).getBlockAt(block.getLocation().subtract(0, 1, 0)).setType(block.getType());
                        RPlace.timedPlayers.add(player.getUniqueId());
                        Bukkit.getScheduler().runTaskLater(RPlace.getPlugin(RPlace.class), () -> {
                            RPlace.timedPlayers.remove(player.getUniqueId());
                        }, 20L * RPlace.canvas.getPlaceBlockTimer());

                        new BukkitRunnable() {
                            int time = RPlace.canvas.getPlaceBlockTimer();

                            @Override
                            public void run() {
                                int mins = (time % 3600) / 60;
                                int seconds = time % 60;
                                if (time == 0) {
                                    cancel();
                                } else {
                                    if (RPlace.playersInCanvas.contains(player.getUniqueId())) {
                                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.format(ChatColor.DARK_BLUE + "%02d:%02d", mins, seconds)));
                                    }
                                    time--;
                                }
                            }
                        }.runTaskTimer(RPlace.getPlugin(RPlace.class), 0L, 20L);
                    } else {
                        int seconds = RPlace.getInstance().getConfig().getInt("place_timer");
                        player.sendMessage("You can only place a block every " + seconds + "secs!");
                    }
                }
            } else {
                player.sendMessage("You cannot place blocks outside of the canvas!");
            }
            event.setCancelled(true);
        }

        if(!RPlace.playersInCanvas.contains(player.getUniqueId()) && RPlace.canvasZone.contains(block.getLocation())) {
            if(RPlace.canvas.getZone().getWorld() != block.getLocation().getWorld()) return;
            event.setCancelled(true);
            player.sendMessage("You cannot place blocks inside the canvas from outside the canvas!");
        }
    }


    @EventHandler
    void onDropItem(PlayerDropItemEvent event) {
        if(RPlace.canvas == null) return;
        if(RPlace.playersInCanvas.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onPickupItem(EntityPickupItemEvent event) {
        if(RPlace.canvas == null) return;
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(RPlace.playersInCanvas.contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    void onInventoryOpenOutsideOfCanvas(InventoryOpenEvent event) {
        if(RPlace.canvas == null) return;
        if(event.getInventory().getType() != InventoryType.PLAYER) {
            if(RPlace.playersInCanvas.contains(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
