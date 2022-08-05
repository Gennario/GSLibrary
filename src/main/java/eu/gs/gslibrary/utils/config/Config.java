package eu.gs.gslibrary.utils.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Getter
@Setter
public class Config {

    private final JavaPlugin plugin;
    private final String path, name;

    private YamlDocument yamlDocument;
    private InputStream resource;

    public Config(JavaPlugin plugin, String path, String name, InputStream resource) {
        this.plugin = plugin;
        this.path = path;
        this.name = name;
        this.resource = resource;
    }

    public void load() throws IOException {
        yamlDocument = YamlDocument.create(new File(plugin.getDataFolder(), path+"/"+name+".yml"), resource,
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
    }

}
