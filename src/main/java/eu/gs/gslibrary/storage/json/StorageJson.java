package eu.gs.gslibrary.storage.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.dejvokep.boostedyaml.YamlDocument;
import eu.gs.gslibrary.storage.StorageAPI;
import eu.gs.gslibrary.storage.json.type.StorageJsonInt;
import eu.gs.gslibrary.storage.json.type.StorageJsonString;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StorageJson {

    private final StorageAPI storageAPI;
    private final YamlDocument yamlJson;

    public StorageJson(StorageAPI storageAPI, YamlDocument yamlJson) {
        this.storageAPI = storageAPI;
        this.yamlJson = yamlJson;
    }

    public void set(String path, String columns, Object... parameters) {
        JSONObject jsonObject = new JSONObject();
        String[] split = columns.split(",");
        int i = 0;

        for (Object parameter : parameters) {
            jsonObject.put(split[i], parameter);
            i++;
        }

        yamlJson.set(path, jsonObject.toString());
        try {
            yamlJson.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String path, String column) {
        JSONObject object = new JSONObject(yamlJson.getString(path));
        return object.getString(column);
    }

    public int getInt(String path, String column) {
        JSONObject object = new JSONObject(yamlJson.getString(path));
        return object.getInt(column);
    }

    public boolean getBoolean(String path, String column) {
        JSONObject object = new JSONObject(yamlJson.getString(path));
        return object.getBoolean(column);
    }

    public double getDouble(String path, String column) {
        JSONObject object = new JSONObject(yamlJson.getString(path));
        return object.getDouble(column);
    }

    public float getFloat(String path, String column) {
        JSONObject object = new JSONObject(yamlJson.getString(path));
        return object.getFloat(column);
    }

    public void save(String path, String column, Collection<?> collection, StorageJsonSave type) {
        JSONObject jsonObject = new JSONObject();

        if (type == StorageJsonSave.INT) {
            List<StorageJsonInt> test = new ArrayList<>((Collection<? extends StorageJsonInt>) collection);

            Map<Integer, StorageJsonInt> collect = test
                    .stream().distinct()
                    .collect(Collectors.toMap(
                            StorageJsonInt::getValue,
                            h -> h
                    ));
            jsonObject.put(column, collect);
        } else if (type == StorageJsonSave.STRING) {
            List<StorageJsonString> test = new ArrayList<>((Collection<? extends StorageJsonString>) collection);

            Map<String, StorageJsonString> collect = test
                    .stream().distinct()
                    .collect(Collectors.toMap(
                            StorageJsonString::getValue,
                            h -> h
                    ));

            jsonObject.put(column, collect);
        }

        yamlJson.set(path, jsonObject.toString());
        try {
            yamlJson.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<?, ?> load(String path, String column, StorageJsonSave type) {
        JSONObject jsonObject = new JSONObject(yamlJson.getString(path));
        JsonElement jsonElement;

        if (jsonObject.has("points") && !(jsonElement = (JsonElement) jsonObject.get(column)).isJsonArray()) {
            Gson gson = new Gson();
            Type types = null;

            if (type == StorageJsonSave.STRING) {
              types =  new TypeToken<Map<String, StorageJsonString>>() {
                }.getType();
            } else if (type == StorageJsonSave.INT) {
                types =  new TypeToken<Map<Integer, StorageJsonString>>() {
                }.getType();
            }

            return gson.fromJson(jsonElement, types);
        }
        return null;
    }

    public enum StorageJsonSave {
        STRING, INT
    }
}
