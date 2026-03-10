package algorithms.sorting;

import algorithms.Algorithm;
import core.StepController;
import ui.CodePane;
import ui.VisualizationPane;

public class BubbleSort implements Algorithm {

    private final String[] codeLines = new String[]{
        "for i = 0 to n-1",
        "  for j = 0 to n-i-1",
        "    if arr[j] > arr[j+1]",
        "      swap arr[j], arr[j+1]"
    };

    @Override
    public String[] getCode() { return codeLines; }

    @Override
    public String getTimeComplexity() { return "O(n²)"; }

    @Override
    public void run(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed, StepController stepController) {
        try {
            int n = arr.length;
            for (int i = 0; i < n - 1; i++) {
                codePane.highlightLine(0);
                stepController.waitIfPaused();

                for (int j = 0; j < n - i - 1; j++) {
                    if (Thread.currentThread().isInterrupted()) return;

                    codePane.highlightLine(1);
                    // Highlight compared bars (j and j+1), grey out sorted (n-i to n)
                    vizPane.updateArray(arr, j, j + 1, n - i, -1);
                    Thread.sleep(speed);
                    stepController.waitIfPaused();

                    codePane.highlightLine(2);
                    if (arr[j] > arr[j + 1]) {
                        codePane.highlightLine(3);
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                        vizPane.updateArray(arr, j, j + 1, n - i, -1);
                        Thread.sleep(speed);
                        stepController.waitIfPaused();
                    }
                }
            }
            // Show fully sorted
            vizPane.updateArray(arr, -1, -1, 0, -1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}