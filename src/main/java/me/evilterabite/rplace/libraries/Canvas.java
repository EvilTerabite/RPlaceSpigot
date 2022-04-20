package me.evilterabite.rplace.libraries;

import me.evilterabite.rplace.RPlace;
import me.evilterabite.rplace.events.CanvasCreateEvent;
import me.evilterabite.rplace.libraries.gui.CanvasGUI;
import me.evilterabite.rplace.utils.Zone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Canvas {

    private String name;
    private Zone zone;
    private int placeBlockTimer;
    private List<Block> resetBlockList;

    public Canvas(String name, Zone zone) {
        this.name = name;
        this.zone = zone;
        this.placeBlockTimer = RPlace.getInstance().getConfig().getInt("place_timer");
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getPlaceBlockTimer() {
        return placeBlockTimer;
    }

    public void setPlaceBlockTimer(int placeBlockTimer) {
        Plugin plugin = RPlace.getPlugin(RPlace.class);
        plugin.getConfig().set("place_timer", placeBlockTimer);
        plugin.saveConfig();
        plugin.reloadConfig();
        this.placeBlockTimer = placeBlockTimer;
    }

    public void create() {
        reset();
        store();
        RPlace.canvas = this;
        RPlace.canvasZone = new Zone(
                new Location(zone.getWorld(), zone.getMaxX(), zone.getMaxY() + 50, zone.getMaxZ()),
                new Location(zone.getWorld(), zone.getMinX(), zone.getMinY() - 50, zone.getMinZ())
        );
        Bukkit.getPluginManager().callEvent(new CanvasCreateEvent(this));
    }

    public void recover() {
        RPlace.getInstance().reloadConfig();
        RPlace.canvas = this;
        RPlace.canvasZone = new Zone(
                new Location(zone.getWorld(), zone.getMinX(), zone.getMinY() - 50, zone.getMinZ()),
                new Location(zone.getWorld(), zone.getMaxX(), zone.getMaxY() + 50, zone.getMaxZ())
        );
    }

    public void reset() {
        List<Block> canvasBlocks = zone.select();
        for(Block b : canvasBlocks) {
            b.setType(Material.WHITE_CONCRETE);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void store() {
        RPlace.getInstance().getConfig().set("place_timer", placeBlockTimer);
        RPlace.getPlugin(RPlace.class).getConfig().set("canvas", this.toString());
        RPlace.getPlugin(RPlace.class).saveConfig();
    }

    public static Canvas deserialize(String serializedCanvas) {
        List<String> rawCanvas = new ArrayList<>(Arrays.asList(serializedCanvas.split(":")));
        if(!rawCanvas.isEmpty()) {
            List<String> rawZone = new ArrayList<>(Arrays.asList(rawCanvas.get(1).split(",")));
            String name = rawCanvas.get(0);
            Zone zone = new Zone(Bukkit.getWorld(rawZone.get(0)),
                    Integer.parseInt(rawZone.get(1)),
                    Integer.parseInt(rawZone.get(2)),
                    Integer.parseInt(rawZone.get(3)),
                    Integer.parseInt(rawZone.get(4)),
                    Integer.parseInt(rawZone.get(5)),
                    Integer.parseInt(rawZone.get(6)));
            int placeBlockTimer = Integer.parseInt(rawCanvas.get(2));

            return new Canvas(name, zone);
        }

        return null;
    }

    public void remove() {
        if(RPlace.canvas != null);
        RPlace.canvasGUI.posOne = null;
        RPlace.canvasGUI.posTwo = null;
        List<Block> canvasBlocks = zone.select();
        for(Block b : canvasBlocks) {
            b.setType(Material.AIR);
        }
        RPlace core = RPlace.getInstance();
        core.getConfig().set("canvas", "null");
        core.saveConfig();
        core.reloadConfig();
        RPlace.canvas = null;
        RPlace.canvasZone = null;
    }

    @Override
    public String toString() {
        String separator = ":";
        return name + separator + zone.toString() + separator + placeBlockTimer;
    }
}
