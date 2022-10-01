package eu.gs.gslibrary.utils.actions;

import eu.gs.gslibrary.utils.replacement.Replacement;
import org.bukkit.entity.Player;

public interface ActionResponse {

    void action(Player player, String identifier, ActionData data, Replacement replacement);

}
