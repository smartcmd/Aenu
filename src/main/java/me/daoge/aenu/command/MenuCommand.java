package me.daoge.aenu.command;

import me.daoge.aenu.Aenu;
import me.daoge.aenu.i18n.TranslationKeys;
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
        super("menu", TranslationKeys.COMMAND_MENU_DESCRIPTION, "aenu.command.menu");
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
                        sender.sendTranslatable(TranslationKeys.COMMAND_MENU_NOT_FOUND, menuName);
                        var accessibleMenus = plugin.getMenuManager().getAccessibleMenus(sender);
                        if (accessibleMenus.isEmpty()) {
                            sender.sendTranslatable(TranslationKeys.COMMAND_MENU_NO_MENUS);
                        } else {
                            sender.sendTranslatable(TranslationKeys.COMMAND_MENU_AVAILABLE, String.join(", ", accessibleMenus));
                        }
                        return context.fail();
                    }

                    // Check menu permission
                    if (!plugin.getMenuManager().hasMenuPermission(sender, menuName)) {
                        sender.sendTranslatable(TranslationKeys.COMMAND_MENU_NO_PERMISSION);
                        return context.fail();
                    }

                    // Show the menu
                    boolean success = plugin.getMenuManager().showMenu(sender, menuName);
                    if (!success) {
                        sender.sendTranslatable(TranslationKeys.COMMAND_MENU_OPEN_FAILED, menuName);
                        return context.fail();
                    }

                    return context.success();
                }, SenderType.ACTUAL_PLAYER)
                .root()
                .key("reload")
                .permission("aenu.command.menu.reload")
                .exec(context -> {
                    context.getSender().sendTranslatable(TranslationKeys.COMMAND_MENU_RELOAD_START);
                    Aenu.getInstance().reload();
                    context.getSender().sendTranslatable(
                            TranslationKeys.COMMAND_MENU_RELOAD_DONE,
                            Aenu.getInstance().getMenuManager().getMenuCount()
                    );
                    return context.success();
                })
                .root()
                .key("list")
                .permission("aenu.command.menu.list")
                .exec((context, sender) -> {
                    // Get accessible menus for this player
                    var accessibleMenus = Aenu.getInstance().getMenuManager().getAccessibleMenus(sender);

                    if (accessibleMenus.isEmpty()) {
                        sender.sendTranslatable(TranslationKeys.COMMAND_MENU_LIST_EMPTY);
                        return context.success();
                    }

                    sender.sendTranslatable(TranslationKeys.COMMAND_MENU_LIST_HEADER, accessibleMenus.size());

                    // List all accessible menus
                    for (String menuName : accessibleMenus) {
                        sender.sendTranslatable(TranslationKeys.COMMAND_MENU_LIST_ITEM, menuName);
                    }
                    return context.success();
                }, SenderType.ACTUAL_PLAYER);

    }
}
