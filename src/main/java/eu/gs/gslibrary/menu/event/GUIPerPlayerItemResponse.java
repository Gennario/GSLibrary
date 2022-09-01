package eu.gs.gslibrary.menu.event;

import eu.gs.gslibrary.menu.GUIItem;
import eu.gs.gslibrary.utils.replacement.Replacement;
import org.bukkit.entity.Player;

public interface GUIPerPlayerItemResponse {

    GUIItem item(Player player, Replacement replacement);

}
