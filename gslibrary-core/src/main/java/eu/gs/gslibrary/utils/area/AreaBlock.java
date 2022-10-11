package eu.gs.gslibrary.utils.area;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class AreaBlock {

    private final World world;
    private final Location location;
    private final Material material;
    private final Block block;

    public void setType() {
        Optional.ofNullable(material).ifPresent(block::setType);
    }

}
