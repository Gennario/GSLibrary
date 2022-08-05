package eu.gs.gslibrary.language;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class LanguageAPI {

    private Map<String, Language> languages;
    private JavaPlugin instance;

    private File file;

    public LanguageAPI(JavaPlugin plugin) {
        this.languages = new HashMap<>();
        this.instance = plugin;

        this.file = new File(instance.getDataFolder()+"/languages/");
        if(!file.exists()) {
            file.mkdir();
        }
    }



}
