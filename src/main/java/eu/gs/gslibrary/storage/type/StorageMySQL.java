package eu.gs.gslibrary.storage.type;

import eu.gs.gslibrary.storage.StorageAPI;
import eu.gs.gslibrary.storage.StorageTable;
import me.zort.sqllib.SQLDatabaseConnectionImpl;
import me.zort.sqllib.api.data.QueryRowsResult;
import me.zort.sqllib.api.data.Row;

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
    private final SQLDatabaseConnectionImpl databaseConnection;

    public StorageMySQL(String table, String condition, StorageAPI storageAPI, StorageTable storageTable) {
        this.table = table;
        this.condition = condition;
        this.api = storageAPI;
        this.storageTable = storageTable;

        this.connection = storageAPI.getConnection();
        this.databaseConnection = storageAPI.getDatabaseConnection();
    }

    /**
     * It takes a string of columns and a list of parameters, and inserts them into the database
     *
     * @param columns The columns you want to insert into.
     */
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

    /**
     * It inserts a row into the table if it doesn't exist, and updates the row if it does
     *
     * @param columns The columns to insert into.
     */
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

    /**
     * It sets the value of the column to the object.
     *
     * @param conditionValue The value of the condition.
     * @param column The column name in the database
     * @param object The object to be updated.
     */
    @Override
    public void set(String conditionValue, String column, Object object) {
        set(condition, conditionValue, column, object);
    }

    /**
     * It returns the value of the column in the table where the condition is true.
     *
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return The value of the column in the row that matches the condition.
     */
    @Override
    public String getString(String conditionValue, String column) {
        return getString(condition, conditionValue, column);
    }

    /**
     * It returns the integer value of the column.
     *
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return The value of the column in the row that matches the condition.
     */
    @Override
    public int getInt(String conditionValue, String column) {
        return getInt(condition, conditionValue, column);
    }

    /**
     *
     *
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return A double value from the database.
     */
    @Override
    public double getDouble(String conditionValue, String column) {
        return getDouble(condition, conditionValue, column);
    }

    /**
     * It returns the float value of the column.
     *
     * @param conditionValue The value of the condition.
     * @param column The column name in the database
     * @return A float value from the database.
     */
    @Override
    public float getFloat(String conditionValue, String column) {
        return getFloat(condition, conditionValue, column);
    }

    /**
     * It returns a boolean value from the database.
     *
     * @param conditionValue The value of the condition.
     * @param column The column name in the database
     * @return A boolean value.
     */
    @Override
    public boolean getBoolean(String conditionValue, String column) {
        return getBoolean(condition, conditionValue, column);
    }

    /**
     *
     *
     * @param conditionValue The value of the condition.
     * @param column The column name in the database table
     * @return The object is being returned.
     */
    @Override
    public Object getObject(String conditionValue, String column) {
        return getObject(condition, conditionValue, column);
    }

    /**
     * > This function returns a list of strings that are the conditions of the current object
     *
     * @return A list of strings
     */
    @Override
    public List<String> getConditions() {
        return getConditions(condition);
    }

    /**
     * > Removes a condition from the condition list
     *
     * @param conditionValue The value of the condition to be removed.
     */
    @Override
    public void removeCondition(String conditionValue) {
        removeCondition(condition, conditionValue);
    }

    /**
     * > This function returns true if the condition value exists in the condition array
     *
     * @param conditionValue The value of the condition.
     * @return A boolean value.
     */
    @Override
    public boolean existsCondition(String conditionValue) {
        return existsCondition(condition, conditionValue);
    }

    /**
     * Get the top values of a column in ascending order.
     *
     * @param column The column to sort by
     * @param limit The number of results you want to get.
     * @return A list of strings
     */
    @Override
    public List<String> getTopConditionAscending(String column, int limit) {
        return getTopConditionAscending(condition, column, limit);
    }

    /**
     * > This function returns a list of strings that are the top `limit` values of the `column` in the table, sorted in
     * descending order
     *
     * @param column The column to get the top values from
     * @param limit The number of results you want to get.
     * @return A list of strings
     */
    @Override
    public List<String> getTopConditionDescending(String column, int limit) {
        return getTopConditionDescending(condition, column, limit);
    }


    /* With condition */
    /**
     * It updates a column in the database with a new value
     *
     * @param condition The condition to check for.
     * @param conditionValue The value of the condition.
     * @param column The column you want to set
     * @param object The object you want to set.
     */
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

    /**
     * It returns the value of a column in a row where the condition is true
     *
     * @param condition The column to check for the condition value.
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return The value of the column in the row that matches the condition.
     */
    @Override
    public String getString(String condition, String conditionValue, String column) {
        return (String) getObject(condition, conditionValue, column);
    }

    /**
     * "If the condition value exists, return the value of the column."
     *
     * The first thing we do is check if the condition value exists. If it doesn't, we return 0
     *
     * @param condition The column name that you want to check for the condition value.
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return The value of the column in the database.
     */
    @Override
    public int getInt(String condition, String conditionValue, String column) {
        return (int) getObject(condition, conditionValue, column);
    }

    /**
     * It returns the value of the column as a double
     *
     * @param condition The column name that you want to check the value of.
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return A double
     */
    @Override
    public double getDouble(String condition, String conditionValue, String column) {
        return (double) getObject(condition, conditionValue, column);
    }

    /**
     * If the condition value exists, return the float value of the column.
     *
     * @param condition The column name that you want to check the value of.
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return A float
     */
    @Override
    public float getFloat(String condition, String conditionValue, String column) {
        return (float) getObject(condition, conditionValue, column);
    }

    /**
     * "If the condition value exists, return the boolean value of the column."
     *
     * The first thing we do is check if the condition value exists. If it doesn't, we return false
     *
     * @param condition The column name that you want to check the value of.
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return A boolean value
     */
    @Override
    public boolean getBoolean(String condition, String conditionValue, String column) {
        return (boolean) getObject(condition, conditionValue, column);
    }

    /**
     * It returns the value of a column in a row where the condition is true
     *
     * @param condition The column to check for the conditionValue
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return The value of the column specified in the method call.
     */
    @Override
    public Object getObject(String condition, String conditionValue, String column) {
        QueryRowsResult<Row> result = databaseConnection.select(column)
                .from(table)
                .where().isEqual(condition, conditionValue)
                .obtainAll();

        return result.get(0).get(column);
    }

    /**
     * It returns a list of all the values in the column of the table that matches the condition
     *
     * @param condition The column name you want to get the data from.
     * @return A list of strings.
     */
    @Override
    public List<String> getConditions(String condition) {
        QueryRowsResult<Row> result = databaseConnection.select()
                .from(table)
                .obtainAll();

        List<String> conditionList = new ArrayList<>();

        result.get(0).forEach((s, d) -> {
            conditionList.add(s);
        });

        return conditionList;
    }

    /**
     * It removes a row from the table where the value of the column specified by the condition parameter is equal to the
     * value of the conditionValue parameter
     *
     * @param condition The column name
     * @param conditionValue The value of the condition.
     */
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

    /**
     * It checks if a row exists in the database with the given condition
     *
     * @param condition The column name
     * @param conditionValue The value of the condition.
     * @return A boolean value.
     */
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

    /**
     * Get the top X rows of a column, ordered by another column, in ascending order
     *
     * @param condition The column you want to get the data from.
     * @param column The column to sort by
     * @param limit The amount of results you want to get.
     * @return A list of strings
     */
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

    /**
     * Get the top X rows of a column, ordered by another column, in descending order
     *
     * @param condition The column you want to get the data from.
     * @param column The column to get the data from
     * @param limit The amount of results you want to get.
     * @return A list of strings
     */
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
