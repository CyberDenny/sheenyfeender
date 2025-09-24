# Shiny Finder Mod

A Minecraft Fabric mod that automatically detects shiny Pokemon in the surrounding area and alerts you when one is discovered!

## ✨ Features

### 🔍 Core Functionality
- **Automatic Shiny Detection**: Continuously scans for shiny Pokemon in a configurable radius around you
- **Real-time Alerts**: Instant notifications when shiny Pokemon are discovered
- **Multi-format Notifications**: 
  - Chat messages with Pokemon name and exact coordinates
  - Visual title notifications on screen
  - Sound alerts (configurable)
  - Console logging for server admins

### ⚙️ Customization Options
- **Scan Radius**: Adjustable detection range (0-500 blocks)
- **Scan Interval**: Configurable scan frequency (5-100 ticks)
- **Sound Settings**: Customizable alert sounds, volume, and pitch
- **Visual Options**: Toggle HUD notifications and title displays
- **Auto-start**: Automatically enables when joining a world/server

### 🎮 User Controls
- **Keybinds**: 
  - **F6**: Toggle scanner on/off
  - **F7**: Enable scanner
  - **F8**: Disable scanner
  - **F9**: Open configuration screen
- **Client-side**: Works on any multiplayer server (no server permissions needed)
- **In-game Configuration**: Modify all settings without restarting

### 🔧 Technical Features
- **Cobblemon Integration**: Seamless compatibility with Cobblemon Pokemon mod
- **Reflection-based**: Works with any compatible Cobblemon version
- **Multiplayer Ready**: Client-side operation bypasses server restrictions
- **Performance Optimized**: Efficient entity scanning and filtering
- **Error Handling**: Graceful fallbacks and comprehensive logging

## 📋 Requirements

### Required
- **Minecraft**: 1.21.1
- **Fabric Loader**: 0.16.14 or higher
- **Fabric API**: Latest version
- **Java**: 21 or higher

### Recommended
- **Cobblemon**: Latest version (required for Pokemon detection)
- **Xaero's Minimap**: For waypoint integration (optional)

## 🚀 Installation

1. **Install Fabric Loader** for Minecraft 1.21.1
2. **Download Fabric API** and place it in your mods folder
3. **Download Cobblemon** mod and place it in your mods folder
4. **Download Shiny Finder** from the [Releases](https://github.com/yourusername/shiny-finder/releases) page
5. **Place the JAR file** in your mods folder
6. **Launch Minecraft** and enjoy!

## 🎯 How to Use

### Basic Usage
1. **Join a world** - The scanner auto-starts (if enabled in config)
2. **Press F6** to toggle the scanner on/off
3. **Press F9** to open the configuration screen
4. **Adjust settings** as needed

### Configuration
- **In-game**: Press F9 to open the configuration screen
- **Manual**: Edit `shinyfinder.json` in your config folder
- **Settings persist** between game sessions

### Keybinds
- **F6**: Toggle Shiny Finder
- **F7**: Enable Shiny Finder
- **F8**: Disable Shiny Finder
- **F9**: Open Configuration

## ⚙️ Configuration

The mod creates a `shinyfinder.json` file in your config folder with these options:

```json
{
  "scanRadius": 50.0,
  "scanInterval": 10,
  "playSound": true,
  "showTitle": true,
  "showHUD": true,
  "logToConsole": true,
  "alertSound": "entity.experience_orb.pickup",
  "soundVolume": 1.0,
  "soundPitch": 1.5,
  "autoStartEnabled": true
}
```

### Configuration Options
- **scanRadius**: Detection range in blocks (0-500)
- **scanInterval**: Scan frequency in ticks (5-100)
- **playSound**: Enable/disable sound alerts
- **showTitle**: Enable/disable title notifications
- **showHUD**: Enable/disable HUD notifications
- **logToConsole**: Enable/disable console logging
- **alertSound**: Sound ID for alerts
- **soundVolume**: Volume level (0.0-1.0)
- **soundPitch**: Pitch level (0.5-2.0)
- **autoStartEnabled**: Auto-enable when joining world

## 🐛 Troubleshooting

### Common Issues
- **Scanner not working**: Make sure Cobblemon is installed and up to date
- **No sound alerts**: Check your sound settings and volume
- **Performance issues**: Increase scan interval or decrease scan radius
- **Mod not loading**: Ensure you have Fabric Loader and Fabric API

### Getting Help
- **Issues**: Report bugs on the [GitHub Issues](https://github.com/yourusername/shiny-finder/issues) page
- **Discord**: Join our Discord server for support
- **Wiki**: Check the [Wiki](https://github.com/yourusername/shiny-finder/wiki) for detailed guides

## 🔄 Updates

### Automatic Updates
- **GitHub Releases**: Check the [Releases](https://github.com/yourusername/shiny-finder/releases) page for updates
- **Modrinth**: Available on Modrinth for easy updates
- **CurseForge**: Coming soon!

### Manual Updates
1. **Download** the latest release
2. **Replace** the old JAR file in your mods folder
3. **Restart** Minecraft

## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch
3. **Make** your changes
4. **Test** thoroughly
5. **Submit** a pull request

### Development Setup
1. **Clone** the repository
2. **Run** `./gradlew build` to build the mod
3. **Test** in your development environment

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Ned** - [NedNet](https://nednet.pages.dev/tutorials/shiny-finder)

## 📥 Download

Get the latest version from the [Releases page](https://github.com/CyberDenny/sheenyfeender/releases/latest)

*Note: Only the main JAR file is included in releases (sources JAR excluded for smaller download size)*

## 🙏 Acknowledgments

- **Cobblemon Team** for the amazing Pokemon mod
- **Fabric Team** for the modding framework
- **Community** for feedback and suggestions

## 📊 Statistics

- **Downloads**: [![Downloads](https://img.shields.io/github/downloads/yourusername/shiny-finder/total.svg)](https://github.com/yourusername/shiny-finder/releases)
- **Stars**: [![Stars](https://img.shields.io/github/stars/yourusername/shiny-finder.svg)](https://github.com/yourusername/shiny-finder)
- **Issues**: [![Issues](https://img.shields.io/github/issues/yourusername/shiny-finder.svg)](https://github.com/yourusername/shiny-finder/issues)

---

**Made with ❤️ for the Minecraft Pokemon community**