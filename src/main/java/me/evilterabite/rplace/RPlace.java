package me.evilterabite.rplace;

import me.evilterabite.rplace.commands.CanvasCommand;
import me.evilterabite.rplace.commands.PaletteCommand;
import me.evilterabite.rplace.files.PlayerDataConfiguration;
import me.evilterabite.rplace.libraries.Canvas;
import me.evilterabite.rplace.libraries.Pixel;
import me.evilterabite.rplace.libraries.PlacePlayer;
import me.evilterabite.rplace.libraries.gui.CanvasGUI;
import me.evilterabite.rplace.libraries.gui.PaletteGUI;
import me.evilterabite.rplace.listeners.BlockListener;
import me.evilterabite.rplace.listeners.CanvasListener;
import me.evilterabite.rplace.listeners.PlayerListener;
import me.evilterabite.rplace.utils.UpdateChecker;
import me.evilterabite.rplace.utils.Zone;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.logging.Level;

public final class RPlace extends JavaPlugin {

    public static Canvas canvas;
    public static CanvasGUI canvasGUI;
    public static PaletteGUI paletteGUI;
    public static Zone canvasZone;
    public static ArrayList<UUID> playersInCanvas;
    public static ArrayList<UUID> timedPlayers;
    public static ArrayList<Material> whitelistedBlocks;
    private static PlayerDataConfiguration playerDataConfiguration;
    public static HashMap<PlacePlayer, List<Pixel>> playerPixelMap;
    public static boolean updateAvailable;

    @Override
    public void onEnable() {
        playerPixelMap = new HashMap<>();
        playerPixelMap = new HashMap<>();
        canvasGUI = new CanvasGUI();
        paletteGUI = new PaletteGUI();
        whitelistedBlocks = new ArrayList<>();
        playersInCanvas = new ArrayList<>();
        timedPlayers = new ArrayList<>();
        playerDataConfiguration = new PlayerDataConfiguration();
        registerCommands();
        registerListeners();

        loadWhitelistedBlocks();
        loadCanvas();
        checkForUpdates();

        saveDefaultConfig();


    }

    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            CanvasListener.restorePlayerContents(player);
        }
        getLogger().log(Level.FINE, "Restored Player Inventories");

        canvas.store();
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("canvas")).setExecutor(new CanvasCommand());
        Objects.requireNonNull(getCommand("palette")).setExecutor(new PaletteCommand());
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new CanvasListener(), this);
        pm.registerEvents(new CanvasCommand(), this);
        pm.registerEvents(new BlockListener(), this);
    }

    public static RPlace getInstance() {
        return RPlace.getPlugin(RPlace.class);
    }

    public void loadCanvas() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!Objects.requireNonNull(getConfig().getString("canvas")).equalsIgnoreCase("null")) {
                    canvas = Canvas.deserialize(Objects.requireNonNull(getConfig().getString("canvas")));
                    assert canvas != null;
                    canvas.recover();
                }
            }
        }.runTaskLater(this, 0);
    }

    public void checkForUpdates() {
        new UpdateChecker(this, 101481).getVersion(version -> {
            updateAvailable = !this.getDescription().getVersion().equals(version);
            if(updateAvailable) {
                getLogger().log(Level.WARNING, "Update Available! Stay updated to keep your server BUG-FREE!");
            }
        });
    }

    public void loadWhitelistedBlocks() {
        for(String s : getConfig().getStringList("canvas_blocks")) {
            Material mat = null;
            try {
                mat = Material.getMaterial(s);
            } catch (NullPointerException e) {
                getLogger().log(Level.SEVERE, "There is an error with the canvas_blocks list! Check the config to make sure you set it up correctly!");
                e.printStackTrace();
            }

            whitelistedBlocks.add(mat);
        }
    }

    public FileConfiguration getPlayerData() {
        return playerDataConfiguration.get();
    }
}
