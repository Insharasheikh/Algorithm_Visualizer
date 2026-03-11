package algorithms.searching;

import algorithms.Algorithm;
import core.StepController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import ui.CodePane;
import ui.VisualizationPane;

public class LinearSearch implements Algorithm {

    private final String[] codeLines = new String[]{
        "for i = 0 to n-1",
        "  if arr[i] == target",
        "    return i  // found!",
        "return -1  // not found"
    };

    @Override
    public String[] getCode() { return codeLines; }

    @Override
    public String getTimeComplexity() { return "O(n)"; }

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
            for (int i = 0; i < arr.length; i++) {
                if (Thread.currentThread().isInterrupted()) return;
                stepController.waitIfPaused();

                // Highlight bar being checked (orange)
                codePane.highlightLine(0);
                vizPane.updateArray(arr, i, -1, -1, -1);
                Thread.sleep(speed);

                codePane.highlightLine(1);
                Thread.sleep(speed / 2);

                if (arr[i] == target) {
                    // Found — light up green via specialIdx
                    codePane.highlightLine(2);
                    vizPane.updateArray(arr, -1, -1, -1, i);
                    Thread.sleep(speed * 2);

                    final int foundAt = i;
                    if (resultLabel != null) {
                        Platform.runLater(() ->
                            resultLabel.setText("✔  Found " + target + " at index " + foundAt));
                    }
                    return;
                }
            }

            // Not found
            codePane.highlightLine(3);
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