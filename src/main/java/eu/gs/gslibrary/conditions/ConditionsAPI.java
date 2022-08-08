package eu.gs.gslibrary.conditions;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.utils.Utils;
import eu.gs.gslibrary.utils.replacement.Replacement;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ConditionsAPI {

    private final Map<String, Condition> conditions;

    public ConditionsAPI() {
        this.conditions = new HashMap<>();

        loadDefaults();
    }

    public void loadDefaults() {
        addCondition("==", (input, output, player, replacement) -> replacement.replace(player, input).equals(output));
        addCondition("==Aa", (input, output, player, replacement) -> replacement.replace(player, input).equalsIgnoreCase(output));
        addCondition(">", (input, output, player, replacement) -> {
            double i = Double.parseDouble(replacement.replace(player, input));
            double o = Double.parseDouble(output);
            return i > o;
        });
        addCondition("<", (input, output, player, replacement) -> {
            double i = Double.parseDouble(replacement.replace(player, input));
            double o = Double.parseDouble(output);
            return i < o;
        });
        addCondition(">=", (input, output, player, replacement) -> {
            double i = Double.parseDouble(replacement.replace(player, input));
            double o = Double.parseDouble(output);
            return i >= o;
        });
        addCondition("<=", (input, output, player, replacement) -> {
            double i = Double.parseDouble(replacement.replace(player, input));
            double o = Double.parseDouble(output);
            return i <= o;
        });
    }

    public void addCondition(String type, ConditionResponse response) {
        conditions.put(type, new Condition(type, response));
    }

    public boolean check(Player player, Section section, @Nullable Replacement replacement) {
        if(replacement == null) replacement = new Replacement((player1, string) -> string);

        String type = section.getString("type");
        String input = Utils.colorize(player, section.getString("input"));
        String output = section.getString("output");

        if(!conditions.containsKey(type)) {
            System.out.println("Condition "+type+" doesn't exist!");
            return false;
        }

        Condition condition = conditions.get(type);

        return condition.getConditionResponse().check(input, output, player, replacement);
    }

    public boolean check(Player player, ConditionValue conditionValue, @Nullable Replacement replacement) {
        if(replacement == null) replacement = new Replacement((player1, string) -> string);

        String input = Utils.colorize(player, Utils.colorize(player, conditionValue.getInput()));
        String output = conditionValue.getOutput();

        Condition condition = conditionValue.getCondition();

        return condition.getConditionResponse().check(input, output, player, replacement);
    }

}
