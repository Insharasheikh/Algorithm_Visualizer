package algorithms.sorting;

import algorithms.Algorithm;
import core.StepController;
import ui.CodePane;
import ui.VisualizationPane;

public class MergeSort implements Algorithm {

    private final String[] codeLines = new String[]{
        "mergeSort(arr, left, right):",
        "  if left < right:",
        "    mid = (left + right) / 2",
        "    mergeSort(left, mid)",
        "    mergeSort(mid+1, right)",
        "    merge(arr, left, mid, right)"
    };

    @Override
    public String[] getCode() { return codeLines; }

    @Override
    public String getTimeComplexity() { return "O(n log n)"; }

    @Override
    public void run(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed, StepController stepController) {
        try {
            mergeSort(arr, 0, arr.length - 1, vizPane, codePane, speed, stepController);
            vizPane.updateArray(arr, -1, -1, 0, -1); // all green = sorted
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void mergeSort(int[] arr, int left, int right,
                           VisualizationPane vizPane, CodePane codePane,
                           int speed, StepController stepController) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) return;

        if (left < right) {
            codePane.highlightLine(1);
            stepController.waitIfPaused();

            int mid = (left + right) / 2;
            codePane.highlightLine(2);
            Thread.sleep(speed);

            codePane.highlightLine(3);
            mergeSort(arr, left, mid, vizPane, codePane, speed, stepController);

            codePane.highlightLine(4);
            mergeSort(arr, mid + 1, right, vizPane, codePane, speed, stepController);

            codePane.highlightLine(5);
            merge(arr, left, mid, right, vizPane, codePane, speed, stepController);
        }
    }

    private void merge(int[] arr, int left, int mid, int right,
                       VisualizationPane vizPane, CodePane codePane,
                       int speed, StepController stepController) throws InterruptedException {

        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (Thread.currentThread().isInterrupted()) return;
            stepController.waitIfPaused();

            if (L[i] <= R[j]) {
                arr[k] = L[i++];
            } else {
                arr[k] = R[j++];
            }
            vizPane.updateArray(arr, k, -1, -1, -1);
            Thread.sleep(speed);
            k++;
        }

        while (i < n1) {
            if (Thread.currentThread().isInterrupted()) return;
            arr[k] = L[i++];
            vizPane.updateArray(arr, k++, -1, -1, -1);
            Thread.sleep(speed);
        }

        while (j < n2) {
            if (Thread.currentThread().isInterrupted()) return;
            arr[k] = R[j++];
            vizPane.updateArray(arr, k++, -1, -1, -1);
            Thread.sleep(speed);
        }
    }
}