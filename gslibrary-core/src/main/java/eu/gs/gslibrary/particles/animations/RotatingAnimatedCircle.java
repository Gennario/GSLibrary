package eu.gs.gslibrary.particles.animations;

import eu.gs.gslibrary.utils.api.MathUtils;
import eu.gs.gslibrary.particles.ParticleAnimationExtender;
import eu.gs.gslibrary.particles.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RotatingAnimatedCircle extends ParticleAnimationExtender {

    private int points, rotatePoints, point;
    private double radius;
    private RotatingAnimatedCircle.CircleType circleType;
    private RotatingAnimatedCircle.CircleDirection circleDirection;
    private Map<ParticleEffect, List<Location>> locationsMap;
    double angle;

    public enum CircleType {
        HORIZONTAL,
        VERTICAL,
    }

    public enum CircleDirection {
        LEFT,
        RIGHT
    }

    public RotatingAnimatedCircle(int points, double radius, int rotatePoints, RotatingAnimatedCircle.CircleType circleType, RotatingAnimatedCircle.CircleDirection circleDirection) {
        this.points = points;
        this.radius = radius;
        this.rotatePoints = rotatePoints;
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
            locationList = MathUtils.drawCircle(particleEffect.getLocation(), points, radius, circleDirection.toString(), circleType.toString().replace("_ROTATE_BOTH", ""));
            locationsMap.put(particleEffect, locationList);
        }

        Location location = locationList.get(point);
        Location l = null;

        if (circleType.equals(RotatingCircle.CircleType.VERTICAL))
            l = rotateVertical(location.clone(), particleEffect.getLocation(), angle);
        else if (circleType.equals(RotatingCircle.CircleType.HORIZONTAL))
            l = rotateHorizontal(location.clone(), particleEffect.getLocation(), angle);

        particleEffect.getParticle().spawnParticleAt(l, particleEffect.getPlayers(l));

        double p = Math.PI / rotatePoints;

        if (circleDirection.equals(RotatingCircle.CircleDirection.RIGHT)) {
            if (angle <= 0) {
                angle = Math.PI * 2;
            } else {
                angle -= p;
            }
        } else {
            if (angle >= Math.PI * 2) {
                angle = 0;
            } else {
                angle += p;
            }
        }

        if (point + 1 >= points) {
            point = 0;
        } else {
            point++;
        }
    }

    private Location rotateHorizontal(Location v, Location center, double angle) {
        Vector x = v.clone()
                .subtract(center.toVector())
                .toVector();
        x.multiply(new Vector(1, 0, 1));
        return x.rotateAroundX(angle)
                .add(center.toVector())
                .toLocation(v.getWorld());
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