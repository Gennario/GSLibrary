package eu.gs.gslibrary.storage.type;

import eu.gs.gslibrary.GSLibrary;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Storage {

    void insert(final String columns, final Object... parameters);

    void insertUpdate(final String columns, final Object... parameters);

    /* Empty condition. */
    void set(String conditionValue, String column, Object object);

    String getString(String conditionValue, String column);

    int getInt(String conditionValue, String column);

    double getDouble(String conditionValue, String column);

    float getFloat(String conditionValue, String column);

    boolean getBoolean(String conditionValue, String column);

    Object getObject(String conditionValue, String column);


    List<String> getConditions();

    void removeCondition(String conditionValue);

    boolean existsCondition(String conditionValue);

    List<String> getTopConditionAscending(String column, int limit);

    List<String> getTopConditionDescending(String column, int limit);

    /* With condition */
    void set(String condition, String conditionValue, String column, Object object);

    String getString(String condition, String conditionValue, String column);

    int getInt(String condition,String conditionValue, String column);

    double getDouble(String condition,String conditionValue, String column);

    float getFloat(String condition,String conditionValue, String column);

    boolean getBoolean(String condition,String conditionValue, String column);

    Object getObject(String condition,String conditionValue, String column);


    List<String> getConditions(String condition);

    void removeCondition(String condition,String conditionValue);

    boolean existsCondition(String condition,String conditionValue);

    List<String> getTopConditionAscending(String condition,String column, int limit);

    List<String> getTopConditionDescending(String condition,String column, int limit);


    default void run(Runnable runnable) {
        try {
            CompletableFuture.runAsync(runnable);
        } catch (Exception e) {
            try {
                Bukkit.getScheduler().runTask(GSLibrary.getInstance(), runnable);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}

