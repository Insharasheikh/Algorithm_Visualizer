package ui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Displays pseudocode with line highlighting.
 * highlightLine() uses 0-based index to match algorithm arrays.
 */
public class CodePane extends VBox {

    private Label[] codeLines;

    private static final String DEFAULT_STYLE =
        "-fx-font-family: 'Courier New'; -fx-font-size: 13px; " +
        "-fx-text-fill: #cdd6f4; -fx-padding: 2 6 2 6;";

    private static final String HIGHLIGHT_STYLE =
        "-fx-font-family: 'Courier New'; -fx-font-size: 13px; " +
        "-fx-text-fill: #1e1e2e; -fx-background-color: #f9e2af; " +
        "-fx-padding: 2 6 2 6; -fx-background-radius: 3;";

    public CodePane() {
        this.setPrefWidth(280);
        this.setSpacing(4);
        this.setStyle(
            "-fx-background-color: #313244; -fx-padding: 12; " +
            "-fx-background-radius: 8;"
        );

        Label title = new Label("Pseudocode");
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #cba6f7; -fx-font-size: 13px; -fx-padding: 0 0 6 0;");
        this.getChildren().add(title);
    }

    /**
     * Load new code into the pane. Clears previous code.
     * @param code array of pseudocode lines
     */
    public void setCode(String[] code) {
        Platform.runLater(() -> {
            // Remove old code lines (keep title label at index 0)
            if (this.getChildren().size() > 1)
                this.getChildren().remove(1, this.getChildren().size());

            codeLines = new Label[code.length];
            for (int i = 0; i < code.length; i++) {
                codeLines[i] = new Label((i + 1) + "  " + code[i]);
                codeLines[i].setStyle(DEFAULT_STYLE);
                codeLines[i].setWrapText(true);
                this.getChildren().add(codeLines[i]);
            }
        });
    }

    /**
     * Highlight a line by 0-based index.
     * All other lines revert to default style.
     * @param lineIndex 0-based line index
     */
    public void highlightLine(int lineIndex) {
        Platform.runLater(() -> {
            if (codeLines == null) return;
            for (int i = 0; i < codeLines.length; i++) {
                codeLines[i].setStyle(i == lineIndex ? HIGHLIGHT_STYLE : DEFAULT_STYLE);
            }
        });
    }
}