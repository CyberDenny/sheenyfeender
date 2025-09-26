package com.ned.shinyfinder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.text.Text;

public class ShinyFinderClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Register keybinds
		ShinyFinderKeybinds.register();
		
		// Register HUD overlay
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> ShinyFinderHudOverlay.renderGameOverlayEvent(drawContext));
		
		// Register client tick event for scanning
		ClientTickEvents.END_CLIENT_TICK.register(ShinyFinderClientCore::onClientTick);
		
		// Reset auto-start when joining a new world - disabled for now due to API issues
		// ClientWorldEvents.LOAD.register((client, world) -> {
		//	ShinyFinderClientCore.resetAutoStart();
		// });
		
		// Register key press handling
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			
			// Handle toggle key press
			if (ShinyFinderKeybinds.toggleScannerKey.wasPressed()) {
				ShinyFinderClientCore.toggleScanner();
				boolean enabled = ShinyFinderClientCore.isScannerEnabled();
				// Use HUD message like XRay - try different approach
				if (enabled) {
					client.player.sendMessage(Text.literal("§aShiny Finder activated"), true); // true = action bar
				} else {
					client.player.sendMessage(Text.literal("§cShiny Finder deactivated"), true); // true = action bar
				}
			}
			
			// Handle config key press
			if (ShinyFinderKeybinds.openConfigKey.wasPressed()) {
				client.setScreen(new ShinyFinderConfigScreen(client.currentScreen));
			}
		});
	}
}