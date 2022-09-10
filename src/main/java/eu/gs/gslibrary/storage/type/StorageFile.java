package eu.gs.gslibrary.storage.type;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.storage.StorageAPI;
import eu.gs.gslibrary.storage.StorageTable;

import java.io.IOException;
import java.util.*;

public class StorageFile implements Storage {

    private final String table, condition;
    private final StorageTable storageTable;
    private final StorageAPI storageAPI;

    private final YamlDocument yamlDocument;

    public StorageFile(String table, String condition, StorageAPI storageAPI, StorageTable storageTable) {
        this.table = table;
        this.condition = condition;
        this.storageTable = storageTable;
        this.storageAPI = storageAPI;

        this.yamlDocument = storageAPI.getYamlFile();
    }

    /**
     * It takes a string of column names, and an array of parameters, and inserts them into the database
     *
     * @param columns The columns you want to insert into.
     */
    @Override
    public void insert(final String columns, final Object... parameters) {
        run(() -> {
            String[] split = columns.split(",");
            int i = 1;
            int a = -1;
            int b = 0;

            for (Object parameter : parameters) {
                if (parameters[0] == parameter) continue;

                if (a != -1 || parameter.equals("-")) {
                    b++;

                    if (b == 1) a = i;

                    try {
                        yamlDocument.set(table + "." + parameters[0] + "." + split[a] + "." + split[i + 1], parameters[i + 1]);
                        i++;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        i--;
                    }

                    continue;
                }

                yamlDocument.set(table + "." + parameters[0] + "." + split[i], parameter);
                i++;
            }

            try {
                yamlDocument.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Inserts a new row into the database with the given columns and parameters.
     *
     * @param columns The columns to insert into.
     */
    @Override
    public void insertUpdate(final String columns, final Object... parameters) {
        insert(columns, parameters);
    }

    /* Empty condition */

    /**
     * It sets the value of a column in a row of a table
     *
     * @param conditionValue The value of the condition.
     * @param column The column you want to set the value to.
     * @param object The object you want to set.
     */
    @Override
    public void set(String conditionValue, String column, Object object) {
        run(() -> {
            if (!existsCondition(conditionValue)) return;

            yamlDocument.set(table + "." + conditionValue + "." + column, object);
            try {
                yamlDocument.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * If the condition exists, return the value of the column
     *
     * @param conditionValue The value of the condition.
     * @param column The column name
     * @return A string
     */
    @Override
    public String getString(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

        if (column.contains("-")) {
            StringBuilder string = new StringBuilder();
            for (String s : column.split("-")) {
                string.append(".").append(s);
            }

            return yamlDocument.getString(table + "." + conditionValue + string);
        }

        return yamlDocument.getString(table + "." + conditionValue + "." + column);
    }

    /**
     * If the condition exists, return the value of the column
     *
     * @param conditionValue The value of the condition.
     * @param column The column name
     * @return The value of the column in the table.
     */
    @Override
    public int getInt(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        if (column.contains("-")) {
            StringBuilder string = new StringBuilder();
            for (String s : column.split("-")) {
                string.append(".").append(s);
            }

            return yamlDocument.getInt(table + "." + conditionValue + string);
        }

        return yamlDocument.getInt(table + "." + conditionValue + "." + column);
    }

    /**
     * If the condition exists, return the double value of the column
     *
     * @param conditionValue The value of the condition.
     * @param column The column name
     * @return A double value
     */
    @Override
    public double getDouble(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        if (column.contains("-")) {
            StringBuilder string = new StringBuilder();
            for (String s : column.split("-")) {
                string.append(".").append(s);
            }

            return yamlDocument.getDouble(table + "." + conditionValue + string);
        }

        return yamlDocument.getDouble(table + "." + conditionValue + "." + column);
    }

    /**
     * If the condition exists, return the float value of the column
     *
     * @param conditionValue The value of the condition.
     * @param column The column name
     * @return A float value
     */
    @Override
    public float getFloat(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        if (column.contains("-")) {
            StringBuilder string = new StringBuilder();
            for (String s : column.split("-")) {
                string.append(".").append(s);
            }

            return yamlDocument.getFloat(table + "." + conditionValue + string);
        }

        return yamlDocument.getFloat(table + "." + conditionValue + "." + column);
    }

    /**
     * If the condition exists, return the boolean value of the column
     *
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return A boolean value.
     */
    @Override
    public boolean getBoolean(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return false;

        if (column.contains("-")) {
            StringBuilder string = new StringBuilder();
            for (String s : column.split("-")) {
                string.append(".").append(s);
            }

            return yamlDocument.getBoolean(table + "." + conditionValue + string);
        }

        return yamlDocument.getBoolean(table + "." + conditionValue + "." + column);
    }

    /**
     * It gets the object from the YAML file
     *
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return The value of the column in the row that matches the condition.
     */
    @Override
    public Object getObject(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

        if (column.contains("-")) {
            StringBuilder string = new StringBuilder();
            for (String s : column.split("-")) {
                string.append(".").append(s);
            }

            return yamlDocument.get(table + "." + conditionValue + string);
        }

        return yamlDocument.get(table + "." + conditionValue + "." + column);
    }


    /**
     * > This function returns a list of all the conditions in the table
     *
     * @return A list of strings.
     */
    @Override
    public List<String> getConditions() {
        List<String> conditionList = new ArrayList<>();

        Section section = yamlDocument.getSection(table);
        if (section == null) {
            return conditionList;
        }

        conditionList.addAll(section.getRoutesAsStrings(false));

        return conditionList;
    }

    /**
     * It removes a condition from the table
     *
     * @param conditionValue The value of the condition.
     */
    @Override
    public void removeCondition(String conditionValue) {
        run(() -> {
            if (!existsCondition(conditionValue)) return;

            yamlDocument.remove(table + "." + conditionValue);
            try {
                yamlDocument.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This function checks if a condition exists in the YAML file.
     *
     * @param conditionValue The value of the condition.
     * @return A boolean value.
     */
    @Override
    public boolean existsCondition(String conditionValue) {
        return yamlDocument.contains(table + "." + conditionValue);
    }

    /**
     * It gets the top values of a column in a table, sorted by ascending order
     *
     * @param column The column you want to get the top values from.
     * @param limit The amount of results you want to get
     * @return A list of strings
     */
    @Override
    public List<String> getTopConditionAscending(String column, int limit) {
        List<String> list = new ArrayList<>();

        HashMap<String, Integer> map = new HashMap<>();
        Section section = yamlDocument.getSection(condition);
        if (section == null) return list;

        for (Object key : section.getKeys()) {
            if (map.size() == limit) break;
            String name = (String) key;
            map.put(name, yamlDocument.getInt(table + "." + name + "." + column));
        }

        return sortByValueAscending(map);


    }

    /**
     * It gets the top X amount of conditions from the table, sorted by the column in descending order
     *
     * @param column The column you want to get the top values from.
     * @param limit The number of results you want to get.
     * @return A list of strings
     */
    @Override
    public List<String> getTopConditionDescending(String column, int limit) {
        List<String> list = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        Section section = yamlDocument.getSection(condition);
        if (section == null) return list;

        for (Object key : section.getKeys()) {
            if (map.size() == limit) break;
            String name = (String) key;
            map.put(name, yamlDocument.getInt(table + "." + name + "." + column));
        }

        return sortByValueDescending(map);

    }

    /* With condition */

    /**
     * Sets the value of the column to the object if the condition is true.
     *
     * @param condition The condition to be used in the WHERE clause.
     * @param conditionValue The value of the condition.
     * @param column The column to be updated
     * @param object The object to be set.
     */
    @Override
    public void set(String condition, String conditionValue, String column, Object object) {
        set(conditionValue, column, object);
    }

    /**
     * Returns the value of the specified column in the specified row.
     *
     * @param condition The column name to check for the conditionValue
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return The value of the column in the row that matches the conditionValue.
     */
    @Override
    public String getString(String condition, String conditionValue, String column) {
        return getString(conditionValue, column);
    }

    /**
     * Returns the integer value of the column in the row where the condition column is equal to the condition value.
     *
     * @param condition The column name to check for the conditionValue
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from.
     * @return The value of the column in the row that matches the conditionValue.
     */
    @Override
    public int getInt(String condition, String conditionValue, String column) {
        return getInt(conditionValue, column);
    }

    /**
     * Returns the double value of the column in the row where the condition column is equal to the condition value.
     *
     * @param condition The column name to check for the conditionValue
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from
     * @return A double value.
     */
    @Override
    public double getDouble(String condition, String conditionValue, String column) {
        return getDouble(conditionValue, column);
    }

    /**
     * Returns the float value of the column in the row where the condition column is equal to the condition value.
     *
     * @param condition The column name to check for the conditionValue
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from
     * @return A float value.
     */
    @Override
    public float getFloat(String condition, String conditionValue, String column) {
        return getFloat(conditionValue, column);
    }

    /**
     * It returns a boolean value.
     *
     * @param condition The column name to check the conditionValue against.
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from
     * @return A boolean value.
     */
    @Override
    public boolean getBoolean(String condition, String conditionValue, String column) {
        return getBoolean(conditionValue, column);
    }

    /**
     * Get the object from the database that matches the condition and conditionValue, and return the column value.
     *
     * @param condition The column name to be used in the WHERE clause.
     * @param conditionValue The value of the condition.
     * @param column The column you want to get the value from
     * @return The object is being returned.
     */
    @Override
    public Object getObject(String condition, String conditionValue, String column) {
        return getObject(conditionValue, column);
    }


    /**
     * This function returns a list of strings that are the conditions of the given condition.
     *
     * @param condition The condition that is being checked.
     * @return A list of strings.
     */
    @Override
    public List<String> getConditions(String condition) {
        return getConditions();
    }

    /**
     * Removes a condition from the list of conditions.
     *
     * @param condition The condition to be removed.
     * @param conditionValue The value of the condition.
     */
    @Override
    public void removeCondition(String condition, String conditionValue) {
        removeCondition(conditionValue);
    }

    /**
     * > This function returns true if the conditionValue is not null and not empty
     *
     * @param condition The name of the condition.
     * @param conditionValue The value of the condition.
     * @return A boolean value.
     */
    @Override
    public boolean existsCondition(String condition, String conditionValue) {
        return existsCondition(conditionValue);
    }

    /**
     * Returns a list of the top values in a column, sorted in ascending order.
     *
     * @param condition The condition to be used in the SQL query.
     * @param column the column to sort by
     * @param limit The number of results you want to get back.
     * @return A list of strings.
     */
    @Override
    public List<String> getTopConditionAscending(String condition, String column, int limit) {
        return getTopConditionAscending(column, limit);
    }

    /**
     * It returns the top condition descending.
     *
     * @param condition The condition to be used in the SQL query.
     * @param column the column to sort by
     * @param limit the number of results you want to get
     * @return A list of strings.
     */
    @Override
    public List<String> getTopConditionDescending(String condition, String column, int limit) {
        return getTopConditionDescending(column, limit);
    }

    /**
     * Sort the HashMap by value in ascending order and return the sorted HashMap as a list
     *
     * @param hm The HashMap that you want to sort.
     * @return A list of strings sorted by value in ascending order.
     */
    private List<String> sortByValueAscending(final HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        final List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(final Map.Entry<String, Integer> o1, final Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // Put data to temp list
        final List<String> temp = new ArrayList<>();
        for (final Map.Entry<String, Integer> aa : list) {
            temp.add(aa.getKey());
        }
        return temp;
    }

    /**
     * Sort the HashMap by value in descending order and return the keys in a list
     *
     * @param hm The HashMap that you want to sort.
     * @return A list of strings sorted by value in descending order.
     */
    private List<String> sortByValueDescending(final HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        final List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(final Map.Entry<String, Integer> o1, final Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // Put data to temp list
        final List<String> temp = new ArrayList<>();
        for (final Map.Entry<String, Integer> aa : list) {
            temp.add(aa.getKey());
        }
        return temp;
    }
}
