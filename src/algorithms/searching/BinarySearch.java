package algorithms.searching;

import algorithms.Algorithm;
import core.StepController;
import ui.CodePane;
import ui.VisualizationPane;
import java.util.Arrays;

public class BinarySearch implements Algorithm {

    private final String[] codeLines = new String[]{
        "low = 0, high = n-1",
        "while low <= high",
        "  mid = (low + high) / 2",
        "  if arr[mid] == target → return mid",
        "  else if arr[mid] < target → low = mid+1",
        "  else → high = mid-1"
    };

    @Override
    public String[] getCode() { return codeLines; }

    @Override
    public String getTimeComplexity() { return "O(log n)"; }

    @Override
    public void run(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed, StepController stepController) {
        try {
            // Binary search requires sorted array
            Arrays.sort(arr);
            vizPane.updateArray(arr, -1, -1, -1, -1);
            Thread.sleep(speed);

            int target = arr[arr.length / 2]; // search for middle value as demo

            int low = 0;
            int high = arr.length - 1;

            codePane.highlightLine(0);
            vizPane.updateArray(arr, low, high, -1, -1);
            Thread.sleep(speed);

            while (low <= high) {
                if (Thread.currentThread().isInterrupted()) return;
                stepController.waitIfPaused();

                codePane.highlightLine(1);
                Thread.sleep(speed / 2);

                int mid = (low + high) / 2;

                codePane.highlightLine(2);
                // low=red, high=red, mid=yellow (reuse highlight colors)
                vizPane.updateArray(arr, low, high, -1, mid);
                Thread.sleep(speed);

                if (arr[mid] == target) {
                    codePane.highlightLine(3);
                    vizPane.updateArray(arr, mid, mid, -1, mid); // found
                    Thread.sleep(speed * 2);
                    return;

                } else if (arr[mid] < target) {
                    codePane.highlightLine(4);
                    low = mid + 1;

                } else {
                    codePane.highlightLine(5);
                    high = mid - 1;
                }

                vizPane.updateArray(arr, low, high, -1, mid);
                Thread.sleep(speed);
            }

            // Not found
            vizPane.updateArray(arr, -1, -1, -1, -1);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}