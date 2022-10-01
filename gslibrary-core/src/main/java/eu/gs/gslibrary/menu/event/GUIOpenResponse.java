package eu.gs.gslibrary.menu.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface GUIOpenResponse {

    void onOpen(Player player, InventoryOpenEvent event);

}
