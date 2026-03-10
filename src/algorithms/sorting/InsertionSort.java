package algorithms.sorting;

import algorithms.Algorithm;
import core.StepController;
import ui.CodePane;
import ui.VisualizationPane;

public class InsertionSort implements Algorithm {

    private final String[] codeLines = new String[]{
        "for i = 1 to n",
        "  key = arr[i]",
        "  j = i - 1",
        "  while j >= 0 and arr[j] > key",
        "    arr[j+1] = arr[j]",
        "    j--",
        "  arr[j+1] = key"
    };

    @Override
    public String[] getCode() { return codeLines; }

    @Override
    public String getTimeComplexity() { return "O(n²)"; }

    @Override
    public void run(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed, StepController stepController) {
        try {
            for (int i = 1; i < arr.length; i++) {
                if (Thread.currentThread().isInterrupted()) return;

                codePane.highlightLine(0);
                stepController.waitIfPaused();

                int key = arr[i];
                codePane.highlightLine(1);
                vizPane.updateArray(arr, i, -1, -1, i);
                Thread.sleep(speed);
                stepController.waitIfPaused();

                int j = i - 1;
                codePane.highlightLine(2);
                Thread.sleep(speed);

                while (j >= 0 && arr[j] > key) {
                    if (Thread.currentThread().isInterrupted()) return;

                    codePane.highlightLine(3);
                    stepController.waitIfPaused();

                    arr[j + 1] = arr[j];
                    codePane.highlightLine(4);
                    vizPane.updateArray(arr, j, j + 1, -1, i);
                    Thread.sleep(speed);

                    j--;
                    codePane.highlightLine(5);
                    stepController.waitIfPaused();
                }

                arr[j + 1] = key;
                codePane.highlightLine(6);
                vizPane.updateArray(arr, j + 1, -1, -1, -1);
                Thread.sleep(speed);
                stepController.waitIfPaused();
            }
            vizPane.updateArray(arr, -1, -1, 0, -1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}