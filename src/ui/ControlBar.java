package ui;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Bottom control bar.
 *
 * Speed slider fix: slider shows 1 (slow) → 10 (fast).
 * Internally this maps to delay: fast=50ms, slow=1000ms.
 * Use getSpeedDelay() to get the actual ms delay for Thread.sleep().
 */
public class ControlBar extends HBox {

    public Button startBtn, pauseBtn, resumeBtn, resetBtn;
    public Slider speedSlider;
    public ComboBox<String> algorithmSelector;
    public Label speedValueLabel;

    // Custom array input
    public TextField arrayInputField;
    public Button    setArrayBtn;
    public Label     arrayErrorLabel;

    public ControlBar() {
        startBtn  = new Button("▶  Start");
        pauseBtn  = new Button("⏸  Pause");
        resumeBtn = new Button("▶  Resume");
        resetBtn  = new Button("↺  Reset");

        styleButton(startBtn,  "#a6e3a1");
        styleButton(pauseBtn,  "#f9e2af");
        styleButton(resumeBtn, "#89b4fa");
        styleButton(resetBtn,  "#f38ba8");

        resumeBtn.setDisable(true); // only enabled when paused

        // Speed slider: 1=slow, 10=fast (user-friendly direction)
        speedSlider = new Slider(1, 10, 5);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setSnapToTicks(true);
        speedSlider.setPrefWidth(160);

        speedValueLabel = new Label("Speed: 5");
        speedValueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            speedValueLabel.setText("Speed: " + newVal.intValue());
        });

        Label speedLabel = new Label("Slow ◂");
        Label speedFastLabel = new Label("▸ Fast");
        speedLabel.setStyle("-fx-text-fill: #a6adc8; -fx-font-size: 11px;");
        speedFastLabel.setStyle("-fx-text-fill: #a6adc8; -fx-font-size: 11px;");

        VBox speedBox = new VBox(2, speedValueLabel,
            new HBox(4, speedLabel, speedSlider, speedFastLabel));
        speedBox.setStyle("-fx-padding: 0 4 0 4;");

        algorithmSelector = new ComboBox<>();
        algorithmSelector.getItems().addAll(
            "BubbleSort", "InsertionSort", "SelectionSort",
            "MergeSort", "QuickSort",
            "LinearSearch", "BinarySearch"
        );
        algorithmSelector.getSelectionModel().selectFirst();
        algorithmSelector.setStyle(
            "-fx-background-color: #45475a; -fx-text-fill: white; " +
            "-fx-font-size: 13px; -fx-pref-width: 150px;"
        );

        // ── Array input ───────────────────────────────────────────────────────
        arrayInputField = new TextField();
        arrayInputField.setPromptText("e.g. 5, 3, 8, 4, 2");
        arrayInputField.setPrefWidth(180);
        arrayInputField.setStyle(
            "-fx-background-color: #45475a; -fx-text-fill: white; " +
            "-fx-prompt-text-fill: #6c7086; -fx-font-size: 12px; " +
            "-fx-background-radius: 6; -fx-padding: 6 8 6 8;"
        );

        setArrayBtn = new Button("Set Array");
        styleButton(setArrayBtn, "#cba6f7");

        arrayErrorLabel = new Label("");
        arrayErrorLabel.setStyle("-fx-text-fill: #f38ba8; -fx-font-size: 11px;");

        Label arrayLabel = new Label("Array:");
        arrayLabel.setStyle("-fx-text-fill: #a6adc8; -fx-font-size: 12px;");

        VBox arrayBox = new VBox(3,
            arrayLabel,
            new HBox(6, arrayInputField, setArrayBtn),
            arrayErrorLabel
        );
        arrayBox.setStyle("-fx-padding: 0 8 0 0;");

        this.getChildren().addAll(
            algorithmSelector, startBtn, pauseBtn, resumeBtn, resetBtn, speedBox, arrayBox
        );
        this.setSpacing(12);
        this.setStyle(
            "-fx-padding: 12 16 12 16; -fx-background-color: #181825; " +
            "-fx-alignment: center-left;"
        );
    }

    /**
     * Convert slider value (1-10) to actual millisecond delay.
     * Slider 10 (fast) → 50ms delay
     * Slider 1  (slow) → 1000ms delay
     */
    public int getSpeedDelay() {
        int sliderVal = (int) speedSlider.getValue();
        // Linear interpolation: delay = 1050 - (sliderVal * 100)
        return 1050 - (sliderVal * 100);
    }

    private void styleButton(Button btn, String color) {
        btn.setStyle(
            "-fx-background-color: " + color + "; -fx-font-size: 13px; " +
            "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 14 6 14;"
        );
    }
}