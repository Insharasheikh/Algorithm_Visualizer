package app;

import algorithms.Algorithm;
import core.AlgorithmLibrary;
import core.StepController;
import ui.CodePane;
import ui.ControlBar;
import ui.VisualizationPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    private int[] originalArray = {5, 3, 8, 4, 2, 7, 1, 6}; // user can change this
    private Thread algoThread;
    private StepController stepController;

    @Override
    public void start(Stage primaryStage) {

        stepController = new StepController();

        VisualizationPane vizPane  = new VisualizationPane(stepController);
        CodePane          codePane = new CodePane();
        ControlBar        ctrlBar  = new ControlBar();

        Label complexityLabel = new Label("Select an algorithm and press Start");
        complexityLabel.setStyle(
            "-fx-font-size: 13px; -fx-font-weight: bold; " +
            "-fx-text-fill: #cdd6f4; -fx-padding: 8 12 8 12;"
        );

        BorderPane root = new BorderPane();
        root.setCenter(vizPane);
        root.setRight(codePane);
        root.setBottom(ctrlBar);
        root.setTop(complexityLabel);
        root.setStyle("-fx-background-color: #1e1e2e;");

        // Draw initial array
        vizPane.updateArray(originalArray.clone());

        // ── START ────────────────────────────────────────────────────────────
        ctrlBar.startBtn.setOnAction(e -> {
            if (algoThread != null && algoThread.isAlive()) return;

            String    name  = ctrlBar.algorithmSelector.getValue();
            Algorithm algo  = AlgorithmLibrary.get(name);
            if (algo == null) return;

            stepController.resume(); // clear any leftover paused state
            codePane.setCode(algo.getCode());
            complexityLabel.setText("Time Complexity: " + algo.getTimeComplexity());

            int[] arrCopy = originalArray.clone();
            int   speed   = ctrlBar.getSpeedDelay();

            algoThread = new Thread(() ->
                algo.run(arrCopy, vizPane, codePane, speed, stepController)
            );
            algoThread.setDaemon(true); // stops with app close
            algoThread.start();

            ctrlBar.startBtn.setDisable(true);
            ctrlBar.pauseBtn.setDisable(false);
            ctrlBar.resumeBtn.setDisable(true);
        });

        // ── PAUSE ────────────────────────────────────────────────────────────
        ctrlBar.pauseBtn.setOnAction(e -> {
            stepController.pause();
            ctrlBar.pauseBtn.setDisable(true);
            ctrlBar.resumeBtn.setDisable(false);
        });

        // ── RESUME ───────────────────────────────────────────────────────────
        ctrlBar.resumeBtn.setOnAction(e -> {
            stepController.resume();
            ctrlBar.pauseBtn.setDisable(false);
            ctrlBar.resumeBtn.setDisable(true);
        });

        // ── RESET ────────────────────────────────────────────────────────────
        ctrlBar.resetBtn.setOnAction(e -> {
            // Stop running thread
            if (algoThread != null && algoThread.isAlive()) {
                stepController.resume(); // unblock if paused so interrupt is received
                algoThread.interrupt();
            }
            stepController.resume();

            vizPane.updateArray(originalArray.clone());
            codePane.setCode(new String[]{});
            complexityLabel.setText("Select an algorithm and press Start");

            ctrlBar.startBtn.setDisable(false);
            ctrlBar.pauseBtn.setDisable(false);
            ctrlBar.resumeBtn.setDisable(true);
        });

        // ── SET ARRAY ─────────────────────────────────────────────────────────
        ctrlBar.setArrayBtn.setOnAction(e -> {
            String input = ctrlBar.arrayInputField.getText().trim();
            if (input.isEmpty()) {
                ctrlBar.arrayErrorLabel.setText("⚠ Please enter some numbers.");
                return;
            }
            try {
                String[] parts = input.split("[,\\s]+"); // split by comma or spaces
                if (parts.length < 2) {
                    ctrlBar.arrayErrorLabel.setText("⚠ Enter at least 2 numbers.");
                    return;
                }
                if (parts.length > 20) {
                    ctrlBar.arrayErrorLabel.setText("⚠ Max 20 numbers allowed.");
                    return;
                }
                int[] newArray = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    int val = Integer.parseInt(parts[i].trim());
                    if (val < 1 || val > 99) {
                        ctrlBar.arrayErrorLabel.setText("⚠ Values must be between 1 and 99.");
                        return;
                    }
                    newArray[i] = val;
                }
                // Stop any running algorithm first
                if (algoThread != null && algoThread.isAlive()) {
                    stepController.resume();
                    algoThread.interrupt();
                }
                originalArray = newArray;
                vizPane.updateArray(originalArray.clone());
                codePane.setCode(new String[]{});
                complexityLabel.setText("Custom array set! Select an algorithm and press Start.");
                ctrlBar.arrayErrorLabel.setText("✔ Array updated!");
                ctrlBar.arrayErrorLabel.setStyle("-fx-text-fill: #a6e3a1; -fx-font-size: 11px;");
                ctrlBar.startBtn.setDisable(false);
                ctrlBar.resumeBtn.setDisable(true);

            } catch (NumberFormatException ex) {
                ctrlBar.arrayErrorLabel.setText("⚠ Only whole numbers allowed.");
                ctrlBar.arrayErrorLabel.setStyle("-fx-text-fill: #f38ba8; -fx-font-size: 11px;");
            }
        });

        // Clear error label when user starts typing again
        ctrlBar.arrayInputField.textProperty().addListener((obs, oldVal, newVal) -> {
            ctrlBar.arrayErrorLabel.setText("");
            ctrlBar.arrayErrorLabel.setStyle("-fx-text-fill: #f38ba8; -fx-font-size: 11px;");
        });
        algoThreadWatcher(ctrlBar);

        Scene scene = new Scene(root, 960, 540);
        scene.getStylesheets(); // placeholder for external CSS if needed
        primaryStage.setScene(scene);
        primaryStage.setTitle("Algorithm Visualizer");
        primaryStage.show();
    }

    /** Poll the thread state and re-enable Start when it finishes. */
    private void algoThreadWatcher(ControlBar ctrlBar) {
        Thread watcher = new Thread(() -> {
            while (true) {
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                if (algoThread != null && !algoThread.isAlive()) {
                    javafx.application.Platform.runLater(() -> {
                        ctrlBar.startBtn.setDisable(false);
                        ctrlBar.pauseBtn.setDisable(false);
                        ctrlBar.resumeBtn.setDisable(true);
                    });
                }
            }
        });
        watcher.setDaemon(true);
        watcher.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}