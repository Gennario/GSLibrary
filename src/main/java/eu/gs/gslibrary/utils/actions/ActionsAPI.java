package eu.gs.gslibrary.utils.actions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ActionsAPI {

    private Map<String, ActionResponse> actions;

    public ActionsAPI() {
        this.actions = new HashMap<>();
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
            actions.get(type).action(player, type, new ActionData(section));
        }
    }

}
