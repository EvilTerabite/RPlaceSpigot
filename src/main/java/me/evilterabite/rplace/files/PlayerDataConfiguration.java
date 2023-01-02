package me.evilterabite.rplace.files;

import me.evilterabite.rplace.RPlace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerDataConfiguration {
    private final File file;
    private FileConfiguration configuration;

    public PlayerDataConfiguration() {
        file = new File(RPlace.getInstance().getDataFolder() + "playerdata.yml");
        try {
            if(file.exists()) {
                file.createNewFile();
                configuration = YamlConfiguration.loadConfiguration(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return configuration;
    }

    public void store() {

    }
}
