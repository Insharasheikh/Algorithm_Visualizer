package ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

public class CodePane extends VBox {

    private Label[] codeLines; // array of labels for each line

    public CodePane() {
        this.setStyle("-fx-background-color: #bdc3c7; -fx-padding: 10;");
        this.setSpacing(5);
    }

    /**
     * Set the code to display in the pane
     * @param code - array of strings, one per line
     */
    public void setCode(String[] code) {
        this.getChildren().clear(); // remove previous code lines
        codeLines = new Label[code.length];

        for (int i = 0; i < code.length; i++) {
            codeLines[i] = new Label((i + 1) + ": " + code[i]);
            this.getChildren().add(codeLines[i]);
        }
    }

    /**
     * Highlight a specific line number (thread-safe)
     * @param lineNumber - line number to highlight (1-indexed)
     */
    public void highlightLine(int lineNumber) {
        Platform.runLater(() -> {
            // Clear previous highlights
            if (codeLines != null) {
                for (Label line : codeLines) {
                    line.setStyle(""); // remove background
                }

                // Highlight the current line
                if (lineNumber - 1 >= 0 && lineNumber - 1 < codeLines.length) {
                    codeLines[lineNumber - 1].setStyle("-fx-background-color: yellow;");
                }
            }
        });
    }
}
