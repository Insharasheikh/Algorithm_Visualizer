package algorithms.sorting;

import ui.CodePane;
import ui.VisualizationPane;

public class SelectionSort implements SortAlgorithm {

    @Override
    public String[] getCode() {
        return new String[]{
                "for i = 0 to n-1",
                "   minIndex = i",
                "   for j = i+1 to n",
                "       if arr[j] < arr[minIndex]",
                "           minIndex = j",
                "   swap arr[i] and arr[minIndex]"
        };
    }

    @Override
    public String getTimeComplexity() {
        return "O(n²)";
    }

    @Override
    public void sort(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed) {

        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {

            codePane.highlightLine(1);
            sleep(speed);

            int minIndex = i;
            codePane.highlightLine(2);
            sleep(speed);

            for (int j = i + 1; j < n; j++) {

                codePane.highlightLine(3);
                sleep(speed);

                codePane.highlightLine(4);
                sleep(speed);

                if (arr[j] < arr[minIndex]) {
                    minIndex = j;

                    codePane.highlightLine(5);
                    sleep(speed);
                }
            }

            // Swap
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;

            codePane.highlightLine(6);
            vizPane.updateArray(arr);
            sleep(speed);
        }
    }

    private void sleep(int speed) {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

