package eu.gs.gslibrary.menu.event;

import eu.gs.gslibrary.menu.GUIItem;
import eu.gs.gslibrary.menu.GUIPagination;
import org.bukkit.entity.Player;

import java.util.List;

public interface GUIPerPlayerPagination {

    List<GUIItem> item(Player player, GUIPagination guiPagination);

}
