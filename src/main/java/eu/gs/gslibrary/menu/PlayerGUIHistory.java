package eu.gs.gslibrary.menu;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerGUIHistory {

    private final Player player;
    private GUI current;
    private List<GUI> previous, following;

    public PlayerGUIHistory(Player player) {
        this.player = player;

        previous = new ArrayList<>();
        following = new ArrayList<>();
    }

    public void setCurrent(GUI gui) {
        if(current != null) {
            previous.add(0, current);
        }
        if(!following.contains(gui)) {
            following.clear();
        }
        current = gui;
    }

    public GUI back() {
        if(previous.isEmpty()) return null;
        GUI gui = previous.get(0);

        following.add(0, gui);
        previous.remove(0);

        return gui;
    }

    public GUI forward() {
        if(following.isEmpty()) return null;
        GUI gui = following.get(0);

        previous.add(0, gui);
        following.remove(0);

        return gui;
    }

}
