package algorithms.searching;

import algorithms.sorting.SortAlgorithm;
import ui.CodePane;
import ui.VisualizationPane;

import java.util.Arrays;

public class BinarySearch implements SortAlgorithm {

    @Override
    public String[] getCode() {
        return new String[]{
                "low = 0, high = n-1",
                "while low <= high",
                "   mid = (low + high) / 2",
                "   if arr[mid] == target return mid",
                "   else if arr[mid] < target low = mid+1",
                "   else high = mid-1"
        };
    }

    @Override
    public String getTimeComplexity() {
        return "O(log n)";
    }

    @Override
    public void sort(int[] arr, VisualizationPane vizPane, CodePane codePane, int speed) {

        Arrays.sort(arr); // binary search needs sorted array
        vizPane.updateArray(arr);

        int target = arr[arr.length - 1];

        int low = 0;
        int high = arr.length - 1;

        codePane.highlightLine(1);
        sleep(speed);

        while (low <= high) {

            codePane.highlightLine(2);

            int mid = (low + high) / 2;

            codePane.highlightLine(3);
            vizPane.updateArray(arr);
            sleep(speed);

            if (arr[mid] == target) {

                codePane.highlightLine(4);
                sleep(speed);
                return;

            } else if (arr[mid] < target) {

                codePane.highlightLine(5);
                low = mid + 1;

            } else {

                codePane.highlightLine(6);
                high = mid - 1;
            }

            sleep(speed);
        }
    }

    private void sleep(int speed){
        try{
            Thread.sleep(speed);
        }catch(Exception e){
            return;
        }
    }
}