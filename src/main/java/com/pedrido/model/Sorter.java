package com.pedrido.model;

/**
 * Represents all classes which sort an array of numbers using
 * various different algorithms. This class itself does not sort
 * anything, but allows for any array to be sorted using
 * a variety of different algorithms (that extends this class)
 * selected by the user in runtime.
 *
 * @author Eric Pedrido
 */
public abstract class Sorter {
    private String displayableName;

    Sorter(String name) {
        this.displayableName = name;
    }

    public Sorter getSorterOfType(SortType type) {
        return type.getSorter();
    }

    public String getDisplayableName() {
        return this.displayableName;
    }

    public abstract int[] sort();
}
