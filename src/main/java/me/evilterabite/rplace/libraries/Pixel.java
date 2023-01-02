package me.evilterabite.rplace.libraries;

import org.bukkit.Location;

public class Pixel {
    private Location location;
    private long timestamp;
    private PlacePlayer owner;

    public Pixel(Location location, long timestamp, PlacePlayer owner) {
        this.location = location;
        this.timestamp = timestamp;
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public PlacePlayer getOwner() {
        return owner;
    }

    public void setOwner(PlacePlayer owner) {
        this.owner = owner;
    }
}
