package eu.gs.gslibrary.storage;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.utils.config.Config;
import me.zort.sqllib.SQLConnectionBuilder;
import me.zort.sqllib.SQLDatabaseConnectionImpl;
import me.zort.sqllib.SQLDatabaseOptions;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;

public class StorageConnect {

    private final JavaPlugin instance;
    private final List<String> dataList;

    public StorageConnect(JavaPlugin instance, List<String> dataList) {
        this.instance = instance;
        this.dataList = dataList;

    }

    public SQLDatabaseConnectionImpl connectMySql(Section sectionMySql) {
        if (sectionMySql == null) return null;

        String host = sectionMySql.getString("host");
        String username = sectionMySql.getString("user");
        String database = sectionMySql.getString("database");
        String password = sectionMySql.getString("password");
        int port = sectionMySql.getInt("port");
        boolean autoReconnect = sectionMySql.getBoolean("autoReconnect");
        boolean useSsl = sectionMySql.getBoolean("useSSL");

        if (host == null || username == null || database == null || password == null) return null;

        SQLDatabaseOptions options = new SQLDatabaseOptions();
        options.setLogSqlErrors(false);

        SQLDatabaseConnectionImpl connection = SQLConnectionBuilder.of(host, port, database, username, password)
                .withParam("autoReconnect", "" + autoReconnect)
                .withParam("useSSL", "" + useSsl)
                .build(options);

        if (connection.connect()) {
            dataList.add(" Successfully joined MySql");
            return connection;
        }

        dataList.add(" Successfully did not join MySql");
        return connection;
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
