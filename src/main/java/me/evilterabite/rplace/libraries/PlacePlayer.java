package me.evilterabite.rplace.libraries;

import java.util.ArrayList;
import java.util.UUID;

public class PlacePlayer {
    private UUID uuid;
    private ArrayList<Pixel> ownedPixels;

    public PlacePlayer(UUID uuid, ArrayList<Pixel> ownedPixels) {
        this.uuid = uuid;
        this.ownedPixels = ownedPixels;
    }
}
