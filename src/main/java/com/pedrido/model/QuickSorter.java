package com.pedrido.model;

/**
 * QuickSort is a divide and conquer algorithm and thus uses recursion.
 * How it works:
 * <ul>
 *     <li> 1) Selects the pivot
 *     <li> 2) Partitions the list into 2 sub-lists
 *     <li> 3) Move the left pointer in the partition until you get an element larger than pivot.
 *              This is so that it can be swapped with a smaller element on the right side.
 *     <li> 4) Move the right pointer until you get an element smaller than pivot
 *     <li> 5) Swap the left and right elements
 *     <li> 6) Left pointer will be the partition index, where steps 1-5 are repeated.
 * </ul>
 *
 * @author Eric Pedrido
 */
public class QuickSorter extends Sorter {

    QuickSorter(String name) {
        super(name);
    }

    @Override
    public void sort(int[] arr) {
        sort(arr, 0, arr.length-1);
    }

    private void sort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }

        int pivot = arr[(left+right)/2];
        int pIndex = partition(arr, left, right, pivot);

        sort(arr, left, pIndex-1);
        sort(arr, pIndex, right);
    }

    private int partition(int[] arr, int left, int right, int pivot) {
        while(left <= right) {
            // Keep moving left pointer until element is bigger than pivot
            while (arr[left] < pivot) {
                left++;
            }

            // Keep moving right pointer until element is smaller than pivot
            while (arr[right] > pivot) {
                right--;
            }

            // Swap the positions of the left and right pointers
            if (left <= right) {
                swap(arr, left, right);
                left++;
                right--;
            }
        }

        // Left will be the partition point
        return left;
    }

    private void swap(int[] arr, int left, int right) {
        int temp = arr[right];
        arr[right] = arr[left];
        arr[left] = temp;
    }
}
