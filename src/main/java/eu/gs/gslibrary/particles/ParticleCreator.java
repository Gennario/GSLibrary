package eu.gs.gslibrary.particles;

import com.cryptomorin.xseries.XMaterial;
import dev.dejvokep.boostedyaml.YamlDocument;
import eu.gs.gslibrary.utils.LocationUtils;
import eu.gs.gslibrary.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.VibrationData;
import xyz.xenondevs.particle.data.color.DustData;
import xyz.xenondevs.particle.data.color.NoteColor;
import xyz.xenondevs.particle.data.texture.BlockTexture;
import xyz.xenondevs.particle.data.texture.ItemTexture;

@Getter
@Setter
public class ParticleCreator {

    private ParticleBuilder particleBuilder;
    private ParticleEffect particleEffect;

    public ParticleCreator(ParticleEffect particleEffect) {
        particleBuilder = new ParticleBuilder(particleEffect);
        this.particleEffect = particleEffect;
    }

    public ParticleCreator() {
        particleBuilder = new ParticleBuilder(ParticleEffect.VILLAGER_HAPPY);
        this.particleEffect = ParticleEffect.VILLAGER_HAPPY;
    }


    public ParticleCreator setType(ParticleEffect particleEffect) {
        particleBuilder = new ParticleBuilder(particleEffect);
        this.particleEffect = particleEffect;
        return this;
    }

    public ParticleCreator setLocation(Location location) {
        particleBuilder.setLocation(location);
        return this;
    }

    public ParticleCreator setOffset(Vector vector) {
        particleBuilder.setOffset(vector);
        return this;
    }

    public ParticleCreator setOffsetX(float x) {
        particleBuilder.setOffsetX(x);
        return this;
    }

    public ParticleCreator setOffsetY(float y) {
        particleBuilder.setOffsetY(y);
        return this;
    }

    public ParticleCreator setOffsetZ(float z) {
        particleBuilder.setOffsetZ(z);
        return this;
    }

    public ParticleCreator setSpeed(float speed) {
        particleBuilder.setSpeed(speed);
        return this;
    }

    public ParticleCreator setAmount(int amount) {
        particleBuilder.setAmount(amount);
        return this;
    }

    public ParticleCreator setDustData(int r, int g, int b, float size) {
        particleBuilder.setParticleData(new DustData(r, g, b, size));
        return this;
    }

    public ParticleCreator setNoteColor(int note) {
        particleBuilder.setParticleData(new NoteColor(note));
        return this;
    }

    public ParticleCreator setBlockTexture(XMaterial material) {
        particleBuilder.setParticleData(new BlockTexture(material.parseMaterial()));
        return this;
    }

    public ParticleCreator setBlockTexture(XMaterial material, byte data) {
        particleBuilder.setParticleData(new BlockTexture(material.parseMaterial(), data));
        return this;
    }

    public ParticleCreator setItemTexture(XMaterial material) {
        assert material.parseMaterial() != null;
        particleBuilder.setParticleData(new ItemTexture(new ItemStack(material.parseMaterial())));
        return this;
    }

    public ParticleCreator setItemTexture(ItemStack stack) {
        particleBuilder.setParticleData(new ItemTexture(new ItemStack(stack)));
        return this;
    }

    public ParticleCreator setVibrationData(Location start, Location end, int ticks) {
        particleBuilder.setParticleData(new VibrationData(start, end, ticks));
        return this;
    }

    public ParticleCreator setVibrationData(Location end, int ticks) {
        particleBuilder.setParticleData(new VibrationData(end, ticks));
        return this;
    }

    public ParticleCreator setVibrationData(Location start, Entity end, int ticks) {
        particleBuilder.setParticleData(new VibrationData(start, end, ticks));
        return this;
    }

    public ParticleCreator setVibrationData(Entity end, int ticks) {
        particleBuilder.setParticleData(new VibrationData(end, ticks));
        return this;
    }

    public void loadFromConfig(YamlDocument yamlDocument) {
        setType(ParticleEffect.valueOf(yamlDocument.getString("type")));
        if (yamlDocument.contains("speed")) setSpeed(yamlDocument.getFloat("speed"));
        if (yamlDocument.contains("offset")) {
            if (yamlDocument.contains("offset.x")) setOffsetX(yamlDocument.getFloat("offset.x"));
            if (yamlDocument.contains("offset.y")) setOffsetY(yamlDocument.getFloat("offset.y"));
            if (yamlDocument.contains("offset.z")) setOffsetZ(yamlDocument.getFloat("offset.z"));
        }
        if (yamlDocument.contains("amount")) setAmount(yamlDocument.getInt("amount"));
        if (yamlDocument.contains("dust_data")) {
            if(yamlDocument.contains("dust_data.color") && yamlDocument.contains("dust_data.size")) {
                String[] colors = yamlDocument.getString("dust_data.color").split(":");
                setDustData(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]), yamlDocument.getFloat("dust_data.size"));
            }
        }else if (yamlDocument.contains("note_data")) {
            if(yamlDocument.contains("note_data.note")) {
                setNoteColor(yamlDocument.getInt("note_data.note"));
            }
        }else if (yamlDocument.contains("block_texture")) {
            if(yamlDocument.contains("block_texture.material") && yamlDocument.contains("block_texture.data")) {
                setBlockTexture(XMaterial.valueOf(yamlDocument.getString("block_texture.material")),
                        yamlDocument.getByte("block_texture.data"));
            }
        }else if (yamlDocument.contains("item_texture")) {
            if(yamlDocument.contains("item_texture.material")) {
                setItemTexture(XMaterial.valueOf(yamlDocument.getString("item_texture.material")));
            }
        }else if (yamlDocument.contains("vibration_data")) {
            if(yamlDocument.contains("vibration_data.start") && yamlDocument.contains("vibration_data.end") && yamlDocument.contains("vibration_data.ticks")) {
                setVibrationData(LocationUtils.getLocation(yamlDocument.getString("vibration_data.start")),
                        LocationUtils.getLocation(yamlDocument.getString("vibration_data.end")),
                        yamlDocument.getInt("vibration_data.ticks"));
            }
        }
    }

}
