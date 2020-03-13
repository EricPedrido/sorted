package com.pedrido.javafx;

import com.pedrido.model.Timer;
import com.pedrido.model.sort.SortType;
import com.pedrido.model.sort.Sorter;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

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
    private Timer timer = Timer.getInstance();
    private List<int[]> stateList = new ArrayList<>();

    private static Controller INSTANCE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        INSTANCE = this;
        graphLabel.setText("");
        arrSize = MIN_SIZE;
        setArrSize(MIN_SIZE);

        timerLabel.setText(timer.getSspTime().get());
        timer.getSspTime().addListener(observable -> Platform.runLater(() -> timerLabel.setText(timer.getSspTime().get())));

        // Combo Box Setup
        ObservableList<String> list = FXCollections.observableArrayList(SortType.getValuesAsString());
        sortTypeComboBox.setItems(list);
        sortTypeComboBox.setOnAction(e -> {
            sorter = SortType.getSorterWithName(sortTypeComboBox.getSelectionModel().getSelectedItem());
            setArrSize(MIN_SIZE);

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
        startButton.setOnAction(e -> {
            timer.startTimer(0);
            sorter.sort(nums);
            updateBars();
        });
        randomizeButton.setOnAction(e -> randomize());
    }

    private void isSorted() {
        for (int i = 1; i < arrSize; i++) {
            if (nums[i] < nums[i-1]) {
                return;
            }
        }
        done();
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

        startButton.setDisable(sizeSpinner.isDisable());
        updateBars();
    }

    /**
     * Called when array is finished being sorted.
     */
    public void done() {
        timer.stopTimer();

        Runnable task = () -> {
            try {

                System.out.println(stateList.size());
                for (int[] state : stateList) {
                    Platform.runLater(() -> {
                        updateBars(state);
                    });

                    Thread.sleep(10);
                }
            }
            catch (InterruptedException ignored) {

            }
        };
        new Thread(task).start();
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
    public void updateBars() {
        updateBars(nums);
    }

    public void updateBars(int[] arr) {
        System.out.println("ping");
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

    public void swap(int index, int swapIndex) {

        int temp = nums[index];
        nums[index] = nums[swapIndex];
        nums[swapIndex] = temp;

        int[] tempArr = new int[arrSize];
        System.arraycopy(nums, 0, tempArr, 0, arrSize);

        stateList.add(tempArr);
        //System.out.println(stateList.size());
        isSorted();
//        Timeline timer = new Timeline(
//                new KeyFrame(Duration.millis(100), e -> updateBars())
//        );
//        timer.play();

//        Task<Void> timer = new Task<>() {
//            @Override
//            protected Void call() {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException ignored) {
//                }
//                return null;
//            }
//        };
//        timer.setOnSucceeded(event -> updateBars());
//        new Thread(timer).start();

//        Runnable task = () -> {
//            try {
//                Platform.runLater(() -> {
//                    updateBars();
//                });
//                System.out.println("ping");
//                Thread.sleep(100);
//            }
//            catch (InterruptedException ignored) {
//
//            }
//        };
//        new Thread(task).start();

        //sleep();
    }

    /**
     * Makes the system wait for a certain amount of time
     * (100 nanoseconds).
     */ //TODO MAKE VISUALISATION SLEEP EVERY SWAP
    public void sleep() {
        updateBars();



//        new java.util.Timer().schedule(
//                new TimerTask() {
//                    @Override
//                    public void run() {
//                        System.out.println("ping");
//                    }
//                }, 100);
//        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), Event::consume));
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();
    }

    public static Controller getInstance() {
        return INSTANCE;
    }

}
