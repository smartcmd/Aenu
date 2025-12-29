# 🎛️ Aenu - Menu Plugin for AllayMC

✨ A powerful, configurable menu plugin for **AllayMC** servers with full **PlaceholderAPI** support.

## ✨ Features

- 🧩 **SimpleForm Menus** – Clean, intuitive menu interface
- **Chest Menus** – FakeChest/FakeDoubleChest UI support
- 📝 **YAML Configuration** – Easy-to-edit menu files
- 🔁 **PlaceholderAPI Integration** – Use placeholders in titles, content, commands, and messages
- 💬 **Direct Messages** – Send messages directly to players without using commands
- ⚡ **Command Execution** – Execute commands when buttons are clicked
- ♻️ **Hot Reload** – Reload menus without restarting the server
- 🔐 **Permission System** – Control access to menus and buttons with permissions
- 🖼️ **Button Images** – Support for custom button icons (path or URL)

## 📦 Installation

1. ⬇️ Download the plugin JAR from the releases page
2. 📁 Place it in your server's `plugins` folder
3. ✅ Ensure **PlaceholderAPI** plugin is also installed
4. 🔄 Restart your server
5. 📄 Example menu configurations will be automatically generated at `plugins/Aenu/example.yml` and `plugins/Aenu/example_chest.yml`
6. ✏️ Edit or create your own menu files in the `plugins/Aenu/` folder
7. 🔁 Use `/menu reload` to reload menus after making changes

## ⌨️ Commands

| Command                  | Description                    | Permission                 |
|--------------------------|--------------------------------|----------------------------|
| `/menu open <menu_name>` | Open a menu                    | `aenu.command.menu.open`   |
| `/menu reload`           | Reload all menu configurations | `aenu.command.menu.reload` |
| `/menu list`             | List all accessible menus      | `aenu.command.menu.list`   |

## 🗂️ Menu Configuration

Menus are stored as **YAML** files in the plugin data folder.  
Each file represents **one menu**.

### ⚙️ Menu Configuration Options

Each menu supports the following fields:

- 🏷️ **title** – The menu title
- **ui** *(optional)* – UI type: `form`, `chest`, or `double_chest` (default: `form`)
- 📜 **content** – The menu description text (form UI only)
- 🔐 **permission** *(optional)* – Permission required to open the menu
- 🔘 **buttons** – List of buttons in the menu

### 🔘 Button Configuration Options

Each button supports the following fields:

- 📝 **text** – The button display text
- **item** *(optional, chest UI)* – Item identifier like `minecraft:diamond`
- **count** *(optional, chest UI)* – Item count (default: 1)
- **meta** *(optional, chest UI)* – Item meta/damage value (default: 0)
- **lore** *(optional, chest UI)* – List of lore lines
- **slot** *(optional, chest UI)* – Slot index (0-26 for chest, 0-53 for double chest)
- **close** *(optional, chest UI)* – Close the menu after click (default: false)
- 🔑 **permission** *(optional)* – Permission required to see this button
- 💬 **messages** *(optional)* – List of messages to send to the player
- ⚡ **commands** *(optional)* – List of commands to execute
- 🖼️ **image** *(optional)* – Button icon configuration

### 📘 Example Menu

```yaml
# Menu title
title: "Example Menu"
ui: "form"

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
````

### 📦 Chest Menu Example

```yaml
title: "Chest Menu"
ui: "chest"

# Content is ignored for chest UI
# content: "This is ignored in chest UI"

buttons:
  - text: "Get Diamond"
    item: "minecraft:diamond"
    slot: 10
    lore:
      - "§7Click to receive a diamond"
    messages:
      - "§aYou received a diamond!"
    commands:
      - "give \"{player_name}\" minecraft:diamond 1"

  - text: "Teleport to Spawn"
    item: "minecraft:ender_pearl"
    slot: 13
    commands:
      - "tp \"{player_name}\" 0 100 0"

  - text: "Close"
    item: "minecraft:barrier"
    slot: 26
    close: true
```

## 🔐 Permission System

Aenu supports a **flexible permission system** for both menus and buttons.

### 📂 Menu Permissions

Restrict who can open specific menus by adding a `permission` field:

```yaml
title: "Admin Menu"
content: "Administrative tools"
permission: "aenu.menu.admin"

buttons:
  - text: "Ban Player"
    commands:
      - "ban {player_name}"
```

### 🔘 Button Permissions

Control which buttons players can see by adding permissions to individual buttons:

```yaml
title: "Server Menu"
content: "Choose an option:"

buttons:
  - text: "Get Started Kit"
    commands:
      - "give \"{player_name}\" iron_sword 1"

  - text: "§6[VIP] Diamond Kit"
    permission: "aenu.button.vip"
    commands:
      - "give \"{player_name}\" diamond_sword 1"

  - text: "§c[Admin] Creative Mode"
    permission: "aenu.button.admin"
    commands:
      - "gamemode creative \"{player_name}\""
```

### ⚙️ How It Works

- ✅ **Menu Permission** – Without permission, players receive an error when opening
- ✅ **Button Permission** – Buttons are automatically hidden if access is denied
- ✅ **No Permission Set** – Everyone can access the menu/button

### 🧱 Example: Tiered Access Menu

```yaml
title: "Rank Shop"
content: "Purchase items based on your rank!"

buttons:
  - text: "Basic Kit"
    messages:
      - "§7You received the basic kit!"
    commands:
      - "give \"{player_name}\" stone_sword 1"

  - text: "§6VIP Kit"
    permission: "shop.vip"
    messages:
      - "§6You received the VIP kit!"
    commands:
      - "give \"{player_name}\" iron_sword 1 {Enchantments:[{id:sharpness,lvl:2}]}"

  - text: "§bPremium Kit"
    permission: "shop.premium"
    messages:
      - "§bYou received the Premium kit!"
    commands:
      - "give \"{player_name}\" diamond_sword 1 {Enchantments:[{id:sharpness,lvl:5}]}"
```

👀 Players will only see buttons they have permission for!

## 🔁 PlaceholderAPI Support

All text fields (**title**, **content**, **messages**, **commands**) support
🔧 **PlaceholderAPI** placeholders.

📚 For a complete list, see:
👉 [https://github.com/AllayMC/PlaceholderAPI](https://github.com/AllayMC/PlaceholderAPI)

## 💡 Example Use Cases

### 🚀 Teleport Menu

```yaml
title: "Teleport Menu"
content: "Choose a destination:"
```

### 📊 Info Menu (Messages Only)

```yaml
title: "Player Information"
content: "View your statistics"
```

## ♻️ Reloading Menus

### ✅ Method 1: `/menu reload` (Recommended)

```
/menu reload
```

### ⚠️ Method 2: Server reload

```
/reload plugin Aenu
```

## 📜 Listing Available Menus

Use `/menu list` to view all accessible menus:

```
/menu list
```

📋 Output includes:

- Number of accessible menus
- Menu names and commands
- Permission-based filtering

## 📄 License

🪪 This project is open source and released under the **MIT License**.
