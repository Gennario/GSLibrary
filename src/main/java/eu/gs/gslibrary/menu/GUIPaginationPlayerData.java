package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.utils.replacement.Replacement;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class GUIPaginationPlayerData {

    private Player player;
    private int page, size;
    private GUIPagination pagination;
    private Map<Integer, GUIItem> paginationItems;

    public GUIPaginationPlayerData(Player player, GUIPagination pagination) {
        this.player = player;
        this.page = 1;
        this.size = 0;

        this.pagination = pagination;
        this.paginationItems = new HashMap<>();

        loadSize();
    }

    public void resetGUIItems() {
        paginationItems.clear();
    }

    public void addGUIItem(Integer slot, GUIItem item) {
        paginationItems.put(slot, item);
    }

    public GUIItem checkGuiItem(Integer slot) {
        if (paginationItems.containsKey(slot)) {
            return paginationItems.get(slot);
        }
        return null;
    }

    public void loadSize() {
        int i = 0;
        for (GUIItem item : pagination.getItems()) {
            if (item.loadItem(player, new Replacement((player1, string) -> string)) != null) {
                i++;
            }
        }
        size = i;
    }

}
