package algorithms.searching;

import algorithms.Algorithm;
import core.StepController;
import javafx.application.Platform;
import javafx.scene.control.Label;
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

    // ── Called by Main (old path — no target, no result label) ────────────────
    @Override
    public void run(int[] arr, VisualizationPane vizPane, CodePane codePane,
                    int speed, StepController stepController) {
        run(arr, vizPane, codePane, speed, stepController, Integer.MIN_VALUE, null);
    }

    // ── Called by Main when Searching mode is active ──────────────────────────
    public void run(int[] arr, VisualizationPane vizPane, CodePane codePane,
                    int speed, StepController stepController,
                    int target, Label resultLabel) {
        try {
            // Binary search needs sorted array — sort and redraw first
            Arrays.sort(arr);
            vizPane.updateArray(arr, -1, -1, -1, -1);
            Thread.sleep(speed);

            int low  = 0;
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

                // low=orange(idx1), high=red(idx2), mid=gold(specialIdx)
                codePane.highlightLine(2);
                vizPane.updateArray(arr, low, high, -1, mid);
                Thread.sleep(speed);

                if (arr[mid] == target) {
                    // Found — full green on mid
                    codePane.highlightLine(3);
                    vizPane.updateArray(arr, -1, -1, -1, mid);
                    Thread.sleep(speed * 2);

                    final int foundAt = mid;
                    if (resultLabel != null) {
                        Platform.runLater(() ->
                            resultLabel.setText("✔  Found " + target
                                + " at index " + foundAt + " (sorted array)"));
                    }
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

            if (resultLabel != null) {
                Platform.runLater(() ->
                    resultLabel.setText("✘  " + target + " not found in array"));
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}