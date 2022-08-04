package eu.gs.gslibrary.utils.cooldowns;

import eu.gs.gslibrary.utils.Pair;
import eu.gs.gslibrary.utils.TimeUtils;
import io.netty.util.concurrent.CompleteFuture;
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

    public Cooldown(String name, int time) {
        this.name = name;
        this.time = time;

        this.paused = false;

        this.data = new HashMap<>();
    }

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

    public void start(Player player) {
        data.put(player, new Pair<>(time, null));
    }

    public void start(Player player, int specialTime) {
        data.put(player, new Pair<>(specialTime, null));
    }

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
