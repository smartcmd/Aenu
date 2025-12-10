package me.daoge.aenu;

import lombok.Getter;
import me.daoge.aenu.command.MenuCommand;
import me.daoge.aenu.manager.MenuManager;
import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.registry.Registries;

/**
 * Main plugin class for Aenu - A configurable menu plugin for AllayMC
 * Supports SimpleForm menus with PlaceholderAPI integration
 */
public class Aenu extends Plugin {

    @Getter
    private static Aenu instance;

    @Getter
    private MenuManager menuManager;

    @Override
    public void onLoad() {
        instance = this;
        this.pluginLogger.info("Aenu is loading...");
    }

    @Override
    public void onEnable() {
        // Initialize menu manager
        this.menuManager = new MenuManager(this);

        // Load all menu configurations from data folder
        this.menuManager.loadMenus();

        // Register the /menu command
        Registries.COMMANDS.register(new MenuCommand());

        this.pluginLogger.info("Aenu has been enabled! Loaded {} menus.", menuManager.getMenuCount());
    }

    @Override
    public void onDisable() {
        this.pluginLogger.info("Aenu has been disabled!");
    }

    @Override
    public boolean isReloadable() {
        return true;
    }

    @Override
    public void reload() {
        this.pluginLogger.info("Reloading Aenu...");

        // Clear existing menus and reload all configurations
        this.menuManager.clearMenus();
        this.menuManager.loadMenus();

        this.pluginLogger.info("Aenu has been reloaded! Loaded {} menus.", menuManager.getMenuCount());
    }
}
