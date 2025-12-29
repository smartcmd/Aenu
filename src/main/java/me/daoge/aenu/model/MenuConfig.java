package me.daoge.aenu.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration model for a menu
 * Represents the structure of a menu YAML file
 */
@Data
public class MenuConfig {

    /**
     * The title of the menu
     */
    private String title = "";

    /**
     * The UI type of the menu: form, chest, or double_chest
     */
    private String ui = "form";

    /**
     * The content/description text of the menu
     */
    private String content = "";

    /**
     * Optional permission required to open this menu
     * If set, only players with this permission can open the menu
     * If null or empty, everyone can open the menu
     */
    private String permission;

    /**
     * List of buttons in this menu
     */
    private List<MenuButton> buttons = new ArrayList<>();
}
