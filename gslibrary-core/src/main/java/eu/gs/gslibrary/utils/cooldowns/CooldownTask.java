package eu.gs.gslibrary.utils.cooldowns;

import eu.gs.gslibrary.GSLibrary;
import org.bukkit.scheduler.BukkitRunnable;

public class CooldownTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Cooldown cooldown : GSLibrary.getInstance().getCooldownAPI().getCooldowns().values()) {
            if(!cooldown.isPaused()) cooldown.update();
        }
    }
}
