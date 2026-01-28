package ui;

import javafx.scene.control.Slider;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ControlBar extends HBox {
    public Button startBtn, pauseBtn, resetBtn;
    public Slider speedSlider;
    public ComboBox<String> algorithmSelector;

    public ControlBar() {
        // Buttons
        startBtn = new Button("Start");
        pauseBtn = new Button("Pause");
        resetBtn = new Button("Reset");

        // Speed slider
        speedSlider = new Slider(50, 1000, 300); // min=50ms, max=1000ms, default=300ms
        Label speedLabel = new Label("Speed:");

        // Algorithm selection ComboBox
        algorithmSelector = new ComboBox<>();
        algorithmSelector.getItems().addAll(
            "BubbleSort",
            "InsertionSort"
            // Add more algorithms here
        );
        algorithmSelector.getSelectionModel().selectFirst(); // default selection

        // Add all controls to HBox
        this.getChildren().addAll(
            algorithmSelector, startBtn, pauseBtn, resetBtn, speedLabel, speedSlider
        );
        this.setSpacing(10);
        this.setStyle("-fx-padding: 10; -fx-background-color: #2c3e50;");
    }
}
