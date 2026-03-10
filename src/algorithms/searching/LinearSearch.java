package algorithms.searching;

import algorithms.sorting.SortAlgorithm;
import ui.CodePane;
import ui.VisualizationPane;

public class LinearSearch implements SortAlgorithm {

    @Override
    public String[] getCode() {
        return new String[]{
                "for i = 0 to n-1",
                "   if arr[i] == target",
                "       return i",
                "return -1"
        };
    }

    @Override
    public String getTimeComplexity() {
        return "O(n)";
    }

    @Override
    public void sort(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed) {

        int target = arr[arr.length - 1]; // last element for demo

        for (int i = 0; i < arr.length; i++) {

            codePane.highlightLine(1); // loop running
            vizPane.updateArray(arr);

            sleep(speed);

            codePane.highlightLine(2); // comparison

            if (arr[i] == target) {
                codePane.highlightLine(3);
                vizPane.updateArray(arr);
                sleep(speed);
                return;
            }

            sleep(speed);
        }

        codePane.highlightLine(4);
    }

    private void sleep(int speed){
        try{
            Thread.sleep(speed);
        }catch(Exception e){
            return;
        }
    }
}