package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.GSLibrary;
import eu.gs.gslibrary.conditions.ConditionValue;
import eu.gs.gslibrary.menu.event.GUIItemResponse;
import eu.gs.gslibrary.utils.Pair;
import eu.gs.gslibrary.utils.replacement.Replacement;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

@Getter
@Setter
public class GUIConditionalItem extends GUIItem {

    private TreeMap<Integer, Pair<List<ConditionValue>, GUINormalItem>> guiItems;
    private GUIItemResponse response;

    private boolean update;

    // Creating a new TreeMap and setting the update to false.
    public GUIConditionalItem() {
        this.guiItems = new TreeMap<>();
        this.update = false;
    }

    /**
     * "Add an item to the GUI with a priority and conditions."
     *
     * The first parameter is the priority of the item. The priority is used to determine the order of the items in the
     * GUI. The higher the priority, the higher the item will be in the GUI
     *
     * @param priority The priority of the item. The higher the priority, the higher the item will be in the GUI.
     * @param item The item to be added to the GUI.
     * @return The GUIConditionalItem object.
     */
    public GUIConditionalItem addItem(int priority, GUINormalItem item, ConditionValue... conditions) {
        guiItems.put(priority, new Pair<>(Arrays.asList(conditions), item));
        return this;
    }

    /**
     * Add an item to the GUI with a priority and a list of conditions.
     *
     * @param priority The priority of the item. The higher the priority, the higher the item will be in the GUI.
     * @param item The GUIItem to add.
     * @param conditions A list of conditions that must be met for the item to be shown.
     * @return A GUIConditionalItem
     */
    public GUIConditionalItem addItem(int priority, GUINormalItem item, List<ConditionValue> conditions) {
        guiItems.put(priority, new Pair<>(conditions, item));
        return this;
    }

    /**
     * If the player has all the conditions, then load the item
     *
     * @param player The player who is viewing the GUI
     * @param replacement The replacement object that contains all the variables that can be used in the item.
     * @return The itemstack that is being returned is the itemstack that is being loaded.
     */
    @Override
    public ItemStack loadItem(Player player, Replacement replacement) {
        for (Pair<List<ConditionValue>, GUINormalItem> pair : guiItems.values()) {
            boolean have = true;
            for (ConditionValue condition : pair.getKey()) {
                if (!GSLibrary.getInstance().getConditionsAPI().check(player, condition, replacement)) {
                    have = false;
                    break;
                }
            }
            if (!have) continue;
            return pair.getValue().setResponse(response).loadItem(player, replacement);
        }
        return null;
    }

    @Override
    public boolean isUpdate() {
        return update;
    }

    @Override
    public GUIConditionalItem setUpdate(boolean b) {
        update = b;
        return this;
    }

    @Override
    public GUIItemResponse getResponse() {
        return response;
    }

    @Override
    public GUIConditionalItem setResponse(GUIItemResponse response) {
        this.response = response;
        return this;
    }
}
