package algorithms.searching;

import algorithms.Algorithm;
import core.StepController;
import ui.CodePane;
import ui.VisualizationPane;

public class LinearSearch implements Algorithm {

    private final String[] codeLines = new String[]{
        "for i = 0 to n-1",
        "  if arr[i] == target",
        "    return i  // found!",
        "return -1  // not found"
    };

    @Override
    public String[] getCode() { return codeLines; }

    @Override
    public String getTimeComplexity() { return "O(n)"; }

    @Override
    public void run(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed, StepController stepController) {
        try {
            int target = arr[arr.length / 2]; // search for middle element as demo

            for (int i = 0; i < arr.length; i++) {
                if (Thread.currentThread().isInterrupted()) return;
                stepController.waitIfPaused();

                codePane.highlightLine(0);
                // highlight current bar being checked (index i), target bar in different color
                vizPane.updateArray(arr, i, -1, -1, -1);
                Thread.sleep(speed);

                codePane.highlightLine(1);
                Thread.sleep(speed / 2);

                if (arr[i] == target) {
                    codePane.highlightLine(2);
                    vizPane.updateArray(arr, i, i, -1, i); // found — highlight green
                    Thread.sleep(speed * 2);
                    return;
                }
            }

            codePane.highlightLine(3);
            vizPane.updateArray(arr, -1, -1, -1, -1);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}