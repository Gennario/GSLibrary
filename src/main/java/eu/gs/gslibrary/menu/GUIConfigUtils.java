package eu.gs.gslibrary.menu;

import com.cryptomorin.xseries.XMaterial;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.GSLibrary;
import eu.gs.gslibrary.conditions.ConditionValue;
import eu.gs.gslibrary.utils.Pair;
import eu.gs.gslibrary.utils.replacement.Replacement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public final class GUIConfigUtils {

    /**
     * It loads the GUI data from the configuration file
     *
     * @param gui The GUI object to load the data into.
     * @param configuration The YamlDocument that contains the configuration.
     */
    public static void loadGuiDataFromConfig(GUI gui, YamlDocument configuration) {
        if (configuration.contains("update-time")) gui.setUpdateTime(configuration.getInt("update-time"));
        if (configuration.contains("title")) {
            gui.setGuiTitle(configuration.getString("title", "title"));
        }
        if (configuration.contains("rows")) gui.setRows(Rows.valueOf(configuration.getString("rows")));
        if (configuration.contains("type"))
            gui.setInventoryType(InventoryType.valueOf(configuration.getString("type")));

        if (configuration.contains("close-actions")) {
            gui.setCloseResponse((player, event) -> {
                for (String s : configuration.getSection("close-actions").getRoutesAsStrings(false)) {
                    GSLibrary.getInstance().getActionsAPI().useAction(player, configuration.getSection("close-actions." + s));
                }
            });
        }
        if (configuration.contains("open-actions")) {
            gui.setOpenResponse((player, event) -> {
                for (String s : configuration.getSection("open-actions").getRoutesAsStrings(false)) {
                    GSLibrary.getInstance().getActionsAPI().useAction(player, configuration.getSection("open-actions." + s));
                }
            });
        }
    }

    /**
     * It gets an item from a section
     *
     * @param section The section of the config file that contains the item.
     * @return A GUIItem
     */
    public static GUIItem getItemFromString(Section section) {
        if (section.contains("condition_item")) {
            GUIConditionalItem guiItem = new GUIConditionalItem();
            guiItem.setUpdate(section.getBoolean("condition_item.update"));
            for (String key : section.getSection("condition_item.list").getRoutesAsStrings(false)) {
                Section section1 = section.getSection("condition_item.list." + key);
                List<ConditionValue> conditions = new ArrayList<>();
                for (String s : section1.getSection("conditions").getRoutesAsStrings(false)) {
                    Section ss = section1.getSection("conditions." + s);
                    conditions.add(new ConditionValue(ss));
                }
                guiItem.addItem(section1.getInt("priority"), (GUINormalItem) getItem(section1.getSection("item"), null), conditions);
            }
            if (section.contains("condition_item.actions")) {
                guiItem.setResponse((player, event) -> {
                    for (String s : section.getSection("condition_item.actions").getRoutesAsStrings(false)) {
                        GSLibrary.getInstance().getActionsAPI().useAction(player, section.getSection("condition_item.actions." + s));
                    }
                });
            }
            return guiItem;
        } else {
            return getItem(section, null);
        }
    }

    /**
     * It gets an item from a section, and if the section contains a condition_item, it will return a GUIConditionalItem,
     * otherwise it will return a GUIItem
     *
     * @param section The section of the config file that contains the item.
     * @param replacement This is a Replacement object, which is used to replace variables in the item.
     * @return A GUIItem
     */
    public static GUIItem getItemFromString(Section section, Replacement replacement) {
        if (section.contains("condition_item")) {
            GUIConditionalItem guiItem = new GUIConditionalItem();
            guiItem.setUpdate(section.getBoolean("condition_item.update"));
            for (String key : section.getSection("condition_item.list").getRoutesAsStrings(false)) {
                Section section1 = section.getSection("condition_item.list." + key);
                List<ConditionValue> conditions = new ArrayList<>();
                for (String s : section1.getSection("conditions").getRoutesAsStrings(false)) {
                    Section ss = section1.getSection("conditions." + s);
                    conditions.add(new ConditionValue(ss));
                }
                guiItem.addItem(section1.getInt("priority"), (GUINormalItem) getItem(section1.getSection("item"), replacement), conditions);
            }

            if (section.contains("condition_item.actions")) {
                guiItem.setResponse((player, event) -> {
                    for (String s : section.getSection("condition_item.actions").getRoutesAsStrings(false)) {
                        GSLibrary.getInstance().getActionsAPI().useAction(player, replacement, section.getSection("condition_item.actions." + s));
                    }
                });
            }
            return guiItem;
        } else {
            return getItem(section, replacement);
        }
    }

    private static GUIItem getItem(Section section, Replacement replacement) {
        GUINormalItem guiItem = null;
        if (section.contains("data")) {
            guiItem = new GUINormalItem(XMaterial.matchXMaterial(section.getString("material")).get().parseMaterial(), section.getByte("data"));
        } else {
            guiItem = new GUINormalItem(XMaterial.matchXMaterial(section.getString("material")).get().parseMaterial());
        }
        if (section.contains("skin")) guiItem.setHeadSkinPlayer(section.getString("skin"));
        if (section.contains("base64")) guiItem.setHeadSkinBase64(section.getString("base64"));
        if (section.contains("amount")) guiItem.setAmount(section.getInt("amount"));
        if (section.contains("name")) guiItem.setName(section.getString("name"));
        if (section.contains("lore")) guiItem.setLore(section.getStringList("lore"));
        if (section.contains("enchants")) {
            List<Pair<Enchantment, Integer>> list = new ArrayList<>();
            for (String s : section.getStringList("enchants")) {
                list.add(new Pair<>(Enchantment.getByName(s.split(";")[0]), Integer.parseInt(s.split(";")[1])));
            }
            guiItem.setEnchantments(list);
        }
        if (section.contains("itemflags")) {
            List<ItemFlag> list = new ArrayList<>();
            for (String s : section.getStringList("itemflags")) {
                list.add(ItemFlag.valueOf(s));
            }
            guiItem.setItemFlags(list);
        }
        if (section.contains("custommodeldata")) guiItem.setCustomModelData(section.getInt("custommodeldata"));
        if (section.contains("update")) guiItem.setUpdate(section.getBoolean("update"));
        guiItem.setResponse((player, event) -> {
            if(section.contains("actions")) {
                for (String s : section.getSection("actions").getRoutesAsStrings(false)) {
                    GSLibrary.getInstance().getActionsAPI().useAction(player, section.getSection("actions." + s));
                }
            }
        });
        if (replacement != null) {
            guiItem.setReplacement(replacement);
        }

        return guiItem;
    }

    public static GUIPagination loadItemsToPagination(Section section, Replacement replacement, GUIPagination pagination) {
        GUIItem nextItem = getItemFromString(section.getSection("next_page.item"), replacement);
        GUIItem nextHideItem = getItemFromString(section.getSection("next_page.hide"), replacement);
        String nextSymbol = section.getString("next_page.symbol");
        GUIItem previousItem = getItemFromString(section.getSection("previous_page.item"), replacement);
        GUIItem previousHideItem = getItemFromString(section.getSection("previous_page.hide"), replacement);
        String previousSymbol = section.getString("previous_page.symbol");

        pagination.setNextPageItem(new Pair<>(nextItem, nextSymbol), replacement)
                .setNextPageEmptyItem(new Pair<>(nextHideItem, nextSymbol))
                .setPreviousPageItem(new Pair<>(previousItem, previousSymbol), replacement)
                .setPreviousPageEmptyItem(new Pair<>(previousHideItem, previousSymbol));

        return pagination;
    }
}
