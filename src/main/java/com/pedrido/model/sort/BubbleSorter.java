package com.pedrido.model.sort;

/**
 * BubbleSort is a very simple sorting algorithm which
 * iterates through the array and if element 'n' is
 * greater than 'n + 1', then their positions are swapped.
 *
 * @author Eric Pedrido
 */
public class BubbleSorter extends Sorter {

    BubbleSorter(String name) {
        super(name);
    }

    @Override
    public void sort(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length-1; i++)
            for (int j = 0; j < length-i-1; j++)
                if (arr[j] > arr[j+1]) {
                    controller.swap(j, j+1);
                }
    }
}
