package eu.gs.gslibrary.storage.type;

import eu.gs.gslibrary.storage.StorageAPI;
import eu.gs.gslibrary.storage.StorageTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StorageMySQL implements Storage {

    private final String table, condition;
    private final StorageAPI api;
    private final StorageTable storageTable;

    private final Connection connection;

    public StorageMySQL(String table, String condition, StorageAPI storageAPI, StorageTable storageTable) {
        this.table = table;
        this.condition = condition;
        this.api = storageAPI;
        this.storageTable = storageTable;

        this.connection = storageAPI.getConnection();
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
    public void set(String conditionValue, String column, Object object) {
        set(condition, conditionValue, column, object);
    }

    @Override
    public String getString(String conditionValue, String column) {
        return getString(condition, conditionValue, column);
    }

    @Override
    public int getInt(String conditionValue, String column) {
        return getInt(condition, conditionValue, column);
    }

    @Override
    public double getDouble(String conditionValue, String column) {
        return getDouble(condition, conditionValue, column);
    }

    @Override
    public float getFloat(String conditionValue, String column) {
        return getFloat(condition, conditionValue, column);
    }

    @Override
    public boolean getBoolean(String conditionValue, String column) {
        return getBoolean(condition, conditionValue, column);
    }

    @Override
    public Object getObject(String conditionValue, String column) {
        return getObject(condition, conditionValue, column);
    }

    @Override
    public List<String> getConditions() {
        return getConditions(condition);
    }

    @Override
    public void removeCondition(String conditionValue) {
        removeCondition(condition, conditionValue);
    }

    @Override
    public boolean existsCondition(String conditionValue) {
        return existsCondition(condition, conditionValue);
    }

    @Override
    public List<String> getTopConditionAscending(String column, int limit) {
        return getTopConditionAscending(condition, column, limit);
    }

    @Override
    public List<String> getTopConditionDescending(String column, int limit) {
        return getTopConditionDescending(condition, column, limit);
    }


    /* With condition */
    @Override
    public void set(String condition, String conditionValue, String column, Object object) {
        run(() -> {
            if (!existsCondition(conditionValue)) return;

            try {
                Statement statement = connection.createStatement();
                api.execute(table, statement, "UPDATE {table} SET " + column + "= '%s' WHERE " + condition + "= '%s';", object, conditionValue);

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String getString(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

        try {
            column = column.contains("-") ? column.split("-")[1] : column;
            Statement statement = connection.createStatement();

            ResultSet resultSet = api.query(table, statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
            if (resultSet.next()) {
                return resultSet.getString(column);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getInt(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        try {
            column = column.contains("-") ? column.split("-")[1] : column;
            Statement statement = connection.createStatement();

            ResultSet resultSet = api.query(table, statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
            if (resultSet.next()) {
                return resultSet.getInt(column);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public double getDouble(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        try {
            column = column.contains("-") ? column.split("-")[1] : column;
            Statement statement = connection.createStatement();

            ResultSet resultSet = api.query(table, statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
            if (resultSet.next()) {
                return resultSet.getDouble(column);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public float getFloat(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        try {
            column = column.contains("-") ? column.split("-")[1] : column;
            Statement statement = connection.createStatement();

            ResultSet resultSet = api.query(table, statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
            if (resultSet.next()) {
                return resultSet.getFloat(column);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean getBoolean(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return false;

        try {
            column = column.contains("-") ? column.split("-")[1] : column;
            Statement statement = connection.createStatement();

            ResultSet resultSet = api.query(table, statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
            if (resultSet.next()) {
                return resultSet.getBoolean(column);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Object getObject(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

        try {
            column = column.contains("-") ? column.split("-")[1] : column;
            Statement statement = connection.createStatement();

            ResultSet resultSet = api.query(table, statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
            if (resultSet.next()) {
                return resultSet.getObject(column);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<String> getConditions(String condition) {
        List<String> conditionList = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);

            while (resultSet.next()) {
                String playerName = resultSet.getString(condition);
                conditionList.add(playerName);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conditionList;
    }

    @Override
    public void removeCondition(String condition, String conditionValue) {
        run(() -> {
            if (!existsCondition(conditionValue)) return;

            try {
                Statement statement = connection.createStatement();
                api.execute(table, statement, "DELETE FROM {table} WHERE " + condition + "='%s';", conditionValue);

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean existsCondition(String condition, String conditionValue) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(table, statement, "SELECT * FROM {table} WHERE " + condition + "='%s'", conditionValue);

            if (resultSet.next()) {
                resultSet.close();
                statement.close();
                return true;
            }

            resultSet.close();
            statement.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

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
