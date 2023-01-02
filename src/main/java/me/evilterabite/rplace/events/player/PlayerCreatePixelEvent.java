package me.evilterabite.rplace.events.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.sql.Timestamp;

public class PlayerCreatePixelEvent extends Event {
    private Player player;
    private Block block;
    private long timestamp;


    public static HandlerList HANDLERS = new HandlerList();

    public PlayerCreatePixelEvent(Player player, Block block, long timestamp) {
        this.player = player;
        this.block = block;
        this.timestamp = timestamp;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
