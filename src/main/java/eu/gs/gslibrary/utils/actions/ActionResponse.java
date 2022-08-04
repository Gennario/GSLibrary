package eu.gs.gslibrary.utils.actions;

import org.bukkit.entity.Player;

public interface ActionResponse {

    void action(Player player, String identifier, ActionData data);

}
