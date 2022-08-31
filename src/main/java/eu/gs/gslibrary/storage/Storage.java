package eu.gs.gslibrary.storage;

import java.util.List;

public interface Storage {

    void insert(final String columns, final Object... parameters);

    void insertUpdate(final String columns, final Object... parameters);

    void set(String conditionValue, String column, Object object);

    String getString(String conditionValue, String column);

    int getInt(String conditionValue, String column);

    double getDouble(String conditionValue, String column);

    double getFloat(String conditionValue, String column);

    boolean getBoolean(String conditionValue, String column);

    Object getObject(String conditionValue, String column);


    List<String> getConditions();

    void removeCondition(String conditionValue);

    boolean existsCondition(String conditionValue);

    List<String> getTopConditionAscending(String column, int limit);

    List<String> getTopConditionDescending(String column, int limit);

}

