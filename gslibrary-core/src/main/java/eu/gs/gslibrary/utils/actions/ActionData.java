package eu.gs.gslibrary.utils.actions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class ActionData {

    private Map<String, Object> data;
    private Section section;

    public ActionData(Section section) {
        data = new HashMap<>();
        this.section = section;

        for (String key : section.getRoutesAsStrings(false)) {
            data.put(key, section.get(key));
        }
    }

    public boolean isExist(String key) {
        return data.containsKey(key);
    }

    public String getString(String key) {
        return String.valueOf(data.get(key));
    }

    public double getDouble(String key) {
        return (double) data.get(key);
    }

    public int getInt(String key) {
        return (int) data.get(key);
    }

    public float getFloat(String key) {
        try {
            return (float) data.get(key);
        }catch (Exception e) {
            return (getInt(key));
        }
    }

    public List<Object> getList(String key) {
        return (List<Object>) data.get(key);
    }

    public List<String> getListString(String key) {
        return getList(key).stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());
    }

    public Object getCustom(String path) {
        return section.get(path);
    }

    public String getCustomString(String path) {
        return section.getString(path);
    }

    public double getCustomDouble(String path) {
        return section.getDouble(path);
    }

    public int getCustomInt(String path) {
        return section.getInt(path);
    }

}
