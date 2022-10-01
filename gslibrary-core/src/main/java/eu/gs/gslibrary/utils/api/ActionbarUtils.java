package eu.gs.gslibrary.utils.api;

import eu.gs.gslibrary.utils.Utils;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * It sends an actionbar message to the player
 */
@UtilityClass
public class ActionbarUtils {

    /**
     * It sends an actionbar message to the player
     *
     * @param player The player you want to send the actionbar to.
     * @param message The message you want to send to the player.
     */
    public static void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Utils.colorize(player, message)));
    }
}
