package algorithms.sorting;

import ui.CodePane;
import ui.VisualizationPane;
import core.StepController;

public interface SortAlgorithm extends Algorithm {
    String[] getCode();
    String getTimeComplexity();
    void sort(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed, StepController stepController);
}