package eu.gs.gslibrary.utils.cooldowns;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CooldownAPI {

    private final Map<String, Cooldown> cooldowns;

    public CooldownAPI() {
        cooldowns = new HashMap<>();
    }

    /**
     * It creates a new cooldown with the name and time specified, and then adds it to the cooldowns HashMap
     *
     * @param name The name of the cooldown.
     * @param time The time in seconds that the cooldown will last.
     * @return A new Cooldown object.
     */
    public Cooldown createCooldown(String name, int time) {
        Cooldown cooldown = new Cooldown(name, time);
        cooldowns.put(name, cooldown);
        return cooldown;
    }

    public Cooldown getCooldown(String name) {
        return cooldowns.get(name);
    }

}
