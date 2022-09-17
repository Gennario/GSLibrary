package eu.gs.gslibrary.storage;

import eu.gs.gslibrary.GSLibrary;
import eu.gs.gslibrary.storage.aids.StorageIsEqual;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Storage {

    void insert(final String columns, final Object... parameters);

    void insertUpdate(final String columns, final Object... parameters);

    void set(String conditionValue, String column, Object parameter);
    void set(String condition, String conditionValue, String column, Object parameter);
    void set(String conditionValue, String column, Object parameter, StorageIsEqual storageIsEqual);
    void set(String condition, String conditionValue, String column, Object parameter, StorageIsEqual storageIsEqual);

    String getString(String conditionValue, String column);
    String getString(String condition, String conditionValue, String column);
    String getString(String conditionValue, String column, StorageIsEqual storageIsEqual);
    String getString(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual);

    int getInt(String conditionValue, String column);
    int getInt(String condition, String conditionValue, String column);
    int getInt(String conditionValue, String column, StorageIsEqual storageIsEqual);
    int getInt(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual);

    double getDouble(String conditionValue, String column);
    double getDouble(String condition, String conditionValue, String column);
    double getDouble(String conditionValue, String column, StorageIsEqual storageIsEqual);
    double getDouble(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual);

    float getFloat(String conditionValue, String column);
    float getFloat(String condition, String conditionValue, String column);
    float getFloat(String conditionValue, String column, StorageIsEqual storageIsEqual);
    float getFloat(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual);

    boolean getBoolean(String conditionValue, String column);
    boolean getBoolean(String condition, String conditionValue, String column);
    boolean getBoolean(String conditionValue, String column, StorageIsEqual storageIsEqual);
    boolean getBoolean(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual);

    Object getObject(String conditionValue, String column);
    Object getObject(String condition, String conditionValue, String column);
    Object getObject(String conditionValue, String column, StorageIsEqual storageIsEqual);
    Object getObject(String condition, String conditionValue, String column, StorageIsEqual storageIsEqual);


    List<String> getConditions();
    List<String> getColumns(String column);

    void removeCondition(String conditionValue);
    void removeColumn(String column, String conditionValue);

    boolean existsCondition(String conditionValue);
    boolean existsColumn(String column, String conditionValue);

    List<String> getTopConditionAscending(String column, int limit);
    List<String> getTopConditionAscending(String condition, String column, int limit);

    List<String> getTopConditionDescending(String column, int limit);
    List<String> getTopConditionDescending(String condition, String column, int limit);


    default void run(Runnable runnable) {
        try {
            CompletableFuture.runAsync(runnable).whenComplete(((unused, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                }
            }));
        } catch (Exception e) {
            try {
                Bukkit.getScheduler().runTask(GSLibrary.getInstance(), runnable);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}

