package com.pedrido.model.sort;

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
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
    }
}
