package eu.gs.gslibrary.utils.actions;

import com.cryptomorin.xseries.XPotion;
import com.cryptomorin.xseries.XSound;
import eu.gs.gslibrary.utils.api.ActionbarUtils;
import eu.gs.gslibrary.utils.api.TitleUtils;
import eu.gs.gslibrary.utils.replacement.Replacement;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

public class ActionsAPI {

    private final Map<String, ActionResponse> actions;
    private final Replacement replacement;

    public ActionsAPI() {
        this.actions = new HashMap<>();
        this.replacement = new Replacement((player, string) -> string.replace("%player%", player.getName()));

        loadDefaults();
    }

    public void loadDefaults() {
        /* Console and player command */
        addAction("console-cmd", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacement.replace(player, data.getString("value")));
            }
        });
        addAction("player-cmd", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                player.chat("/" + replacement.replace(player, data.getString("value")));
            }
        });

        /* Close inventory action */
        addAction("close-inventory", (player, identifier, data, replacement) -> {
            player.closeInventory();
        });

        /* Actionbar action */
        addAction("actionbar", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                ActionbarUtils.sendActionbar(player, replacement.replace(player, data.getString("value")));
            }
        });
        addAction("actionbar-all", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    ActionbarUtils.sendActionbar(player, replacement.replace(player, data.getString("value")));
                }
            }
        });

        /* Title action */
        addAction("title", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                String[] split = replacement.replace(player, data.getString("value")).split(";");
                TitleUtils.sendTitleMessage(player, split[0], split[1]);
            }
        });
        addAction("title-all", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                String[] split = replacement.replace(player, data.getString("value")).split(";");
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    TitleUtils.sendTitleMessage(onlinePlayer, split[0], split[1]);
                }
            }
        });

        /* Sound action */
        addAction("sound", ((player, identifier, data, replacement1) -> {
            if (data.isExist("value")) {
                String[] split = replacement.replace(player, data.getString("value")).split(";");
                player.playSound(player.getLocation(), XSound.valueOf(split[0].toUpperCase()).parseSound(), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
            }
        }));
        addAction("sound-all", ((player, identifier, data, replacement1) -> {
            if (data.isExist("value")) {
                String[] split = replacement.replace(player, data.getString("value")).split(";");
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), XSound.valueOf(split[0].toUpperCase()).parseSound(), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
                }
            }
        }));

        /* PotionEffect action */
        addAction("potioneffect", ((player, identifier, data, replacement1) -> {
            if (data.isExist("value")) {
                String[] split = replacement.replace(player, data.getString("value")).split(";");
                PotionEffect effect = XPotion.valueOf(split[0].toUpperCase()).buildPotionEffect(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                if (effect != null) {
                    player.addPotionEffect(effect);
                }
            }
        }));
        addAction("potioneffect-all", ((player, identifier, data, replacement1) -> {
            if (data.isExist("value")) {
                String[] split = replacement.replace(player, data.getString("value")).split(";");
                PotionEffect effect = XPotion.valueOf(split[0].toUpperCase()).buildPotionEffect(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                if (effect != null) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.addPotionEffect(effect);
                    }
                }
            }
        }));

        /* Gamemode action */
        addAction("gamemode", ((player, identifier, data, replacement1) -> {
            if (data.isExist("value")) {
                player.setGameMode(GameMode.valueOf(replacement.replace(player, data.getString("value")).toUpperCase()));
            }
        }));
        addAction("gamemode-all", ((player, identifier, data, replacement1) -> {
            if (data.isExist("value")) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.setGameMode(GameMode.valueOf(replacement.replace(player, data.getString("value")).toUpperCase()));
                }
            }
        }));

        /* Fly action */
        addAction("fly", (player, identifier, data, replacement1) -> {
            player.setAllowFlight(!player.getAllowFlight());
        });
        addAction("fly-all", (player, identifier, data, replacement1) -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.setAllowFlight(!onlinePlayer.getAllowFlight());
            }
        });
    }

    public void addAction(String identifier, ActionResponse response) {
        actions.put(identifier, response);
    }

    public void useAction(Player player, ConfigurationSection... actionConfigurations) {
        for (ConfigurationSection section : actionConfigurations) {
            String type = section.getString("type");
            if (actions.containsKey(type)) {
                System.out.println("Action " + type + " doesn't exist! Please try something else...");
                return;
            }
            ActionData data = new ActionData(section);
            actions.get(type).action(player, type, data, replacement);
        }
    }

}
