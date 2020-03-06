package com.pedrido.model.sort;

import java.util.ArrayList;
import java.util.List;

public enum SortType {
    QUICK_SORT(new QuickSorter("Quick Sort")),
    BUBBLE_SORT(new BubbleSorter("Bubble Sort"));

    private Sorter sorter;

    SortType(Sorter sorter) {
        this.sorter = sorter;
    }

    public Sorter getSorter() {
        return this.sorter;
    }

    public static List<String> getValuesAsString() {
        List<String> list = new ArrayList<>();

        for (SortType type : SortType.values()) {
            list.add(type.sorter.getDisplayableName());
        }

        return list;
    }

    public static Sorter getSorterWithName(String displayableName) {
        for (SortType s : values()) {
            Sorter sorter = s.sorter;
            if (sorter.getDisplayableName().equals(displayableName)) {
                return sorter;
            }
        }

        // This line should never be reached since the param entered only refers to existing
        // sorters
        return null;
    }
}
