package eu.gs.gslibrary.menu;

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
public class GUITitle {

    private TitleType type;
    private Map<Integer, String> lines;
    private int currentTick, currentLine, maxTick;

    public GUITitle(TitleType type, String... lines) {
        this.type = type;
        this.lines = new HashMap<>();
        for (String line : lines) {
            Integer i = Integer.parseInt(line.split("--")[0]);
            String s = line.replaceFirst(i + "--", "");
            this.lines.put(i, s);
        }
        this.currentTick = 0;
        this.currentLine = 0;
        this.maxTick = getMaxTick();
    }

    public int getMaxTick() {
        int i = 0;
        for (Integer integer : lines.keySet()) {
            i = i + integer;
        }
        return i;
    }

    public int getMaxTickForCurrentLine() {
        int i = 0;
        int ii = 0;
        for (Integer integer : lines.keySet()) {
            i = i + integer;
            if (ii == currentLine) return i;
            ii++;
        }
        return i;
    }

    public String next(Player player, @Nullable Replacement replacement) {
        String s = "";
        switch (type) {
            case NORMAL:
                s = lines.get(0);
                break;
            case ANIMATED:
                s = lines.get(currentLine);
                if (currentTick < getMaxTickForCurrentLine()) {
                    currentTick++;
                } else if (currentLine + 1 >= lines.size() && currentTick == getMaxTickForCurrentLine()) {
                    currentLine = 0;
                    currentTick = 0;
                } else if (currentTick == getMaxTickForCurrentLine()) {
                    currentLine++;
                    currentTick++;
                }
        }
        if (replacement != null) {
            s = replacement.replace(player, s);
        }
        s = Utils.colorize(player, s);
        return s;
    }

    public enum TitleType {
        NORMAL,
        ANIMATED
    }

}
