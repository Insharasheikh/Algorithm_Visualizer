package ui;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import core.StepController;

public class VisualizationPane extends Pane {

    private StepController stepController;
    private Rectangle[] bars;
    private double paneHeight = 400;

    public VisualizationPane(StepController stepController) {
        this.stepController = stepController;
        this.setPrefHeight(paneHeight);
        this.setStyle("-fx-background-color: #ecf0f1;");
    }

    /**
     * Draw array as bars with values on top
     */
    private void drawArray(int[] array) {
        this.getChildren().clear();
        bars = new Rectangle[array.length];

        double width = this.getWidth() / array.length;

        for (int i = 0; i < array.length; i++) {
            Rectangle rect = new Rectangle(width - 5, array[i] * 10);
            rect.setFill(Color.DODGERBLUE);
            rect.setX(i * width);
            rect.setY(paneHeight - array[i] * 10);

            bars[i] = rect;
            this.getChildren().add(rect);

            Text valueText = new Text(String.valueOf(array[i]));
            valueText.setX(i * width + width / 4);
            valueText.setY(paneHeight - array[i] * 10 - 5);
            this.getChildren().add(valueText);
        }
    }

    /**
     * Thread-safe update
     */
    public void updateArray(int[] array) {
        int[] snapshot = array.clone();
        Platform.runLater(() -> drawArray(snapshot));
    }
}
