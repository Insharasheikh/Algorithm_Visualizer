package algorithms.sorting;

import algorithms.Algorithm;
import core.StepController;
import ui.CodePane;
import ui.VisualizationPane;

public class SelectionSort implements Algorithm {

    private final String[] codeLines = new String[]{
        "for i = 0 to n-1",
        "  minIndex = i",
        "  for j = i+1 to n",
        "    if arr[j] < arr[minIndex]",
        "      minIndex = j",
        "  swap arr[i] and arr[minIndex]"
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
                if (Thread.currentThread().isInterrupted()) return;

                codePane.highlightLine(0);
                stepController.waitIfPaused();

                int minIndex = i;
                codePane.highlightLine(1);
                vizPane.updateArray(arr, i, -1, -1, minIndex);
                Thread.sleep(speed);

                for (int j = i + 1; j < n; j++) {
                    if (Thread.currentThread().isInterrupted()) return;

                    codePane.highlightLine(2);
                    vizPane.updateArray(arr, j, minIndex, -1, i);
                    Thread.sleep(speed);
                    stepController.waitIfPaused();

                    codePane.highlightLine(3);
                    if (arr[j] < arr[minIndex]) {
                        minIndex = j;
                        codePane.highlightLine(4);
                        vizPane.updateArray(arr, j, minIndex, -1, i);
                        Thread.sleep(speed);
                        stepController.waitIfPaused();
                    }
                }

                // Swap
                int temp = arr[minIndex];
                arr[minIndex] = arr[i];
                arr[i] = temp;

                codePane.highlightLine(5);
                vizPane.updateArray(arr, i, minIndex, -1, -1);
                Thread.sleep(speed);
                stepController.waitIfPaused();
            }
            vizPane.updateArray(arr, -1, -1, 0, -1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}