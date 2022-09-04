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

    @Override
    public void insertUpdate(final String columns, final Object... parameters) {
        insert(columns, parameters);
    }

    /* Empty condition */
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

    @Override
    public boolean existsCondition(String conditionValue) {
        return yamlDocument.contains(table + "." + conditionValue);
    }

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
    @Override
    public void set(String condition, String conditionValue, String column, Object object) {
        set(conditionValue, column, object);
    }

    @Override
    public String getString(String condition, String conditionValue, String column) {
        return getString(conditionValue, column);
    }

    @Override
    public int getInt(String condition, String conditionValue, String column) {
        return getInt(conditionValue, column);
    }

    @Override
    public double getDouble(String condition, String conditionValue, String column) {
        return getDouble(conditionValue, column);
    }

    @Override
    public float getFloat(String condition, String conditionValue, String column) {
        return getFloat(conditionValue, column);
    }

    @Override
    public boolean getBoolean(String condition, String conditionValue, String column) {
        return getBoolean(conditionValue, column);
    }

    @Override
    public Object getObject(String condition, String conditionValue, String column) {
        return getObject(conditionValue, column);
    }


    @Override
    public List<String> getConditions(String condition) {
        return getConditions();
    }

    @Override
    public void removeCondition(String condition, String conditionValue) {
        removeCondition(conditionValue);
    }

    @Override
    public boolean existsCondition(String condition, String conditionValue) {
        return existsCondition(conditionValue);
    }

    @Override
    public List<String> getTopConditionAscending(String condition, String column, int limit) {
        return getTopConditionAscending(column, limit);


    }

    @Override
    public List<String> getTopConditionDescending(String condition, String column, int limit) {
        return getTopConditionDescending(column, limit);

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
