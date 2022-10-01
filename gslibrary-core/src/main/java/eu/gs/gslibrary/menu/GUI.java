package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.menu.event.GUICloseResponse;
import eu.gs.gslibrary.menu.event.GUIItemResponse;
import eu.gs.gslibrary.menu.event.GUIOpenResponse;
import eu.gs.gslibrary.GSLibrary;
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
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class GUI implements Listener {

    private final String type;

    private String guiTitle;
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

    /**
     * It takes a key and a GUIItem, and then it puts the GUIItem in all the slots that are associated with that key
     *
     * @param c       The key of the item
     * @param guiItem The GUIItem to set
     */
    public void setItem(String c, GUIItem guiItem) {
        for (Integer integer : guiItemMap.getSlotsByKey(c)) {
            items.put(integer, guiItem);
        }
    }

    /**
     * It sets the item in the GUI by the key, and sets the response to the item
     *
     * @param c        The key of the item
     * @param guiItem  The GUIItem object you want to set.
     * @param response The response that will be executed when the item is clicked.
     */
    public void setItem(String c, GUIItem guiItem, GUIItemResponse response) {
        guiItem.setResponse(response);
        for (Integer integer : guiItemMap.getSlotsByKey(c)) {
            items.put(integer, guiItem);
        }
    }

    /**
     * It sets the item in the GUI
     *
     * @param c         The key of the item
     * @param guiItem   The GUIItem object you want to set.
     * @param positions The positions you want the item to be in.
     * @param response  The response that will be executed when the item is clicked.
     */
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

    /**
     * Adds a pagination to the list of paginations.
     *
     * @param pagination The pagination to add.
     */
    public void addPagination(GUIPagination pagination) {
        paginations.add(pagination);
    }

    /**
     * It opens the GUI for the specified players
     *
     * @param replacement This is the replacement object that will be used to replace the placeholders in the GUI.
     */
    public void open(Replacement replacement, Player... players) {
        GUI gui = this;
        new AsyncMethod() {
            @Override
            public void action() {
                for (Player player : players) {
                    PlayerGUIHistory playerGUIHistory;
                    if (!GSLibrary.getInstance().getPlayerGUIHistory().containsKey(player)) {
                        playerGUIHistory = new PlayerGUIHistory(player);
                    } else {
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

    /**
     * It opens the GUI for the specified players
     *
     * @param replacement The replacement to use for the GUI.
     * @param resetPage   If true, the page will be reset to 1.
     */
    public void open(Replacement replacement, boolean resetPage, Player... players) {
        GUI gui = this;
        new AsyncMethod() {
            @Override
            public void action() {
                try {
                    for (Player player : players) {
                        PlayerGUIHistory playerGUIHistory;
                        if (!GSLibrary.getInstance().getPlayerGUIHistory().containsKey(player)) {
                            playerGUIHistory = new PlayerGUIHistory(player);
                        } else {
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
                        if (resetPage) {
                            for (GUIPagination pagination : paginations) {
                                pagination.getPaginationPlayerDataMap().get(player).setPage(1);
                            }
                        }
                        playerGUIData.updateGui(true);
                        playerGUIData.open();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
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
                            if (openResponse != null) {
                                openResponse.onOpen(player, event);
                            }
                        }
                    }
                }
            }
        };
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            Inventory inventory = event.getPlayer().getOpenInventory().getTopInventory();
            if (playerGUIDataMap.containsKey(player)) {
                PlayerGUIData guiData = playerGUIDataMap.get(player);
                if (inventory.equals(guiData.getInventory())) {
                    if (closeResponse != null) {
                        closeResponse.onClose(player, event);
                    }
                }
            }
        }
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
