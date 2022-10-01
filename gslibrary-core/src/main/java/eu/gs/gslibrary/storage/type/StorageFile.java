package eu.gs.gslibrary.storage.type;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.utils.HashMapUtils;
import eu.gs.gslibrary.storage.Storage;
import eu.gs.gslibrary.storage.StorageAPI;
import eu.gs.gslibrary.storage.aids.StorageIsEqual;
import eu.gs.gslibrary.storage.connect.StorageTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public void set(String conditionValue, String column, Object parameter) {
        set(condition, conditionValue, column, parameter);
    }

    @Override
    public void set(String condition, String conditionValue, String column, Object parameter) {
        run(() -> {
            if (!existsCondition(conditionValue)) return;

            String path = table + "." + conditionValue + "." + column.replace("-", ".");
            yamlDocument.set(path, parameter);
            try {
                yamlDocument.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void set(String conditionValue, String column, Object parameter, StorageIsEqual storageIsEqual) {
        set(condition, conditionValue, column, parameter, storageIsEqual);
    }

    @Override
    public void set(String condition, String conditionValue, String column, Object parameter, StorageIsEqual storageIsEqual) {
        run(() -> {
            if (!existsCondition(conditionValue)) return;

            String s = table + "." + conditionValue + "." + storageIsEqual.getColumn().replace("-", ".");
            if (!yamlDocument.contains(s)) return;

            Object object = storageIsEqual.getObject();
            if (object != null && object != yamlDocument.get(s)) return;

            String path = table + "." + conditionValue + "." + column.replace("-", ".");
            yamlDocument.set(path, parameter);
            try {
                yamlDocument.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String getString(String conditionValue, String column) {
        return getString(condition, conditionValue, column);
    }

    @Override
    public String getString(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return "";

        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.isString(path)) return "";

        return yamlDocument.getString(path);
    }

    @Override
    public String getString(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getString(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public String getString(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        if (!existsCondition(conditionValue)) return "";

        String s = table + "." + conditionValue + "." + storageIsEqual.getColumn().replace("-", ".");
        if (!yamlDocument.contains(s)) return "";

        Object object = storageIsEqual.getObject();
        if (object != null && object != yamlDocument.get(s)) return "";

        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.isString(path)) return "";

        return yamlDocument.getString(path);
    }

    @Override
    public int getInt(String conditionValue, String column) {
        return getInt(condition, conditionValue, column);
    }

    @Override
    public int getInt(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.isInt(path)) return 0;

        return yamlDocument.getInt(path);
    }

    @Override
    public int getInt(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getInt(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public int getInt(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        if (!existsCondition(conditionValue)) return 0;

        String s = table + "." + conditionValue + "." + storageIsEqual.getColumn().replace("-", ".");
        if (!yamlDocument.contains(s)) return 0;


        Object object = storageIsEqual.getObject();
        if (object != null && object != yamlDocument.get(s)) return 0;


        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.isInt(path)) return 0;

        return yamlDocument.getInt(path);
    }


    @Override
    public double getDouble(String conditionValue, String column) {
        return getDouble(condition, conditionValue, column);
    }

    @Override
    public double getDouble(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.isDouble(path)) return 0;

        return yamlDocument.getDouble(path);
    }

    @Override
    public double getDouble(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getDouble(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public double getDouble(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        if (!existsCondition(conditionValue)) return 0;

        String s = table + "." + conditionValue + "." + storageIsEqual.getColumn().replace("-", ".");
        if (!yamlDocument.contains(s)) return 0;

        Object object = storageIsEqual.getObject();
        if (object != null && object != yamlDocument.get(s)) return 0;

        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.isDouble(path)) return 0;

        return yamlDocument.getDouble(path);
    }


    @Override
    public float getFloat(String conditionValue, String column) {
        return getFloat(condition, conditionValue, column);
    }

    @Override
    public float getFloat(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return 0;

        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.isFloat(path)) return 0;

        return yamlDocument.getFloat(path);
    }

    @Override
    public float getFloat(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getFloat(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public float getFloat(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        if (!existsCondition(conditionValue)) return 0;

        String s = table + "." + conditionValue + "." + storageIsEqual.getColumn().replace("-", ".");
        if (!yamlDocument.contains(s)) return 0;

        Object object = storageIsEqual.getObject();
        if (object != null && object != yamlDocument.get(s)) return 0;

        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.isFloat(path)) return 0;

        return yamlDocument.getFloat(path);
    }


    @Override
    public boolean getBoolean(String conditionValue, String column) {
        return getBoolean(condition, conditionValue, column);
    }

    @Override
    public boolean getBoolean(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return false;

        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.isBoolean(path)) return false;

        return yamlDocument.getBoolean(path);
    }

    @Override
    public boolean getBoolean(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getBoolean(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public boolean getBoolean(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        if (!existsCondition(conditionValue)) return false;

        String s = table + "." + conditionValue + "." + storageIsEqual.getColumn().replace("-", ".");
        if (!yamlDocument.contains(s)) return false;

        Object object = storageIsEqual.getObject();
        if (object != null && object != yamlDocument.get(s)) return false;

        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.isBoolean(path)) return false;

        return yamlDocument.getBoolean(path);
    }


    @Override
    public Object getObject(String conditionValue, String column) {
        return getObject(condition, conditionValue, column);
    }

    @Override
    public Object getObject(String condition, String conditionValue, String column) {
        if (!existsCondition(conditionValue)) return null;

        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.contains(path)) return null;

        return yamlDocument.get(path);
    }

    @Override
    public Object getObject(String conditionValue, String column, StorageIsEqual storageIsEqual) {
        return getObject(condition, conditionValue, column, storageIsEqual);
    }

    @Override
    public Object getObject(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual) {
        if (!existsCondition(conditionValue)) return null;

        String s = table + "." + conditionValue + "." + storageIsEqual.getColumn().replace("-", ".");
        if (!yamlDocument.contains(s)) return null;

        Object object = storageIsEqual.getObject();
        if (object != null && object != yamlDocument.get(s)) return null;

        String path = table + "." + conditionValue + "." + column.replace("-", ".");
        if (!yamlDocument.contains(path)) return null;

        return yamlDocument.get(path);
    }


    @Override
    public List<String> getConditions() {
        return getColumns(condition);
    }

    @Override
    public List<String> getColumns(String condition) {
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
        removeColumn(condition, conditionValue);
    }

    @Override
    public void removeColumn(String column, String conditionValue) {
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
        return existsColumn(condition, conditionValue);
    }

    @Override
    public boolean existsColumn(String column, String conditionValue) {
        return yamlDocument.contains(table + "." + conditionValue);
    }


    @Override
    public List<String> getTopConditionAscending(String column, int limit) {
        return getTopConditionAscending(condition, column, limit);
    }

    @Override
    public List<String> getTopConditionAscending(String condition, String column, int limit) {
        List<String> list = new ArrayList<>();

        HashMap<String, Integer> map = new HashMap<>();
        Section section = yamlDocument.getSection(condition);
        if (section == null) return list;

        for (Object key : section.getKeys()) {
            if (map.size() == limit) break;
            String name = (String) key;
            map.put(name, yamlDocument.getInt(table + "." + name + "." + column));
        }

        return HashMapUtils.sortByValueAscending(map);
    }

    @Override
    public List<String> getTopConditionDescending(String column, int limit) {
        return getTopConditionDescending(condition, column, limit);
    }

    @Override
    public List<String> getTopConditionDescending(String condition, String column, int limit) {
        List<String> list = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        Section section = yamlDocument.getSection(condition);
        if (section == null) return list;

        for (Object key : section.getKeys()) {
            if (map.size() == limit) break;
            String name = (String) key;
            map.put(name, yamlDocument.getInt(table + "." + name + "." + column));
        }

        return HashMapUtils.sortByValueDescending(map);
    }
}
