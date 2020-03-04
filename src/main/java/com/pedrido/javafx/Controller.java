package com.pedrido.javafx;

import com.pedrido.model.SortType;
import com.pedrido.model.Sorter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public Label graphLabel, timerLabel;
    @FXML
    public ComboBox<String> sortTypeComboBox;
    @FXML
    public Button startButton, pseudocodeButton, randomizeButton;
    @FXML
    public Spinner<Integer> sizeSpinner;
    @FXML
    public Pane mainPane;

    private final int MIN_SIZE = 100;
    private final int MAX_SIZE = 10000;
    private final int STEP_SIZE = 50;

    private Sorter sorter;
    private int[] nums;
    private int arrSize;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graphLabel.setText("");
        arrSize = MIN_SIZE;
        setArrSize(MIN_SIZE);

        // Combo Box Setup
        ObservableList<String> list = FXCollections.observableArrayList(SortType.getValuesAsString());
        sortTypeComboBox.setItems(list);
        sortTypeComboBox.setOnAction(e -> {
            sorter = SortType.getSorterWithName(sortTypeComboBox.getSelectionModel().getSelectedItem());

            sizeSpinner.setDisable(false);
            pseudocodeButton.setDisable(false);

            updateText();
        });

        // Spinner Setup
        sizeSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_SIZE, MAX_SIZE, MIN_SIZE, STEP_SIZE));
        sizeSpinner.getValueFactory().setValue(MIN_SIZE);
        sizeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            setArrSize(newValue);
            startButton.setDisable(true);
            updateText();
        });

        // Buttons Setup
        startButton.setOnAction(e -> sorter.sort(nums)); //TODO Visualise the sorting live
        randomizeButton.setOnAction(e -> randomize());
    }

    private boolean isSorted() {
        for (int i = 1; i < arrSize; i++) {
            if (nums[i] < nums[i-1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Randomizes the current array by swapping the element in each index
     * with an element in a random index
     */
    private void randomize() {
        Random rand = new Random();

        for (int i = 0; i < arrSize; i++) {
            int swap = rand.nextInt(arrSize - 1);
            int temp = nums[i];

            // Swap current element with an element of random index
            nums[i] = nums[swap];
            nums[swap] = temp;
        }

        startButton.setDisable(false);
        updateBars();
    }

    /**
     * Updates the text about the bars which inform the user of sort algorithm and array size
     */
    private void updateText() {
        graphLabel.setText(sorter.getDisplayableName() + " - " + arrSize);
    }

    /**
     * Sets the size of the array being sorted in run time.
     * User can select size and it will be portrayed live in {@link #updateBars()}
     *
     * @param newSize Size of new array
     */
    private void setArrSize(int newSize) {
        nums = new int[newSize];
        arrSize = newSize;

        for (int i = 0; i < newSize; i++) {
            nums[i] = i;
        }

        updateBars();
    }

    /**
     * Updates the bars based off the selected array size.
     * A live visualisation of the array size helps the user gauge how big the array is
     * that will be sorted.
     */
    private void updateBars() {
        mainPane.getChildren().clear();

        final double WIDTH = mainPane.getPrefWidth() / arrSize;
        final double HEIGHT = mainPane.getPrefHeight(); // Depends on elements position in array

        double x = 0; // Y value changes depending on height of the bar and so has no intial value

        // Create a bar for each element in the array
        for (int num : nums) {
            double barHeight = (num) * HEIGHT / arrSize;
            Rectangle bar = new Rectangle();

            bar.setX(x);
            bar.setY(HEIGHT - barHeight);
            bar.setWidth(WIDTH);
            bar.setHeight(barHeight);
            bar.setFill(Paint.valueOf("#5c5c5c"));

            mainPane.getChildren().add(bar);

            x += WIDTH;
        }

    }
}
