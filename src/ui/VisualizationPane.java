package ui;

import core.StepController;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Renders the array as colored bars.
 *
 * Color scheme:
 *   DODGERBLUE  = default / unsorted bar
 *   ORANGE      = bar at index1 (first comparison target / current element)
 *   RED         = bar at index2 (second comparison target)
 *   LIMEGREEN   = sorted region (index >= sortedFrom) or found element
 *   GOLD        = pivot / key element (special highlight)
 */
public class VisualizationPane extends Pane {

    private static final double PANE_HEIGHT = 380;
    private static final double BAR_GAP = 4;
    private static final int    BAR_SCALE = 35; // pixels per unit value

    public VisualizationPane(StepController stepController) {
        this.setPrefHeight(PANE_HEIGHT);
        this.setStyle("-fx-background-color: #1e1e2e; -fx-background-radius: 8;");
    }

    /**
     * Thread-safe array update with optional bar highlights.
     *
     * @param array      current array state
     * @param index1     first highlighted bar  (-1 = none) — shown in ORANGE
     * @param index2     second highlighted bar (-1 = none) — shown in RED
     * @param sortedFrom bars from this index onwards are sorted (-1 = none) — shown in GREEN
     *                   pass 0 to mark the entire array green (fully sorted)
     * @param specialIdx pivot / key / found element (-1 = none) — shown in GOLD
     */
    public void updateArray(int[] array, int index1, int index2, int sortedFrom, int specialIdx) {
        int[] snapshot = array.clone();
        Platform.runLater(() -> drawArray(snapshot, index1, index2, sortedFrom, specialIdx));
    }

    /** Convenience overload — draws all bars in default blue (used for reset/init). */
    public void updateArray(int[] array) {
        updateArray(array, -1, -1, -1, -1);
    }

    // ── Private drawing ──────────────────────────────────────────────────────

    private void drawArray(int[] array, int index1, int index2, int sortedFrom, int specialIdx) {
        this.getChildren().clear();

        double paneWidth  = Math.max(this.getWidth(), 400);
        double barWidth   = (paneWidth - BAR_GAP) / array.length - BAR_GAP;

        int maxVal = 1;
        for (int v : array) if (v > maxVal) maxVal = v;

        // Scale bars to fit pane height nicely (leave 30px for labels)
        double scale = (PANE_HEIGHT - 30) / (double) maxVal;

        for (int i = 0; i < array.length; i++) {
            double barHeight = array[i] * scale;
            double x = BAR_GAP + i * (barWidth + BAR_GAP);
            double y = PANE_HEIGHT - barHeight - 20;

            Color color;
            if (i == specialIdx)                               color = Color.GOLD;
            else if (sortedFrom == 0)                          color = Color.LIMEGREEN; // fully sorted
            else if (sortedFrom > 0 && i >= sortedFrom)        color = Color.LIMEGREEN;
            else if (i == index1)                              color = Color.ORANGE;
            else if (i == index2)                              color = Color.TOMATO;
            else                                               color = Color.DODGERBLUE;

            Rectangle bar = new Rectangle(barWidth, barHeight);
            bar.setFill(color);
            bar.setArcWidth(4);
            bar.setArcHeight(4);
            bar.setX(x);
            bar.setY(y);
            this.getChildren().add(bar);

            // Value label below bar
            Text label = new Text(String.valueOf(array[i]));
            label.setFill(Color.WHITE);
            label.setStyle("-fx-font-size: 11px;");
            label.setX(x + barWidth / 2 - 5);
            label.setY(PANE_HEIGHT - 4);
            this.getChildren().add(label);
        }
    }
}