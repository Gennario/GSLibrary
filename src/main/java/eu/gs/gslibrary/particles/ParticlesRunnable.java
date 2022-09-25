package eu.gs.gslibrary.particles;

import eu.gs.gslibrary.GSLibrary;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticlesRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (ParticleEffect particleEffect : GSLibrary.getInstance().getParticleEffectsMap().values()) {
            particleEffect.tick();
        }
    }
}
