package eu.gs.gslibrary.conditions;

import eu.gs.gslibrary.utils.replacement.Replacement;
import org.bukkit.entity.Player;

public interface ConditionResponse {

    boolean check(String input, String output, Player player, Replacement replacement);

}
