package eu.gs.gslibrary.particles.animations;

import eu.gs.gslibrary.particles.ParticleAnimationExtender;
import eu.gs.gslibrary.particles.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Point extends ParticleAnimationExtender {

    private Vector vector;

    public Point() {
        vector = new Vector();
    }

    public Point(Vector offset) {
        vector = offset;
    }

    @Override
    public void tick(ParticleEffect particleEffect) {
        Location location = particleEffect.getLocation().clone();
        location.add(vector);

        particleEffect.getParticle().spawnParticleAt(location, particleEffect.getPlayers(location));
    }
}
