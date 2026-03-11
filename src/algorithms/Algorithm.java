package algorithms;

import core.StepController;
import ui.CodePane;
import ui.VisualizationPane;

public interface Algorithm {
    String[] getCode();
    String getTimeComplexity();
    void run(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed, StepController stepController);
}