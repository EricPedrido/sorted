package com.pedrido.model.sort;

/**
 * InsertionSort works by setting a marker indicating
 * which section is sorted. Here, that marker is the variable 'marker'.
 * Then, it takes the first element after the marker, 'toInsert',
 * and holds it while iterating through the sorted section (before the marker)
 * and makes space by moving elements greater than 'toInsert' one space to the right
 * until 'toInsert' is in the correct position. This repeats until the entire array is behind
 * the marker.
 *
 * @author Eric Pedrido
 */
public class InsertionSorter extends Sorter {

    InsertionSorter(String name) {
        super(name);
    }

    @Override
    public void sort(int[] arr) {
            int toInsert;

            for (int marker = 1; marker < arr.length; marker++) {
                // Select the first unsorted element
                toInsert = arr[marker];
                int i = marker - 1;

                // Shift all elements to the right to make space for element to insert to correct position
                while (i >= 0 && arr[i] > toInsert) {
                    controller.place(arr[i], i + 1);
                    i = i - 1;
                }

                // Insert the element into the correct position into the array
                controller.place(toInsert, i + 1);
            }
    }
}
