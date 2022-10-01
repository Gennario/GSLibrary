package eu.gs.gslibrary.utils;

import java.util.*;

public class HashMapUtils {

    /**
     * It takes a HashMap as input, creates a list from the HashMap, sorts the list, and returns a list of the keys from
     * the HashMap
     *
     * @param hm The HashMap that you want to sort.
     * @return A list of strings
     */
    public static List<String> sortByValueAscending(final HashMap<String, Integer> hm) {
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


    /**
     * Sort a HashMap by value in descending order
     *
     * @param hm The HashMap that you want to sort.
     * @return A list of strings
     */
    public static List<String> sortByValueDescending(final HashMap<String, Integer> hm) {
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
