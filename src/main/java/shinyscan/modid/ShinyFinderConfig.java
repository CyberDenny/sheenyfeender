package com.ned.shinyfinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ShinyFinderConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "shinyfinder.json");
    
    public double scanRadius = 50.0;
    public long scanInterval = 10L; // ticks (10 ticks = 0.5 seconds) - faster detection
    public boolean playSound = true;
    public boolean showTitle = true;
    public boolean showHUD = true;
    public boolean logToConsole = true;
    
    public String alertSound = "entity.experience_orb.pickup";
    public float soundVolume = 1.0f;
    public float soundPitch = 1.5f;
    
    // Auto-start settings
    public boolean autoStartEnabled = true; // Start scanner automatically when joining world
    
    public static ShinyFinderConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, ShinyFinderConfig.class);
            } catch (IOException e) {
                ShinyFinder.LOGGER.error("Failed to load config file", e);
            }
        }
        
        // Create default config
        ShinyFinderConfig config = new ShinyFinderConfig();
        config.save();
        return config;
    }
    
    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            ShinyFinder.LOGGER.error("Failed to save config file", e);
        }
    }
    
    public static ShinyFinderConfig INSTANCE = load();
}
