package algorithms.sorting;

import ui.CodePane;
import ui.VisualizationPane;

public class QuickSort implements SortAlgorithm {

    @Override
    public String[] getCode() {
        return new String[]{
                "quickSort(arr, low, high):",
                " if low < high:",
                "   pivotIndex = partition(arr, low, high)",
                "   quickSort(low, pivotIndex-1)",
                "   quickSort(pivotIndex+1, high)"
        };
    }

    @Override
    public String getTimeComplexity() {
        return "Average: O(n log n), Worst: O(n^2)";
    }

    @Override
    public void sort(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed) {
        quickSort(arr, 0, arr.length - 1, vizPane, codePane, speed);
        vizPane.updateArray(arr);
    }

    private void quickSort(int[] arr, int low, int high,
                           VisualizationPane vizPane, CodePane codePane, int speed) {

        if (low < high) {

            codePane.highlightLine(2);

            int pivotIndex = partition(arr, low, high, vizPane, codePane, speed);
            codePane.highlightLine(3);

            quickSort(arr, low, pivotIndex - 1, vizPane, codePane, speed);
            codePane.highlightLine(4);

            quickSort(arr, pivotIndex + 1, high, vizPane, codePane, speed);
            codePane.highlightLine(5);
        }
    }

    private int partition(int[] arr, int low, int high,
                          VisualizationPane vizPane, CodePane codePane, int speed) {

        int pivot = arr[high]; // pivot = last element
        int i = low - 1;

        for (int j = low; j < high; j++) {

            if (arr[j] < pivot) {
                i++;

                // Swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;

                vizPane.updateArray(arr);

                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    return high;
                }
            }
        }

        // Place pivot in correct position
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        vizPane.updateArray(arr);

        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            return high;
        }

        return i + 1;
    }
}