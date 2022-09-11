package eu.gs.gslibrary.storage.type;

import eu.gs.gslibrary.storage.Storage;
import eu.gs.gslibrary.storage.StorageAPI;
import eu.gs.gslibrary.storage.aids.StorageIsEqual;
import eu.gs.gslibrary.storage.connect.StorageTable;
import me.zort.sqllib.SQLDatabaseConnectionImpl;
import me.zort.sqllib.api.data.Row;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StorageMySQL implements Storage {

    private final String table, condition;
    private final StorageAPI api;
    private final StorageTable storageTable;

    private final Connection connection;
    private final SQLDatabaseConnectionImpl databaseConnection;

    public StorageMySQL(String table, String condition, StorageAPI storageAPI, StorageTable storageTable) {
        this.table = table;
        this.condition = condition;
        this.api = storageAPI;
        this.storageTable = storageTable;

        this.connection = storageAPI.getConnection();
        this.databaseConnection = storageAPI.getDatabaseConnection();
    }

    @Override
    public void insert(final String columns, final Object... parameters) {
        run(() -> {
            try {
                int i = 0;

                Statement statement = connection.createStatement();

                String[] split = columns.split(",");
                StringBuilder column = new StringBuilder();

                StringBuilder values = new StringBuilder();

                for (Object parameter : parameters) {
                    if (!storageTable.getColumns().contains(split[i])) {
                        i++;
                        continue;
                    }

                    if (i == 0) {
                        column = new StringBuilder(split[i]);
                        values.append(String.format("'%s'", parameter));
                    } else {
                        column.append(",").append(split[i]);
                        values.append(String.format(",'%s'", parameter));
                    }

                    i++;
                }

                api.execute(table, statement, "INSERT INTO {table}(%s) VALUES (%s);", column.toString(), values.toString());

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void insertUpdate(final String columns, final Object... parameters) {
        run(() -> {
            try {
                int i = 0;

                Statement statement = connection.createStatement();

                String[] split = columns.split(",");
                StringBuilder column = new StringBuilder();

                StringBuilder values = new StringBuilder();
                StringBuilder update = new StringBuilder();

                for (Object parameter : parameters) {
                    if (!storageTable.getColumns().contains(split[i])) {
                        i++;
                        continue;
                    }

                    if (i == 0) {
                        column = new StringBuilder(split[i]);
                        update.append(String.format(split[i] + "='%s'", parameter));
                        values.append(String.format("'%s'", parameter));
                    } else {
                        column.append(",").append(split[i]);
                        update.append(String.format("," + split[i] + "='%s'", parameter));
                        values.append(String.format(",'%s'", parameter));
                    }

                    i++;
                }

                api.execute(table, statement, "INSERT INTO {table}(%s) VALUES (%s) ON DUPLICATE KEY UPDATE %s;", column.toString(), values.toString(), update.toString());

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void set(String conditionValue, String column, Object parameter) {
        set(condition, conditionValue, column, parameter);
    }

    @Override
    public void set(String condition, String conditionValue, String column, Object parameter) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        String finalColumn = column;
        run(() -> databaseConnection.update(table)
                .set(finalColumn, parameter)
                .where().isEqual(condition, conditionValue)
                .execute());
    }

    @Override
    public void set(String conditionValue, String column, Object parameter, StorageIsEqual storageIsEqual) {
        set(condition, conditionValue, column, parameter);
    }

    @Override
    public void set(String condition, String conditionValue, String column, Object parameter, StorageIsEqual storageIsEqual) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        String isEqualColumn = storageIsEqual.getColumn();
        if (isEqualColumn.contains("-")) {
            String[] isEqualSplitColumn = isEqualColumn.split("-");
            isEqualColumn = isEqualSplitColumn[(isEqualSplitColumn.length - 1)];
        }

        String finalColumn = column;
        String finalIsEqualColumn = isEqualColumn;
        run(() -> databaseConnection.update(table)
                .set(finalColumn, parameter)
                .where().isEqual(condition, conditionValue)
                .and().in(finalIsEqualColumn, storageIsEqual.getObject())
                .execute());
    }

    @Override
    public String getString(String conditionValue, String column) {
        return getString(condition, conditionValue, column);
    }

    @Override
    public String getString(String condition, String conditionValue, String column) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .obtainOne();

        if (!result.isPresent()) return "";
        return (String) result.get().get(column);
    }

    @Override
    public String getString(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getString(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public String getString(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        String isEqualColumn = storageIsEqual.getColumn();
        if (isEqualColumn.contains("-")) {
            String[] isEqualSplitColumn = isEqualColumn.split("-");
            isEqualColumn = isEqualSplitColumn[(isEqualSplitColumn.length - 1)];
        }

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .and().in(isEqualColumn, storageIsEqual.getObject())
                .obtainOne();

        if (!result.isPresent()) return "";
        return (String) result.get().get(column);
    }


    @Override
    public int getInt(String conditionValue, String column) {
        return getInt(condition, conditionValue, column);
    }

    @Override
    public int getInt(String condition, String conditionValue, String column) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .obtainOne();

        if (!result.isPresent()) return 0;
        return (int) result.get().get(column);
    }

    @Override
    public int getInt(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getInt(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public int getInt(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        String isEqualColumn = storageIsEqual.getColumn();
        if (isEqualColumn.contains("-")) {
            String[] isEqualSplitColumn = isEqualColumn.split("-");
            isEqualColumn = isEqualSplitColumn[(isEqualSplitColumn.length - 1)];
        }

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .and().in(isEqualColumn, storageIsEqual.getObject())
                .obtainOne();

        if (!result.isPresent()) return 0;
        return (int) result.get().get(column);
    }


    @Override
    public double getDouble(String conditionValue, String column) {
        return getDouble(condition, conditionValue, column);
    }

    @Override
    public double getDouble(String condition, String conditionValue, String column) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .obtainOne();

        if (!result.isPresent()) return 0;
        return (double) result.get().get(column);
    }

    @Override
    public double getDouble(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getDouble(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public double getDouble(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        String isEqualColumn = storageIsEqual.getColumn();
        if (isEqualColumn.contains("-")) {
            String[] isEqualSplitColumn = isEqualColumn.split("-");
            isEqualColumn = isEqualSplitColumn[(isEqualSplitColumn.length - 1)];
        }

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .and().in(isEqualColumn, storageIsEqual.getObject())
                .obtainOne();

        if (!result.isPresent()) return 0;
        return (double) result.get().get(column);
    }


    @Override
    public float getFloat(String conditionValue, String column) {
        return getFloat(condition, conditionValue, column);
    }

    @Override
    public float getFloat(String condition, String conditionValue, String column) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .obtainOne();

        if (!result.isPresent()) return 0;
        return (float) result.get().get(column);
    }

    @Override
    public float getFloat(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getFloat(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public float getFloat(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        String isEqualColumn = storageIsEqual.getColumn();
        if (isEqualColumn.contains("-")) {
            String[] isEqualSplitColumn = isEqualColumn.split("-");
            isEqualColumn = isEqualSplitColumn[(isEqualSplitColumn.length - 1)];
        }

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .and().in(isEqualColumn, storageIsEqual.getObject())
                .obtainOne();

        if (!result.isPresent()) return 0;
        return (float) result.get().get(column);
    }


    @Override
    public boolean getBoolean(String conditionValue, String column) {
        return getBoolean(condition, conditionValue, column);
    }

    @Override
    public boolean getBoolean(String condition, String conditionValue, String column) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .obtainOne();

        if (!result.isPresent()) return false;
        return (boolean) result.get().get(column);
    }

    @Override
    public boolean getBoolean(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getBoolean(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public boolean getBoolean(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        String isEqualColumn = storageIsEqual.getColumn();
        if (isEqualColumn.contains("-")) {
            String[] isEqualSplitColumn = isEqualColumn.split("-");
            isEqualColumn = isEqualSplitColumn[(isEqualSplitColumn.length - 1)];
        }

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .and().in(isEqualColumn, storageIsEqual.getObject())
                .obtainOne();

        if (!result.isPresent()) return false;
        return (boolean) result.get().get(column);
    }

    @Override
    public Object getObject(String conditionValue, String column) {
        return getObject(condition, conditionValue, column);
    }

    @Override
    public Object getObject(String condition, String conditionValue, String column) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .obtainOne();

        if (!result.isPresent()) return null;
        return result.get().get(column);
    }

    @Override
    public Object getObject(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getObject(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public Object getObject(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        String[] split = column.split("-");
        column = column.contains("-") ? split[(split.length - 1)] : column;

        String isEqualColumn = storageIsEqual.getColumn();
        if (isEqualColumn.contains("-")) {
            String[] isEqualSplitColumn = isEqualColumn.split("-");
            isEqualColumn = isEqualSplitColumn[(isEqualSplitColumn.length - 1)];
        }

        Optional<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .and().in(isEqualColumn, storageIsEqual.getObject())
                .obtainOne();

        if (!result.isPresent()) return null;
        return result.get().get(column);
    }


    @Override
    public List<String> getConditions() {
        return getColumns(condition);
    }

    @Override
    public List<String> getColumns(String column) {
        List<String> conditionList = new ArrayList<>();

        List<Row> rows = databaseConnection.select(column)
                .from(table).obtainAll();
        for(Row row : rows) {
            String conditionValue = (String) row.get(column);
            conditionList.add(conditionValue);
        }

        return conditionList;
    }


    @Override
    public void removeCondition(String conditionValue) {
        run(() -> removeColumn(condition, conditionValue));
    }

    @Override
    public void removeColumn(String column, String columnValue) {
        run(() -> databaseConnection.delete()
                .from(table)
                .where().isEqual(column, columnValue)
                .execute());
    }


    @Override
    public boolean existsCondition(String conditionValue) {
        return existsColumn(condition, conditionValue);
    }

    @Override
    public boolean existsColumn(String column, String columnValue) {
        return databaseConnection.select()
                .from(table)
                .where().isEqual(column, columnValue)
                .obtainOne().isPresent();
    }


    @Override
    public List<String> getTopConditionAscending(String column, int limit) {
        return getTopConditionAscending(condition, column, limit);
    }

    @Override
    public List<String> getTopConditionAscending(String condition, String column, int limit) {
        List<String> list = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(table, statement, "SELECT " + condition + " FROM {table} ORDER BY " + column + " ASC LIMIT " + limit);
            if (resultSet == null) return list;
            while (resultSet.next()) {
                list.add(resultSet.getString(condition));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    @Override
    public List<String> getTopConditionDescending(String column, int limit) {
        return getTopConditionDescending(condition, column, limit);
    }

    @Override
    public List<String> getTopConditionDescending(String condition, String column, int limit) {
        List<String> list = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(table, statement, "SELECT " + condition + " FROM {table} ORDER BY " + column + " DESC LIMIT " + limit);
            if (resultSet == null) return list;
            while (resultSet.next()) {
                list.add(resultSet.getString(condition));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


}
