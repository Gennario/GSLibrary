package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.GSLibrary;
import eu.gs.gslibrary.menu.event.GUICloseResponse;
import eu.gs.gslibrary.menu.event.GUIItemResponse;
import eu.gs.gslibrary.menu.event.GUIOpenResponse;
import eu.gs.gslibrary.utils.methods.AsyncMethod;
import eu.gs.gslibrary.utils.replacement.Replacement;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class GUI implements Listener {

    private final String type;

    private GUITitle guiTitle;
    private Rows rows;
    private GUIItemMap guiItemMap;
    private InventoryType inventoryType;
    private Map<Integer, GUIItem> items;
    private List<GUIPagination> paginations;

    private Map<Player, PlayerGUIData> playerGUIDataMap;

    private GUIOpenResponse openResponse;
    private GUICloseResponse closeResponse;

    private int updateTime, updateTick;

    public GUI(String type, GUIItemMap guiItemMap) {
        this.type = type;
        playerGUIDataMap = new HashMap<>();
        items = new HashMap<>();
        paginations = new ArrayList<>();
        this.inventoryType = InventoryType.CHEST;
        this.guiItemMap = guiItemMap;
        GSLibrary.getInstance().getGuis().put(type, this);

        updateTime = 0;
        updateTick = 0;

        Bukkit.getPluginManager().registerEvents(this, GSLibrary.getInstance());
    }

    public abstract Replacement onGuiUpdate(Player player);

    public abstract void guiOpen(Player player);

    public void setItem(String c, GUIItem guiItem) {
        for (Integer integer : guiItemMap.getSlotsByKey(c)) {
            items.put(integer, guiItem);
        }
    }

    public void setItem(String c, GUIItem guiItem, GUIItemResponse response) {
        guiItem.setResponse(response);
        for (Integer integer : guiItemMap.getSlotsByKey(c)) {
            items.put(integer, guiItem);
        }
    }

    public void setItem(String c, GUIItem guiItem, List<Integer> positions, GUIItemResponse response) {
        guiItem.setResponse(response);
        int position = 1;
        for (Integer integer : guiItemMap.getSlotsByKey(c)) {
            if (positions.contains(position)) {
                items.put(integer, guiItem);
            }
            position++;
        }
    }

    public void addPagination(GUIPagination pagination) {
        paginations.add(pagination);
    }

    public void open(Replacement replacement, Player... players) {
        GUI gui = this;
        new AsyncMethod() {
            @Override
            public void action() {
                for (Player player : players) {
                    PlayerGUIHistory playerGUIHistory = null;
                    if(!GSLibrary.getInstance().getPlayerGUIHistory().containsKey(player)) {
                        playerGUIHistory = new PlayerGUIHistory(player);
                    }else {
                        playerGUIHistory = GSLibrary.getInstance().getPlayerGUIHistory().get(player);
                    }
                    playerGUIHistory.setCurrent(gui);
                    PlayerGUIData playerGUIData = null;
                    if (playerGUIDataMap.containsKey(player)) {
                        playerGUIData = playerGUIDataMap.get(player);
                    } else {
                        playerGUIData = new PlayerGUIData(gui, player, replacement);
                        playerGUIDataMap.put(player, playerGUIData);
                        playerGUIData.load();
                    }

                    playerGUIData.updateGui(true);
                    playerGUIData.open();
                }
            }
        };
    }

    public void open(Replacement replacement, boolean resetPage, Player... players) {
        GUI gui = this;
        new AsyncMethod() {
            @Override
            public void action() {
                for (Player player : players) {
                    PlayerGUIData playerGUIData = null;
                    if (playerGUIDataMap.containsKey(player)) {
                        playerGUIData = playerGUIDataMap.get(player);
                    } else {
                        playerGUIData = new PlayerGUIData(gui, player, replacement);
                        playerGUIDataMap.put(player, playerGUIData);
                        playerGUIData.load();
                    }
                    if (resetPage) {
                        for (GUIPagination pagination : paginations) {
                            pagination.getPaginationPlayerDataMap().get(player).setPage(1);
                        }
                    }
                    playerGUIData.updateGui(true);
                    playerGUIData.open();
                }
            }
        };
    }

    public void registerOpenResponse(GUIOpenResponse response) {
        this.openResponse = response;
    }

    public void registerCloseResponse(GUICloseResponse response) {
        this.closeResponse = response;
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        new AsyncMethod() {
            @Override
            public void action() {
                if (event.getPlayer() instanceof Player) {
                    Player player = (Player) event.getPlayer();
                    if (playerGUIDataMap.containsKey(player)) {
                        if (event.getInventory().equals(playerGUIDataMap.get(player).getInventory())) {
                            if (openResponse != null) openResponse.onOpen(player, event);
                        }
                    }
                }
            }
        };
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        new AsyncMethod() {
            @Override
            public void action() {
                if (event.getPlayer() instanceof Player) {
                    Player player = (Player) event.getPlayer();
                    if (playerGUIDataMap.containsKey(player)) {
                        if (event.getPlayer().getOpenInventory().getTopInventory().equals(playerGUIDataMap.get(player).getInventory())) {
                            if (closeResponse != null) closeResponse.onClose(player, event);
                        }
                    }
                }
            }
        };
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (playerGUIDataMap.containsKey(player)) {
                if (event.getClickedInventory() == null) return;

                PlayerGUIData guiData = playerGUIDataMap.get(player);
                if (player.getOpenInventory().getTopInventory().equals(guiData.getInventory())) {
                    event.setCancelled(true);
                    if (items.containsKey(event.getRawSlot())) {
                        if (items.get(event.getRawSlot()).getResponse() != null)
                            items.get(event.getRawSlot()).getResponse().onClick(player, event);
                    } else if (!paginations.isEmpty()) {
                        for (GUIPagination pagination : paginations) {
                            if (pagination.getPaginationPlayerDataMap().containsKey(player)) {
                                GUIPaginationPlayerData playerData = pagination.getPaginationPlayerDataMap().get(player);
                                GUIItem item = playerData.checkGuiItem(event.getRawSlot());
                                if (item != null) {
                                    if (item.getResponse() != null) {
                                        item.getResponse().onClick(player, event);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
