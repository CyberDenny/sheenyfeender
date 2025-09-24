package com.ned.shinyfinder;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ShinyFinderCommands {
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        ShinyFinder.LOGGER.info("Registering Shiny Finder commands...");
        dispatcher.register(CommandManager.literal("shinyfinder")
            .requires(source -> source.hasPermissionLevel(0)) // Anyone can use
            .executes(ShinyFinderCommands::testCommand) // Test command
            .then(CommandManager.literal("toggle")
                .requires(source -> source.hasPermissionLevel(0)) // Anyone can use
                .executes(ShinyFinderCommands::toggleScanner)
            )
            .then(CommandManager.literal("on")
                .requires(source -> source.hasPermissionLevel(0)) // Anyone can use
                .executes(ShinyFinderCommands::enableScanner)
            )
            .then(CommandManager.literal("off")
                .requires(source -> source.hasPermissionLevel(0)) // Anyone can use
                .executes(ShinyFinderCommands::disableScanner)
            )
            .then(CommandManager.literal("enable")
                .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes(ShinyFinderCommands::setScannerState)
                )
            )
            .then(CommandManager.literal("status")
                .executes(ShinyFinderCommands::getScannerStatus)
            )
            .then(CommandManager.literal("config")
                .requires(source -> source.hasPermissionLevel(2)) // OP only
                .then(CommandManager.literal("reload")
                    .executes(ShinyFinderCommands::reloadConfig)
                )
            )
        );
        
        // Alternative command name in case "shinyfinder" is blocked
        dispatcher.register(CommandManager.literal("ss")
            .requires(source -> source.hasPermissionLevel(0)) // Anyone can use
            .executes(ShinyFinderCommands::testCommand) // Test command
            .then(CommandManager.literal("toggle")
                .requires(source -> source.hasPermissionLevel(0)) // Anyone can use
                .executes(ShinyFinderCommands::toggleScanner)
            )
            .then(CommandManager.literal("on")
                .requires(source -> source.hasPermissionLevel(0)) // Anyone can use
                .executes(ShinyFinderCommands::enableScanner)
            )
            .then(CommandManager.literal("off")
                .requires(source -> source.hasPermissionLevel(0)) // Anyone can use
                .executes(ShinyFinderCommands::disableScanner)
            )
        );
    }
    
    private static int testCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.literal("§aShiny Finder mod is working! Commands registered successfully."), false);
        return 1;
    }
    
    private static int enableScanner(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ShinyFinderCore.setScannerEnabled(source.getPlayer().getUuid(), true);
        source.sendFeedback(() -> Text.literal("§aShiny Finder enabled! You will be alerted when shiny Pokemon are nearby."), false);
        return 1;
    }
    
    private static int disableScanner(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ShinyFinderCore.setScannerEnabled(source.getPlayer().getUuid(), false);
        source.sendFeedback(() -> Text.literal("§cShiny Finder disabled."), false);
        return 1;
    }
    
    private static int toggleScanner(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ShinyFinderCore.toggleScanner(source.getPlayer().getUuid());
        
        boolean isEnabled = ShinyFinderCore.isScannerEnabled(source.getPlayer().getUuid());
        String message = isEnabled ? 
            "§aShiny Finder enabled! You will be alerted when shiny Pokemon are nearby." :
            "§cShiny Finder disabled.";
            
        source.sendFeedback(() -> Text.literal(message), false);
        return 1;
    }
    
    private static int setScannerState(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        
        ShinyFinderCore.setScannerEnabled(source.getPlayer().getUuid(), enabled);
        
        String message = enabled ? 
            "§aShiny Finder enabled! You will be alerted when shiny Pokemon are nearby." :
            "§cShiny Finder disabled.";
            
        source.sendFeedback(() -> Text.literal(message), false);
        return 1;
    }
    
    private static int getScannerStatus(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        boolean isEnabled = ShinyFinderCore.isScannerEnabled(source.getPlayer().getUuid());
        
        String message = isEnabled ? 
            "§aShiny Finder is currently §lENABLED§r§a." :
            "§cShiny Finder is currently §lDISABLED§r§c.";
            
        source.sendFeedback(() -> Text.literal(message), false);
        return 1;
    }
    
    private static int reloadConfig(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ShinyFinderConfig.INSTANCE = ShinyFinderConfig.load();
        
        source.sendFeedback(() -> Text.literal("§aShiny Finder configuration reloaded!"), true);
        return 1;
    }
    
}
