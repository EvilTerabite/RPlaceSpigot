package me.evilterabite.rplace.utils;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Zone {

    private final World world;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;

    public Zone(Location loc1, Location loc2) {
        this(loc1.getWorld(), loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
    }

    public Zone(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.world = world;

        minX = Math.min(x1, x2);
        minY = Math.min(y1, y2);
        minZ = Math.min(z1, z2);
        maxX = Math.max(x1, x2);
        maxY = Math.max(y1, y2);
        maxZ = Math.max(z1, z2);
    }

    public World getWorld() {
        return world;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public boolean contains(Zone Zone) {
        return Zone.getWorld().equals(world) &&
                Zone.getMinX() >= minX && Zone.getMaxX() <= maxX &&
                Zone.getMinY() >= minY && Zone.getMaxY() <= maxY &&
                Zone.getMinZ() >= minZ && Zone.getMaxZ() <= maxZ;
    }

    public boolean contains(Location location) {
        return contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean contains(int x, int y, int z) {
        return x >= minX && x <= maxX &&
                y >= minY && y <= maxY &&
                z >= minZ && z <= maxZ;
    }

    public boolean overlaps(Zone Zone) {
        return Zone.getWorld().equals(world) &&
                !(Zone.getMinX() > maxX || Zone.getMinY() > maxY || Zone.getMinZ() > maxZ ||
                        minZ > Zone.getMaxX() || minY > Zone.getMaxY() || minZ > Zone.getMaxZ());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Zone)) {
            return false;
        }
        final Zone other = (Zone) obj;
        return world.equals(other.world)
                && minX == other.minX
                && minY == other.minY
                && minZ == other.minZ
                && maxX == other.maxX
                && maxY == other.maxY
                && maxZ == other.maxZ;
    }

    @Override
    public String toString() {
        return world.getName() +
                "," + minX +
                "," + minY +
                "," + minZ +
                "," + maxX +
                "," + maxY +
                "," + maxZ;
    }

    public static List<Block> select(Location loc1, Location loc2, World w){

        List<Block> blocks = new ArrayList<>();

        int x1 = loc1.getBlockX();
        int y1 = loc1.getBlockY();
        int z1 = loc1.getBlockZ();

        int x2 = loc2.getBlockX();
        int y2 = loc2.getBlockY();
        int z2 = loc2.getBlockZ();

        int xMin, yMin, zMin;
        int xMax, yMax, zMax;
        int x, y, z;

        if(x1 > x2){
            xMin = x2;
            xMax = x1;
        }else{
            xMin = x1;
            xMax = x2;
        }

        if(y1 > y2){
            yMin = y2;
            yMax = y1;
        }else{
            yMin = y1;
            yMax = y2;
        }

        if(z1 > z2){
            zMin = z2;
            zMax = z1;
        }else{
            zMin = z1;
            zMax = z2;
        }

        for(x = xMin; x <= xMax; x ++){
            for(y = yMin; y <= yMax; y ++){
                for(z = zMin; z <= zMax; z ++){
                    Block b = new Location(w, x, y, z).getBlock();
                    blocks.add(b);
                }
            }
        }

        return blocks;
    }

    public List<Block> select(){

        List<Block> blocks = new ArrayList<>();

        for(int x = minX; x <= maxX; x ++){
            for(int y = minY; y <= maxY; y ++){
                for(int z = minZ; z <= maxZ; z ++){
                    Block b = new Location(world, x, y, z).getBlock();
                    blocks.add(b);
                }
            }
        }

        return blocks;
    }
}