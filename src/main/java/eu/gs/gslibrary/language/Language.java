package eu.gs.gslibrary.language;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.configuration.file.YamlConfiguration;

public class Language {

    private final LanguageAPI languageAPI;

    private String name;
    private YamlConfiguration configuration;

    public Language(LanguageAPI languageAPI, String name) {
        this.languageAPI = languageAPI;

        //YamlDocument config = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"));
    }

}
