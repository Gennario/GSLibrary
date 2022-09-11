package eu.gs.gslibrary.storage.connect;

import eu.gs.gslibrary.storage.StorageAPI;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StorageTable {

    private final String table;
    private String condition;
    private StorageAPI storageAPI;
    private StringBuilder stringBuilder;

    private final List<String> columns = new ArrayList<>();

    public StorageTable(String table, String conditionUniquePrimaryKey) {
        this.table = table;
        this.condition = conditionUniquePrimaryKey;
        this.stringBuilder = null;

        if (conditionUniquePrimaryKey.isEmpty()) return;
        this.addColumnUnique(conditionUniquePrimaryKey);
    }

    public StorageTable loadColumns(String condition, String... columns) {
        this.addColumnUnique(condition);
        for (String column : columns) {
            String[] split = column.split("//");
            switch (split[1].toLowerCase()) {
                case "condition":
                    this.addColumnCondition(split[0]);
                case "string":
                case "text":
                    this.addColumn(StorageSQLType.STRING, split[0]);
                case "int":
                case "integer":
                    this.addColumn(StorageSQLType.INTEGER, split[0]);
                case "boolean":
                    this.addColumn(StorageSQLType.BOOLEAN, split[0]);
                case "float":
                    this.addColumn(StorageSQLType.FLOAT, split[0]);
                case "double":
                    this.addColumn(StorageSQLType.DOUBLE, split[0]);
            }
        }
        return this;
    }

    public StorageTable addColumnUnique(String column) {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder("(" + column + " varchar(32) UNIQUE PRIMARY KEY");
        } else {
            stringBuilder.append(", ").append(column).append(" varchar(32) UNIQUE PRIMARY KEY");
        }

        columns.add(column);
        return this;
    }

    public StorageTable addColumnCondition(String column) {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder("(" + column + " varchar(32)");
        } else {
            stringBuilder.append(", ").append(column).append(" varchar(32)");
        }

        this.condition = column;
        columns.add(column);
        return this;
    }

    public StorageTable addColumns(StorageSQLType sqlType, String... columns) {
        String type = sqlType.getType();

        for (String column : columns) {
            if (stringBuilder == null) {
                stringBuilder = new StringBuilder("(" + column + " " + type);
            } else {
                stringBuilder.append(", ").append(column).append(" ").append(type);
            }

            this.columns.add(column);
        }

        return this;
    }

    public StorageTable addColumn(StorageSQLType sqlType, String column) {
        String type = sqlType.getType();

        if (stringBuilder == null) {
            stringBuilder = new StringBuilder("(" + column + " " + type);
        } else {
            stringBuilder.append(", ").append(column).append(" ").append(type);
        }

        this.columns.add(column);
        return this;
    }

    public void createMySqlTable() {
        if (storageAPI.getStorageType() != StorageAPI.StorageType.MYSQL) return;
        stringBuilder.append(")");

        try {
            Statement statement = storageAPI.getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + table + " " + stringBuilder + ";");

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public enum StorageSQLType {
        STRING("text"),
        INTEGER("int"),
        FLOAT("float"),
        DOUBLE("double"),
        BOOLEAN("boolean");

        private final String type;

        StorageSQLType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
