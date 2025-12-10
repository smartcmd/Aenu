# Aenu - Menu Plugin for AllayMC

A powerful, configurable menu plugin for AllayMC servers with full PlaceholderAPI support.

## Features

- **SimpleForm Menus** - Clean, intuitive menu interface
- **YAML Configuration** - Easy-to-edit menu files
- **PlaceholderAPI Integration** - Use placeholders in titles, content, commands, and messages
- **Direct Messages** - Send messages directly to players without using commands
- **Command Execution** - Execute commands when buttons are clicked
- **Hot Reload** - Reload menus without restarting the server
- **Permission System** - Control access to menus and buttons with permissions
- **Button Images** - Support for custom button icons (path or URL)

## Installation

1. Download the plugin JAR from the releases page
2. Place it in your server's `plugins` folder
3. Ensure **PlaceholderAPI** plugin is also installed
4. Restart your server
5. An example menu configuration will be automatically generated at `plugins/Aenu/example.yml`
6. Edit or create your own menu files in the `plugins/Aenu/` folder
7. Use `/menu reload` to reload menus after making changes

## Commands

| Command                  | Description                    | Permission                 |
|--------------------------|--------------------------------|----------------------------|
| `/menu open <menu_name>` | Open a menu                    | `aenu.command.menu.open`   |
| `/menu reload`           | Reload all menu configurations | `aenu.command.menu.reload` |
| `/menu list`             | List all accessible menus      | `aenu.command.menu.list`   |

## Menu Configuration

Menus are stored as YAML files in the plugin data folder. Each file represents one menu.

### Menu Configuration Options

Each menu supports the following fields:

- **title** - The menu title
- **content** - The menu description text
- **permission** (optional) - Permission required to open the menu
- **buttons** - List of buttons in the menu

### Button Configuration Options

Each button supports the following fields:

- **text** - The button display text
- **permission** (optional) - Permission required to see this button
- **messages** (optional) - List of messages to send to the player
- **commands** (optional) - List of commands to execute
- **image** (optional) - Button icon configuration

### Example Menu

```yaml
# Menu title
title: "Example Menu"

# Menu content/description
content: "Welcome {player_name}! Choose an option:"

# List of buttons
buttons:
  # Button with messages and commands
  - text: "Get Diamond"
    image:
      type: "path"
      data: "textures/items/diamond.png"
    messages:
      - "§aYou received a diamond!"
      - "§7Use it wisely, {player_name}!"
    commands:
      - "give \"{player_name}\" minecraft:diamond 1"

  # Button with only commands (old way still works)
  - text: "Teleport to Spawn"
    commands:
      - "tp \"{player_name}\" 0 100 0"

  # Button with only messages (no commands needed!)
  - text: "Show Info"
    messages:
      - "§e========== Player Info =========="
      - "§bName: §f{player_name}"
      - "§bPosition: §fX={x} Y={y} Z={z}"
      - "§bGame mode: §f{game_mode}"
      - "§bDimension: §f{dimension}"
      - "§e================================="

  # Button with both messages and commands
  - text: "Heal Me"
    messages:
      - "§aHealing you now..."
      - "§7You have been fully healed!"
    commands:
      - "effect \"{player_name}\" instant_health 1 255"
```

## Permission System

Aenu supports a flexible permission system for both menus and buttons.

### Menu Permissions

Restrict who can open specific menus by adding a `permission` field:

```yaml
title: "Admin Menu"
content: "Administrative tools"
permission: "aenu.menu.admin"  # Only players with this permission can open

buttons:
  - text: "Ban Player"
    commands:
      - "ban {player_name}"
```

### Button Permissions

Control which buttons players can see by adding permissions to individual buttons:

```yaml
title: "Server Menu"
content: "Choose an option:"

buttons:
  # Everyone can see this button
  - text: "Get Started Kit"
    commands:
      - "give \"{player_name}\" iron_sword 1"

  # Only players with permission can see this
  - text: "§6[VIP] Diamond Kit"
    permission: "aenu.button.vip"
    commands:
      - "give \"{player_name}\" diamond_sword 1"

  # Only players that has `aenu.button.admin` permission can see this
  - text: "§c[Admin] Creative Mode"
    permission: "aenu.button.admin"
    commands:
      - "gamemode creative \"{player_name}\""
```

### How It Works

- **Menu Permission**: If a player doesn't have the menu permission, they get an error message when trying to open it
- **Button Permission**: Buttons without permission are automatically hidden from players who don't have access
- **No Permission Set**: If no permission is configured, everyone can access the menu/button

### Example: Tiered Access Menu

```yaml
title: "Rank Shop"
content: "Purchase items based on your rank!"

buttons:
  # Free for everyone
  - text: "Basic Kit"
    messages:
      - "§7You received the basic kit!"
    commands:
      - "give \"{player_name}\" stone_sword 1"

  # VIP only
  - text: "§6VIP Kit"
    permission: "shop.vip"
    messages:
      - "§6You received the VIP kit!"
    commands:
      - "give \"{player_name}\" iron_sword 1 {Enchantments:[{id:sharpness,lvl:2}]}"

  # Premium only
  - text: "§bPremium Kit"
    permission: "shop.premium"
    messages:
      - "§bYou received the Premium kit!"
    commands:
      - "give \"{player_name}\" diamond_sword 1 {Enchantments:[{id:sharpness,lvl:5}]}"
```

Players will only see the buttons they have permission for!

## PlaceholderAPI Support

All text fields (title, content, messages, commands) support PlaceholderAPI placeholders. For a complete
list, see [PlaceholderAPI](https://github.com/AllayMC/PlaceholderAPI).

## Example Use Cases

### Teleport Menu

```yaml
title: "Teleport Menu"
content: "Choose a destination:"

buttons:
  - text: "Spawn"
    messages:
      - "§aTeleporting to spawn..."
    commands:
      - "tp \"{player_name}\" 0 100 0"

  - text: "PvP Arena"
    messages:
      - "§cEntering PvP Arena!"
      - "§7Good luck, {player_name}!"
    commands:
      - "tp \"{player_name}\" 1000 80 1000"
      - "gamemode adventure \"{player_name}\""
```

### Info Menu (Messages Only)

```yaml
title: "Player Information"
content: "View your statistics"

buttons:
  - text: "Show Stats"
    messages:
      - "§e========== Your Stats =========="
      - "§bName: §f{player_name}"
      - "§bPosition: §fX={x} Y={y} Z={z}"
      - "§bDimension: §f{dimension}"
      - "§bGame Mode: §f{game_mode}"
      - "§bExp Level: §f{exp_level}"
      - "§bPing: §f{ping}ms"
      - "§e================================"
```

## Reloading Menus

There are two ways to reload menu configurations without restarting the server:

### Method 1: Using `/menu reload` (Recommended)

```
/menu reload
```

### Method 2: Using server reload command

```
/reload plugin Aenu
```

## Listing Available Menus

Players can use `/menu list` to see all menus they have permission to access:

```
/menu list
```

This will display a formatted list showing:
- The number of accessible menus
- Menu names with usage commands
- Only menus the player has permission to open

Example output:
```
You can access 3 menu(s):
  ▪ example - /menu example
  ▪ shop - /menu shop
  ▪ teleport - /menu teleport
```

If a menu has a permission requirement, it will only appear in the list for players who have that permission.

## License

This project is open source and available under the MIT License.