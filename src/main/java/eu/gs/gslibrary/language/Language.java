package eu.gs.gslibrary.language;

import eu.gs.gslibrary.utils.config.Config;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@Getter
@Setter
public class Language {

    private final JavaPlugin plugin;

    private String name;
    private Config config;

    public Language(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;

        config = new Config(plugin, "languages", name, plugin.getResource("languages/"+name+".yml"));
        try {
            config.load();
        } catch (IOException e) {
        }
    }

}
