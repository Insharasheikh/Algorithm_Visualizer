package algorithms.sorting;

import ui.CodePane;
import ui.VisualizationPane;


public interface SortAlgorithm {
    /**
     * Return the lines of code for displaying in CodePane
     */
    String[] getCode();

    /**
     * Return time complexity as string
     */
    String getTimeComplexity();

    /**
     * The actual sorting logic with visualization
     */
    void sort(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed);
}
