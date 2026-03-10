package core;

import algorithms.searching.BinarySearch;
import algorithms.searching.LinearSearch;
import algorithms.sorting.*;
import java.util.HashMap;

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

        // Register SelectionSort 
        SelectionSort selection = new SelectionSort();
        algorithmsMap.put("SelectionSort", selection);
        timeComplexities.put("SelectionSort", selection.getTimeComplexity());

        // Register MergeSort ✅
        MergeSort merge = new MergeSort();
        algorithmsMap.put("MergeSort", merge);
        timeComplexities.put("MergeSort", merge.getTimeComplexity());

        // Register QuickSort ✅
        QuickSort quick = new QuickSort();
        algorithmsMap.put("QuickSort", quick);
        timeComplexities.put("QuickSort", quick.getTimeComplexity());

        LinearSearch linear = new LinearSearch();
        algorithmsMap.put("LinearSearch", linear);
        timeComplexities.put("LinearSearch", linear.getTimeComplexity());

        BinarySearch binary = new BinarySearch();
        algorithmsMap.put("BinarySearch", binary);
        timeComplexities.put("BinarySearch", binary.getTimeComplexity());

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
