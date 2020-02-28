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
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML public Label graphLabel, arraySizeLabel;
    @FXML public ComboBox<String> sortTypeComboBox;
    @FXML public Slider arraySizeSlider;
    @FXML public Button startButton;

    private Sorter sorter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graphLabel.setText("");

        // Combo Box Setup
        ObservableList<String> list = FXCollections.observableArrayList(SortType.getValuesAsString());
        sortTypeComboBox.setItems(list);
        sortTypeComboBox.setOnAction(
                e -> sorter = SortType.getSorterWithName(sortTypeComboBox.getSelectionModel().getSelectedItem()));


    }

}
