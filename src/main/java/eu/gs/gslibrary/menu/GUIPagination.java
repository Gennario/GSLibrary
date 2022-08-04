package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.utils.Pair;
import eu.gs.gslibrary.utils.replacement.Replacement;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GUIPagination {

    private String paginationId;
    private String c;
    private GUI gui;

    private Pair<GUIItem, String> nextPageItem, previousPageItem, nextPageEmptyItem, previousPageEmptyItem;
    private List<Integer> slots;

    private List<GUIItem> items;

    private Map<Player, GUIPaginationPlayerData> paginationPlayerDataMap;

    private boolean update;

    public GUIPagination(String paginationId, String c, GUI gui) {
        this.paginationId = paginationId;
        this.c = c;
        this.gui = gui;

        this.items = new ArrayList<>();
        this.paginationPlayerDataMap = new HashMap<>();

        this.slots = gui.getGuiItemMap().getSlotsByKey(c);

        this.update = false;
    }

    public List<GUIItem> getPageItems(Player player) {
        GUIPaginationPlayerData playerData = paginationPlayerDataMap.get(player);
        int page = playerData.getPage();

        List<GUIItem> list = new ArrayList<>();
        int i = 0;
        for (GUIItem item : items) {
            if (item.loadItem(player, new Replacement((player1, string) -> string)) != null) {
                if (i >= (page) * slots.size()) return list;
                if (i >= ((page - 1) * slots.size())) {
                    list.add(item);
                }
                i++;
            }
        }
        return list;
    }

    public void loadPlayerData(Player player) {
        GUIPaginationPlayerData playerData = new GUIPaginationPlayerData(player, this);
        paginationPlayerDataMap.put(player, playerData);
        playerData.loadSize();
    }

    public void updatePlayerData(Player player) {
        GUIPaginationPlayerData playerData = paginationPlayerDataMap.get(player);
        playerData.loadSize();
    }

    public GUIPagination setItems(List<GUIItem> items) {
        this.items = items;
        return this;
    }

    public boolean canNextPage(int page, int size) {
        return (page * slots.size() < size);
    }

    public boolean canPreviousPage(int page) {
        return page > 1;
    }

    public GUIPagination setNextPageItem(Pair<GUIItem, String> nextPageItem, Replacement replacement) {
        GUIPagination pagination = this;
        nextPageItem.getKey().setResponse((player, event) -> {
            if (paginationPlayerDataMap.containsKey(player)) {
                GUIPaginationPlayerData playerData = paginationPlayerDataMap.get(player);

                int page = playerData.getPage();
                int size = playerData.getSize();
                if (!canNextPage(page, size)) return;
                playerData.setPage((page + 1));
                gui.open(replacement, player);
            } else {
                paginationPlayerDataMap.put(player, new GUIPaginationPlayerData(player, pagination));
            }
        });
        gui.getGuiItemMap().getSlotsByKey(nextPageItem.getValue()).forEach(integer -> gui.getItems().put(integer, nextPageItem.getKey()));
        this.nextPageItem = nextPageItem;
        return this;
    }

    public GUIPagination setPreviousPageItem(Pair<GUIItem, String> previousPageItem, Replacement replacement) {
        previousPageItem.getKey().setResponse((player, event) -> {
            if (paginationPlayerDataMap.containsKey(player)) {
                GUIPaginationPlayerData playerData = paginationPlayerDataMap.get(player);

                int page = playerData.getPage();
                if (!canPreviousPage(page)) return;
                playerData.setPage((page - 1));
                gui.open(replacement, player);
            } else {
                loadPlayerData(player);
            }
        });
        gui.getGuiItemMap().getSlotsByKey(previousPageItem.getValue()).forEach(integer -> gui.getItems().put(integer, previousPageItem.getKey()));
        this.previousPageItem = previousPageItem;
        return this;
    }

    public GUIPagination setUpdate(boolean update) {
        this.update = update;
        return this;
    }


    public GUIPagination setNextPageEmptyItem(Pair<GUIItem, String> nextPageEmptyItem) {
        this.nextPageEmptyItem = nextPageEmptyItem;
        return this;
    }

    public GUIPagination setPreviousPageEmptyItem(Pair<GUIItem, String> previousPageEmptyItem) {
        this.previousPageEmptyItem = previousPageEmptyItem;
        return this;
    }

}
