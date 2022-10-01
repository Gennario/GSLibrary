package eu.gs.gslibrary.particles.animations;

import eu.gs.gslibrary.particles.ParticleAnimationExtender;
import eu.gs.gslibrary.particles.ParticleEffect;
import eu.gs.gslibrary.utils.api.MathUtils;
import lombok.Getter;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Circle extends ParticleAnimationExtender {

    private int points;
    private double radius;
    private CircleType circleType;
    private CircleDirection circleDirection;
    private Map<ParticleEffect, List<Location>> locationsMap;

    public enum CircleType {
        HORIZONTAL,
        VERTICAL
    }

    public enum CircleDirection {
        LEFT,
        RIGHT
    }

    public Circle(int points, double radius, CircleType circleType, CircleDirection circleDirection) {
        this.points = points;
        this.radius = radius;
        this.circleType = circleType;
        this.circleDirection = circleDirection;
        this.locationsMap = new HashMap<>();
    }

    @Override
    public void tick(ParticleEffect particleEffect) {
        List<Location> locationList;
        if(locationsMap.containsKey(particleEffect)) {
            locationList = locationsMap.get(particleEffect);
        }else {
            locationList = MathUtils.drawCircle(particleEffect.getLocation(), points, radius, circleDirection.toString(), circleType.toString());
            locationsMap.put(particleEffect, locationList);
        }

        for (Location location : locationList) {
            particleEffect.getParticle().spawnParticleAt(location, particleEffect.getPlayers(location));
        }
    }

}