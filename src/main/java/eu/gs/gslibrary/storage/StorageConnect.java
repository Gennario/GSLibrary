package eu.gs.gslibrary.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.utils.config.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;

public class StorageConnect {

    private final JavaPlugin instance;
    private final List<String> dataList;

    /* MySql connection */
    private final HikariConfig config = new HikariConfig();

    public StorageConnect(JavaPlugin instance, List<String> dataList) {
        this.instance = instance;
        this.dataList = dataList;

    }

    public HikariDataSource connectMySql(Section sectionMySql) {
        try {
            if (sectionMySql == null) return null;

            String host = sectionMySql.getString("host");
            String username = sectionMySql.getString("user");
            String database = sectionMySql.getString("database");
            String password = sectionMySql.getString("password");
            int port = sectionMySql.getInt("port");
            boolean autoReconnect = sectionMySql.getBoolean("autoReconnect");
            boolean useSsl = sectionMySql.getBoolean("useSSL");

            if (host == null || username == null || database == null || password == null) return null;

            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=" + autoReconnect + "&useSSL=" + useSsl);
            config.setUsername(username);
            config.setPassword(password);

            HikariDataSource dataSource = new HikariDataSource(config);
            dataList.add(" Successfully joined MySql");
            return dataSource;
        } catch (Exception e) {
            dataList.add(" Successfully did not join MySql");
        }

        return null;
    }

    public YamlDocument connectFile(Section section) {
        if (section == null) return null;
        if (!section.isString("name")) return null;

        try {
            Config config = new Config(instance, "", section.getString("name"));
            config.setUpdate(false);
            try {
                config.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            dataList.add(" Successfully joined File");
            return config.getYamlDocument();
        } catch (Exception e) {
            dataList.add(" Successfully did not join File");
        }

        return null;
    }

    public YamlDocument connectJson(Section section) {
        if (section == null) return null;
        if (!section.isString("name")) return null;

        try {
            Config config = new Config(instance, "", section.getString("name"));
            config.setUpdate(false);
            try {
                config.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            dataList.add(" Successfully joined Json");
            return config.getYamlDocument();
        } catch (Exception e) {
            dataList.add(" Successfully did not join Json");
        }

        return null;
    }
}
