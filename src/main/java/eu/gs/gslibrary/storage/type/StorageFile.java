package eu.gs.gslibrary.storage.type;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.storage.Storage;
import eu.gs.gslibrary.storage.StorageAPI;

import java.io.IOException;
import java.util.*;

public class StorageFile implements Storage {

    private final String table, condition;

    private final YamlDocument yamlDocument;

    public StorageFile(String table, String condition, StorageAPI storageAPI) {
        this.table = table;
        this.condition = condition;

        this.yamlDocument = storageAPI.getYamlDocument();
    }

    @Override
    public void insert(final String columns, final Object... parameters) {
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
    }

    @Override
    public void insertUpdate(final String columns, final Object... parameters) {
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
    }

    @Override
    public void set(String conditionValue, String column, Object object) {
        if (!existsCondition(conditionValue)) return;

        yamlDocument.set(table + "." + conditionValue + "." + column, object);
        try {
            yamlDocument.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getString(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

        return yamlDocument.getString(table + "." + conditionValue + "." + column);
    }

    @Override
    public int getInt(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        return yamlDocument.getInt(table + "." + conditionValue + "." + column);
    }

    @Override
    public double getDouble(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        return yamlDocument.getDouble(table + "." + conditionValue + "." + column);
    }

    @Override
    public double getFloat(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        return yamlDocument.getFloat(table + "." + conditionValue + "." + column);
    }

    @Override
    public boolean getBoolean(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return false;

        return yamlDocument.getBoolean(table + "." + conditionValue + "." + column);
    }

    @Override
    public Object getObject(String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

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
        if (!existsCondition(conditionValue)) return;

        yamlDocument.remove(table + "." + conditionValue);
        try {
            yamlDocument.save();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
