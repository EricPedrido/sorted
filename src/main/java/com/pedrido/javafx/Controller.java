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
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML public Label graphLabel, timerLabel;
    @FXML public ComboBox<String> sortTypeComboBox;
    @FXML public Button startButton;
    @FXML public Spinner<Integer> sizeSpinner;
    @FXML public Pane mainPane;

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
            if (sizeSpinner.isDisabled())
                sizeSpinner.setDisable(false);
            updateText();
        });

        // Spinner Setup
        sizeSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_SIZE, MAX_SIZE, MIN_SIZE, STEP_SIZE));
        sizeSpinner.getValueFactory().setValue(MIN_SIZE);
        sizeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            setArrSize(newValue);
            updateText();
        });
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

        final double WIDTH = mainPane.getPrefWidth()/nums.length;
        final double HEIGHT = mainPane.getPrefHeight(); // Depends on elements position in array
        final double LENGTH = nums.length;

        double x = 0; // Y value changes depending on height of the bar and so has no intial value

        // Create a bar for each element in the array
        for (int i = 0; i < LENGTH; i++) {
            double barHeight = (i + 1)*HEIGHT/LENGTH;
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
