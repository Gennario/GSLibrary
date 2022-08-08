package eu.gs.gslibrary.conditions;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.GSLibrary;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionValue {

    private String type, input, output;
    private ConditionsAPI conditionsAPI;
    private Condition condition;

    public ConditionValue(Section section) {
        conditionsAPI = GSLibrary.getInstance().getConditionsAPI();

        type = section.getString("type");
        input = section.getString("input");
        output = section.getString("output");

        if(!conditionsAPI.getConditions().containsKey(type)) {
            System.out.println("Condition "+type+" doesn't exist!");
            return;
        }

        condition = conditionsAPI.getConditions().get(type);
    }

}
