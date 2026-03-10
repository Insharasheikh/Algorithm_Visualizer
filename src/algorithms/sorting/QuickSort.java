package algorithms.sorting;

import algorithms.Algorithm;
import core.StepController;
import ui.CodePane;
import ui.VisualizationPane;

public class QuickSort implements Algorithm {

    private final String[] codeLines = new String[]{
        "quickSort(arr, low, high):",
        "  if low < high:",
        "    pivotIndex = partition(arr, low, high)",
        "    quickSort(low, pivotIndex-1)",
        "    quickSort(pivotIndex+1, high)"
    };

    @Override
    public String[] getCode() { return codeLines; }

    @Override
    public String getTimeComplexity() { return "Avg: O(n log n), Worst: O(n²)"; }

    @Override
    public void run(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed, StepController stepController) {
        try {
            quickSort(arr, 0, arr.length - 1, vizPane, codePane, speed, stepController);
            vizPane.updateArray(arr, -1, -1, 0, -1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void quickSort(int[] arr, int low, int high,
                           VisualizationPane vizPane, CodePane codePane,
                           int speed, StepController stepController) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) return;

        if (low < high) {
            codePane.highlightLine(1);
            stepController.waitIfPaused();

            int pivotIndex = partition(arr, low, high, vizPane, codePane, speed, stepController);

            codePane.highlightLine(3);
            quickSort(arr, low, pivotIndex - 1, vizPane, codePane, speed, stepController);

            codePane.highlightLine(4);
            quickSort(arr, pivotIndex + 1, high, vizPane, codePane, speed, stepController);
        }
    }

    private int partition(int[] arr, int low, int high,
                          VisualizationPane vizPane, CodePane codePane,
                          int speed, StepController stepController) throws InterruptedException {
        int pivot = arr[high];
        int i = low - 1;

        codePane.highlightLine(2);
        vizPane.updateArray(arr, -1, high, -1, high); // highlight pivot

        for (int j = low; j < high; j++) {
            if (Thread.currentThread().isInterrupted()) return high;
            stepController.waitIfPaused();

            vizPane.updateArray(arr, j, high, -1, high); // j vs pivot
            Thread.sleep(speed);

            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                vizPane.updateArray(arr, i, j, -1, high);
                Thread.sleep(speed);
            }
        }

        // Place pivot in correct position
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        vizPane.updateArray(arr, i + 1, -1, -1, -1);
        Thread.sleep(speed);

        return i + 1;
    }
}