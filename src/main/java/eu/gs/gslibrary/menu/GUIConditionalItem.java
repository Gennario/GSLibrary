package eu.gs.gslibrary.menu;

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

    private TreeMap<Integer, Pair<List<GUICondition>, GUIItem>> guiItems;
    private GUIItemResponse response;

    private boolean update;

    public GUIConditionalItem() {
        this.guiItems = new TreeMap<>();
        this.update = false;
    }

    public GUIConditionalItem addItem(int priority, GUIItem item, GUICondition... conditions) {
        item.setResponse(response);
        guiItems.put(priority, new Pair<>(Arrays.asList(conditions), item));
        return this;
    }

    public GUIConditionalItem addItem(int priority, GUIItem item, List<GUICondition> conditions) {
        item.setResponse(response);
        guiItems.put(priority, new Pair<>(conditions, item));
        return this;
    }

    @Override
    public ItemStack loadItem(Player player, Replacement replacement) {
        for (Pair<List<GUICondition>, GUIItem> pair : guiItems.values()) {
            boolean have = true;
            for (GUICondition condition : pair.getKey()) {
                condition.setInput(PlaceholderAPI.setPlaceholders(player, condition.getInput()));
                condition.setOutput(PlaceholderAPI.setPlaceholders(player, condition.getOutput()));
                if (!condition.check(player)) {
                    have = false;
                    break;
                }
            }
            if (!have) continue;
            return pair.getValue().loadItem(player, replacement);
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
        for (Pair<List<GUICondition>, GUIItem> pair : guiItems.values()) {
            pair.getValue().setResponse(response);
        }
        return this;
    }
}
