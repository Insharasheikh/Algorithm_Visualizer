package ui;

import algorithms.graph.GraphNode;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;

public class ControlBar extends HBox {

    public Button startBtn, pauseBtn, resumeBtn, resetBtn;
    public Slider speedSlider;
    public Label  speedValueLabel;

    public ComboBox<String> categorySelector;
    public ComboBox<String> algorithmSelector;

    // Array input
    public TextField arrayInputField;
    public Button    setArrayBtn;
    public Label     arrayErrorLabel;
    public VBox      arrayBox;

    // Target input — Searching only
    public TextField targetInputField;
    public Label     targetErrorLabel;
    public VBox      targetBox;

    // Graph controls
    public Button           clearGraphBtn;
    public CheckBox         directedCheckBox;
    public ComboBox<String> srcSelector;
    public ComboBox<String> destSelector;
    public HBox             srcDestBox;      // shown/hidden per algorithm
    public HBox             graphControlBox;

    public ControlBar() {
        startBtn  = new Button("\u25B6  Start");
        pauseBtn  = new Button("\u23F8  Pause");
        resumeBtn = new Button("\u25B6  Resume");
        resetBtn  = new Button("\u21BA  Reset");

        styleButton(startBtn,  "#a6e3a1");
        styleButton(pauseBtn,  "#f9e2af");
        styleButton(resumeBtn, "#89b4fa");
        styleButton(resetBtn,  "#f38ba8");
        resumeBtn.setDisable(true);

        // ── Speed ─────────────────────────────────────────────────────────────
        speedSlider = new Slider(1, 10, 5);
        speedSlider.setMajorTickUnit(1); speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true); speedSlider.setSnapToTicks(true);
        speedSlider.setPrefWidth(140);

        speedValueLabel = new Label("Speed: 5");
        speedValueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        speedSlider.valueProperty().addListener((obs, o, n) ->
            speedValueLabel.setText("Speed: " + n.intValue()));

        Label slowLbl = new Label("Slow \u25C2");
        Label fastLbl = new Label("\u25B8 Fast");
        slowLbl.setStyle("-fx-text-fill: #a6adc8; -fx-font-size: 11px;");
        fastLbl.setStyle("-fx-text-fill: #a6adc8; -fx-font-size: 11px;");

        VBox speedBox = new VBox(2, speedValueLabel, new HBox(4, slowLbl, speedSlider, fastLbl));
        speedBox.setStyle("-fx-padding: 0 4 0 4;");

        // ── Dropdowns ─────────────────────────────────────────────────────────
        categorySelector = new ComboBox<>();
        categorySelector.getItems().addAll("Sorting", "Searching", "Graph");
        categorySelector.getSelectionModel().selectFirst();
        styleCombo(categorySelector, "120px");

        algorithmSelector = new ComboBox<>();
        styleCombo(algorithmSelector, "160px");
        refreshAlgorithmList();

        // ── Array input ───────────────────────────────────────────────────────
        arrayInputField = new TextField();
        arrayInputField.setPromptText("e.g. 5, 3, 8, 4, 2");
        arrayInputField.setPrefWidth(160);
        arrayInputField.setStyle(
            "-fx-background-color: #45475a; -fx-text-fill: white; " +
            "-fx-prompt-text-fill: #6c7086; -fx-font-size: 12px; " +
            "-fx-background-radius: 6; -fx-padding: 6 8 6 8;");

        setArrayBtn = new Button("Set");
        styleButton(setArrayBtn, "#cba6f7");

        arrayErrorLabel = new Label("");
        arrayErrorLabel.setStyle("-fx-text-fill: #f38ba8; -fx-font-size: 11px;");

        Label arrLbl = new Label("Array:");
        arrLbl.setStyle("-fx-text-fill: #a6adc8; -fx-font-size: 12px;");

        arrayBox = new VBox(3, arrLbl, new HBox(6, arrayInputField, setArrayBtn), arrayErrorLabel);
        arrayBox.setStyle("-fx-padding: 0 8 0 0;");

        // ── Target input ──────────────────────────────────────────────────────
        targetInputField = new TextField();
        targetInputField.setPromptText("e.g. 7");
        targetInputField.setPrefWidth(80);
        targetInputField.setStyle(
            "-fx-background-color: #45475a; -fx-text-fill: white; " +
            "-fx-prompt-text-fill: #6c7086; -fx-font-size: 12px; " +
            "-fx-background-radius: 6; -fx-padding: 6 8 6 8;");

        targetErrorLabel = new Label("");
        targetErrorLabel.setStyle("-fx-text-fill: #f38ba8; -fx-font-size: 11px;");

        Label tgtLbl = new Label("Target:");
        tgtLbl.setStyle("-fx-text-fill: #f9e2af; -fx-font-size: 12px; -fx-font-weight: bold;");

        targetBox = new VBox(3, tgtLbl, targetInputField, targetErrorLabel);
        targetBox.setStyle("-fx-padding: 0 8 0 0;");
        targetBox.setVisible(false); targetBox.setManaged(false);
        targetInputField.textProperty().addListener((obs, o, n) -> targetErrorLabel.setText(""));

        // ── Graph controls ────────────────────────────────────────────────────
        clearGraphBtn    = new Button("Clear");
        directedCheckBox = new CheckBox("Directed");
        directedCheckBox.setStyle("-fx-text-fill: #cdd6f4; -fx-font-size: 12px;");
        styleButton(clearGraphBtn, "#f38ba8");

        Label srcLbl = new Label("Src:");
        srcLbl.setStyle("-fx-text-fill: #f9e2af; -fx-font-size: 12px; -fx-font-weight: bold;");
        srcSelector = new ComboBox<>();
        srcSelector.setPromptText("Start node");
        styleCombo(srcSelector, "90px");

        Label destLbl = new Label("Dest:");
        destLbl.setStyle("-fx-text-fill: #f38ba8; -fx-font-size: 12px; -fx-font-weight: bold;");
        destSelector = new ComboBox<>();
        destSelector.setPromptText("(optional)");
        styleCombo(destSelector, "100px");

        Label graphHintLbl = new Label("Click: node  |  2 nodes: edge  |  Click edge: weight  |  Right-click: delete");
        graphHintLbl.setStyle("-fx-text-fill: #6c7086; -fx-font-size: 11px;");

        // Src/dest in their own box so we can hide them for TopologicalSort
        srcDestBox = new HBox(6, srcLbl, srcSelector, destLbl, destSelector);
        srcDestBox.setStyle("-fx-alignment: center-left;");

        graphControlBox = new HBox(8,
            clearGraphBtn, directedCheckBox,
            srcDestBox,
            graphHintLbl);
        graphControlBox.setStyle("-fx-alignment: center-left; -fx-padding: 0 8 0 0;");
        graphControlBox.setVisible(false); graphControlBox.setManaged(false);

        // ── Layout ────────────────────────────────────────────────────────────
        this.getChildren().addAll(
            categorySelector, algorithmSelector,
            startBtn, pauseBtn, resumeBtn, resetBtn,
            speedBox, arrayBox, targetBox, graphControlBox);
        this.setSpacing(10);
        this.setStyle("-fx-padding: 10 14 10 14; -fx-background-color: #181825; -fx-alignment: center-left;");

        categorySelector.valueProperty().addListener((obs, o, n) -> {
            refreshAlgorithmList();
            switchMode(n);
        });

        // Hide src/dest when TopologicalSort is selected
        algorithmSelector.valueProperty().addListener((obs, o, n) -> {
            if ("Graph".equals(categorySelector.getValue())) {
                boolean needsSrcDest = !"TopologicalSort".equals(n);
                srcDestBox.setVisible(needsSrcDest);
                srcDestBox.setManaged(needsSrcDest);
            }
        });
    }

    // ── Public helpers ────────────────────────────────────────────────────────

    public int getTarget() {
        String raw = targetInputField.getText().trim();
        if (raw.isEmpty()) { targetErrorLabel.setText("\u26A0 Enter a target number."); return Integer.MIN_VALUE; }
        try { int v = Integer.parseInt(raw); targetErrorLabel.setText(""); return v; }
        catch (NumberFormatException e) { targetErrorLabel.setText("\u26A0 Whole numbers only."); return Integer.MIN_VALUE; }
    }

    public int getSpeedDelay() { return 1050 - ((int) speedSlider.getValue() * 100); }
    public boolean isGraphMode()     { return "Graph".equals(categorySelector.getValue()); }
    public boolean isSearchingMode() { return "Searching".equals(categorySelector.getValue()); }

    /** Call this after any node is added or removed to keep src/dest lists fresh */
    public void refreshNodeSelectors(List<GraphNode> nodes) {
        String prevSrc  = srcSelector.getValue();
        String prevDest = destSelector.getValue();

        srcSelector.getItems().clear();
        destSelector.getItems().clear();
        destSelector.getItems().add("(none)");

        for (GraphNode n : nodes) {
            srcSelector.getItems().add(n.label);
            destSelector.getItems().add(n.label);
        }

        if (prevSrc  != null && srcSelector.getItems().contains(prevSrc))   srcSelector.setValue(prevSrc);
        else if (!srcSelector.getItems().isEmpty()) srcSelector.getSelectionModel().selectFirst();

        if (prevDest != null && destSelector.getItems().contains(prevDest)) destSelector.setValue(prevDest);
        else destSelector.setValue("(none)");
    }

    public String getSelectedSrc()  { return srcSelector.getValue(); }
    public String getSelectedDest() {
        String v = destSelector.getValue();
        return (v == null || v.equals("(none)")) ? null : v;
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private void refreshAlgorithmList() {
        algorithmSelector.getItems().clear();
        String cat = categorySelector.getValue();
        if (cat == null) return;
        switch (cat) {
            case "Sorting"   -> algorithmSelector.getItems().addAll("BubbleSort","InsertionSort","SelectionSort","MergeSort","QuickSort");
            case "Searching" -> algorithmSelector.getItems().addAll("LinearSearch","BinarySearch");
            case "Graph"     -> algorithmSelector.getItems().addAll("BFS","DFS","Dijkstra","TopologicalSort");
        }
        algorithmSelector.getSelectionModel().selectFirst();
    }

    private void switchMode(String category) {
        if (category == null) return;
        boolean isGraph     = category.equals("Graph");
        boolean isSearching = category.equals("Searching");
        arrayBox.setVisible(!isGraph);       arrayBox.setManaged(!isGraph);
        targetBox.setVisible(isSearching);   targetBox.setManaged(isSearching);
        graphControlBox.setVisible(isGraph); graphControlBox.setManaged(isGraph);
        if (!isSearching) { targetInputField.clear(); targetErrorLabel.setText(""); }

        // When entering Graph mode, set srcDestBox visibility based on current algo
        if (isGraph) {
            boolean needsSrcDest = !"TopologicalSort".equals(algorithmSelector.getValue());
            srcDestBox.setVisible(needsSrcDest);
            srcDestBox.setManaged(needsSrcDest);
        }
    }

    private void styleButton(Button btn, String color) {
        btn.setStyle("-fx-background-color: " + color + "; -fx-font-size: 12px; " +
            "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 12 6 12;");
    }

    private void styleCombo(ComboBox<String> cb, String width) {
        cb.setStyle("-fx-background-color: #45475a; -fx-text-fill: white; " +
            "-fx-font-size: 12px; -fx-pref-width: " + width + ";");
    }
}