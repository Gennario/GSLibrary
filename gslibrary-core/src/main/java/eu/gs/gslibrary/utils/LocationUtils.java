package eu.gs.gslibrary.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class LocationUtils {

    public static Location getLocation(String s) {
        String[] splitted = s.replace(")", "").split("\\(");
        if (splitted.length == 2) {
            World world = Bukkit.getWorld(splitted[0]);
            double x = Double.parseDouble(splitted[1].split(",")[0]);
            double y = Double.parseDouble(splitted[1].split(",")[1]);
            double z = Double.parseDouble(splitted[1].split(",")[2]);
            return new Location(world, x, y, z);
        }else {
            World world = Bukkit.getWorld(splitted[0]);
            double x = Double.parseDouble(splitted[1].split(",")[0]);
            double y = Double.parseDouble(splitted[1].split(",")[1]);
            double z = Double.parseDouble(splitted[1].split(",")[2]);
            float pitch = Float.parseFloat(splitted[1].split(",")[3]);
            float yaw = Float.parseFloat(splitted[1].split(",")[4]);
            return new Location(world, x, y, z, pitch, yaw);
        }
    }

    public static String locationToString(Location location) {
        String loc = location.getWorld().getName() + "(";
        loc += location.getX() + ",";
        loc += location.getY() + ",";
        if(location.getPitch() != 0 || location.getYaw() != 0) {
            loc += location.getZ() + ",";
            loc += location.getPitch() + ",";
            loc += location.getYaw() + ")";
        }else {
            loc += location.getZ() + ")";
        }
        return loc;
    }

    public static String locationToStringCenter(Location location) {
        String loc = location.getWorld().getName() + "(";
        loc += (location.getX() + 0.5) + ",";
        loc += location.getY() + ",";
        if(location.getPitch() != 0 || location.getYaw() != 0) {
            loc += (location.getZ() + 0.5) + ",";
            loc += location.getPitch() + ",";
            loc += location.getYaw() + ")";
        }else {
            loc += (location.getZ() + 0.5) + ")";
        }
        return loc;
    }

}
