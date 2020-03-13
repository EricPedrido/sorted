package com.pedrido.model.sort;

public class InsertionSorter extends Sorter {

    InsertionSorter(String name) {
        super(name);
    }

    @Override
    public void sort(int[] arr) {
            int toInsert;

            for (int i = 1; i < arr.length; i++) {
                // Select the first unsorted element
                toInsert = arr[i];
                int j = i - 1;

                // Shift all elements to the right to make space for element to insert to correct position
                while (j >= 0 && arr[j] > toInsert) {
                    controller.place(arr[j], j + 1);
                    j = j - 1;
                }

                // Insert the element into the correct position into the array
                controller.place(toInsert, j + 1);
            }
    }
}
