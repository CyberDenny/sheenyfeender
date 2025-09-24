package com.ned.shinyfinder;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ShinyFinderKeybinds {
    public static KeyBinding toggleScannerKey;
    public static KeyBinding enableScannerKey;
    public static KeyBinding disableScannerKey;
    public static KeyBinding openConfigKey;
    
    public static void register() {
        toggleScannerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.shinyfinder.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F6, // F6 key
            "category.shinyfinder.general"
        ));
        
        enableScannerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.shinyfinder.enable",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F7, // F7 key
            "category.shinyfinder.general"
        ));
        
        disableScannerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.shinyfinder.disable",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F8, // F8 key
            "category.shinyfinder.general"
        ));
        
        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.shinyfinder.config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F9, // F9 key
            "category.shinyfinder.general"
        ));
    }
}

