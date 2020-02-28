package com.pedrido.javafx;

import com.pedrido.model.SortType;
import com.pedrido.model.Sorter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML public Label graphLabel, arraySizeLabel;
    @FXML public ComboBox<SortType> sortTypeComboBox;
    @FXML public Slider arraySizeSlider;
    @FXML public Button startButton;

    private Sorter sorter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graphLabel.setText("");

        ObservableList<SortType> list = FXCollections.observableArrayList(Arrays.asList(SortType.values()));
        sortTypeComboBox.setItems(list);

    }
}
