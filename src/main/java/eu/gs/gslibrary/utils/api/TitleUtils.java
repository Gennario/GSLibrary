package eu.gs.gslibrary.utils.api;

import eu.gs.gslibrary.utils.Utils;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class TitleUtils {

    public static void sendTitleMessage(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
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
