package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.GSLibrary;
import eu.gs.gslibrary.utils.Pair;
import eu.gs.gslibrary.utils.replacement.Replacement;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public final class GUIConfigUtils {
/*/
    public static void loadGuiDataFromConfig(GUI gui, YamlConfiguration configuration) {
        if (configuration.contains("update-time")) gui.setUpdateTime(configuration.getInt("update-time"));
        if (configuration.contains("title"))
            gui.setGuiTitle(new GUITitle(GUITitle.TitleType.NORMAL, "0--" + configuration.getString("title")));
        if (configuration.contains("rows")) gui.setRows(Rows.valueOf(configuration.getString("rows")));
        if (configuration.contains("type"))
            gui.setInventoryType(InventoryType.valueOf(configuration.getString("type")));

        if (configuration.contains("close-actions")) {
            gui.setCloseResponse((player, event) -> {
                for (String s : configuration.getStringList("close-actions")) {
                    Action.run(s, player, 0);
                }
            });
        }
        if (configuration.contains("open-actions")) {
            gui.setOpenResponse((player, event) -> {
                for (String s : configuration.getConfigurationSection("open-actions").getKeys(false)) {
                    GSLibrary.getInstance().getActionsAPI().useAction(player, );
                }
            });
        }
    }

    public static GUIItem getItemFromString(ConfigurationSection section) {
        if (section.contains("condition_item")) {
            GUIConditionalItem guiItem = new GUIConditionalItem();
            guiItem.setUpdate(section.getBoolean("condition_item.update"));
            for (String key : section.getConfigurationSection("condition_item.list").getKeys(false)) {
                ConfigurationSection section1 = section.getConfigurationSection("condition_item.list." + key);
                List<GUICondition> conditions = new ArrayList<>();
                for (String s : section1.getConfigurationSection("conditions").getKeys(false)) {
                    ConfigurationSection ss = section1.getConfigurationSection("conditions." + s);
                    conditions.add(new GUICondition(ss.getString("type"), ss.getString("input"), ss.getString("output")));
                }
                guiItem.addItem(section1.getInt("priority"), getItem(section1.getConfigurationSection("item"), null), conditions);
            }
            guiItem.setResponse((player, event) -> {
                for (String s : section.getStringList("condition_item.actions")) {
                    Action.run(s, player, 0);
                }
            });
            return guiItem;
        } else {
            return getItem(section, null);
        }
    }

    public static GUIItem getItemFromString(ConfigurationSection section, Replacement replacement) {
        if (section.contains("condition_item")) {
            GUIConditionalItem guiItem = new GUIConditionalItem();
            guiItem.setUpdate(section.getBoolean("condition_item.update"));
            for (String key : section.getConfigurationSection("condition_item.list").getKeys(false)) {
                ConfigurationSection section1 = section.getConfigurationSection("condition_item.list." + key);
                List<GUICondition> conditions = new ArrayList<>();
                for (String s : section1.getConfigurationSection("conditions").getKeys(false)) {
                    ConfigurationSection ss = section1.getConfigurationSection("conditions." + s);
                    conditions.add(new GUICondition(ss.getString("type"), ss.getString("input"), ss.getString("output")));
                }
                guiItem.addItem(section1.getInt("priority"), getItem(section1.getConfigurationSection("item"), replacement), conditions);
            }
            guiItem.setResponse((player, event) -> {
                for (String s : section.getStringList("condition_item.actions")) {
                    Action.run(s, player, 0);
                }
            });
            return guiItem;
        } else {
            return getItem(section, replacement);
        }
    }

    private static GUIItem getItem(ConfigurationSection section, Replacement replacement) {
        GUINormalItem guiItem = null;
        if (section.contains("data")) {
            guiItem = new GUINormalItem(Material.valueOf(section.getString("material")), (byte) section.getInt("data"));
        } else {
            guiItem = new GUINormalItem(Material.valueOf(section.getString("material")));
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
            for (String s : section.getStringList("actions")) {
                Action.run(s, player, 0);
            }
        });
        if (replacement != null) {
            guiItem.setReplacement(replacement);
        }

        return guiItem;
    }
/*/
}
