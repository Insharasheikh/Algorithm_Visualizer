package core;

import algorithms.Algorithm;
import algorithms.searching.BinarySearch;
import algorithms.searching.LinearSearch;
import algorithms.sorting.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Central registry of all algorithms.
 * Uses Algorithm interface so sorting + searching coexist cleanly.
 */
public class AlgorithmLibrary {

    // LinkedHashMap preserves insertion order for ComboBox display
    private static final Map<String, Algorithm> algorithmsMap = new LinkedHashMap<>();

    static {
        algorithmsMap.put("BubbleSort",    new BubbleSort());
        algorithmsMap.put("InsertionSort", new InsertionSort());
        algorithmsMap.put("SelectionSort", new SelectionSort());
        algorithmsMap.put("MergeSort",     new MergeSort());
        algorithmsMap.put("QuickSort",     new QuickSort());
        algorithmsMap.put("LinearSearch",  new LinearSearch());
        algorithmsMap.put("BinarySearch",  new BinarySearch());
    }

    public static Map<String, Algorithm> getAll() {
        return algorithmsMap;
    }

    public static Algorithm get(String name) {
        return algorithmsMap.get(name);
    }

    public static String[] getCodeLines(String name) {
        Algorithm algo = algorithmsMap.get(name);
        return algo != null ? algo.getCode() : new String[]{};
    }

    public static String getTimeComplexity(String name) {
        Algorithm algo = algorithmsMap.get(name);
        return algo != null ? algo.getTimeComplexity() : "";
    }
}