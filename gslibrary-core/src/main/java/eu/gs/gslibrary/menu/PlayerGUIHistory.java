package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.GSLibrary;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class PlayerGUIHistory {

    private final Player player;
    private GUI current;
    private List<String> previous, following;

    public PlayerGUIHistory(Player player) {
        this.player = player;

        previous = new ArrayList<>();
        following = new ArrayList<>();

        GSLibrary.getInstance().getPlayerGUIHistory().put(player, this);
    }

    public void setCurrent(GUI gui) {
        if(current != null && !Objects.equals(gui.getType(), current.getType())) {
            previous.add(0, current.getType());
        }
        if(following.contains(gui.getType())) {
            following.clear();
        }

        current = gui;
    }

    public GUI back() {
        if(previous.isEmpty()) return current;
        GUI gui = GSLibrary.getInstance().getGUI(previous.get(0));

        following.add(0, current.getType());
        previous.remove(0);

        current = gui;

        return gui;
    }

    public GUI forward() {
        if(following.isEmpty()) return current;
        GUI gui = GSLibrary.getInstance().getGUI(following.get(0));

        previous.add(0, current.getType());
        following.remove(0);

        current = gui;

        return gui;
    }

}
