package eu.gs.gslibrary.menu;

import com.google.gson.stream.JsonToken;
import eu.gs.gslibrary.utils.methods.AsyncMethod;
import eu.gs.gslibrary.utils.methods.SyncMethod;
import eu.gs.gslibrary.utils.replacement.Replacement;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PlayerGUIData {

    private GUI gui;
    private Player player;
    private Replacement replacement;

    private Inventory inventory;
    private Map<GUIPagination, Integer> oldPageMap;

    public PlayerGUIData(GUI gui, Player player, Replacement replacement) {
        this.gui = gui;
        this.player = player;
        this.replacement = replacement;
        this.oldPageMap = new HashMap<>();
    }

    public void load() {
        inventory = Bukkit.createInventory(player, gui.getInventoryType(), gui.getGuiTitle().next(player, replacement));
        if (inventory.getType().equals(InventoryType.CHEST) || inventory.getType().equals(InventoryType.ENDER_CHEST) || inventory.getType().equals(InventoryType.SHULKER_BOX)) {
            inventory = Bukkit.createInventory(player, gui.getRows().getSlots(), gui.getGuiTitle().next(player, replacement));
        }

        for (Integer integer : gui.getItems().keySet()) {
            GUIItem guiItem = gui.getItems().get(integer);
            ItemStack stack = guiItem.loadItem(player, replacement);
            inventory.setItem(integer, stack);
        }

        for (GUIPagination pagination : gui.getPaginations()) {
            if (!pagination.getPaginationPlayerDataMap().containsKey(player)) {
                pagination.loadPlayerData(player);
            }

            GUIPaginationPlayerData playerData = pagination.getPaginationPlayerDataMap().get(player);

            int i = 0;
            for (GUIItem pageItem : pagination.getPageItems(player)) {
                int slot = pagination.getSlots().get(i);
                playerData.addGUIItem(slot, pageItem);
                inventory.setItem(slot, pageItem.loadItem(player, replacement));
                i++;
            }

            if (pagination.getPreviousPageItem() != null) {
                if (pagination.canPreviousPage(playerData.getPage())) {
                    gui.getGuiItemMap().getSlotsByKey(pagination.getPreviousPageItem().getValue()).forEach(integer -> {
                        GUIItem guiItem = pagination.getPreviousPageItem().getKey();
                        inventory.setItem(integer, guiItem.loadItem(player, replacement));
                    });
                } else if (pagination.getPreviousPageEmptyItem() != null) {
                    gui.getGuiItemMap().getSlotsByKey(pagination.getPreviousPageItem().getValue()).forEach(integer -> {
                        GUIItem guiItem = pagination.getPreviousPageEmptyItem().getKey();
                        inventory.setItem(integer, guiItem.loadItem(player, replacement));
                    });
                }
            }

            if (pagination.getNextPageItem() != null) {
                if (pagination.canNextPage(playerData.getPage(), playerData.getSize())) {
                    gui.getGuiItemMap().getSlotsByKey(pagination.getNextPageItem().getValue()).forEach(integer -> {
                        GUIItem guiItem = pagination.getNextPageItem().getKey();
                        inventory.setItem(integer, guiItem.loadItem(player, replacement));
                    });
                } else if (pagination.getNextPageEmptyItem() != null) {
                    gui.getGuiItemMap().getSlotsByKey(pagination.getNextPageItem().getValue()).forEach(integer -> {
                        GUIItem guiItem = pagination.getNextPageEmptyItem().getKey();
                        inventory.setItem(integer, guiItem.loadItem(player, replacement));
                    });
                }
            }
        }

    }

    public void updateGui(boolean resetItems) {
        new AsyncMethod() {
            @Override
            public void action() {
                Replacement replacement = gui.onGuiUpdate(player);

                for (Integer integer : gui.getItems().keySet()) {
                    GUIItem guiItem = gui.getItems().get(integer);
                    if (guiItem.isUpdate()) {
                        inventory.setItem(integer, guiItem.loadItem(player, replacement));
                    }
                }

                for (GUIPagination pagination : gui.getPaginations()) {
                    GUIPaginationPlayerData playerData = pagination.getPaginationPlayerDataMap().get(player);

                    if (!pagination.isUpdate()) continue;
                    pagination.updatePlayerData(player);
                    int i = 0;

                    boolean newPage = false;
                    if (oldPageMap.containsKey(pagination)) {
                        if (playerData.getPage() != oldPageMap.get(pagination)) {
                            newPage = true;
                            playerData.resetGUIItems();
                            for (Integer slot : pagination.getSlots()) {
                                inventory.setItem(slot, new ItemStack(Material.AIR));
                            }
                            oldPageMap.put(pagination, playerData.getPage());
                        }
                    } else {
                        oldPageMap.put(pagination, playerData.getPage());
                    }

                    if (resetItems) {
                        playerData.resetGUIItems();
                        for (Integer slot : pagination.getSlots()) {
                            inventory.setItem(slot, new ItemStack(Material.AIR));
                        }
                        newPage = true;
                    }

                    for (GUIItem pageItem : pagination.getPageItems(player)) {
                        int slot = pagination.getSlots().get(i);
                        inventory.setItem(slot, pageItem.loadItem(player, replacement));
                        if (newPage) playerData.addGUIItem(slot, pageItem);
                        i++;
                    }

                    System.out.println("+++++++++++++++++++++++++++++++");
                    if (pagination.getPreviousPageItem() != null) {
                        System.out.println("previous vyvolano");
                        if (pagination.canPreviousPage(playerData.getPage())) {
                            System.out.println("previous item");
                            gui.getGuiItemMap().getSlotsByKey(pagination.getPreviousPageItem().getValue()).forEach(integer -> {
                                System.out.println("symbol - "+pagination.getPreviousPageItem().getValue()+",  slot - "+integer);
                                GUIItem guiItem = pagination.getPreviousPageItem().getKey();
                                ItemStack itemStack = guiItem.loadItem(player, replacement);
                                System.out.println(""+(itemStack.getType().name())+", "+(itemStack != null));
                                inventory.setItem(integer, itemStack);
                            });
                        } else if (pagination.getPreviousPageEmptyItem() != null) {
                            System.out.println("previous empty");
                            gui.getGuiItemMap().getSlotsByKey(pagination.getPreviousPageItem().getValue()).forEach(integer -> {
                                GUIItem guiItem = pagination.getPreviousPageEmptyItem().getKey();
                                inventory.setItem(integer, guiItem.loadItem(player, replacement));
                            });
                        } else {
                            System.out.println("previous null");
                            gui.getGuiItemMap().getSlotsByKey(pagination.getPreviousPageItem().getValue()).forEach(integer -> {
                                inventory.setItem(integer, new ItemStack(Material.AIR));
                            });
                        }
                    }else {
                        System.out.println("previous je proste null");
                    }
                    if (pagination.getNextPageItem() != null) {
                        if (pagination.canNextPage(playerData.getPage(), playerData.getSize())) {
                            gui.getGuiItemMap().getSlotsByKey(pagination.getNextPageItem().getValue()).forEach(integer -> {
                                GUIItem guiItem = pagination.getNextPageItem().getKey();
                                inventory.setItem(integer, guiItem.loadItem(player, replacement));
                            });
                        } else if (pagination.getNextPageEmptyItem() != null) {
                            gui.getGuiItemMap().getSlotsByKey(pagination.getNextPageItem().getValue()).forEach(integer -> {
                                GUIItem guiItem = pagination.getNextPageEmptyItem().getKey();
                                inventory.setItem(integer, guiItem.loadItem(player, replacement));
                            });
                        } else {
                            gui.getGuiItemMap().getSlotsByKey(pagination.getNextPageItem().getValue()).forEach(integer -> {
                                inventory.setItem(integer, new ItemStack(Material.AIR));
                            });
                        }
                    }
                }
            }
        };
    }

    public boolean checkOpened() {
        return player.getOpenInventory().getTopInventory().equals(inventory);
    }

    public void open() {
        new SyncMethod() {
            @Override
            public void action() {
                player.openInventory(inventory);
            }
        };
    }

}
