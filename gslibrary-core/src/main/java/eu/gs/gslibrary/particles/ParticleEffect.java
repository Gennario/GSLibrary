package eu.gs.gslibrary.particles;

import eu.gs.gslibrary.GSLibrary;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ParticleEffect {

    private UUID id;

    private Location location;
    private Particle particle;
    private List<ParticleAnimationExtender> particleAnimationExtenders;

    private int viewDistance;

    public enum ShowMode {
        NOTHING,
        WHITELIST,
        BLACKLIST
    }

    private ShowMode showMode;
    private List<String> showList;

    private boolean run;

    public ParticleEffect(Location location) {
        id = UUID.randomUUID();

        this.location = location;

        this.viewDistance = 30;
        this.showMode = ShowMode.NOTHING;
        this.showList = new ArrayList<>();

        particleAnimationExtenders = new ArrayList<>();

        GSLibrary.getInstance().getParticleEffectsMap().put(id, this);
    }

    public ParticleEffect(Location location, Particle particle) {
        id = UUID.randomUUID();

        this.location = location;
        this.particle = particle;

        this.viewDistance = 30;
        this.showMode = ShowMode.NOTHING;
        this.showList = new ArrayList<>();

        GSLibrary.getInstance().getParticleEffectsMap().put(id, this);
    }

    public ParticleEffect(Location location, Particle particle, List<ParticleAnimationExtender> particleAnimationExtenders) {
        id = UUID.randomUUID();

        this.location = location;
        this.particle = particle;
        this.particleAnimationExtenders = particleAnimationExtenders;

        this.viewDistance = 30;
        this.showMode = ShowMode.NOTHING;
        this.showList = new ArrayList<>();

        GSLibrary.getInstance().getParticleEffectsMap().put(id, this);
    }

    public void tick() {
        if (run) {
            for (ParticleAnimationExtender extender : particleAnimationExtenders) {
                extender.tick(this);
            }
        }
    }

    public List<Player> getPlayers(Location location) {
        List<Player> players = new ArrayList<>();
        for (Player player : location.getWorld().getPlayers()) {
            if (showMode.equals(ShowMode.WHITELIST) || showMode.equals(ShowMode.BLACKLIST)) {
                switch (showMode) {
                    case BLACKLIST:
                        if (showList.contains(player.getName())) continue;
                        break;
                    case WHITELIST:
                        if (!showList.contains(player.getName())) continue;
                        break;
                }
            }

            Location playerLocation = player.getLocation().clone();
            playerLocation.setY(location.clone().getY());
            if (location.distance(playerLocation) <= viewDistance) {
                players.add(player);
            }
        }
        return players;
    }

    public ParticleEffect start() {
        if (particleAnimationExtenders == null || particle == null || location == null) return this;
        run = true;
        return this;
    }

    public ParticleEffect startForTime(int ticks) {
        if (particleAnimationExtenders == null || particle == null || location == null) return this;
        run = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                stop();
            }
        }.runTaskLater(GSLibrary.getInstance(), ticks);
        return this;
    }

    public ParticleEffect startOnce(int ticks) {
        if (particleAnimationExtenders == null || particle == null || location == null) return this;
        run = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                stop();
                delete();
            }
        }.runTaskLater(GSLibrary.getInstance(), ticks);
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
}
