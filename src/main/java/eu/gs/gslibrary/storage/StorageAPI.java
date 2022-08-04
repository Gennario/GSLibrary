package eu.gs.gslibrary.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Getter
public class StorageAPI {

    private final HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;

    private final Storage storage;
    private final StorageType type;
    private final YamlDocument yaml;
    private final ConfigurationSection mySqlSection;
    private final String condition;

    private Connection connection;
    private String table;

    public StorageAPI(StorageType type, YamlDocument yaml, ConfigurationSection section, String condition, String... columns) throws SQLException {
        this.type = type;
        this.yaml = yaml;
        this.mySqlSection = section;
        this.condition = condition;
        this.storage = new Storage(this);

        this.connect(columns);
    }

    private void connect(String... columns) throws SQLException {
        if (type != StorageType.MYSQL) return;
        if (isConnected()) return;
        if (mySqlSection == null) return;

        String host = mySqlSection.getString("host");
        String username = mySqlSection.getString("username");
        String database = mySqlSection.getString("database");
        String password = mySqlSection.getString("password");
        int port = mySqlSection.getInt("port");
        boolean autoReconnect = mySqlSection.getBoolean("autoReconnect");
        boolean useSsl = mySqlSection.getBoolean("useSSL");
        this.table = mySqlSection.getString("table");

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
