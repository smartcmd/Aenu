package me.daoge.aenu.command;

import me.daoge.aenu.Aenu;
import org.allaymc.api.command.Command;
import org.allaymc.api.command.SenderType;
import org.allaymc.api.command.tree.CommandTree;
import org.allaymc.api.permission.OpPermissionCalculator;

import java.util.Set;

/**
 * Command handler for /menu command
 */
public class MenuCommand extends Command {

    public MenuCommand() {
        super("menu", "Open a menu", "aenu.command.menu");
        OpPermissionCalculator.NON_OP_PERMISSIONS.addAll(Set.of(
                "aenu.command.menu",
                "aenu.command.menu.open",
                "aenu.command.menu.list"
        ));
    }

    @Override
    public void prepareCommandTree(CommandTree tree) {
        tree.getRoot()
                .key("open")
                .str("menu_name")
                .exec((context, sender) -> {
                    // The sender parameter is automatically typed as EntityPlayer by SenderType.PLAYER
                    // Parameters are retrieved by index using getResult()
                    String menuName = context.getResult(1);

                    // Get the menu manager
                    Aenu plugin = Aenu.getInstance();
                    if (plugin == null || plugin.getMenuManager() == null) {
                        sender.sendMessage("§cMenu plugin is not properly loaded!");
                        return context.fail();
                    }

                    // Check if menu exists
                    if (!plugin.getMenuManager().hasMenu(menuName)) {
                        sender.sendMessage("§cMenu '" + menuName + "' does not exist!");
                        sender.sendMessage("§7Available menus: " + String.join(", ", plugin.getMenuManager().getMenus().keySet()));
                        return context.fail();
                    }

                    // Check menu permission
                    if (!plugin.getMenuManager().hasMenuPermission(sender, menuName)) {
                        sender.sendMessage("§cYou don't have permission to open this menu!");
                        return context.fail();
                    }

                    // Show the menu
                    boolean success = plugin.getMenuManager().showMenu(sender, menuName);
                    if (!success) {
                        sender.sendMessage("§cFailed to open menu '" + menuName + "'!");
                        return context.fail();
                    }

                    return context.success();
                }, SenderType.ACTUAL_PLAYER)
                .root()
                .key("reload")
                .permission("aenu.command.reload")
                .exec(context -> {
                    context.getSender().sendMessage("Reloading Aenu...");
                    Aenu.getInstance().reload();
                    context.getSender().sendMessage("Aenu has been reloaded! Loaded " + Aenu.getInstance().getMenuManager().getMenuCount() + " menus.");
                    return context.success();
                })
                .root()
                .key("list")
                .exec((context, sender) -> {
                    Aenu plugin = Aenu.getInstance();
                    if (plugin == null || plugin.getMenuManager() == null) {
                        sender.sendMessage("§cMenu plugin is not properly loaded!");
                        return context.fail();
                    }

                    // Get accessible menus for this player
                    var accessibleMenus = plugin.getMenuManager().getAccessibleMenus(sender);

                    if (accessibleMenus.isEmpty()) {
                        sender.sendMessage("§eNo menus available for you.");
                        return context.success();
                    }

                    sender.sendMessage("§7You can access §f" + accessibleMenus.size() + "§7 menu(s):");

                    // List all accessible menus
                    for (String menuName : accessibleMenus) {
                        sender.sendMessage("§a  ▪ §f" + menuName + " §7- §e/menu " + menuName);
                    }
                    return context.success();
                }, SenderType.ACTUAL_PLAYER);

    }
}
