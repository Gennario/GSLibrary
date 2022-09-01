package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.menu.event.GUIItemResponse;
import eu.gs.gslibrary.menu.event.GUIPerPlayerItemResponse;
import eu.gs.gslibrary.utils.replacement.Replacement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GUIPerPlayerItem extends GUIItem {

    private GUIPerPlayerItemResponse guiPerPlayerItemResponse;
    private boolean update;

    public GUIPerPlayerItem() {
        guiPerPlayerItemResponse = (player, replacement) -> new GUINormalItem(Material.STONE);
        update = false;
    }

    public GUIPerPlayerItem setGuiPerPlayerItemResponse(GUIPerPlayerItemResponse guiPerPlayerItemResponse) {
        this.guiPerPlayerItemResponse = guiPerPlayerItemResponse;
        return this;
    }

    public GUIPerPlayerItem setUpdate(boolean value) {
        update = value;
        return this;
    }

    @Override
    public ItemStack loadItem(Player player, Replacement replacement) {
        return guiPerPlayerItemResponse.item(player, replacement).loadItem(player, replacement);
    }

    @Override
    public boolean isUpdate() {
        return update;
    }

    @Override
    public GUIItemResponse getResponse() {
        return null;
    }

    @Override
    public GUIItem setResponse(GUIItemResponse response) {
        return null;
    }
}
