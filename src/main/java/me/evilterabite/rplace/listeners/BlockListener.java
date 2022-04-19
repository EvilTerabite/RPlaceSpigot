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
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.Objects;

public class BlockListener implements Listener {

    @EventHandler
    void onBlockBreak(BlockBreakEvent event) {
        if(RPlace.canvas == null) return;
        Block block = event.getBlock();
        if(RPlace.canvas.getZone().contains(block.getLocation())) {
            event.getPlayer().sendMessage("This block is part of the canvas and cannot be broken!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if(RPlace.playersInCanvas.contains(player.getUniqueId())) {
            if(RPlace.whitelistedBlocks.contains(block.getType())) {
                if(!RPlace.timedPlayers.contains(player.getUniqueId())) {
                    Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(block.getLocation().subtract(0, 1, 0)).setType(block.getType());
                    RPlace.timedPlayers.add(player.getUniqueId());
                    Bukkit.getScheduler().runTaskLater(RPlace.getPlugin(RPlace.class), () -> {
                        RPlace.timedPlayers.remove(player.getUniqueId());
                    }, 20L * RPlace.canvas.getPlaceBlockTimer());

                    new BukkitRunnable() {
                        int time = RPlace.canvas.getPlaceBlockTimer();
                        @Override
                        public void run() {
                            int mins = (time%3600)/60;
                            int seconds = time % 60;
                            if(time == 0) {
                                cancel();
                            } else {
                                if(RPlace.playersInCanvas.contains(player.getUniqueId())) {
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.format("%02d:%02d", mins, seconds), ChatColor.DARK_BLUE));
                                }
                                time--;
                            }
                        }
                    }.runTaskTimer(RPlace.getPlugin(RPlace.class), 0L, 20L);
                } else {
                    player.sendMessage("You can only place one block every 2mins!");
                }
            }
            event.setCancelled(true);
        }
    }
}
