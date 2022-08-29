package eu.gs.gslibrary.storage;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Storage {

    private final String table, condition;
    private final StorageAPI api;
    
    private final YamlDocument yamlDocument;

    private final StorageAPI.StorageType type;

    private final Connection connection;

    public Storage(String table, String condition, StorageAPI storageAPI) {
        this.table = table;
        this.condition = condition;
        this.api = storageAPI;

        this.yamlDocument = storageAPI.getYamlDocument();
        this.connection = storageAPI.getConnection();
        this.type = storageAPI.getStorageType();
    }

    public void insert(final String columns, final Object... parameters) {
        if (type == StorageAPI.StorageType.FILE) {
            String[] split = columns.split(",");
            int i = 1;
            for (Object parameter : parameters) {
                if (parameters[0] == parameter) continue;
                yamlDocument.set(table + "." + parameters[0] + "." + split[i], parameter);
                i++;
            }

            try { yamlDocument.save(); } catch (IOException e) { e.printStackTrace(); }
            return;
        }

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

    public void insertUpdate(final String columns, final Object... parameters) {
        if (type == StorageAPI.StorageType.FILE) {
            String[] split = columns.split(",");
            int i = 1;
            for (Object parameter : parameters) {
                if (parameters[0] == parameter) continue;
                yamlDocument.set(table + "." + parameters[0] + "." + split[i], parameter);
                i++;
            }

            try {
                yamlDocument.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

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

    public void set(String conditionValue, String column, Object object) {
        if (!existsCondition(conditionValue)) return;

        if (type == StorageAPI.StorageType.FILE) {
            yamlDocument.set(table + "." + conditionValue + "." + column, object);
            try {
                yamlDocument.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            Statement statement = connection.createStatement();
            api.execute(table, statement, "UPDATE {table} SET " + column + "= '%s' WHERE " + condition + "= '%s';", object, conditionValue);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getString(String table, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

        if (type == StorageAPI.StorageType.FILE) {
            return yamlDocument.getString(table + "." + conditionValue + "." + column);
        }

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

    public int getInt(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        if (type == StorageAPI.StorageType.FILE) {
            return yamlDocument.getInt(table + "." + conditionValue + "." + column);
        }

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

    public double getDouble(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        if (type == StorageAPI.StorageType.FILE) {
            return yamlDocument.getDouble(table + "." + conditionValue + "." + column);
        }

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

    public double getFloat(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        if (type == StorageAPI.StorageType.FILE) {
            return yamlDocument.getFloat(table + "." + conditionValue + "." + column);
        }

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

    public boolean getBoolean(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return false;

        if (type == StorageAPI.StorageType.FILE) {
            return yamlDocument.getBoolean(table + "." + conditionValue + "." + column);
        }

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

    public Object getObject(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

        if (type == StorageAPI.StorageType.FILE) {
            return yamlDocument.get(table + "." + conditionValue + "." + column);
        }

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


    public List<String> getConditions() {
        List<String> conditionList = new ArrayList<>();

        if (type == StorageAPI.StorageType.FILE) {
            Section section = yamlDocument.getSection(table);
            if (section == null) {
                return conditionList;
            }

            conditionList.addAll(section.getRoutesAsStrings(false));

            return conditionList;
        }

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

    public void removeCondition(String conditionValue) {
        if (!existsCondition(conditionValue)) return;

        if (type == StorageAPI.StorageType.FILE) {
            yamlDocument.remove(table + "." + conditionValue);
            try { yamlDocument.save(); } catch (IOException e) { e.printStackTrace(); }
            return;
        }

        try {
            Statement statement = connection.createStatement();
            api.execute(table, statement, "DELETE FROM {table} WHERE " + condition + "='%s';", conditionValue);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existsCondition(String conditionValue) {
        if (type == StorageAPI.StorageType.FILE) {
            return yamlDocument.contains(table + "." + conditionValue);
        }

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

    public List<String> getTopConditionAscending(String column, int limit) {
        List<String> list = new ArrayList<>();

        if (type == StorageAPI.StorageType.FILE) {
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

    public List<String> getTopConditionDescending(String column, int limit) {
        List<String> list = new ArrayList<>();
        if (type == StorageAPI.StorageType.FILE) {
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

