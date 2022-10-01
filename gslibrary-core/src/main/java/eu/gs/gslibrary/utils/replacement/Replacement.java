package eu.gs.gslibrary.utils.replacement;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class Replacement {

    private ReplacementAction replacementAction;

    public Replacement(ReplacementAction replacementAction) {
        this.replacementAction = replacementAction;
    }

    public String replace(Player player, String s) {
        return replacementAction.Action(player, s);
    }

}
