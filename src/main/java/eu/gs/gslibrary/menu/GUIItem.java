package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.menu.event.GUIItemResponse;
import eu.gs.gslibrary.utils.replacement.Replacement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class GUIItem {

    public abstract ItemStack loadItem(Player player, Replacement replacement);

    public abstract boolean isUpdate();

    public abstract GUIItem setUpdate(boolean b);

    public abstract GUIItemResponse getResponse();

    public abstract GUIItem setResponse(GUIItemResponse response);

}
