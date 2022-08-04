package eu.gs.gslibrary.menu.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface GUIItemResponse {

    void onClick(Player player, InventoryClickEvent event);

}
