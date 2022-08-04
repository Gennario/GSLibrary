package eu.gs.gslibrary.utils.actions;

import eu.gs.gslibrary.utils.replacement.Replacement;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ActionsAPI {

    private Map<String, ActionResponse> actions;
    private Replacement replacement;

    public ActionsAPI() {
        this.actions = new HashMap<>();
        this.replacement = new Replacement((player, string) -> string.replace("%player%", player.getName()));

        loadDefaults();
    }

    public void loadDefaults() {
        addAction("console-cmd", (player, identifier, data, replacement) -> {
            if(data.isExist("value")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacement.replace(player, data.getString("value")));
            }
        });
        addAction("player-cmd", (player, identifier, data, replacement) -> {
            if(data.isExist("value")) {
                player.chat("/"+replacement.replace(player, data.getString("value")));
            }
        });
    }

    public void addAction(String identifier, ActionResponse response) {
        actions.put(identifier, response);
    }

    public void useAction(Player player, ConfigurationSection... actionConfigurations) {
        for (ConfigurationSection section : actionConfigurations) {
            String type = section.getString("type");
            if(actions.containsKey(type)) {
                System.out.println("Action "+type+" doesn't exist! Please try something else...");
                return;
            }
            ActionData data = new ActionData(section);
            actions.get(type).action(player, type, data, replacement);
        }
    }

}
