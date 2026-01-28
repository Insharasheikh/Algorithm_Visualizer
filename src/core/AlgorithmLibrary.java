package core;

import java.util.HashMap;
import algorithms.sorting.*;

public class AlgorithmLibrary {

    public static HashMap<String, SortAlgorithm> algorithmsMap = new HashMap<>();
    public static HashMap<String, String> timeComplexities = new HashMap<>();

    static {
        // Register BubbleSort
        BubbleSort bubble = new BubbleSort();
        algorithmsMap.put("BubbleSort", bubble);
        timeComplexities.put("BubbleSort", bubble.getTimeComplexity());

        // Register InsertionSort
        InsertionSort insertion = new InsertionSort();
        algorithmsMap.put("InsertionSort", insertion);
        timeComplexities.put("InsertionSort", insertion.getTimeComplexity());

        // Add more algorithms here later
    }

    public static String[] getCodeLines(String algoName) {
        SortAlgorithm algo = algorithmsMap.get(algoName);
        return algo != null ? algo.getCode() : new String[]{};
    }

    public static String getTimeComplexity(String algoName) {
        return timeComplexities.getOrDefault(algoName, "");
    }
}
