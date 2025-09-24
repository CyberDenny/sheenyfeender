package com.ned.shinyfinder;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShinyFinder implements ModInitializer {
	public static final String MOD_ID = "shinyfinder";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Shiny Finder mod initialized!");
		
		// Commands removed - using keybinds instead for multiplayer compatibility
		// ServerTickEvents.END_SERVER_TICK.register(ShinyFinderCore::onServerTick);
		
		// Test change to trigger v1.0.2 release
	}
}