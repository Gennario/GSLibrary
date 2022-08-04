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
/*/
    private final StorageAPI api;
    private final YamlDocument yaml;

    private final Connection connection;
    private final StorageAPI.StorageType type;
    private final String condition;

    public Storage(StorageAPI storageAPI) {
        this.api = storageAPI;
        this.yaml = storageAPI.getYaml();
        this.connection = storageAPI.getConnection();
        this.type = storageAPI.getType();
        this.condition = storageAPI.getCondition();
    }

    public void insert(final String columns, final Object... parameters) {
        if (type == StorageAPI.StorageType.FILE) {
            String[] split = columns.split(",");
            int i = 1;
            for (Object parameter : parameters) {
                if (parameters[0] == parameter) continue;
                yaml.set(condition + "." + parameters[0] + "." + split[i], parameter);
                i++;
            }

            try { yaml.save(); } catch (IOException e) { e.printStackTrace(); }
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

            api.execute(statement, "INSERT INTO {table}(%s) VALUES (%s);", columns, values.toString());

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
                yaml.set(condition + "." + parameters[0] + "." + split[i], parameter);
                i++;
            }

            try {
                yaml.save();
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

            api.execute(statement, "INSERT INTO {table}(%s) VALUES (%s) ON DUPLICATE KEY UPDATE %s;", columns, values.toString(), update.toString());

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(String conditionValue, String column, Object object) {
        if (!existsCondition(conditionValue)) return;

        if (type == StorageAPI.StorageType.FILE) {
            yaml.set(condition + "." + conditionValue + "." + column, object);
            try {
                yaml.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            Statement statement = connection.createStatement();
            api.execute(statement, "UPDATE {table} SET " + column + "= '%s' WHERE " + condition + "= '%s';", object, conditionValue);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getString(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

        if (type == StorageAPI.StorageType.FILE) {
            return yaml.getString(condition + "." + conditionValue + "." + column);
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
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
            return yaml.getInt(condition + "." + conditionValue + "." + column);
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
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
            return yaml.getDouble(condition + "." + conditionValue + "." + column);
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
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
            return yaml.getFloat(condition + "." + conditionValue + "." + column);
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
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
            return yaml.getBoolean(condition + "." + conditionValue + "." + column);
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
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
            return yaml.get(condition + "." + conditionValue + "." + column);
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(statement, "SELECT " + column + " FROM {table} WHERE " + condition + "='%s'", conditionValue);
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
            Section section = yaml.getSection(condition);
            if (section == null) return conditionList;

            for (Object key : section.getKeys()) {
                conditionList.add((String) key);
            }

            return conditionList;
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + api.getTable());

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
            yaml.remove(condition + "." + conditionValue);
            try { yaml.save(); } catch (IOException e) { e.printStackTrace(); }
            return;
        }

        try {
            Statement statement = connection.createStatement();
            api.execute(statement, "DELETE FROM {table} WHERE " + condition + "='%s';", conditionValue);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existsCondition(String conditionValue) {
        if (type == StorageAPI.StorageType.FILE) {
            return yaml.contains(condition + "." + conditionValue);
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(statement, "SELECT * FROM {table} WHERE " + condition + "='%s'", conditionValue);

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
            Section section = yaml.getSection(condition);
            if (section == null) return list;

            for (Object key : section.getKeys()) {
                if (map.size() == limit) break;
                String name = (String) key;
                map.put(name, yaml.getInt(condition + "." + name + "." + column));
            }

            return sortByValueAscending(map);
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(statement, "SELECT " + condition + " FROM {table} ORDER BY " + column + " ASC LIMIT " + limit);
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
            Section section = yaml.getSection(condition);
            if (section == null) return list;

            for (Object key : section.getKeys()) {
                if (map.size() == limit) break;
                String name = (String) key;
                map.put(name, yaml.getInt(condition + "." + name + "." + column));
            }

            return sortByValueDescending(map);
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = api.query(statement, "SELECT " + condition + " FROM {table} ORDER BY " + column + " DESC LIMIT " + limit);
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
    }/*/
}

