package com.ned.shinyfinder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShinyFinderClientCore {
    private static final Map<UUID, Boolean> playerScannerStates = new HashMap<>();
    private static final Map<UUID, Long> lastScanTimes = new HashMap<>();
    private static boolean scannerEnabled = false;
    private static boolean hasAutoStarted = false; // Track if we've auto-started this session
    
    private static int tickCounter = 0;
    
    public static void onClientTick(MinecraftClient client) {
        if (client.world == null || client.player == null) return;
        
        ShinyFinderConfig config = ShinyFinderConfig.INSTANCE;
        
        // Auto-start scanner if enabled and we haven't started yet
        if (config.autoStartEnabled && !hasAutoStarted && !scannerEnabled) {
            scannerEnabled = true;
            hasAutoStarted = true;
            client.player.sendMessage(Text.literal("§aShiny Finder auto-started! Press F6 to toggle."), false);
        }
        
        tickCounter++;
        if (tickCounter % config.scanInterval != 0) return;
        
        // Check if scanner is enabled
        if (!scannerEnabled) return;
        
        // Check scan cooldown
        UUID playerId = client.player.getUuid();
        long currentTime = client.world.getTime();
        Long lastScan = lastScanTimes.get(playerId);
        if (lastScan != null && currentTime - lastScan < config.scanInterval) return;
        
        // Perform scan
        scanForShinies(client.player, client.world);
        lastScanTimes.put(playerId, currentTime);
    }
    
    private static void scanForShinies(ClientPlayerEntity player, World world) {
        ShinyFinderConfig config = ShinyFinderConfig.INSTANCE;
        
        try {
            // Use reflection to access Cobblemon classes
            Class<?> pokemonEntityClass = Class.forName("com.cobblemon.mod.common.entity.pokemon.PokemonEntity");
            
            // Get only Pokemon entities directly - more efficient than filtering all entities
            var pokemonEntities = world.getEntitiesByClass(Entity.class, 
                player.getBoundingBox().expand(config.scanRadius), 
                entity -> pokemonEntityClass.isInstance(entity)
            );
            
            // Debug logging
            if (!pokemonEntities.isEmpty()) {
                ShinyFinder.LOGGER.info("Found {} Pokemon entities in range", pokemonEntities.size());
            }
            
            pokemonEntities.forEach(entity -> {
                try {
                    // Get Pokemon from entity using reflection
                    Method getPokemonMethod = pokemonEntityClass.getMethod("getPokemon");
                    Object pokemon = getPokemonMethod.invoke(entity);
                    
                    if (pokemon != null) {
                        // Check if Pokemon is shiny using reflection
                        Method getShinyMethod = pokemon.getClass().getMethod("getShiny");
                        Boolean isShiny = (Boolean) getShinyMethod.invoke(pokemon);
                        
                        if (isShiny != null && isShiny) {
                            // Found a shiny! Send alert
                            sendShinyAlert(player, entity, pokemon);
                        }
                    }
                } catch (Exception e) {
                    ShinyFinder.LOGGER.warn("Error checking Pokemon entity: {}", e.getMessage());
                }
            });
        } catch (ClassNotFoundException e) {
            ShinyFinder.LOGGER.warn("Cobblemon not found or incompatible version");
        } catch (Exception e) {
            ShinyFinder.LOGGER.error("Error scanning for Pokemon: {}", e.getMessage());
        }
    }
    
    private static void sendShinyAlert(ClientPlayerEntity player, Object pokemonEntity, Object pokemon) {
        ShinyFinderConfig config = ShinyFinderConfig.INSTANCE;
        
        try {
            // Get Pokemon name using reflection
            Method getDisplayNameMethod = pokemon.getClass().getMethod("getDisplayName");
            Object displayName = getDisplayNameMethod.invoke(pokemon);
            Method getStringMethod = displayName.getClass().getMethod("getString");
            String pokemonName = (String) getStringMethod.invoke(displayName);
            
            // Get entity position
            Entity entity = (Entity) pokemonEntity;
            String message = String.format("§6§l[SHINY ALERT] §fFound shiny %s at coordinates: %.1f, %.1f, %.1f", 
                pokemonName, 
                entity.getX(), 
                entity.getY(), 
                entity.getZ()
            );
            
            // Send message to player
            player.sendMessage(Text.literal(message).formatted(Formatting.GOLD), false);
            
            // Send HUD notification to client (if enabled)
            if (config.showHUD) {
                sendHUDNotification(player, pokemonName, entity.getX(), entity.getY(), entity.getZ());
            }
            
            // Play sound effect directly (client-side)
            if (config.playSound) {
                try {
                    // Use a simple sound mapping approach
                    net.minecraft.sound.SoundEvent soundEvent = getSimpleSoundEvent(config.alertSound);
                    player.playSound(soundEvent, config.soundVolume, config.soundPitch);
                } catch (Exception e) {
                    // Fallback to experience orb sound on any error
                    player.playSound(net.minecraft.sound.SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 
                        config.soundVolume, config.soundPitch);
                }
            }
            
            // Log to console if enabled
            if (config.logToConsole) {
                ShinyFinder.LOGGER.info("Shiny Pokemon detected for player {}: {} at ({}, {}, {})", 
                    player.getName().getString(), 
                    pokemonName,
                    (int)entity.getX(),
                    (int)entity.getY(),
                    (int)entity.getZ()
                );
            }
        } catch (Exception e) {
            ShinyFinder.LOGGER.error("Error sending shiny alert: {}", e.getMessage());
        }
    }
    
    public static void setScannerEnabled(boolean enabled) {
        scannerEnabled = enabled;
    }
    
    public static boolean isScannerEnabled() {
        return scannerEnabled;
    }
    
    public static void toggleScanner() {
        scannerEnabled = !scannerEnabled;
    }
    
    public static void resetAutoStart() {
        hasAutoStarted = false;
    }
    
    private static net.minecraft.sound.SoundEvent getSimpleSoundEvent(String soundId) {
        // Simple sound mapping - just return experience orb for now
        // This ensures the mod builds and works, then we can add more sounds later
        return net.minecraft.sound.SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
    }
    
    private static void sendHUDNotification(ClientPlayerEntity player, String pokemonName, double x, double y, double z) {
        try {
            // Send title notification (appears on screen)
            player.sendMessage(Text.literal("§6§l✨ SHINY POKEMON DETECTED! ✨")
                .formatted(Formatting.GOLD, Formatting.BOLD), true);
            
            // Send subtitle with Pokemon name and coordinates
            String subtitle = String.format("§e%s §7at %.0f, %.0f, %.0f", 
                pokemonName, x, y, z);
            player.sendMessage(Text.literal(subtitle)
                .formatted(Formatting.YELLOW), true);
                
        } catch (Exception e) {
            ShinyFinder.LOGGER.warn("Failed to send HUD notification: {}", e.getMessage());
        }
    }
}
