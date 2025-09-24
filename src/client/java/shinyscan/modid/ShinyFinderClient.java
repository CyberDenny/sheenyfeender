package com.ned.shinyfinder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ShinyFinderClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Register keybinds
		ShinyFinderKeybinds.register();
		
		// Register client tick event for scanning
		ClientTickEvents.END_CLIENT_TICK.register(ShinyFinderClientCore::onClientTick);
		
		// Reset auto-start when joining a new world - disabled for now due to API issues
		// ClientWorldEvents.LOAD.register((client, world) -> {
		//	ShinyFinderClientCore.resetAutoStart();
		// });
		
		// Register key press handling
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			
			// Handle key presses
			if (ShinyFinderKeybinds.toggleScannerKey.wasPressed()) {
				ShinyFinderClientCore.toggleScanner();
				boolean enabled = ShinyFinderClientCore.isScannerEnabled();
				String message = enabled ? 
					"§aShiny Finder enabled! You will be alerted when shiny Pokemon are nearby." :
					"§cShiny Finder disabled.";
				client.player.sendMessage(Text.literal(message), false);
			}
			
			if (ShinyFinderKeybinds.enableScannerKey.wasPressed()) {
				ShinyFinderClientCore.setScannerEnabled(true);
				client.player.sendMessage(Text.literal("§aShiny Finder enabled!"), false);
			}
			
			if (ShinyFinderKeybinds.disableScannerKey.wasPressed()) {
				ShinyFinderClientCore.setScannerEnabled(false);
				client.player.sendMessage(Text.literal("§cShiny Finder disabled."), false);
			}
			
			if (ShinyFinderKeybinds.openConfigKey.wasPressed()) {
				client.setScreen(new ShinyFinderConfigScreen(client.currentScreen));
			}
		});
	}
}