package eu.gs.gslibrary.particles;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.particles.animations.*;
import org.bukkit.Location;

public class ParticleConfigUtils {

    public ParticleEffect getParticleEffect(Section section, Location location) {
        ParticleEffect particleEffect = new ParticleEffect(location.clone());
        ParticleCreator particleCreator = new ParticleCreator();
        particleCreator.loadFromConfig(section.getSection("particle"));
        particleEffect.setParticle(new Particle(particleCreator));
        if(section.contains("animations")) {
            for (String s : section.getSection("animations").getRoutesAsStrings(false)) {
                particleEffect.getParticleAnimationExtenders().add(getAnimation(section.getSection("animations."+s)));
            }
        }
        return particleEffect;
    }

    public ParticleAnimationExtender getAnimation(Section section) {
        switch (section.getString("type", "POINT").toUpperCase()) {
            case "POINT":
                return new Point();
            case "CIRCLE":
                return new Circle(section.getInt("points", 15), section.getDouble("radius", 1.0),
                        Circle.CircleType.valueOf(section.getString("circle_type", "HORIZONTAL")),
                        Circle.CircleDirection.valueOf(section.getString("circle_direction", "LEFT")));
            case "ANIMATED_CIRCLE":
                return new AnimatedCircle(section.getInt("points", 15), section.getDouble("radius", 1.0),
                        AnimatedCircle.CircleType.valueOf(section.getString("circle_type", "HORIZONTAL")),
                        AnimatedCircle.CircleDirection.valueOf(section.getString("circle_direction", "LEFT")));
            case "ROTATING_CIRCLE":
                return new RotatingCircle(section.getInt("points", 15), section.getDouble("radius", 1.0), section.getInt("rotate_points", 50),
                        RotatingCircle.CircleType.valueOf(section.getString("circle_type", "HORIZONTAL")),
                        RotatingCircle.CircleDirection.valueOf(section.getString("circle_direction", "LEFT")));
            case "ROTATING_ANIMATED_CIRCLE":
                return new RotatingAnimatedCircle(section.getInt("points", 15), section.getDouble("radius", 1.0), section.getInt("rotate_points", 50),
                        RotatingAnimatedCircle.CircleType.valueOf(section.getString("circle_type", "HORIZONTAL")),
                        RotatingAnimatedCircle.CircleDirection.valueOf(section.getString("circle_direction", "LEFT")));
            case "ROUND":
                return new Round(section.getInt("points", 15), section.getDouble("radius", 1.0), section.getInt("rotations", 15));
            case "SHINING_ROUND":
                return new ShiningRound(section.getInt("points", 15), section.getDouble("radius", 1.0), section.getInt("rotations", 15),
                        section.getInt("percentage", 4), section.getInt("pause", 1));
        }
        return new Point();
    }

}
