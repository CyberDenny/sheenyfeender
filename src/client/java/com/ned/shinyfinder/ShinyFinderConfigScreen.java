package com.ned.shinyfinder;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ShinyFinderConfigScreen extends Screen {
    private final Screen parent;
    private final ShinyFinderConfig config;
    
    // UI Elements
    private SliderWidget scanRadiusSlider;
    private SliderWidget scanIntervalSlider;
    private SliderWidget soundVolumeSlider;
    private SliderWidget soundPitchSlider;
    private TextFieldWidget alertSoundField;
    private ButtonWidget playSoundButton;
    private ButtonWidget showTitleButton;
    private ButtonWidget showHUDButton;
    private ButtonWidget logToConsoleButton;
    private ButtonWidget autoStartButton;
    private ButtonWidget saveButton;
    private ButtonWidget cancelButton;
    
    // Temporary values for editing
    private double tempScanRadius;
    private long tempScanInterval;
    private boolean tempPlaySound;
    private boolean tempShowTitle;
    private boolean tempShowHUD;
    private boolean tempLogToConsole;
    private boolean tempAutoStartEnabled;
    private String tempAlertSound;
    private float tempSoundVolume;
    private float tempSoundPitch;

    public ShinyFinderConfigScreen(Screen parent) {
        super(Text.literal("Shiny Finder Configuration"));
        this.parent = parent;
        this.config = ShinyFinderConfig.INSTANCE;
        
        // Initialize temporary values
        this.tempScanRadius = config.scanRadius;
        this.tempScanInterval = config.scanInterval;
        this.tempPlaySound = config.playSound;
        this.tempShowTitle = config.showTitle;
        this.tempShowHUD = config.showHUD;
        this.tempLogToConsole = config.logToConsole;
        this.tempAutoStartEnabled = config.autoStartEnabled;
        this.tempAlertSound = config.alertSound;
        this.tempSoundVolume = config.soundVolume;
        this.tempSoundPitch = config.soundPitch;
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = 35;
        int spacing = 18;
        int currentY = startY;
        
        // Scan Radius Slider
        this.scanRadiusSlider = new SliderWidget(centerX - 100, currentY, 200, 18, 
            Text.literal("Scan Radius: " + String.format("%.1f", tempScanRadius)), 
            tempScanRadius / 500.0) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Scan Radius: " + String.format("%.1f", tempScanRadius)));
            }
            
            @Override
            protected void applyValue() {
                tempScanRadius = this.value * 500.0;
            }
        };
        this.addDrawableChild(scanRadiusSlider);
        currentY += spacing;
        
        // Scan Interval Slider
        this.scanIntervalSlider = new SliderWidget(centerX - 100, currentY, 200, 18, 
            Text.literal("Scan Interval: " + tempScanInterval + " ticks"), 
            (float) (tempScanInterval - 5) / 95) { // 5-100 ticks range
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Scan Interval: " + tempScanInterval + " ticks"));
            }
            
            @Override
            protected void applyValue() {
                tempScanInterval = (long) (5 + this.value * 95);
            }
        };
        this.addDrawableChild(scanIntervalSlider);
        currentY += spacing;
        
        // Sound Volume Slider
        this.soundVolumeSlider = new SliderWidget(centerX - 100, currentY, 200, 18, 
            Text.literal("Sound Volume: " + String.format("%.1f", tempSoundVolume)), 
            tempSoundVolume) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Sound Volume: " + String.format("%.1f", tempSoundVolume)));
            }
            
            @Override
            protected void applyValue() {
                tempSoundVolume = (float) this.value;
            }
        };
        this.addDrawableChild(soundVolumeSlider);
        currentY += spacing;
        
        // Sound Pitch Slider
        this.soundPitchSlider = new SliderWidget(centerX - 100, currentY, 200, 18, 
            Text.literal("Sound Pitch: " + String.format("%.1f", tempSoundPitch)), 
            (tempSoundPitch - 0.5f) / 1.5f) { // 0.5-2.0 range
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Sound Pitch: " + String.format("%.1f", tempSoundPitch)));
            }
            
            @Override
            protected void applyValue() {
                tempSoundPitch = 0.5f + (float) this.value * 1.5f;
            }
        };
        this.addDrawableChild(soundPitchSlider);
        currentY += spacing;
        
        // Alert Sound Text Field
        this.alertSoundField = new TextFieldWidget(this.textRenderer, centerX - 100, currentY, 200, 18, 
            Text.literal("Alert Sound"));
        this.alertSoundField.setText(tempAlertSound);
        this.addDrawableChild(alertSoundField);
        currentY += spacing;
        
        // Play Sound Button
        this.playSoundButton = ButtonWidget.builder(
            Text.literal("Play Sound: " + (tempPlaySound ? "ON" : "OFF")), 
            button -> {
                tempPlaySound = !tempPlaySound;
                button.setMessage(Text.literal("Play Sound: " + (tempPlaySound ? "ON" : "OFF")));
            }
        ).dimensions(centerX - 100, currentY, 200, 18).build();
        this.addDrawableChild(playSoundButton);
        currentY += spacing;
        
        // Show Title Button
        this.showTitleButton = ButtonWidget.builder(
            Text.literal("Show Title: " + (tempShowTitle ? "ON" : "OFF")), 
            button -> {
                tempShowTitle = !tempShowTitle;
                button.setMessage(Text.literal("Show Title: " + (tempShowTitle ? "ON" : "OFF")));
            }
        ).dimensions(centerX - 100, currentY, 200, 18).build();
        this.addDrawableChild(showTitleButton);
        currentY += spacing;
        
        // Show HUD Button
        this.showHUDButton = ButtonWidget.builder(
            Text.literal("Show HUD: " + (tempShowHUD ? "ON" : "OFF")), 
            button -> {
                tempShowHUD = !tempShowHUD;
                button.setMessage(Text.literal("Show HUD: " + (tempShowHUD ? "ON" : "OFF")));
            }
        ).dimensions(centerX - 100, currentY, 200, 18).build();
        this.addDrawableChild(showHUDButton);
        currentY += spacing;
        
        // Log to Console Button
        this.logToConsoleButton = ButtonWidget.builder(
            Text.literal("Log to Console: " + (tempLogToConsole ? "ON" : "OFF")), 
            button -> {
                tempLogToConsole = !tempLogToConsole;
                button.setMessage(Text.literal("Log to Console: " + (tempLogToConsole ? "ON" : "OFF")));
            }
        ).dimensions(centerX - 100, currentY, 200, 18).build();
        this.addDrawableChild(logToConsoleButton);
        currentY += spacing;
        
        // Auto Start Button
        this.autoStartButton = ButtonWidget.builder(
            Text.literal("Auto Start: " + (tempAutoStartEnabled ? "ON" : "OFF")), 
            button -> {
                tempAutoStartEnabled = !tempAutoStartEnabled;
                button.setMessage(Text.literal("Auto Start: " + (tempAutoStartEnabled ? "ON" : "OFF")));
            }
        ).dimensions(centerX - 100, currentY, 200, 18).build();
        this.addDrawableChild(autoStartButton);
        currentY += spacing + 5;
        
        // Save Button
        this.saveButton = ButtonWidget.builder(
            Text.literal("Save Configuration"), 
            button -> saveConfig()
        ).dimensions(centerX - 110, currentY, 100, 18).build();
        this.addDrawableChild(saveButton);
        
        // Cancel Button
        this.cancelButton = ButtonWidget.builder(
            Text.literal("Cancel"), 
            button -> close()
        ).dimensions(centerX + 10, currentY, 100, 18).build();
        this.addDrawableChild(cancelButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        
        // Draw title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        
        // Draw description
        context.drawCenteredTextWithShadow(this.textRenderer, 
            Text.literal("Configure Shiny Finder settings").formatted(Formatting.GRAY), 
            this.width / 2, 22, 0x808080);
        
        super.render(context, mouseX, mouseY, delta);
    }

    private void saveConfig() {
        // Update config with temporary values
        config.scanRadius = tempScanRadius;
        config.scanInterval = tempScanInterval;
        config.playSound = tempPlaySound;
        config.showTitle = tempShowTitle;
        config.showHUD = tempShowHUD;
        config.logToConsole = tempLogToConsole;
        config.autoStartEnabled = tempAutoStartEnabled;
        config.alertSound = alertSoundField.getText();
        config.soundVolume = tempSoundVolume;
        config.soundPitch = tempSoundPitch;
        
        // Save to file
        config.save();
        
        // Show success message
        if (this.client != null && this.client.player != null) {
            this.client.player.sendMessage(Text.literal("§aShiny Finder configuration saved!"), false);
        }
        
        // Close screen
        close();
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
