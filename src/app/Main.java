package app;
import algorithms.sorting.*;
import core.StepController;
import ui.CodePane;
import ui.ControlBar;
import ui.VisualizationPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private int[] originalArray = {5, 3, 8, 4, 2, 7, 1, 6};
    private Thread sortingThread;

    // Map algorithm name -> SortAlgorithm instance
    private Map<String, SortAlgorithm> algoMap = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {

        // --- REGISTER ALGORITHMS HERE ---
        algoMap.put("BubbleSort", new BubbleSort());
        algoMap.put("InsertionSort", new InsertionSort());
        // add more algorithms later like algoMap.put("SelectionSort", new SelectionSort());

        StepController stepController = new StepController();
        VisualizationPane vizPane = new VisualizationPane(stepController);
        CodePane codePane = new CodePane();
        ControlBar controlBar = new ControlBar();

        Label complexityLabel = new Label("Time Complexity: ");
        complexityLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");

        BorderPane root = new BorderPane();
        root.setCenter(vizPane);
        root.setRight(codePane);
        root.setBottom(controlBar);
        root.setTop(complexityLabel);

        // Draw initial array
        vizPane.updateArray(originalArray);

        // --- START BUTTON ---
        controlBar.startBtn.setOnAction(e -> {
            if (sortingThread != null && sortingThread.isAlive()) return;

            String selectedAlgo = controlBar.algorithmSelector.getValue();
            SortAlgorithm algo = algoMap.get(selectedAlgo);

            codePane.setCode(algo.getCode());
            complexityLabel.setText("Time Complexity: " + algo.getTimeComplexity());

            int[] arrCopy = originalArray.clone();
            int speed = (int) controlBar.speedSlider.getValue();

            sortingThread = new Thread(() -> algo.sort(arrCopy, vizPane, codePane, speed));
            sortingThread.start();
        });

        // --- RESET BUTTON ---
        controlBar.resetBtn.setOnAction(e -> {
            if (sortingThread != null && sortingThread.isAlive()) sortingThread.interrupt();
            vizPane.updateArray(originalArray.clone());
            codePane.setCode(new String[]{}); // clear code
            complexityLabel.setText("Time Complexity: ");
        });

        // --- PAUSE BUTTON ---
        controlBar.pauseBtn.setOnAction(e -> {
            if (sortingThread != null && sortingThread.isAlive()) {
                sortingThread.interrupt(); // stops current sorting
            }
        });

        Scene scene = new Scene(root, 900, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Algorithm Visualizer");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
