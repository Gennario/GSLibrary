package eu.gs.gslibrary.utils;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * It's a utility class that makes it easier to use PlaceholderAPI
 */
@UtilityClass
public class PlaceholderAPIUtils {

    private static boolean isInitialized;

    /**
     * If PlaceholderAPI is enabled, set isInitialized to true
     */
    public static void init() {
        try {
            if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                isInitialized = false;
                return;
            }

            isInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            isInitialized = false;
        }
    }

    /**
     * It registers a placeholder expansion
     */
    public static void register(PlaceholderExpansion... expansions) {
        if (!isInitialized) return;

        for (PlaceholderExpansion expansion : expansions) {
            expansion.register();
        }
    }

    /**
     * It takes a string and replaces all placeholders in it with their respective values
     *
     * @param text The text to replace placeholders in.
     * @return The text with the placeholders replaced.
     */
    public static String setPlaceholders(String text) {
        if (!isInitialized) return "";

        return PlaceholderAPI.setPlaceholders(null, text);
    }

    /**
     * It replaces all placeholders in the given text with their corresponding values
     *
     * @param text The text to replace placeholders in.
     * @param player The player to use for the placeholders.
     * @return The text with the placeholders replaced.
     */
    public static String setPlaceholders(String text, Player player) {
        if (!isInitialized) return "";

        return PlaceholderAPI.setPlaceholders(player, text);
    }

    /**
     * It takes a list of strings, and replaces all placeholders in each string with their respective values
     *
     * @param player The player to set the placeholders for.
     * @param stringList The list of strings you want to replace the placeholders in.
     * @return A list of strings with the placeholders replaced.
     */
    public static List<String> setPlaceholders(Player player, List<String> stringList) {
        if (!isInitialized) new ArrayList<>();

        return stringList.stream().map(s -> setPlaceholders(s, player)).collect(Collectors.toList());
    }

    /**
     * Returns true if the text contains placeholders
     *
     * @param text The text to check for placeholders
     * @return A boolean value.
     */
    public static boolean containsPlaceholders(String text) {
        if (!isInitialized) return false;

        return PlaceholderAPI.containsPlaceholders(text);
    }

    /**
     * If the plugin is not initialized, return an empty string. Otherwise, return the string with the placeholders
     * replaced
     *
     * @param placeholder The placeholder you want to get the value of.
     * @return The string that is being returned is the placeholder that is being passed in.
     */
    public static String getString(String placeholder) {
        if (!isInitialized) return "";

        return setPlaceholders(placeholder);
    }

    /**
     * It returns a double value of the placeholder
     *
     * @param placeholder The placeholder you want to get the value of.
     * @return A double value
     */
    public static double getDouble(String placeholder) {
        if (!isInitialized) return 0;

        return Double.parseDouble(setPlaceholders(placeholder));
    }

    /**
     * If the plugin is initialized, return the integer value of the placeholder.
     *
     * @param placeholder The placeholder you want to get the value of.
     * @return The value of the placeholder.
     */
    public static int getInt(String placeholder) {
        if (!isInitialized) return 0;

        return Integer.parseInt(setPlaceholders(placeholder));
    }

    /**
     * If the plugin is initialized, return the boolean value of the placeholder.
     *
     * @param placeholder The placeholder you want to get the value of.
     * @return A boolean value.
     */
    public static boolean getBoolean(String placeholder) {
        if (!isInitialized) return false;

        return Boolean.parseBoolean(setPlaceholders(placeholder));
    }

    /**
     * It returns a float value of the placeholder
     *
     * @param placeholder The placeholder you want to get the value of.
     * @return A float value
     */
    public static float getFloat(String placeholder) {
        if (!isInitialized) return 0;

        return Float.parseFloat(setPlaceholders(placeholder));
    }
}
