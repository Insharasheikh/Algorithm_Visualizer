package algorithms.sorting;
import ui.CodePane;
import ui.VisualizationPane;


public class BubbleSort implements SortAlgorithm {

    private final String[] codeLines = new String[]{
        "for i = 0 to n-1",
        "  for j = 0 to n-i-1",
        "    if arr[j] > arr[j+1] swap arr[j], arr[j+1]"
    };

    @Override
    public String[] getCode() {
        return codeLines;
    }

    @Override
    public String getTimeComplexity() {
        return "O(n^2)";
    }

    @Override
    public void sort(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed) {
        try {
            for (int i = 0; i < arr.length - 1; i++) {
                codePane.highlightLine(1);
                Thread.sleep(speed);

                for (int j = 0; j < arr.length - i - 1; j++) {
                    codePane.highlightLine(2);
                    Thread.sleep(speed);

                    if (arr[j] > arr[j + 1]) {
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                        vizPane.updateArray(arr);
                    }
                }
            }
        } catch (InterruptedException e) {
            // Thread interrupted = stop sorting
        }
    }
}
