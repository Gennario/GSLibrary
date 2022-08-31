package eu.gs.gslibrary.storage;

import com.mysql.cj.exceptions.CJCommunicationsException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.storage.type.StorageFile;
import eu.gs.gslibrary.storage.type.StorageMySQL;
import eu.gs.gslibrary.utils.config.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.Buffer;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class StorageAPI {

    private final Map<String, Storage> storageMap = new ConcurrentHashMap<>();

    private final HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;

    private final JavaPlugin plugin;
    private final StorageType storageType;
    private final Section sectionMySql, sectionFile;

    private YamlDocument yamlDocument;
    private Connection connection;

    public StorageAPI(JavaPlugin plugin, String type, Section sectionFile, Section sectionMySql, StorageTable... tables) throws Exception {
        this.plugin = plugin;
        this.sectionFile = sectionFile;
        this.sectionMySql = sectionMySql;
        this.storageType = type == null ? StorageType.FILE : StorageType.valueOf(type.toUpperCase());


        this.connect();

        /* Load all tables */
        for (StorageTable storageTable : tables) {
            storageTable.setStorageAPI(this);
            if (storageType == StorageType.FILE) {
                storageMap.put(storageTable.getTable(), new StorageFile(storageTable.getTable(), storageTable.getCondition(), this));
            } else if (storageType == StorageType.MYSQL) {
                storageMap.put(storageTable.getTable(), new StorageMySQL(storageTable.getTable(), storageTable.getCondition(), this));
            }
            storageTable.createMySqlTable();
        }
    }

    private void connect() throws Exception {
        if (storageType == StorageType.FILE) {
            String name = sectionFile.getString("name");
            Config config = new Config(plugin, "", name);
            config.setUpdate(false);
            try {
                config.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            yamlDocument = config.getYamlDocument();
            return;
        }

        if (isConnected()) return;
        if (sectionMySql == null) return;

        String host = sectionMySql.getString("host");
        String username = sectionMySql.getString("user");
        String database = sectionMySql.getString("database");
        String password = sectionMySql.getString("password");
        int port = sectionMySql.getInt("port");
        boolean autoReconnect = sectionMySql.getBoolean("autoReconnect");
        boolean useSsl = sectionMySql.getBoolean("useSSL");

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=" + autoReconnect + "&useSSL=" + useSsl);
        config.setUsername(username);
        config.setPassword(password);

        dataSource = new HikariDataSource(config);
        connection = dataSource.getConnection();
    }

    public void disconnect() {
        dataSource.close();
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void execute(String table, Statement statement, String sql, Object... objects) throws SQLException {
        sql = sql.replace("{table}", table);
        statement.execute(String.format(sql, objects));
    }

    public ResultSet query(String table, Statement statement, String sql, Object... objects) throws SQLException {
        sql = sql.replace("{table}", table);
        return statement.executeQuery(String.format(sql, objects));
    }

    public Storage getStorage(String table) {
        return storageMap.get(table);
    }

    public enum StorageType {
        MYSQL, FILE
    }
}
