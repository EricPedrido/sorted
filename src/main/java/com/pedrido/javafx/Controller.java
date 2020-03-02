package com.pedrido.javafx;

import com.pedrido.model.SortType;
import com.pedrido.model.Sorter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML public Label graphLabel, timerLabel;
    @FXML public ComboBox<String> sortTypeComboBox;
    @FXML public Button startButton;
    @FXML public BarChart chart;
    @FXML public Spinner<Integer> sizeSpinner;

    private final int MIN_SIZE = 10;
    private final int MAX_SIZE = 1000;
    private final int STEP_SIZE = 10;

    private Sorter sorter;
    private XYChart.Series series;
    private List<XYChart.Data> nums;
    private int arrSize;

    //TODO SWITCH FROM CHARTS TO MANUALLY PAINTING RECTANGLES SINCE CHARTS ARE SLOW AT LARGE VALUES

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graphLabel.setText("");
        series = new XYChart.Series();
        chart.getData().add(series);
        nums = new ArrayList<>();
        arrSize = MIN_SIZE;

        increaseArraySize(MIN_SIZE);

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
            if (newValue < arrSize) {
                decreaseArraySize(newValue);
            } else {
                increaseArraySize(newValue);
            }
            arrSize = newValue;

            updateText();
        });


    }

    private void updateText() {
        graphLabel.setText(sorter.getDisplayableName() + " - " + arrSize);
    }

    private void increaseArraySize(int newSize) {
        for (int i = nums.size()+1; i <= newSize; i++) {
            nums.add(new XYChart.Data<>(""+i, i));
        }

        updateChart();
    }

    private void decreaseArraySize(int newSize) {
        for (int i = nums.size(); i > newSize; i--) {
            nums.remove(nums.size()-1);
        }

        updateChart();
    }

    /**
     * Updates the chart based off the selected values of sort type and array size.
     * A live visualisation of the array size helps the user gauge how big the array is
     * that will be sorted.
     */
    private void updateChart() {
        ObservableList<XYChart.Data> dataList = FXCollections.observableList(nums);
        series.getData().clear();
        series.getData().addAll(dataList);
    }


}
