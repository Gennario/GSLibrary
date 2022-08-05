package eu.gs.gslibrary.language;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Language {

    private String name;
    private YamlConfiguration configuration;

    public Language(String name) {
        //YamlDocument config = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"));
    }

}
