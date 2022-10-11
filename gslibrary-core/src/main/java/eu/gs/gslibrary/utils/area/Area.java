package eu.gs.gslibrary.utils.area;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Area {

    private final List<AreaBlock> areaBlockList = new ArrayList<>();

    private final Location pos1, pos2;
    private final String name;

    private Vector least, most;

    public Area(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;

        this.sortCords();
        this.loadBlocks();
    }

    public void sortCords() {
        if (pos1 == null || pos2 == null)
            return;

        Pair<Vector, Vector> tuple = this.sortMostToLeast(this.pos1.toVector(), this.pos2.toVector());
        this.most = tuple.getKey();
        this.least = tuple.getValue();
    }

    private Pair<Vector, Vector> sortMostToLeast(Vector vec0, Vector vec1) {
        Vector most = new Vector();
        Vector least = new Vector();

        if (vec0.getX() > vec1.getX()) {
            most.setX(vec0.getX());
            least.setX(vec1.getX());
        } else {
            most.setX(vec1.getX());
            least.setX(vec0.getX());
        }

        if (vec0.getY() > vec1.getY()) {
            most.setY(vec0.getY());
            least.setY(vec1.getY());
        } else {
            most.setY(vec1.getY());
            least.setY(vec0.getY());
        }

        if (vec0.getZ() > vec1.getZ()) {
            most.setZ(vec0.getZ());
            least.setZ(vec1.getZ());
        } else {
            most.setZ(vec1.getZ());
            least.setZ(vec0.getZ());
        }

        return Pair.of(most, least);
    }

    public boolean isInside(Location loc) {
        return loc.getWorld() == pos1.getWorld() && loc.getBlockX() >= least.getBlockX() && loc.getBlockX() <= most.getBlockX() && loc.getBlockY() >= least.getBlockY() && loc.getBlockY() <= most.getBlockY() && loc.getBlockZ() >= least.getBlockZ() && loc.getBlockZ() <= most.getBlockZ();
    }

    public String toString() {
        return toLocation(this.pos1) + ";" + toLocation(this.pos2);
    }

    public String toLocation(Location loc) {
        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + loc.getWorld().getName();
    }

    public Vector getMinimum() {
        return Vector.getMinimum(pos1.toVector(), pos2.toVector());
    }

    public Vector getMaximum() {
        return Vector.getMaximum(pos1.toVector(), pos2.toVector());
    }

    private void loadBlocks() {
        World world = pos1.getWorld();
        if (world == null) return;

        Vector min = getMinimum();
        Vector max = getMaximum();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.AIR) continue;

                    AreaBlock areaBlock = new AreaBlock(world, new Location(world, x, y, z), block.getType(), block);

                    if (!areaBlockList.contains(areaBlock))
                        areaBlockList.add(areaBlock);
                }
            }
        }

    }

}
