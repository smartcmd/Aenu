package me.daoge.aenu.config;

import org.intellij.lang.annotations.Language;

public final class DefaultMenuConfigs {

    private DefaultMenuConfigs() {
    }

    @Language("yml")
    public static final String FORM = """
            # Example menu configuration for Aenu
            # Menu name is the filename without .yml extension

            # Title of the menu
            title: "Example Menu"
            ui: "form"

            # Content/description text
            content: "Welcome {player_name}! Choose an option below:"

            # Optional: Permission required to open this menu
            # If not set, everyone can open it
            # permission: "aenu.menu.example"

            # List of buttons
            buttons:
              # Button with direct message (no commands needed!)
              - text: "Get Diamond"
                image:
                  type: "path"
                  data: "textures/items/diamond.png"
                messages:
                  - "\\u00a7aYou received a diamond!"
                  - "\\u00a77Use it wisely, {player_name}!"
                commands:
                  - 'give "{player_name}" minecraft:diamond 1'

              # Button with only command (old way still works)
              - text: "Teleport to Spawn"
                commands:
                  - 'tp "{player_name}" 0 100 0'

              # Button with only messages (no commands)
              - text: "Show Info"
                messages:
                  - "\\u00a7e========== Player Info =========="
                  - "\\u00a7bName: \\u00a7f{player_name}"
                  - "\\u00a7bPosition: \\u00a7fX={x} Y={y} Z={z}"
                  - "\\u00a7bGame mode: \\u00a7f{game_mode}"
                  - "\\u00a7bDimension: \\u00a7f{dimension}"
                  - "\\u00a7bExp Level: \\u00a7f{exp_level}"
                  - "\\u00a7e================================="

              # Button with permission requirement (only OPs can see)
              - text: "\\u00a7c[Admin] Heal Me"
                permission: "aenu.button.heal"
                messages:
                  - "\\u00a7aHealing you now..."
                  - "\\u00a77You have been fully healed!"
                commands:
                  - 'effect "{player_name}" instant_health 1 255'
                  - 'effect "{player_name}" saturation 1 255'
            """;

    @Language("yml")
    public static final String CHEST = """
            # Example chest menu configuration for Aenu
            # Menu name is the filename without .yml extension

            # Title of the menu
            title: "Quick Actions - {player_name}"
            ui: "chest"

            # Content is ignored for chest UI
            # content: "This is ignored in chest UI"

            # List of buttons
            buttons:
              - text: "Starter Kit"
                item: "minecraft:chest"
                slot: 10
                lore:
                  - "\\u00a77A basic kit for new players"
                  - "\\u00a7eClick to claim"
                messages:
                  - "\\u00a7aStarter kit claimed!"
                commands:
                  - 'give "{player_name}" minecraft:stone_sword 1'
                  - 'give "{player_name}" minecraft:bread 16'

              - text: "Teleport to Spawn"
                item: "minecraft:ender_pearl"
                slot: 12
                commands:
                  - 'tp "{player_name}" 0 100 0'

              - text: "Your Stats"
                item: "minecraft:book"
                slot: 14
                lore:
                  - "\\u00a77Name: \\u00a7f{player_name}"
                  - "\\u00a77Level: \\u00a7f{exp_level}"
                  - "\\u00a77Game mode: \\u00a7f{game_mode}"
                messages:
                  - "\\u00a7e========== Stats =========="
                  - "\\u00a7fX={x} Y={y} Z={z}"
                  - "\\u00a7fDimension: {dimension}"
                  - "\\u00a7e==========================="

              - text: "Open Form Menu"
                item: "minecraft:paper"
                slot: 16
                jump: "example"

              - text: "\\u00a7c[Admin] Heal"
                item: "minecraft:golden_apple"
                slot: 22
                permission: "aenu.button.admin.heal"
                messages:
                  - "\\u00a7aHealing you now..."
                commands:
                  - 'effect "{player_name}" instant_health 1 255'

              - text: "Close"
                item: "minecraft:barrier"
                slot: 26
                close: true
            """;

    @Language("yml")
    public static final String DOUBLE_CHEST = """
            # Example double chest menu configuration for Aenu
            # Menu name is the filename without .yml extension

            # Title of the menu
            title: "Server Hub - {player_name}"
            ui: "double_chest"

            # Content is ignored for chest UI
            # content: "This is ignored in chest UI"

            # List of buttons
            buttons:
              - text: "Daily Reward"
                item: "minecraft:chest"
                slot: 10
                lore:
                  - "\\u00a77Claim once per day"
                messages:
                  - "\\u00a7aReward claimed!"
                commands:
                  - 'give "{player_name}" minecraft:gold_ingot 5'

              - text: "Diamond Bundle"
                item: "minecraft:diamond"
                count: 3
                slot: 12
                messages:
                  - "\\u00a7bEnjoy your diamonds!"
                commands:
                  - 'give "{player_name}" minecraft:diamond 3'

              - text: "Teleport to Spawn"
                item: "minecraft:ender_pearl"
                slot: 14
                commands:
                  - 'tp "{player_name}" 0 100 0'

              - text: "Player Info"
                item: "minecraft:book"
                slot: 16
                lore:
                  - "\\u00a77Name: \\u00a7f{player_name}"
                  - "\\u00a77Pos: \\u00a7fX={x} Y={y} Z={z}"
                  - "\\u00a77World: \\u00a7f{dimension}"
                messages:
                  - "\\u00a7eLevel: \\u00a7f{exp_level}"

              - text: "Open Form Menu"
                item: "minecraft:paper"
                slot: 28
                jump: "example"

              - text: "Open Chest Menu"
                item: "minecraft:barrel"
                slot: 30
                jump: "example_chest"

              - text: "VIP Shop"
                item: "minecraft:emerald"
                slot: 32
                permission: "aenu.menu.vip"
                lore:
                  - "\\u00a76VIP only"
                commands:
                  - 'give "{player_name}" minecraft:emerald 5'

              - text: "Admin Tools"
                item: "minecraft:command_block"
                slot: 34
                permission: "aenu.menu.admin"
                messages:
                  - "\\u00a7cAdmin tools opened."
                jump: "admin_menu"

              - text: "Previous Page"
                item: "minecraft:arrow"
                slot: 45
                messages:
                  - "\\u00a7eNo previous page"

              - text: "Next Page"
                item: "minecraft:arrow"
                slot: 53
                messages:
                  - "\\u00a7eNo next page"

              - text: "Close"
                item: "minecraft:barrier"
                slot: 49
                close: true
            """;
}
