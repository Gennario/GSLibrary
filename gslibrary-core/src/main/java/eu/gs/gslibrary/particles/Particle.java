package eu.gs.gslibrary.particles;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@Setter
public class Particle {

    private ParticleCreator creator;

    public Particle(ParticleCreator creator) {
        this.creator = creator;
    }

    public void spawnParticleAt(Location location, Player player) {
        creator.getParticleBuilder().setLocation(location).display(player);
    }

    public void spawnParticleAt(Location location, Player... players) {
        creator.getParticleBuilder().setLocation(location).display(players);
    }

    public void spawnParticleAt(Location location, List<Player> players) {
        creator.getParticleBuilder().setLocation(location).display(players);
    }

}
