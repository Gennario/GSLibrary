package eu.gs.gslibrary.utils.api;

import eu.gs.gslibrary.utils.Utils;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

/**
 * It sends a title message to a player
 */
@UtilityClass
public class TitleUtils {

    /**
     * It sends a title message to a player
     *
     * @param player The player to send the title to.
     * @param title The title to send
     * @param subtitle The subtitle to send.
     * @param fadeIn The time in ticks it takes for the title to fade in.
     * @param stay How long the title will stay on the screen
     * @param fadeOut The time in ticks it takes for the title to fade out.
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (title == null) title = "";
        if (subtitle == null) subtitle = "";

        title = Utils.colorize(player, title);
        subtitle = Utils.colorize(player, subtitle);

        try {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        } catch (Exception e) {
            player.sendTitle(title, subtitle);
        }
    }
}
