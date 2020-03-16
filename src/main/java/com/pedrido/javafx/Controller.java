package com.pedrido.javafx;

import com.pedrido.model.sort.SortType;
import com.pedrido.model.sort.Sorter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public Label graphLabel, percentageLabel, arraySwapLabel,
            startLabel, pauseLabel, infoLabel, skipLabel, repeatLabel;
    @FXML
    public ComboBox<String> sortTypeComboBox;
    @FXML
    public Button pseudocodeButton, randomizeButton;
    @FXML
    public Spinner<Integer> sizeSpinner;
    @FXML
    public Slider speedSlider;
    @FXML
    public Pane mainPane;

    private final int MIN_SIZE = 100;
    private final int MAX_SIZE = 500;
    private final int STEP_SIZE = 50;
    private final int DELAY_VARIABLE = 200; // Larger number = longer delays on the slider

    private Sorter sorter;
    private int[] nums;
    private int arrSize;

    private List<int[]> stateList = new ArrayList<>();
    private int delay;
    private boolean sorted = false;
    private boolean run = true;
    private int curState;

    private static Controller INSTANCE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        INSTANCE = this;
        delay = DELAY_VARIABLE/(int)speedSlider.getValue();
        graphLabel.setText("");
        infoLabel.setText("Select an algorithm");
        arrSize = MIN_SIZE;
        setArrSize(MIN_SIZE);

        // Sort Type Combo Box Setup
        ObservableList<String> list = FXCollections.observableArrayList(SortType.getValuesAsString());
        sortTypeComboBox.setItems(list);
        sortTypeComboBox.setOnAction(e -> {
            sorter = SortType.getSorterWithName(sortTypeComboBox.getSelectionModel().getSelectedItem());
            setArrSize(arrSize);
            infoLabel.setText("Randomize the array");

            sizeSpinner.setDisable(false);
            setButtonDisable(false);
            setMediaLabelDisable(true);
            pseudocodeButton.setDisable(false);

            updateText();
        });

        // Array Size Spinner Setup
        sizeSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_SIZE, MAX_SIZE, MIN_SIZE, STEP_SIZE));
        sizeSpinner.getValueFactory().setValue(MIN_SIZE);
        sizeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            setArrSize(newValue);
            infoLabel.setText("Randomize the array");
            startLabel.setDisable(true);
            updateText();
        });

        // Speed Slider Setup
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            delay = DELAY_VARIABLE/newValue.intValue();
        });


        // Clickable Label Setup
        startLabel.setOnMouseClicked(e -> {
            if (!run) {
                done();
            } else {
                sorter.sort(nums);
            }

            infoLabel.setText("Sorting array");
            setMediaLabelDisable(false);
            setButtonDisable(true);
            setStartVisible(false);

            run = true;
        });
        pauseLabel.setOnMouseClicked(e -> {
            run = false;
            infoLabel.setText("Paused");
        });
        randomizeButton.setOnAction(e -> {
            stateList = new ArrayList<>();
            sorted = false;
            infoLabel.setText("Press Play!");
            randomize();
        });
    }

    private void setButtonDisable(boolean disable) {
        sortTypeComboBox.setDisable(disable);
        sizeSpinner.setDisable(disable);
        randomizeButton.setDisable(disable);
    }

    private void setMediaLabelDisable(boolean disable) {
        startLabel.setDisable(disable);
        skipLabel.setDisable(disable);
        repeatLabel.setDisable(disable);
    }

    private void setStartVisible(boolean visible) {
        startLabel.setVisible(visible);
        pauseLabel.setVisible(!visible);
    }

    /**
     * Checks if the sorting algorithm is complete, whereby
     * it visualizes a replay of each step it took in
     * {@link #done()}
     */
    private void isSorted() {
        for (int i = 1; i < arrSize; i++) {
            if (nums[i] < nums[i - 1]) {
                return;
            }
        }

        sorted = true;
        done();
    }

    /**
     * Called when array is finished being sorted.
     * This method visualizes the sorting algorithm by playing back each state
     * of the sort with a delay (variable)
     */
    private void done() {
        Task task = new Task() {
            @Override
            protected Object call() {
                try {
                    for (int i = curState; i < stateList.size(); i++) {
                        int[] state = stateList.get(i);
                        double stateNum = i;
                        if (run) {
                            Platform.runLater(() ->  {
                                updateBars(state);
                                percentageLabel.setText(Math.round((stateNum/stateList.size())*100) + "%");
                                arraySwapLabel.setText(Integer.toString((int) stateNum));
                            });
                            Thread.sleep(delay);
                        } else {
                            curState = i;
                            break;
                        }
                    }
                } catch (InterruptedException ignored) {
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            setStartVisible(true);

            if (run) {
                curState = 0;
                setMediaLabelDisable(true);
                setButtonDisable(false);
                percentageLabel.setText("100%");
                infoLabel.setText("Pick another algorithm and do it again");
            } else {
                setButtonDisable(true);
            }
        });
        new Thread(task).start();
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

        startLabel.setDisable(sizeSpinner.isDisable());
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
        updateBars(nums);
    }

    private void updateBars(int[] arr) {
        mainPane.getChildren().clear();

        final double WIDTH = mainPane.getPrefWidth() / arrSize;
        final double HEIGHT = mainPane.getPrefHeight(); // Depends on elements position in array

        double x = 0; // Y value changes depending on height of the bar and so has no initial value

        // Create a bar for each element in the array
        for (int num : arr) {
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

    /**
     * Places an element into the array at the given index.
     * Also adds this placement into the state list.
     */
    public void place(int element, int index) {
        nums[index] = element;

        saveState();
    }

    public void swap(int index, int swapIndex) {
        int temp = nums[index];
        nums[index] = nums[swapIndex];
        nums[swapIndex] = temp;

        saveState();
    }

    private void saveState() {
        int[] tempArr = new int[arrSize];
        System.arraycopy(nums, 0, tempArr, 0, arrSize);
        stateList.add(tempArr);

        if (!sorted) {
            isSorted();
        }
    }

    public static Controller getInstance() {
        return INSTANCE;
    }

}
