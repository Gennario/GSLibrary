package eu.gs.gslibrary.particles.animations;

import eu.gs.gslibrary.particles.ParticleAnimationExtender;
import eu.gs.gslibrary.particles.ParticleEffect;
import eu.gs.gslibrary.utils.utility.MathUtils;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimatedCircle extends ParticleAnimationExtender {

    private int points, point;
    private double radius;
    private AnimatedCircle.CircleType circleType;
    private AnimatedCircle.CircleDirection circleDirection;
    private Map<ParticleEffect, List<Location>> locationsMap;

    public enum CircleType {
        HORIZONTAL,
        VERTICAL
    }

    public enum CircleDirection {
        LEFT,
        RIGHT
    }

    public AnimatedCircle(int points, double radius, AnimatedCircle.CircleType circleType, AnimatedCircle.CircleDirection circleDirection) {
        this.points = points;
        this.radius = radius;
        this.circleType = circleType;
        this.circleDirection = circleDirection;
        this.locationsMap = new HashMap<>();
    }

    @Override
    public void tick(ParticleEffect particleEffect) {
        List<Location> locationList;
        if (locationsMap.containsKey(particleEffect)) {
            locationList = locationsMap.get(particleEffect);
        } else {
            locationList = MathUtils.drawCircle(particleEffect.getLocation(), points, radius, circleDirection.toString(), circleType.toString());
            locationsMap.put(particleEffect, locationList);
        }

        particleEffect.getParticle().spawnParticleAt(locationList.get(point), particleEffect.getPlayers(locationList.get(point)));

        if(point+1 >= points) {
            point = 0;
        }else {
            point++;
        }
    }
}