package me.releasedsnow.com.lunar;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {


    public static FileConfiguration configuration = LunarElement.getPlugin().getConfig();


    public ConfigManager() {
        this.defaults();
    }


    public static FileConfiguration getConfiguration() {
        return configuration;
    }

    public void defaults() {
        FileConfiguration config = getConfiguration();

        String path = "Abilities.Lunar.";
    }
}


