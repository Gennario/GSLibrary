package eu.gs.gslibrary.storage.type;

import eu.gs.gslibrary.storage.Storage;
import eu.gs.gslibrary.storage.StorageAPI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StorageMySQL implements Storage {

    private final String table, condition;
    private final StorageAPI api;

    private final Connection connection;

    public StorageMySQL(String table, String condition, StorageAPI storageAPI) {
        this.table = table;
        this.condition = condition;
        this.api = storageAPI;

        this.connection = storageAPI.getConnection();
    }

    @Override
    public void insert(final String columns, final Object... parameters) {
        try {
            Statement statement = connection.createStatement();

            int i = 0;
            StringBuilder values = new StringBuilder();
            for (Object parameter : parameters) {
                if (i == 0) {
                    values.append(String.format("'%s'", parameter));
                } else {
                    values.append(String.format(",'%s'", parameter));
                }

                i++;
            }

            api.execute(table, statement, "INSERT INTO {table}(%s) VALUES (%s);", columns, values.toString());

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertUpdate(final String columns, final Object... parameters) {
        try {
            Statement statement = connection.createStatement();

            String[] split = columns.split(",");
            int i = 0;
            StringBuilder values = new StringBuilder();
            StringBuilder update = new StringBuilder();
            for (Object parameter : parameters) {
                if (i == 0) {
                    update.append(String.format(split[i] + "='%s'", parameter));
                    values.append(String.format("'%s'", parameter));
                } else {
                    update.append(String.format("," + split[i] + "='%s'", parameter));
                    values.append(String.format(",'%s'", parameter));
                }

                i++;
            }

            api.execute(table, statement, "INSERT INTO {table}(%s) VALUES (%s) ON DUPLICATE KEY UPDATE %s;", columns, values.toString(), update.toString());

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void set(String conditionValue, String column, Object object) {
        if (!existsCondition(conditionValue)) return;

        try {
            Statement statement = connection.createStatement();
            api.execute(table, statement, "UPDATE {table} SET " + column + "= '%s' WHERE " + condition + "= '%s';", object, conditionValue);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getString(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

        try {
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
    public int getInt(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        try {
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
    public double getDouble(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        try {
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
    public double getFloat(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        try {
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
    public boolean getBoolean(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return false;

        try {
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
    public Object getObject(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

        try {
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
    public List<String> getConditions() {
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
    public void removeCondition(String conditionValue) {
        if (!existsCondition(conditionValue)) return;

        try {
            Statement statement = connection.createStatement();
            api.execute(table, statement, "DELETE FROM {table} WHERE " + condition + "='%s';", conditionValue);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsCondition(String conditionValue) {
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
    public List<String> getTopConditionAscending(String column, int limit) {
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
