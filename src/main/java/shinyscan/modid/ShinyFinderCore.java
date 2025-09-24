package com.ned.shinyfinder;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShinyFinderCore {
    private static final Map<UUID, Boolean> playerScannerStates = new HashMap<>();
    private static final Map<UUID, Long> lastScanTimes = new HashMap<>();
    
    public static void onServerTick(MinecraftServer server) {
        ShinyFinderConfig config = ShinyFinderConfig.INSTANCE;
        if (server.getTicks() % config.scanInterval != 0) return;
        
        // Debug logging
        if (server.getTicks() % 200 == 0) { // Log every 10 seconds
            ShinyFinder.LOGGER.info("Scanner tick - checking for Pokemon entities...");
        }
        
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            UUID playerId = player.getUuid();
            
            // Check if scanner is enabled for this player
            if (!isScannerEnabled(playerId)) continue;
            
            // Debug logging
            if (server.getTicks() % 200 == 0) { // Log every 10 seconds
                ShinyFinder.LOGGER.info("Scanning for player: {}", player.getName().getString());
            }
            
            // Check scan cooldown
            long currentTime = server.getOverworld().getTime();
            Long lastScan = lastScanTimes.get(playerId);
            if (lastScan != null && currentTime - lastScan < config.scanInterval) continue;
            
            // Perform scan
            scanForShinies(player, server);
            lastScanTimes.put(playerId, currentTime);
        }
    }
    
    private static void scanForShinies(ServerPlayerEntity player, MinecraftServer server) {
        World world = player.getWorld();
        ShinyFinderConfig config = ShinyFinderConfig.INSTANCE;
        
        try {
            // Use reflection to access Cobblemon classes
            Class<?> pokemonEntityClass = Class.forName("com.cobblemon.mod.common.entity.pokemon.PokemonEntity");
            
            // Get all entities in range and filter for Pokemon entities
            world.getEntitiesByClass(Entity.class, 
                player.getBoundingBox().expand(config.scanRadius), 
                entity -> pokemonEntityClass.isInstance(entity)
            ).forEach(entity -> {
                ShinyFinder.LOGGER.info("Found Pokemon entity: {}", entity.getClass().getSimpleName());
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
    
    private static void sendShinyAlert(ServerPlayerEntity player, Object pokemonEntity, Object pokemon) {
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
            
            // Play sound effect directly (server-side but should be audible)
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
    
    public static void setScannerEnabled(UUID playerId, boolean enabled) {
        playerScannerStates.put(playerId, enabled);
    }
    
    public static boolean isScannerEnabled(UUID playerId) {
        return playerScannerStates.getOrDefault(playerId, false);
    }
    
    public static void toggleScanner(UUID playerId) {
        boolean currentState = isScannerEnabled(playerId);
        setScannerEnabled(playerId, !currentState);
    }
    
    private static net.minecraft.sound.SoundEvent getSimpleSoundEvent(String soundId) {
        // Simple sound mapping - just return experience orb for now
        // This ensures the mod builds and works, then we can add more sounds later
        return net.minecraft.sound.SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
    }
    
    private static void sendHUDNotification(ServerPlayerEntity player, String pokemonName, double x, double y, double z) {
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
