package core;

import algorithms.Algorithm;
import algorithms.graph.BFS;
import algorithms.graph.DFS;
import algorithms.graph.Dijkstra;
import algorithms.graph.GraphAlgorithm;
import algorithms.graph.TopologicalSort;
import algorithms.searching.BinarySearch;
import algorithms.searching.LinearSearch;
import algorithms.sorting.BubbleSort;
import algorithms.sorting.InsertionSort;
import algorithms.sorting.MergeSort;
import algorithms.sorting.QuickSort;
import algorithms.sorting.SelectionSort;

import java.util.LinkedHashMap;
import java.util.Map;

public class AlgorithmLibrary {

    private static final Map<String, Algorithm>      algoMap  = new LinkedHashMap<>();
    private static final Map<String, GraphAlgorithm> graphMap = new LinkedHashMap<>();

    static {
        // Sorting
        algoMap.put("BubbleSort",    new BubbleSort());
        algoMap.put("InsertionSort", new InsertionSort());
        algoMap.put("SelectionSort", new SelectionSort());
        algoMap.put("MergeSort",     new MergeSort());
        algoMap.put("QuickSort",     new QuickSort());

        // Searching
        algoMap.put("LinearSearch",  new LinearSearch());
        algoMap.put("BinarySearch",  new BinarySearch());

        // Graph
        graphMap.put("BFS",             new BFS());
        graphMap.put("DFS",             new DFS());
        graphMap.put("Dijkstra",        new Dijkstra());
        graphMap.put("TopologicalSort", new TopologicalSort());
    }

    public static Algorithm get(String name) {
        return algoMap.get(name);
    }

    public static GraphAlgorithm getGraph(String name) {
        return graphMap.get(name);
    }

    public static String[] getCodeLines(String name) {
        Algorithm a = algoMap.get(name);
        if (a != null) return a.getCode();
        GraphAlgorithm g = graphMap.get(name);
        return g != null ? g.getCode() : new String[]{};
    }

    public static String getTimeComplexity(String name) {
        Algorithm a = algoMap.get(name);
        if (a != null) return a.getTimeComplexity();
        GraphAlgorithm g = graphMap.get(name);
        return g != null ? g.getTimeComplexity() : "";
    }
}