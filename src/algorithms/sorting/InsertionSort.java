package algorithms.sorting;
import ui.CodePane;
import ui.VisualizationPane;


public class InsertionSort implements SortAlgorithm {

    private final String[] codeLines = new String[]{
        "for i = 1 to n",
        "  key = arr[i]",
        "  j = i-1",
        "  while j>=0 and arr[j]>key",
        "    arr[j+1] = arr[j]",
        "    j--",
        "  arr[j+1] = key"
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
            for (int i = 1; i < arr.length; i++) {
                codePane.highlightLine(1);
                Thread.sleep(speed);

                int key = arr[i];
                int j = i - 1;
                codePane.highlightLine(2);
                Thread.sleep(speed);

                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j--;
                    vizPane.updateArray(arr);
                    Thread.sleep(speed);
                }
                arr[j + 1] = key;
                vizPane.updateArray(arr);
            }
        } catch (InterruptedException e) {
            // Thread interrupted = stop sorting
        }
    }
}
