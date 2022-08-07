package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.utils.Utils;
import eu.gs.gslibrary.utils.replacement.Replacement;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class GUICondition {

    private final String condition;
    private String input;
    private String output;
    private Replacement replacement;

    public GUICondition(String condition, String input, String output) {
        this.condition = condition;
        this.input = input;
        this.output = output;
    }

    public GUICondition(String condition, String input, String output, Replacement replacement) {
        this.condition = condition;
        this.input = input;
        this.output = output;
        this.replacement = replacement;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean check(Player player) {
        boolean b = false;
        String input = replacement.replace(player, Utils.colorize(player, this.input));
        switch (condition) {
            case "hasPermission":
                b = player.hasPermission(input);
                break;
            case "!hasPermission":
                b = !player.hasPermission(input);
                break;
            case "==":
                b = input.equals(output);
                break;
            case "!=":
                b = !input.equals(output);
                break;
            case ">":
                if (!isNumeric(input) || !isNumeric(output)) break;
                b = Double.parseDouble(input) > Double.parseDouble(output);
                break;
            case "<":
                if (!isNumeric(input) || !isNumeric(output)) break;
                b = Double.parseDouble(input) < Double.parseDouble(output);
                break;
            case "<=":
                if (!isNumeric(input) || !isNumeric(output)) break;
                b = Double.parseDouble(input) <= Double.parseDouble(output);
                break;
            case ">=":
                if (!isNumeric(input) || !isNumeric(output)) break;
                b = Double.parseDouble(input) >= Double.parseDouble(output);
                break;
        }
        return b;
    }

}
