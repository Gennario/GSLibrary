package eu.gs.gslibrary.utils.cooldowns;

import eu.gs.gslibrary.utils.Pair;
import eu.gs.gslibrary.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Cooldown {

    private final String name;
    private int time;

    private boolean paused;

    private Map<Player, Pair<Integer, CooldownDoneResponse>> data;

    // A constructor.
    public Cooldown(String name, int time) {
        this.name = name;
        this.time = time;

        this.paused = false;

        this.data = new HashMap<>();
    }

    /**
     * For each player in the data map, get the time left, decrement it, and if it's 0, stop the player, otherwise, if the
     * player has a tickable, tick it, and then put the player back into the map with the new time
     */
    public void update() {
        for (Player player : data.keySet()) {
            int t = data.get(player).getKey();
            t--;
            if(t == 0) {
                stop(player);
                continue;
            }
            if(data.get(player).getValue() != null) data.get(player).getValue().tick();
            data.put(player, new Pair<>(t, data.get(player).getValue()));
        }
    }

    /**
     * It creates a new Pair object with the time and null, and then puts it into the data HashMap with the player as the
     * key
     *
     * @param player The player to start the cooldown for.
     */
    public void start(Player player) {
        data.put(player, new Pair<>(time, null));
    }

    /**
     * It takes a player and a special time, and adds them to a map
     *
     * @param player The player to start the special attack for.
     * @param specialTime The time in seconds that the special bar will last.
     */
    public void start(Player player, int specialTime) {
        data.put(player, new Pair<>(specialTime, null));
    }

    /**
     * It starts the cooldown for the player, and when the cooldown is done, it calls the response
     *
     * @param player The player who's cooldown is being started.
     * @param specialTime The time in seconds that the cooldown will last.
     * @param response The response that will be called when the cooldown is done.
     */
    public void start(Player player, int specialTime, CooldownDoneResponse response) {
        data.put(player, new Pair<>(specialTime, response));
        response.start();
    }

    public int getPlayerTime(Player player) {
        return data.get(player).getKey();
    }

    public String getPlayerTimeFormatted(Player player) {
        return TimeUtils.calculateTime(data.get(player).getKey());
    }

    public boolean check(Player player) {
        return data.containsKey(player);
    }

    public void stop(Player player) {
        if(!check(player)) return;
        if(data.get(player).getValue() != null) data.get(player).getValue().stop();
        data.remove(player);
    }

    public void stopAll() {
        for (Pair<Integer, CooldownDoneResponse> pair : data.values()) {
            if(pair.getValue() != null) pair.getValue().stop();
        }
        data.clear();
    }

    public void togglePause() {
        this.paused = !paused;
    }

}
