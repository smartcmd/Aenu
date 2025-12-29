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
                .permission("aenu.command.menu.open")
                .str("menu_name")
                .exec((context, sender) -> {
                    // The sender parameter is automatically typed as EntityPlayer by SenderType.PLAYER
                    // Parameters are retrieved by index using getResult()
                    String menuName = context.getResult(1);

                    Aenu plugin = Aenu.getInstance();
                    // Check if menu exists
                    if (!plugin.getMenuManager().hasMenu(menuName)) {
                        sender.sendMessage("§cMenu '" + menuName + "' does not exist!");
                        var accessibleMenus = plugin.getMenuManager().getAccessibleMenus(sender);
                        if (accessibleMenus.isEmpty()) {
                            sender.sendMessage("§7No menus available for you.");
                        } else {
                            sender.sendMessage("§7Available menus: " + String.join(", ", accessibleMenus));
                        }
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
                .permission("aenu.command.menu.reload")
                .exec(context -> {
                    context.getSender().sendMessage("Reloading Aenu...");
                    Aenu.getInstance().reload();
                    context.getSender().sendMessage("Aenu has been reloaded! Loaded " + Aenu.getInstance().getMenuManager().getMenuCount() + " menus.");
                    return context.success();
                })
                .root()
                .key("list")
                .permission("aenu.command.menu.list")
                .exec((context, sender) -> {
                    // Get accessible menus for this player
                    var accessibleMenus = Aenu.getInstance().getMenuManager().getAccessibleMenus(sender);

                    if (accessibleMenus.isEmpty()) {
                        sender.sendMessage("§eNo menus available for you.");
                        return context.success();
                    }

                    sender.sendMessage("§7You can access §f" + accessibleMenus.size() + "§7 menu(s):");

                    // List all accessible menus
                    for (String menuName : accessibleMenus) {
                        sender.sendMessage("§a  ▪ §f" + menuName + " §7- §e/menu open " + menuName);
                    }
                    return context.success();
                }, SenderType.ACTUAL_PLAYER);

    }
}
