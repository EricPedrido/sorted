package com.pedrido.model;

import java.util.ArrayList;
import java.util.List;

public enum SortType {
    QUICK_SORT(new QuickSorter("Quick Sort"));

    private Sorter sorter;

    SortType(Sorter sorter) {
        this.sorter = sorter;
    }

    public Sorter getSorter() {
        return this.sorter;
    }

    public List<String> getValuesAsString() {
        List<String> list = new ArrayList<>();

        for (SortType type : SortType.values()) {
            list.add(type.sorter.getDisplayableName());
        }

        return list;
    }
}
