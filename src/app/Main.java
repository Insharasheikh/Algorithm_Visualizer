package app;

import algorithms.Algorithm;
import algorithms.graph.*;
import algorithms.searching.BinarySearch;
import algorithms.searching.LinearSearch;
import core.AlgorithmLibrary;
import core.StepController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.CodePane;
import ui.ControlBar;
import ui.GraphPane;
import ui.VisualizationPane;

import java.util.List;

public class Main extends Application {

    private int[] originalArray = {5, 3, 8, 4, 2, 7, 1, 6};
    private Thread         algoThread;
    private StepController stepController;

    private VisualizationPane vizPane;
    private GraphPane         graphPane;
    private BorderPane        root;

    @Override
    public void start(Stage primaryStage) {

        stepController = new StepController();
        vizPane        = new VisualizationPane(stepController);
        graphPane      = new GraphPane();
        CodePane   codePane = new CodePane();
        ControlBar ctrlBar  = new ControlBar();

        // ── Top bar ───────────────────────────────────────────────────────────
        Label complexityLabel = new Label("Select a category and algorithm, then press Start");
        complexityLabel.setStyle(
            "-fx-font-size: 13px; -fx-font-weight: bold; " +
            "-fx-text-fill: #cdd6f4; -fx-padding: 6 12 2 12;");

        Label resultLabel = new Label("");
        resultLabel.setStyle(
            "-fx-font-size: 13px; -fx-font-weight: bold; " +
            "-fx-text-fill: #a6e3a1; -fx-padding: 2 12 6 12;");

        VBox topBox = new VBox(complexityLabel, resultLabel);
        topBox.setStyle("-fx-background-color: #1e1e2e;");

        root = new BorderPane();
        root.setCenter(vizPane);
        root.setRight(codePane);
        root.setBottom(ctrlBar);
        root.setTop(topBox);
        root.setStyle("-fx-background-color: #1e1e2e;");

        vizPane.updateArray(originalArray.clone());

        // ── Category switch ───────────────────────────────────────────────────
        ctrlBar.categorySelector.setOnAction(e -> {
            stopCurrentThread();
            resultLabel.setText("");
            if (ctrlBar.isGraphMode()) {
                root.setCenter(graphPane);
                graphPane.resetVisualization();
                ctrlBar.refreshNodeSelectors(graphPane.getNodes());
            } else {
                root.setCenter(vizPane);
                vizPane.updateArray(originalArray.clone());
            }
            codePane.setCode(new String[]{});
            complexityLabel.setText("Select an algorithm and press Start");
            ctrlBar.startBtn.setDisable(false);
            ctrlBar.resumeBtn.setDisable(true);
        });

        // Directed checkbox
        ctrlBar.directedCheckBox.setOnAction(e ->
            graphPane.setDirected(ctrlBar.directedCheckBox.isSelected()));

        // Clear graph
        ctrlBar.clearGraphBtn.setOnAction(e -> {
            stopCurrentThread();
            graphPane.clearGraph();
            ctrlBar.refreshNodeSelectors(graphPane.getNodes());
            codePane.setCode(new String[]{});
            resultLabel.setText("");
            complexityLabel.setText("Graph cleared. Draw nodes and press Start.");
            ctrlBar.startBtn.setDisable(false);
            ctrlBar.resumeBtn.setDisable(true);
        });

        // ── Refresh src/dest selectors whenever a node is added/removed ───────
        // We hook into GraphPane's node list changes by refreshing on mouse click
        graphPane.setOnMouseClicked(e -> {
            if (ctrlBar.isGraphMode()) {
                Platform.runLater(() -> ctrlBar.refreshNodeSelectors(graphPane.getNodes()));
            }
        });

        // ── START ─────────────────────────────────────────────────────────────
        ctrlBar.startBtn.setOnAction(e -> {
            if (algoThread != null && algoThread.isAlive()) return;

            String algoName = ctrlBar.algorithmSelector.getValue();
            if (algoName == null) return;

            stepController.resume();
            int speed = ctrlBar.getSpeedDelay();
            resultLabel.setText("");

            if (ctrlBar.isGraphMode()) {
                // ── Graph mode ────────────────────────────────────────────────
                GraphAlgorithm algo = AlgorithmLibrary.getGraph(algoName);
                if (algo == null) return;

                List<GraphNode> nodes = graphPane.getNodes();
                if (nodes.isEmpty()) {
                    complexityLabel.setText("\u26A0 Draw some nodes on the graph first!");
                    return;
                }

                // Resolve src node
                String srcLabel  = ctrlBar.getSelectedSrc();
                String destLabel = ctrlBar.getSelectedDest(); // may be null

                GraphNode startNode = resolveNode(nodes, srcLabel);
                if (startNode == null) startNode = nodes.get(0); // fallback

                GraphNode endNode = (destLabel != null) ? resolveNode(nodes, destLabel) : null;

                // TopologicalSort ignores src/dest
                if (algoName.equals("TopologicalSort")) {
                    endNode = null;
                }

                // Mark src/dest visually on the canvas
                graphPane.setSrcDest(startNode, endNode);
                graphPane.resetVisualization();
                codePane.setCode(algo.getCode());
                complexityLabel.setText("Time Complexity: " + algo.getTimeComplexity()
                    + (endNode != null ? "   |   " + startNode.label + " \u2192 " + endNode.label : "   |   Start: " + startNode.label));

                // Get LIVE edges (captures any weight edits done before Start)
                List<GraphEdge> liveEdges = graphPane.getEdges();

                final GraphNode finalStart = startNode;
                final GraphNode finalEnd   = endNode;

                algoThread = new Thread(() -> {
                    if (algo instanceof BFS bfs) {
                        bfs.run(nodes, liveEdges, finalStart, finalEnd,
                                graphPane, codePane, speed, stepController, resultLabel);
                    } else if (algo instanceof DFS dfs) {
                        dfs.run(nodes, liveEdges, finalStart, finalEnd,
                                graphPane, codePane, speed, stepController, resultLabel);
                    } else if (algo instanceof Dijkstra dijk) {
                        dijk.run(nodes, liveEdges, finalStart, finalEnd,
                                 graphPane, codePane, speed, stepController, resultLabel);
                    } else if (algo instanceof TopologicalSort topo) {
                        topo.run(nodes, liveEdges, finalStart, finalEnd,
                                 graphPane, codePane, speed, stepController, resultLabel);
                    }
                });

            } else if (ctrlBar.isSearchingMode()) {
                // ── Searching mode ────────────────────────────────────────────
                int target = ctrlBar.getTarget();
                if (target == Integer.MIN_VALUE) return;

                Algorithm algo = AlgorithmLibrary.get(algoName);
                if (algo == null) return;

                codePane.setCode(algo.getCode());
                complexityLabel.setText("Time Complexity: " + algo.getTimeComplexity()
                    + "   |   Searching for: " + target);

                int[] arrCopy = originalArray.clone();
                algoThread = new Thread(() -> {
                    if (algo instanceof LinearSearch ls)
                        ls.run(arrCopy, vizPane, codePane, speed, stepController, target, resultLabel);
                    else if (algo instanceof BinarySearch bs)
                        bs.run(arrCopy, vizPane, codePane, speed, stepController, target, resultLabel);
                });

            } else {
                // ── Sorting mode ──────────────────────────────────────────────
                Algorithm algo = AlgorithmLibrary.get(algoName);
                if (algo == null) return;
                codePane.setCode(algo.getCode());
                complexityLabel.setText("Time Complexity: " + algo.getTimeComplexity());
                int[] arrCopy = originalArray.clone();
                algoThread = new Thread(() ->
                    algo.run(arrCopy, vizPane, codePane, speed, stepController));
            }

            algoThread.setDaemon(true);
            algoThread.start();
            ctrlBar.startBtn.setDisable(true);
            ctrlBar.pauseBtn.setDisable(false);
            ctrlBar.resumeBtn.setDisable(true);
        });

        // ── PAUSE ─────────────────────────────────────────────────────────────
        ctrlBar.pauseBtn.setOnAction(e -> {
            stepController.pause();
            ctrlBar.pauseBtn.setDisable(true);
            ctrlBar.resumeBtn.setDisable(false);
        });

        // ── RESUME ────────────────────────────────────────────────────────────
        ctrlBar.resumeBtn.setOnAction(e -> {
            stepController.resume();
            ctrlBar.pauseBtn.setDisable(false);
            ctrlBar.resumeBtn.setDisable(true);
        });

        // ── RESET ─────────────────────────────────────────────────────────────
        ctrlBar.resetBtn.setOnAction(e -> {
            stopCurrentThread();
            resultLabel.setText("");
            if (ctrlBar.isGraphMode()) {
                graphPane.resetVisualization();
                graphPane.setSrcDest(null, null);
            } else {
                vizPane.updateArray(originalArray.clone());
            }
            codePane.setCode(new String[]{});
            complexityLabel.setText("Select an algorithm and press Start");
            ctrlBar.startBtn.setDisable(false);
            ctrlBar.pauseBtn.setDisable(false);
            ctrlBar.resumeBtn.setDisable(true);
        });

        // ── SET ARRAY ─────────────────────────────────────────────────────────
        ctrlBar.setArrayBtn.setOnAction(e -> {
            String input = ctrlBar.arrayInputField.getText().trim();
            if (input.isEmpty()) { ctrlBar.arrayErrorLabel.setText("\u26A0 Please enter some numbers."); return; }
            try {
                String[] parts = input.split("[,\\s]+");
                if (parts.length < 2)  { ctrlBar.arrayErrorLabel.setText("\u26A0 At least 2 numbers."); return; }
                if (parts.length > 20) { ctrlBar.arrayErrorLabel.setText("\u26A0 Max 20 numbers."); return; }
                int[] newArray = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    int val = Integer.parseInt(parts[i].trim());
                    if (val < 1 || val > 99) { ctrlBar.arrayErrorLabel.setText("\u26A0 Values 1\u201399 only."); return; }
                    newArray[i] = val;
                }
                stopCurrentThread();
                originalArray = newArray;
                resultLabel.setText("");
                vizPane.updateArray(originalArray.clone());
                codePane.setCode(new String[]{});
                complexityLabel.setText("Custom array set! Press Start.");
                ctrlBar.arrayErrorLabel.setText("\u2714 Array updated!");
                ctrlBar.arrayErrorLabel.setStyle("-fx-text-fill: #a6e3a1; -fx-font-size: 11px;");
                ctrlBar.startBtn.setDisable(false);
                ctrlBar.resumeBtn.setDisable(true);
            } catch (NumberFormatException ex) {
                ctrlBar.arrayErrorLabel.setText("\u26A0 Only whole numbers allowed.");
                ctrlBar.arrayErrorLabel.setStyle("-fx-text-fill: #f38ba8; -fx-font-size: 11px;");
            }
        });

        ctrlBar.arrayInputField.textProperty().addListener((obs, o, n) -> {
            ctrlBar.arrayErrorLabel.setText("");
            ctrlBar.arrayErrorLabel.setStyle("-fx-text-fill: #f38ba8; -fx-font-size: 11px;");
        });

        algoThreadWatcher(ctrlBar);

        Scene scene = new Scene(root, 1100, 580);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Algorithm Visualizer");
        primaryStage.show();
    }

    /** Find node by label string */
    private GraphNode resolveNode(List<GraphNode> nodes, String label) {
        if (label == null) return null;
        for (GraphNode n : nodes) if (n.label.equals(label)) return n;
        return null;
    }

    private void stopCurrentThread() {
        if (algoThread != null && algoThread.isAlive()) {
            stepController.resume();
            algoThread.interrupt();
            try { algoThread.join(400); } catch (InterruptedException ignored) {}
        }
        stepController.resume();
    }

    private void algoThreadWatcher(ControlBar ctrlBar) {
        Thread watcher = new Thread(() -> {
            while (true) {
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                if (algoThread != null && !algoThread.isAlive()) {
                    Platform.runLater(() -> {
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

    public static void main(String[] args) { launch(args); }
}