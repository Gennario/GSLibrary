package eu.gs.gslibrary.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public final class MathUtils {

    public static List<Location> calculateArc(Location from, Location to, int points, double offset) {
        List<Location> locations = new ArrayList<>();

        Location toCopy = to.clone();
        toCopy.setY(from.getY());

        int point = 1;
        for (Location location : drawLine(from, toCopy, (from.distance(toCopy) / points))) {
            double height = calculateArcPointLocation(points, point, offset, from, to);
            locations.add(location.clone().add(0, height, 0));
            point++;
            Bukkit.broadcastMessage(height + "");
        }

        return locations;
    }

    private static double calculateArcPointLocation(int points, int point, double offset, Location from, Location to) {
        int originalPoint = point;
        boolean bigger = point > (points / 2);
        double maxY = (to.getY() - from.getY()) + offset;
        double jump = maxY / points;
        double finalY = (maxY / (points / 2)) * point;
        if (bigger) {
            point = (points / 2) - (point - (points / 2));
            finalY = ((offset / (points / 2)) * point) + (to.getY() - from.getY());
        }
        return finalY;
    }

    public static List<Location> drawLine(Location point1, Location point2, double space) {
        List<Location> locations = new ArrayList<>();
        World world = point1.getWorld();
        Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            locations.add(new Location(world, p1.getX(), p1.getY(), p1.getZ()));
            length += space;
        }
        return locations;
    }

    public static List<Location> drawCircle(Location location, int points, double radius, String direcion) {
        Location origin = location.clone();
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            if (direcion.equals("RIGHT")) {
                Location point = origin.clone().add(radius * Math.cos(angle), 0.0d, radius * Math.sin(angle));
                locations.add(point);
            } else if (direcion.equals("LEFT")) {
                Location point = origin.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));
                locations.add(point);
            }
        }
        return locations;
    }

}
