package eu.gs.gslibrary.particles;

import eu.gs.gslibrary.GSLibrary;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ParticleEffect {

    private UUID id;

    private Location location;
    private Particle particle;
    private ParticleAnimationExtender particleAnimationExtender;

    private int viewDistance;

    private boolean run;

    public ParticleEffect(Location location) {
        id = UUID.randomUUID();

        this.location = location;

        this.viewDistance = 30;

        GSLibrary.getInstance().getParticleEffectsMap().put(id, this);
    }

    public ParticleEffect(Location location, Particle particle) {
        id = UUID.randomUUID();

        this.location = location;
        this.particle = particle;

        this.viewDistance = 30;

        GSLibrary.getInstance().getParticleEffectsMap().put(id, this);
    }

    public ParticleEffect(Location location, Particle particle, ParticleAnimationExtender particleAnimationExtender) {
        id = UUID.randomUUID();

        this.location = location;
        this.particle = particle;
        this.particleAnimationExtender = particleAnimationExtender;

        this.viewDistance = 30;

        GSLibrary.getInstance().getParticleEffectsMap().put(id, this);
    }

    public void tick() {
        if(run) {
            particleAnimationExtender.tick(this);
        }
    }

    public List<Player> getPlayers(Location location) {
        List<Player> players = new ArrayList<>();
        for (Player player : location.getWorld().getPlayers()) {
            Location playerLocation = player.getLocation().clone();
            playerLocation.setY(location.clone().getY());
            if(location.distance(playerLocation) <= viewDistance) {
                players.add(player);
            }
        }
        return players;
    }

    public ParticleEffect start() {
        if(particleAnimationExtender == null || particle == null || location == null) return this;
        run = true;
        return this;
    }

    public ParticleEffect stop() {
        run = false;
        return this;
    }

    public void delete() {
        GSLibrary.getInstance().getParticleEffectsMap().remove(id);
    }

    public ParticleEffect setLocation(Location location) {
        this.location = location;
        return this;
    }

    public ParticleEffect setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
        return this;
    }

    public ParticleEffect setId(UUID id) {
        this.id = id;
        return this;
    }

    public ParticleEffect setParticle(Particle particle) {
        this.particle = particle;
        return this;
    }

    public ParticleEffect setParticleAnimationExtender(ParticleAnimationExtender particleAnimationExtender) {
        this.particleAnimationExtender = particleAnimationExtender;
        return this;
    }
}
