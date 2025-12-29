package me.daoge.aenu.manager;

import lombok.Getter;
import me.daoge.aenu.Aenu;
import me.daoge.aenu.model.MenuButton;
import me.daoge.aenu.model.MenuConfig;
import org.allaymc.api.container.FakeContainerFactory;
import org.allaymc.api.container.interfaces.FakeContainer;
import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.form.Forms;
import org.allaymc.api.form.element.Button;
import org.allaymc.api.form.element.ImageData;
import org.allaymc.api.form.type.SimpleForm;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeGetter;
import org.allaymc.api.item.type.ItemTypes;
import org.allaymc.api.player.Player;
import org.allaymc.api.registry.Registries;
import org.allaymc.papi.PlaceholderAPI;
import org.intellij.lang.annotations.Language;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Manages menu configurations and handles menu display
 */
public class MenuManager {

    private final Aenu plugin;
    private final Path dataFolder;

    @Getter
    private final Map<String, MenuConfig> menus = new HashMap<>();

    private final Yaml yaml;

    public MenuManager(Aenu plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getPluginContainer().dataFolder();

        // Initialize YAML parser with proper configuration
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        Representer representer = new Representer(options);
        representer.getPropertyUtils().setSkipMissingProperties(true);

        this.yaml = new Yaml(new Constructor(MenuConfig.class, new org.yaml.snakeyaml.LoaderOptions()), representer, options);

        // Ensure data folder exists
        try {
            if (!Files.exists(dataFolder)) {
                Files.createDirectories(dataFolder);
                plugin.getPluginLogger().info("Created plugin data folder at: {}", dataFolder);
            }
        } catch (IOException e) {
            plugin.getPluginLogger().error("Failed to create data folder", e);
        }
    }

    /**
     * Load all menu configurations from the data folder
     */
    public void loadMenus() {
        try {
            // Create example menu if no menus exist
            if (isDataFolderEmpty()) {
                createExampleMenu();
            }

            // Load all .yml files from data folder
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataFolder, "*.yml")) {
                for (Path file : stream) {
                    loadMenu(file);
                }
            }

            plugin.getPluginLogger().info("Loaded {} menu(s) from {}", menus.size(), dataFolder);
        } catch (IOException e) {
            plugin.getPluginLogger().error("Failed to load menus", e);
        }
    }

    /**
     * Load a single menu configuration from a file
     */
    private void loadMenu(Path file) {
        try {
            String fileName = file.getFileName().toString();
            String menuName = fileName.substring(0, fileName.length() - 4); // Remove .yml extension

            String content = Files.readString(file);
            MenuConfig config = yaml.load(content);

            if (config == null) {
                plugin.getPluginLogger().warn("Menu file {} is empty or invalid", fileName);
                return;
            }

            menus.put(menuName, config);
            plugin.getPluginLogger().debug("Loaded menu: {}", menuName);
        } catch (Exception e) {
            plugin.getPluginLogger().error("Failed to load menu from file: {}", file.getFileName(), e);
        }
    }

    /**
     * Check if the data folder is empty
     */
    private boolean isDataFolderEmpty() throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataFolder, "*.yml")) {
            return !stream.iterator().hasNext();
        }
    }

    /**
     * Create an example menu configuration
     */
    private void createExampleMenu() {
        try {
            Path exampleFile = dataFolder.resolve("example.yml");
            Path chestExampleFile = dataFolder.resolve("example_chest.yml");

            @Language("yml")
            String exampleContent = """
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
                          - "§aYou received a diamond!"
                          - "§7Use it wisely, {player_name}!"
                        commands:
                          - "give \\"{player_name}\\" minecraft:diamond 1"

                      # Button with only command (old way still works)
                      - text: "Teleport to Spawn"
                        commands:
                          - "tp \\"{player_name}\\" 0 100 0"

                      # Button with only messages (no commands)
                      - text: "Show Info"
                        messages:
                          - "§e========== Player Info =========="
                          - "§bName: §f{player_name}"
                          - "§bPosition: §fX={x} Y={y} Z={z}"
                          - "§bGame mode: §f{game_mode}"
                          - "§bDimension: §f{dimension}"
                          - "§bExp Level: §f{exp_level}"
                          - "§e================================="

                      # Button with permission requirement (only OPs can see)
                      - text: "§c[Admin] Heal Me"
                        permission: "aenu.button.heal"
                        messages:
                          - "§aHealing you now..."
                          - "§7You have been fully healed!"
                        commands:
                          - "effect \\"{player_name}\\" instant_health 1 255"
                          - "effect \\"{player_name}\\" saturation 1 255"
                    """;

            @Language("yml")
            String chestExampleContent = """
                    # Example chest menu configuration for Aenu
                    # Menu name is the filename without .yml extension

                    # Title of the menu
                    title: "Chest Menu"
                    ui: "chest"

                    # Content is ignored for chest UI
                    # content: "This is ignored in chest UI"

                    # List of buttons
                    buttons:
                      - text: "Get Diamond"
                        item: "minecraft:diamond"
                        slot: 10
                        lore:
                          - "§7Click to receive a diamond"
                        messages:
                          - "§aYou received a diamond!"
                        commands:
                          - "give \\"{player_name}\\" minecraft:diamond 1"

                      - text: "Teleport to Spawn"
                        item: "minecraft:ender_pearl"
                        slot: 13
                        lore:
                          - "§7Click to teleport to spawn"
                        commands:
                          - "tp \\"{player_name}\\" 0 100 0"

                      - text: "Close"
                        item: "minecraft:barrier"
                        slot: 26
                        close: true
                    """;

            Files.writeString(exampleFile, exampleContent);
            Files.writeString(chestExampleFile, chestExampleContent);
            plugin.getPluginLogger().info("Created example menus at: {}, {}", exampleFile, chestExampleFile);
        } catch (IOException e) {
            plugin.getPluginLogger().error("Failed to create example menu", e);
        }
    }

    /**
     * Check if a player has permission to open a menu
     *
     * @param player   The player to check
     * @param menuName The name of the menu
     * @return true if the player has permission, false otherwise
     */
    public boolean hasMenuPermission(EntityPlayer player, String menuName) {
        MenuConfig config = menus.get(menuName);
        if (config == null) {
            return false;
        }

        // If no permission is set, everyone can access
        if (config.getPermission() == null || config.getPermission().isEmpty()) {
            return true;
        }

        // Check if player has the required permission
        return player.hasPermission(config.getPermission()).asBoolean();
    }

    /**
     * Check if a player has permission to see a button
     *
     * @param player The player to check
     * @param button The button configuration
     * @return true if the player has permission, false otherwise
     */
    private boolean hasButtonPermission(EntityPlayer player, MenuButton button) {
        // If no permission is set, everyone can see the button
        if (button.getPermission() == null || button.getPermission().isEmpty()) {
            return true;
        }

        // Check if player has the required permission
        return player.hasPermission(button.getPermission()).asBoolean();
    }

    /**
     * Display a menu to a player
     *
     * @param player   The player to show the menu to
     * @param menuName The name of the menu to display
     * @return true if the menu was shown, false if the menu doesn't exist
     */
    public boolean showMenu(EntityPlayer player, String menuName) {
        MenuConfig config = menus.get(menuName);
        if (config == null) {
            return false;
        }

        MenuUiType uiType = resolveUiType(config.getUi());
        if (uiType != MenuUiType.FORM) {
            return showChestMenu(player, menuName, config, uiType);
        }

        return showFormMenu(player, config);
    }

    private boolean showFormMenu(EntityPlayer player, MenuConfig config) {
        // Resolve placeholders in title and content
        PlaceholderAPI papi = PlaceholderAPI.getAPI();
        String title = resolvePlaceholders(player, papi, config.getTitle());
        String content = resolvePlaceholders(player, papi, config.getContent());

        // Build the SimpleForm
        SimpleForm form = Forms.simple()
                .title(title)
                .content(content);

        // Add buttons (only those the player has permission to see)
        if (config.getButtons() != null) {
            for (MenuButton buttonConfig : config.getButtons()) {
                // Skip buttons the player doesn't have permission for
                if (!hasButtonPermission(player, buttonConfig)) {
                    continue;
                }

                String buttonText = resolvePlaceholders(player, papi, buttonConfig.getText());

                Button button;

                // Handle button image if present
                if (buttonConfig.getImage() != null) {
                    MenuButton.ImageConfig imageConfig = buttonConfig.getImage();
                    ImageData.ImageType imageType = "url".equalsIgnoreCase(imageConfig.getType())
                        ? ImageData.ImageType.URL
                        : ImageData.ImageType.PATH;
                    button = form.button(buttonText, imageType, imageConfig.getData());
                } else {
                    button = form.button(buttonText);
                }

                // Set button click handler
                button.onClick(btn -> {
                    // First, send messages to the player (if any)
                    sendMessages(player, buttonConfig.getMessages());

                    // Then, execute commands
                    executeCommands(player, buttonConfig.getCommands());
                });
            }
        }

        // Show the form to the player
        var controller = player.getController();
        if (controller == null) {
            plugin.getPluginLogger().warn("Cannot open form menu for a fake player");
            return false;
        }
        controller.viewForm(form);
        return true;
    }

    private boolean showChestMenu(EntityPlayer player, String menuName, MenuConfig config, MenuUiType uiType) {
        PlaceholderAPI papi = PlaceholderAPI.getAPI();
        Player controller = player.getController();
        if (controller == null) {
            plugin.getPluginLogger().warn("Cannot open chest menu for a fake player");
            return false;
        }

        FakeContainer container = uiType == MenuUiType.DOUBLE_CHEST
                ? FakeContainerFactory.getFactory().createFakeDoubleChestContainer()
                : FakeContainerFactory.getFactory().createFakeChestContainer();

        String title = resolvePlaceholders(player, papi, config.getTitle());
        if (!title.isEmpty()) {
            container.setCustomName(title);
        }

        int size = uiType == MenuUiType.DOUBLE_CHEST ? 54 : 27;
        boolean[] usedSlots = new boolean[size];
        int nextAutoSlot = 0;

        if (config.getButtons() != null) {
            for (MenuButton buttonConfig : config.getButtons()) {
                if (!hasButtonPermission(player, buttonConfig)) {
                    continue;
                }

                Integer preferredSlot = buttonConfig.getSlot();
                int slot;
                if (preferredSlot != null) {
                    slot = preferredSlot;
                    if (slot < 0 || slot >= size) {
                        plugin.getPluginLogger().warn("Menu {} has button slot out of range: {}", menuName, slot);
                        continue;
                    }
                    if (usedSlots[slot]) {
                        plugin.getPluginLogger().warn("Menu {} has duplicate button slot: {}", menuName, slot);
                        continue;
                    }
                } else {
                    slot = findNextFreeSlot(usedSlots, nextAutoSlot);
                    if (slot == -1) {
                        plugin.getPluginLogger().warn("Menu {} has more buttons than slots", menuName);
                        break;
                    }
                    nextAutoSlot = slot + 1;
                }

                usedSlots[slot] = true;

                ItemStack itemStack = createButtonItemStack(player, papi, menuName, buttonConfig);
                if (itemStack == null) {
                    continue;
                }

                boolean hasActions = hasButtonActions(buttonConfig);
                if (hasActions) {
                    container.setItemStackWithListener(slot, itemStack, () -> {
                        sendMessages(player, buttonConfig.getMessages());
                        executeCommands(player, buttonConfig.getCommands());
                        if (buttonConfig.isClose()) {
                            container.removeViewer(controller);
                        }
                    });
                } else {
                    container.setItemStack(slot, itemStack);
                }
            }
        }

        container.addPlayer(controller);
        return true;
    }

    private ItemStack createButtonItemStack(EntityPlayer player, PlaceholderAPI papi, String menuName, MenuButton buttonConfig) {
        String itemName = buttonConfig.getItem();
        if (itemName == null || itemName.isBlank()) {
            itemName = "minecraft:paper";
        } else if (!itemName.contains(":")) {
            itemName = "minecraft:" + itemName;
        }

        ItemType<?> itemType;
        try {
            itemType = ItemTypeGetter.name(itemName).itemType();
        } catch (IllegalArgumentException e) {
            plugin.getPluginLogger().warn("Menu {} has invalid item name: {}", menuName, itemName);
            itemType = ItemTypes.BARRIER;
        }

        if (itemType == ItemTypes.UNKNOWN) {
            plugin.getPluginLogger().warn("Menu {} has unknown item name: {}", menuName, itemName);
            itemType = ItemTypes.BARRIER;
        }

        int count = Math.max(1, buttonConfig.getCount());
        int meta = Math.max(0, buttonConfig.getMeta());
        ItemStack itemStack = itemType.createItemStack(count, meta);

        String displayName = buttonConfig.getText();
        if (displayName != null && !displayName.isEmpty()) {
            itemStack.setCustomName(resolvePlaceholders(player, papi, displayName));
        }

        List<String> lore = buttonConfig.getLore();
        if (lore != null && !lore.isEmpty()) {
            itemStack.setLore(lore.stream()
                    .map(line -> resolvePlaceholders(player, papi, line))
                    .toList());
        }

        return itemStack;
    }

    private boolean hasButtonActions(MenuButton buttonConfig) {
        return (buttonConfig.getMessages() != null && !buttonConfig.getMessages().isEmpty())
                || (buttonConfig.getCommands() != null && !buttonConfig.getCommands().isEmpty())
                || buttonConfig.isClose();
    }

    private String resolvePlaceholders(EntityPlayer player, PlaceholderAPI papi, String text) {
        if (text == null) {
            return "";
        }
        return papi.setPlaceholders(player, text);
    }

    private int findNextFreeSlot(boolean[] usedSlots, int startIndex) {
        for (int i = startIndex; i < usedSlots.length; i++) {
            if (!usedSlots[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Send messages to a player
     * PlaceholderAPI placeholders are resolved before sending
     *
     * @param player   The player entity to send messages to
     * @param messages The list of messages to send
     */
    private void sendMessages(EntityPlayer player, List<String> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }

        PlaceholderAPI papi = PlaceholderAPI.getAPI();

        // Get the player controller to send messages
        var controller = player.getController();
        if (controller == null) {
            plugin.getPluginLogger().warn("Cannot send messages to a fake player");
            return;
        }

        for (String message : messages) {
            // Resolve placeholders
            String resolvedMessage = papi.setPlaceholders(player, message);

            // Send the message to the player
            controller.sendMessage(resolvedMessage);
        }
    }

    /**
     * Execute a list of commands for a player
     * Commands are executed as if the player typed them
     * PlaceholderAPI placeholders are resolved before execution
     *
     * @param player   The player entity who clicked the button
     * @param commands The list of commands to execute
     */
    private void executeCommands(EntityPlayer player, List<String> commands) {
        if (commands == null || commands.isEmpty()) {
            return;
        }

        PlaceholderAPI papi = PlaceholderAPI.getAPI();

        // EntityPlayer implements CommandSender through EntityBaseComponent
        // So we can pass it directly to execute()
        for (String command : commands) {
            // Resolve placeholders
            String resolvedCommand = papi.setPlaceholders(player, command);

            // Execute the command as the player entity (which implements CommandSender)
            // Note: Commands are executed through the command registry with the EntityPlayer as sender
            plugin.getPluginLogger().debug("Executing command for player: {}", resolvedCommand);
            Registries.COMMANDS.execute(player, resolvedCommand);
        }
    }

    /**
     * Clear all loaded menus
     */
    public void clearMenus() {
        menus.clear();
    }

    /**
     * Get the number of loaded menus
     */
    public int getMenuCount() {
        return menus.size();
    }

    /**
     * Check if a menu exists
     */
    public boolean hasMenu(String menuName) {
        return menus.containsKey(menuName);
    }

    /**
     * Get a list of menu names that the player has permission to access
     *
     * @param player The player to check permissions for
     * @return A list of accessible menu names
     */
    public java.util.List<String> getAccessibleMenus(EntityPlayer player) {
        return menus.entrySet().stream()
                .filter(entry -> {
                    MenuConfig config = entry.getValue();
                    // If no permission is set, everyone can access
                    if (config.getPermission() == null || config.getPermission().isEmpty()) {
                        return true;
                    }
                    // Check if player has the required permission
                    return player.hasPermission(config.getPermission()).asBoolean();
                })
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
    }

    private MenuUiType resolveUiType(String ui) {
        if (ui == null) {
            return MenuUiType.FORM;
        }

        return switch (ui.trim().toLowerCase(Locale.ROOT)) {
            case "chest" -> MenuUiType.CHEST;
            case "double_chest", "doublechest", "double" -> MenuUiType.DOUBLE_CHEST;
            default -> MenuUiType.FORM;
        };
    }

    private enum MenuUiType {
        FORM,
        CHEST,
        DOUBLE_CHEST
    }
}
