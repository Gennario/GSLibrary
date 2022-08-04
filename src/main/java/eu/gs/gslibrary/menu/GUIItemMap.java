package eu.gs.gslibrary.menu;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class GUIItemMap {

    private List<String> lines;
    private Map<String, List<Integer>> slots;

    public GUIItemMap(String... mapLines) {
        this.lines = new ArrayList<>();
        this.slots = new HashMap<>();
        this.lines.addAll(Arrays.asList(mapLines));

        convert();
    }

    public List<Integer> getSlotsByKey(String key) {
        if (!slots.containsKey(key)) {
            return new ArrayList<>();
        } else {
            return slots.get(key);
        }
    }

    public void convert() {
        int slot = 0;
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                String s = Character.toString(c);
                if (slots.containsKey(s)) {
                    List<Integer> data = slots.get(s);
                    data.add(slot);
                    slots.put(s, data);
                } else {
                    List<Integer> data = new ArrayList<>();
                    data.add(slot);
                    slots.put(s, data);
                }
                slot++;
            }
        }
    }

}
