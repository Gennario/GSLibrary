package eu.gs.gslibrary.menu.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface GUICloseResponse {

    void onClose(Player player, InventoryCloseEvent event);

}
