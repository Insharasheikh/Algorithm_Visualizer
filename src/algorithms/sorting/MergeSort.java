package algorithms.sorting;

import ui.CodePane;
import ui.VisualizationPane;

public class MergeSort implements SortAlgorithm {

    @Override
    public String[] getCode() {
        return new String[]{
                "mergeSort(arr, left, right):",
                " if left < right:",
                "   mid = (left+right)/2",
                "   mergeSort(left, mid)",
                "   mergeSort(mid+1, right)",
                "   merge(arr, left, mid, right)"
        };
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }

    @Override
    public void sort(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed) {
        mergeSort(arr, 0, arr.length - 1, vizPane, codePane, speed);
        vizPane.updateArray(arr);
    }

    private void mergeSort(int[] arr, int left, int right,
                           VisualizationPane vizPane, CodePane codePane, int speed) {

        if (left < right) {

            codePane.highlightLine(2);

            int mid = (left + right) / 2;
            codePane.highlightLine(3);

            mergeSort(arr, left, mid, vizPane, codePane, speed);
            codePane.highlightLine(4);

            mergeSort(arr, mid + 1, right, vizPane, codePane, speed);
            codePane.highlightLine(5);

            merge(arr, left, mid, right, vizPane, codePane, speed);
            codePane.highlightLine(6);
        }
    }

    private void merge(int[] arr, int left, int mid, int right,
                       VisualizationPane vizPane, CodePane codePane, int speed) {

        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        // Copy data
        for (int i = 0; i < n1; i++)
            L[i] = arr[left + i];

        for (int j = 0; j < n2; j++)
            R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        // Merge arrays
        while (i < n1 && j < n2) {

            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }

            k++;

            vizPane.updateArray(arr);

            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                return;
            }
        }

        // Copy remaining elements
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
            vizPane.updateArray(arr);

            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                return;
            }
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
            vizPane.updateArray(arr);

            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}