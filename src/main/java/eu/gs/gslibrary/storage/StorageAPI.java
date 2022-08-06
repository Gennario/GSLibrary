package eu.gs.gslibrary.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.dejvokep.boostedyaml.YamlDocument;
import eu.gs.gslibrary.utils.config.Config;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Getter
public class StorageAPI {

    private final HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;

    private final JavaPlugin plugin;
    private final Storage storage;
    private final StorageType type;
    private final ConfigurationSection sectionMySql, sectionFile;
    private final String condition;

    private YamlDocument yaml;
    private Connection connection;
    private String table;

    public StorageAPI(JavaPlugin plugin, StorageType type, ConfigurationSection sectionFile, ConfigurationSection sectionMySql, String condition, String... columns) throws SQLException {
        this.plugin = plugin;
        this.type = type;
        this.sectionFile = sectionFile;
        this.sectionMySql = sectionMySql;
        this.condition = condition;
        this.storage = new Storage(this);

        this.connect(columns);
    }

    private void connect(String... columns) throws SQLException {
        if (type == StorageType.FILE) {
            String name = sectionFile.getString("name");
            Config config = new Config(plugin, "", name);
            config.setUpdate(false);
            try {
                config.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            yaml = config.getYamlDocument();
            return;
        }
        if (isConnected()) return;
        if (sectionMySql == null) return;

        String host = sectionMySql.getString("host");
        String username = sectionMySql.getString("username");
        String database = sectionMySql.getString("database");
        String password = sectionMySql.getString("password");
        int port = sectionMySql.getInt("port");
        boolean autoReconnect = sectionMySql.getBoolean("autoReconnect");
        boolean useSsl = sectionMySql.getBoolean("useSSL");
        this.table = sectionMySql.getString("table");

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=" + autoReconnect + "&useSSL=" + useSsl);
        config.setUsername(username);
        config.setPassword(password);

        dataSource = new HikariDataSource(config);
        connection = dataSource.getConnection();

        new StorageTable(table, connection).loadColumns(condition, columns).create();
    }

    public void disconnect() {
        dataSource.close();
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void execute(Statement statement, String sql, Object... objects) throws SQLException {
        sql = sql.replace("{table}", table);
        statement.execute(String.format(sql, objects));
    }

    public ResultSet query(Statement statement, String sql, Object... objects) throws SQLException {
        sql = sql.replace("{table}", table);
        return statement.executeQuery(String.format(sql, objects));
    }

    public enum StorageType {
        MYSQL, FILE
    }
}
