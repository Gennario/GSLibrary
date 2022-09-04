package eu.gs.gslibrary.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.GSLibrary;
import eu.gs.gslibrary.storage.json.StorageJson;
import eu.gs.gslibrary.storage.type.Storage;
import eu.gs.gslibrary.storage.type.StorageFile;
import eu.gs.gslibrary.storage.type.StorageMySQL;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class StorageAPI {

    private final JavaPlugin instance;

    private final Map<String, Storage> storageMap = new ConcurrentHashMap<>();
    private final HikariConfig config = new HikariConfig();
    private final StorageType storageType;
    private final Section sectionMySql, sectionFile, sectionJson;
    private StorageJson storageJson;
    private HikariDataSource dataSource;
    private YamlDocument yamlFile, yamlJson;
    private Connection connection;

    public StorageAPI(JavaPlugin plugin, String type, Section sectionFile, Section sectionMySql, Section sectionJson, StorageTable... tables) {
        this.instance = plugin;
        this.sectionFile = sectionFile;
        this.sectionMySql = sectionMySql;
        this.sectionJson = sectionJson;
        this.storageType = type == null ? StorageAPI.StorageType.FILE : StorageAPI.StorageType.valueOf(type.toUpperCase());


        this.connect();

        /* Load all tables */
        if (storageType == StorageType.JSON) {
            storageJson = new StorageJson(this);
            return;
        }


        for (StorageTable storageTable : tables) {
            storageTable.setStorageAPI(this);
            if (storageType == StorageType.FILE) {
                storageMap.put(storageTable.getTable(), new StorageFile(storageTable.getTable(), storageTable.getCondition(), this, storageTable));
            } else if (storageType == StorageType.MYSQL) {
                storageMap.put(storageTable.getTable(), new StorageMySQL(storageTable.getTable(), storageTable.getCondition(), this, storageTable));
            }

            System.out.println(connection);
            storageTable.createMySqlTable();
        }
    }

    private void connect() {
        List<String> dataList = GSLibrary.getInstance().getPluginLoaderMap().get(instance).getDataList();
        StorageConnect connect = new StorageConnect(instance, dataList);

        if (storageType == StorageType.FILE) {
            YamlDocument yaml = connect.connectFile(sectionFile);
            if (yaml == null) return;

            yamlFile = yaml;
            return;
        } else if (storageType == StorageType.JSON) {
            YamlDocument yaml = connect.connectJson(sectionJson);
            if (yaml == null) return;

            yamlJson = yaml;
            return;
        }

        if (connection != null) return;
        HikariDataSource dataSource = connect.connectMySql(sectionMySql);
        if (dataSource == null) return;

        this.dataSource = dataSource;
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (dataSource == null) return;
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
        MYSQL, FILE, JSON
    }
}
