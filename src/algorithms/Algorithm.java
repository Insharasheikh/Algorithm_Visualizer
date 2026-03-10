package algorithms;

import ui.CodePane;
import ui.VisualizationPane;
import core.StepController;

/**
 * Common interface for ALL algorithms (sorting + searching).
 * Replaces the misuse of SortAlgorithm for search algorithms.
 */
public interface Algorithm {
    String[] getCode();
    String getTimeComplexity();
    void run(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed, StepController stepController);
}


