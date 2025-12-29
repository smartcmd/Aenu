package me.daoge.aenu.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a button in a menu
 * Each button has a display text and a list of commands to execute when clicked
 */
@Data
public class MenuButton {

    /**
     * The display text of the button
     */
    private String text;

    /**
     * The item type identifier for chest UI buttons (e.g. "minecraft:diamond")
     */
    private String item = "minecraft:paper";

    /**
     * Optional item count for chest UI buttons
     */
    private int count = 1;

    /**
     * Optional item meta for chest UI buttons
     */
    private int meta = 0;

    /**
     * Optional item lore for chest UI buttons
     */
    private List<String> lore = new ArrayList<>();

    /**
     * Optional slot index for chest UI buttons
     */
    private Integer slot;

    /**
     * Whether to close the chest UI after clicking this button
     */
    private boolean close = false;

    /**
     * Optional permission required to see and click this button
     * If set, only players with this permission can see this button
     * If null or empty, everyone can see the button
     */
    private String permission;

    /**
     * Optional messages to send to the player when this button is clicked
     * Messages support PlaceholderAPI placeholders which will be resolved before sending
     * These messages are sent BEFORE executing commands
     */
    private List<String> messages = new ArrayList<>();

    /**
     * The list of commands to execute when this button is clicked
     * Commands support PlaceholderAPI placeholders which will be resolved before execution
     */
    private List<String> commands = new ArrayList<>();

    /**
     * Optional image data for the button
     */
    private ImageConfig image;

    /**
     * Image configuration for buttons
     */
    @Data
    public static class ImageConfig {
        /**
         * Type of image: "path" or "url"
         */
        private String type;

        /**
         * Image data: either a path or URL depending on type
         */
        private String data;
    }
}
