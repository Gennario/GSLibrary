package eu.gs.gslibrary.particles.animations;

import eu.gs.gslibrary.utils.utility.MathUtils;
import eu.gs.gslibrary.particles.ParticleAnimationExtender;
import eu.gs.gslibrary.particles.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Round extends ParticleAnimationExtender {

    private int points, rotations;
    private double radius;
    private Map<ParticleEffect, List<Location>> locationsMap;

    public Round(int points, double radius, int rotations) {
        this.points = points;
        this.radius = radius;
        this.rotations = rotations;
        this.locationsMap = new HashMap<>();
    }

    @Override
    public void tick(ParticleEffect particleEffect) {
        List<Location> locationList = new ArrayList<>();
        if(locationsMap.containsKey(particleEffect)) {
            locationList = locationsMap.get(particleEffect);
        }else {
            List<Location> l = MathUtils.drawCircle(particleEffect.getLocation(), points, radius, "LEFT", "VERTICAL");
            locationList.addAll(l);
            double p = Math.PI / rotations;
            double angle = 0d;
            for (int i = 0; i < rotations; i++) {
                angle += p;
                for (Location location : l) {
                    locationList.add(rotateVertical(location.clone(), particleEffect.getLocation().clone(), angle));
                }
            }
            locationsMap.put(particleEffect, locationList);
        }

        for (Location location : locationList) {
            particleEffect.getParticle().spawnParticleAt(location, particleEffect.getPlayers(location));
        }
    }


    private Location rotateVertical(Location v, Location center, double angle) {
        Vector x = v.clone()
                .subtract(center.toVector())
                .toVector();
        x.multiply(new Vector(0, 1, 1));
        return x.rotateAroundY(angle)
                .add(center.toVector())
                .toLocation(v.getWorld());
    }

}
